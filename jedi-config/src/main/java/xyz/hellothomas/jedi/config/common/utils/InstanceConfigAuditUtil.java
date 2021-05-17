package xyz.hellothomas.jedi.config.common.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.biz.domain.Instance;
import xyz.hellothomas.jedi.biz.domain.InstanceConfig;
import xyz.hellothomas.jedi.config.application.InstanceService;
import xyz.hellothomas.jedi.core.utils.JediThreadFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static xyz.hellothomas.jedi.core.constants.Constants.NAMESPACE_EXECUTOR_SEPARATOR;

@Slf4j
@Service
public class InstanceConfigAuditUtil implements InitializingBean {
    private static final int INSTANCE_CONFIG_AUDIT_MAX_SIZE = 10000;
    private static final int INSTANCE_CACHE_MAX_SIZE = 50000;
    private static final int INSTANCE_CONFIG_CACHE_MAX_SIZE = 50000;
    private static final long OFFER_TIME_LAST_MODIFIED_TIME_THRESHOLD_IN_MILLI = TimeUnit.MINUTES.toMillis(10);//10
    // minutes
    private static final Joiner STRING_JOINER = Joiner.on(NAMESPACE_EXECUTOR_SEPARATOR);
    private BlockingQueue<InstanceConfigAuditModel> audits = Queues.newLinkedBlockingQueue
            (INSTANCE_CONFIG_AUDIT_MAX_SIZE);
    private final InstanceService instanceService;
    private final ExecutorService auditExecutorService;
    private final AtomicBoolean auditStopped;
    private Cache<String, Long> instanceCache;
    private Cache<String, String> instanceConfigReleaseKeyCache;

    public InstanceConfigAuditUtil(InstanceService instanceService) {
        this.instanceService = instanceService;
        this.auditExecutorService = Executors.newSingleThreadExecutor(
                JediThreadFactory.create("InstanceConfigAuditUtil", true));
        auditStopped = new AtomicBoolean(false);
        instanceCache = Caffeine.newBuilder().expireAfterAccess(1, TimeUnit.HOURS)
                .maximumSize(INSTANCE_CACHE_MAX_SIZE).build();
        instanceConfigReleaseKeyCache = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.DAYS)
                .maximumSize(INSTANCE_CONFIG_CACHE_MAX_SIZE).build();
    }

    public boolean audit(String ip, String configNamespace, String configAppId, String configExecutorName,
                         String releaseKey) {
        return this.audits.offer(new InstanceConfigAuditModel(ip, configNamespace, configAppId, configExecutorName,
                releaseKey));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        auditExecutorService.submit(() -> {
            while (!auditStopped.get() && !Thread.currentThread().isInterrupted()) {
                try {
                    InstanceConfigAuditModel model = audits.poll();
                    if (model == null) {
                        TimeUnit.SECONDS.sleep(1);
                        continue;
                    }
                    doAudit(model);
                } catch (Throwable ex) {
                    log.error("实例配置登记异常{}", ex);
                }
            }
        });
    }

    private void doAudit(InstanceConfigAuditModel auditModel) {
        String instanceCacheKey = assembleInstanceKey(auditModel.getConfigNamespace(), auditModel
                .getConfigAppId(), auditModel.getIp());
        Long instanceId = instanceCache.getIfPresent(instanceCacheKey);
        if (instanceId == null) {
            instanceId = prepareInstanceId(auditModel);
            instanceCache.put(instanceCacheKey, instanceId);
        }

        //load instance config release key from cache, and check if release key is the same
        String instanceConfigCacheKey = assembleInstanceConfigKey(instanceId, auditModel
                .getConfigAppId(), auditModel.getConfigExecutorName());
        String cacheReleaseKey = instanceConfigReleaseKeyCache.getIfPresent(instanceConfigCacheKey);

        //if release key is the same, then skip audit
        if (cacheReleaseKey != null && Objects.equals(cacheReleaseKey, auditModel.getReleaseKey())) {
            return;
        }

        instanceConfigReleaseKeyCache.put(instanceConfigCacheKey, auditModel.getReleaseKey());

        //if release key is not the same or cannot find in cache, then do audit
        InstanceConfig instanceConfig = instanceService.findInstanceConfig(instanceId, auditModel
                .getConfigAppId(), auditModel.getConfigExecutorName());

        if (instanceConfig != null) {
            if (!Objects.equals(instanceConfig.getReleaseKey(), auditModel.getReleaseKey())) {
                instanceConfig.setReleaseKey(auditModel.getReleaseKey());
                instanceConfig.setReleaseDeliveryTime(auditModel.getOfferTime());
            } else if (offerTimeAndLastModifiedTimeCloseEnough(auditModel.getOfferTime(),
                    instanceConfig.getDataChangeLastModifiedTime())) {
                //when releaseKey is the same, optimize to reduce writes if the record was updated not long ago
                return;
            }
            //we need to update no matter the release key is the same or not, to ensure the
            //last modified time is updated each day
            instanceConfig.setDataChangeLastModifiedTime(auditModel.getOfferTime());
            instanceService.updateInstanceConfig(instanceConfig);
            return;
        }

        instanceConfig = new InstanceConfig();
        instanceConfig.setInstanceId(instanceId);
        instanceConfig.setConfigNamespaceName(auditModel.getConfigNamespace());
        instanceConfig.setConfigAppId(auditModel.getConfigAppId());
        instanceConfig.setConfigExecutorName(auditModel.getConfigExecutorName());
        instanceConfig.setReleaseKey(auditModel.getReleaseKey());
        instanceConfig.setReleaseDeliveryTime(auditModel.getOfferTime());
        instanceConfig.setDataChangeCreatedTime(auditModel.getOfferTime());
        instanceConfig.setDataChangeLastModifiedTime(auditModel.getOfferTime());

        try {
            instanceService.createInstanceConfig(instanceConfig);
        } catch (DataIntegrityViolationException ex) {
            //concurrent insertion, safe to ignore
            log.debug("create instance config duplicated error:{0}", ex);
        }
    }

    private long prepareInstanceId(InstanceConfigAuditModel auditModel) {
        Instance instance = instanceService.findInstance(auditModel.getConfigNamespace(), auditModel
                .getConfigAppId(), auditModel.getIp());
        if (instance != null) {
            return instance.getId();
        }

        try {
            return instanceService.createInstance(auditModel.getConfigNamespace(), auditModel.getConfigAppId(),
                    auditModel.getIp()).getId();
        } catch (DataIntegrityViolationException ex) {
            //return the one exists
            return instanceService.findInstance(instance.getNamespaceName(), instance.getAppId(), instance.getIp()).getId();
        }
    }

    private String assembleInstanceKey(String namespaceName, String appId, String ip) {
        List<String> keyParts = Lists.newArrayList(namespaceName, appId, ip);
        return STRING_JOINER.join(keyParts);
    }

    private String assembleInstanceConfigKey(long instanceId, String configAppId, String configExecutor) {
        return STRING_JOINER.join(instanceId, configAppId, configExecutor);
    }

    private boolean offerTimeAndLastModifiedTimeCloseEnough(LocalDateTime offerTime, LocalDateTime lastModifiedTime) {
        Duration duration = Duration.between(lastModifiedTime, offerTime);
        return duration.toMillis() <
                OFFER_TIME_LAST_MODIFIED_TIME_THRESHOLD_IN_MILLI;
    }

    public static class InstanceConfigAuditModel {
        private String ip;
        private String configNamespace;
        private String configAppId;
        private String configExecutorName;
        private String releaseKey;
        private LocalDateTime offerTime;

        public InstanceConfigAuditModel(String clientIp, String configNamespace, String configAppId,
                                        String configExecutorName, String releaseKey) {
            this.offerTime = LocalDateTime.now();
            this.ip = clientIp;
            this.configAppId = configAppId;
            this.configExecutorName = configExecutorName;
            this.configNamespace = configNamespace;
            this.releaseKey = releaseKey;
        }

        public String getIp() {
            return ip;
        }

        public String getConfigAppId() {
            return configAppId;
        }

        public String getConfigNamespace() {
            return configNamespace;
        }

        public String getReleaseKey() {
            return releaseKey;
        }

        public String getConfigExecutorName() {
            return configExecutorName;
        }

        public LocalDateTime getOfferTime() {
            return offerTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            InstanceConfigAuditModel model = (InstanceConfigAuditModel) o;
            return Objects.equals(ip, model.ip) &&
                    Objects.equals(configNamespace, model.configNamespace) &&
                    Objects.equals(configAppId, model.configAppId) &&
                    Objects.equals(configExecutorName, model.configExecutorName) &&
                    Objects.equals(releaseKey, model.releaseKey);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ip, configNamespace, configAppId, configExecutorName,
                    releaseKey);
        }
    }
}
