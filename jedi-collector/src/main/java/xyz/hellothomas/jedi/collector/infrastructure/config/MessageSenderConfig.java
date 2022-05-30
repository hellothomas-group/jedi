package xyz.hellothomas.jedi.collector.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.hellothomas.jedi.biz.infrastructure.mapper.monitor.UserMapper;
import xyz.hellothomas.jedi.collector.application.LocalMessageSender;
import xyz.hellothomas.jedi.collector.application.MessageSender;

/**
 * @author Thomas
 * @date 2022/5/30 20:58
 * @description
 * @version 1.0
 */
@Slf4j
@Configuration
public class MessageSenderConfig {

    @Bean
    @ConditionalOnMissingBean
    public MessageSender localMessageSender(UserMapper userMapper) {
        log.debug("initialize localMessageSender");
        return new LocalMessageSender(userMapper);
    }
}
