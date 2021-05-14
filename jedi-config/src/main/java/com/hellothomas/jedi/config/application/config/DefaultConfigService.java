package com.hellothomas.jedi.config.application.config;

import com.hellothomas.jedi.biz.domain.Release;
import com.hellothomas.jedi.biz.domain.ReleaseMessage;
import com.hellothomas.jedi.config.application.ReleaseService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * config service with no cache
 */
public class DefaultConfigService extends AbstractConfigService {

    @Autowired
    private ReleaseService releaseService;

    @Override
    protected Release findLatestActiveRelease(String namespaceName, String appId, String executorName,
                                              long notificationId) {
        return releaseService.findLatestActiveRelease(namespaceName, appId, executorName);
    }

    @Override
    public void handleMessage(ReleaseMessage message, String channel) {
        // since there is no cache, so do nothing
    }
}
