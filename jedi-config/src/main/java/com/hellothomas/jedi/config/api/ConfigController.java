package com.hellothomas.jedi.config.api;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hellothomas.jedi.biz.common.utils.ReleaseMessageKeyGenerator;
import com.hellothomas.jedi.biz.domain.Release;
import com.hellothomas.jedi.config.application.config.ConfigService;
import com.hellothomas.jedi.config.common.utils.InstanceConfigAuditUtil;
import com.hellothomas.jedi.core.dto.config.JediConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import static com.hellothomas.jedi.core.constants.Constants.NO_APPID_PLACEHOLDER;

@RestController
@RequestMapping("/configs")
@Slf4j
public class ConfigController {
    private static final Splitter X_FORWARDED_FOR_SPLITTER = Splitter.on(",").omitEmptyStrings()
            .trimResults();
    private static final Type CONFIGURATION_TYPE_REFERENCE = new TypeToken<Map<String, String>>() {
    }.getType();

    private final ConfigService configService;
    private final InstanceConfigAuditUtil instanceConfigAuditUtil;
    private final Gson gson;

    public ConfigController(ConfigService configService, InstanceConfigAuditUtil instanceConfigAuditUtil, Gson gson) {
        this.configService = configService;
        this.instanceConfigAuditUtil = instanceConfigAuditUtil;
        this.gson = gson;
    }

    @GetMapping(value = "/{namespace}/{appId}/{executorName}")
    public JediConfig queryConfig(@PathVariable String namespace, @PathVariable String appId,
                                  @PathVariable String executorName,
                                  @RequestParam(value = "releaseKey", defaultValue = "-1") String clientSideReleaseKey,
                                  @RequestParam(value = "ip", required = false) String clientIp,
                                  @RequestParam(value = "notificationId", defaultValue = "-1") long notificationId,
                                  HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (Strings.isNullOrEmpty(clientIp)) {
            clientIp = tryToGetClientIp(request);
        }

        Release release = null;

        if (!NO_APPID_PLACEHOLDER.equalsIgnoreCase(appId)) {
            Release currentAppRelease = configService.loadConfig(namespace, appId, executorName, notificationId);

            if (currentAppRelease != null) {
                release = currentAppRelease;
            }
        }

        if (release == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,
                    String.format(
                            "Could not load configuration with namespace: %s, appId: %s, executorName: %s",
                            namespace, appId, executorName));
            log.warn("Jedi.Config.NotFound:{}", assembleKey(namespace, appId, executorName));
            return null;
        }

        auditReleases(namespace, appId, executorName, clientIp, release);

        if (release.getReleaseKey().equals(clientSideReleaseKey)) {
            // Client side configuration is the same with server side, return 304
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            log.info("Jedi.Config.NotModified:{}", assembleKey(namespace, appId, executorName));
            return null;
        }

        JediConfig jediConfig = new JediConfig(namespace, appId, executorName, release.getReleaseKey());
        jediConfig.setConfigurations(transferReleaseConfigurations(release));

        log.info("Jedi.Config.Found:{}", assembleKey(namespace, appId, executorName));
        return jediConfig;
    }

    Map<String, String> transferReleaseConfigurations(Release release) {
        Map<String, String> result = Maps.newLinkedHashMap();
        result.putAll(gson.fromJson(release.getConfigurations(), CONFIGURATION_TYPE_REFERENCE));

        return result;
    }

    private String assembleKey(String namespace, String appId, String executorName) {
        return ReleaseMessageKeyGenerator.generate(namespace, appId, executorName);
    }

    private void auditReleases(String namespace, String appId, String executorName, String clientIp,
                               Release release) {
        if (Strings.isNullOrEmpty(clientIp)) {
            //no need to audit instance config when there is no ip
            return;
        }
        instanceConfigAuditUtil.audit(clientIp, release.getNamespaceName(), release.getAppId(),
                release.getExecutorName(), release.getReleaseKey());
    }

    private String tryToGetClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-FORWARDED-FOR");
        if (!Strings.isNullOrEmpty(forwardedFor)) {
            return X_FORWARDED_FOR_SPLITTER.splitToList(forwardedFor).get(0);
        }
        return request.getRemoteAddr();
    }
}
