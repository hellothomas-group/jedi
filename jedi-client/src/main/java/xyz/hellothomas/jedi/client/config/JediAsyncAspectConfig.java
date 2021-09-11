package xyz.hellothomas.jedi.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.hellothomas.jedi.client.aop.JediAsyncAspect;
import xyz.hellothomas.jedi.client.constants.Constants;
import xyz.hellothomas.jedi.core.internals.executor.JediThreadPoolExecutor;

import java.util.Map;

@Slf4j
@ConditionalOnProperty(value = Constants.JEDI_CONFIG_ENABLE_KEY, havingValue = "true")
@Configuration
public class JediAsyncAspectConfig implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public JediAsyncAspect jediAsyncAspect() {
        Map<String, JediThreadPoolExecutor> executorMap =
                this.applicationContext.getBeansOfType(JediThreadPoolExecutor.class);
        JediConfig jediConfig = this.applicationContext.getBean(JediConfig.class);
        return new JediAsyncAspect(executorMap, jediConfig.getOrder());
    }
}
