package xyz.hellothomas.jedi.config.application.config;

import xyz.hellothomas.jedi.biz.domain.Release;
import xyz.hellothomas.jedi.config.application.message.ReleaseMessageListener;

public interface ConfigService extends ReleaseMessageListener {

    /**
     * loadConfig
     *
     * @param namespaceName
     * @param appId
     * @param executorName
     * @param notificationId
     * @return Release
     */
    Release loadConfig(String namespaceName, String appId, String executorName, long notificationId);
}
