package xyz.hellothomas.jedi.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import xyz.hellothomas.jedi.client.constants.Constants;
import xyz.hellothomas.jedi.client.internals.PropertySourcesProcessor;


/**
 * @author Thomas
 * @date 2021/4/11 13:37
 * @description
 * @version 1.0
 */
@Slf4j
@Configuration
@Import(JediExecutorRegistrar.class)
@ConditionalOnProperty(value = Constants.JEDI_CONFIG_ENABLE_KEY, havingValue = "true")
public class JediExecutorAutoConfig {

    /**
     * mode enum see {@link xyz.hellothomas.jedi.client.enums.JediModeEnum}
     *
     * see the {@link Bean @Bean} javadocs for details
     * on working with {@code BeanFactoryPostProcessor} types such as
     * {@code PropertySourcesPlaceholderConfigurer}
     *
     * @return
     */
    @Bean
    @ConditionalOnExpression("#{environment['jedi.mode']==null||'0'.equals(environment['jedi.mode'])||" +
            "'2'.equals(environment['jedi.mode'])}")
    public static PropertySourcesProcessor propertySourcesProcessor() {
        return new PropertySourcesProcessor();
    }
}
