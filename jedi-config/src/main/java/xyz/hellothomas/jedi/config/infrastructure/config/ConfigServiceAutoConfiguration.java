package xyz.hellothomas.jedi.config.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.hellothomas.jedi.biz.infrastructure.mapper.ReleaseMessageMapper;
import xyz.hellothomas.jedi.config.api.CalibrationController;
import xyz.hellothomas.jedi.config.application.config.ConfigService;
import xyz.hellothomas.jedi.config.application.config.ConfigServiceWithCache;
import xyz.hellothomas.jedi.config.application.config.DefaultConfigService;
import xyz.hellothomas.jedi.config.application.message.ReleaseMessageScanner;
import xyz.hellothomas.jedi.config.application.message.ReleaseMessageServiceWithCache;

@Configuration
public class ConfigServiceAutoConfiguration {
    private final ConfigServiceProperty configServiceProperty;

    public ConfigServiceAutoConfiguration(ConfigServiceProperty configServiceProperty) {
        this.configServiceProperty = configServiceProperty;
    }

    @Bean
    public ConfigService configService() {
        if (configServiceProperty.isCacheEnabled()) {
            return new ConfigServiceWithCache();
        } else {
            return new DefaultConfigService();
        }
    }

    @Configuration
    static class MessageScannerConfiguration {
        private final CalibrationController calibrationController;
        private final ConfigService configService;
        private final ReleaseMessageServiceWithCache releaseMessageServiceWithCache;

        public MessageScannerConfiguration(CalibrationController calibrationController,
                                           ConfigService configService,
                                           ReleaseMessageServiceWithCache releaseMessageServiceWithCache) {
            this.calibrationController = calibrationController;
            this.configService = configService;
            this.releaseMessageServiceWithCache = releaseMessageServiceWithCache;
        }

        @Bean
        public ReleaseMessageScanner releaseMessageScanner(ReleaseMessageMapper releaseMessageMapper) {
            ReleaseMessageScanner releaseMessageScanner = new ReleaseMessageScanner(releaseMessageMapper);
            //0. handle release message cache
            releaseMessageScanner.addMessageListener(releaseMessageServiceWithCache);
            //1. handle server cache
            releaseMessageScanner.addMessageListener(configService);
            //2. notify clients
            releaseMessageScanner.addMessageListener(calibrationController);
            return releaseMessageScanner;
        }
    }

}
