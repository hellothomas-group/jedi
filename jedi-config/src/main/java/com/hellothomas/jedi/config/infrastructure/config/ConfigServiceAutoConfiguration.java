package com.hellothomas.jedi.config.infrastructure.config;

import com.hellothomas.jedi.biz.infrastructure.mapper.ReleaseMessageMapper;
import com.hellothomas.jedi.config.api.CalibrationController;
import com.hellothomas.jedi.config.application.config.ConfigService;
import com.hellothomas.jedi.config.application.config.ConfigServiceWithCache;
import com.hellothomas.jedi.config.application.config.DefaultConfigService;
import com.hellothomas.jedi.config.application.message.ReleaseMessageScanner;
import com.hellothomas.jedi.config.application.message.ReleaseMessageServiceWithCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
