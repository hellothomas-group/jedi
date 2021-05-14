package com.hellothomas.jedi.client.internals;

import com.hellothomas.jedi.client.ConfigService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @className PropertySourcesProcessor
 * @author 80234613 唐圆
 * @date 2021/4/30 16:37
 * @descripton
 * @version 1.0
 */
public class PropertySourcesProcessor implements ApplicationContextAware, BeanFactoryPostProcessor {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        AutoUpdateConfigChangeToExecutorListener autoUpdateConfigChangeListener =
                new AutoUpdateConfigChangeToExecutorListener(applicationContext);
        ConfigService.getConfigs().stream().forEach(i -> i.addChangeListener(autoUpdateConfigChangeListener));
    }
}
