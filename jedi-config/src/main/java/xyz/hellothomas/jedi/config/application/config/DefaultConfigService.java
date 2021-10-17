package xyz.hellothomas.jedi.config.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import xyz.hellothomas.jedi.biz.domain.config.Release;
import xyz.hellothomas.jedi.biz.domain.config.ReleaseMessage;
import xyz.hellothomas.jedi.config.application.ReleaseService;

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
