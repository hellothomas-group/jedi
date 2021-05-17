package xyz.hellothomas.jedi.config.application.config;

import xyz.hellothomas.jedi.biz.domain.Release;

public abstract class AbstractConfigService implements ConfigService {

    @Override
    public Release loadConfig(String namespaceName, String appId, String executorName, long notificationId) {

        return findLatestActiveRelease(namespaceName, appId, executorName, notificationId);
    }

    /**
     * Find active release by app id, cluster name and namespace name
     */
    protected abstract Release findLatestActiveRelease(String namespaceName, String appId, String executorName,
                                                       long notificationId);
}
