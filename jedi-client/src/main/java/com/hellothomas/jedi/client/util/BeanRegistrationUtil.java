package com.hellothomas.jedi.client.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Map;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class BeanRegistrationUtil {
    public static boolean registerBeanDefinitionIfNotExists(BeanDefinitionRegistry registry, String beanName,
                                                            Class<?> beanClass) {
        return registerBeanDefinitionIfNotExists(registry, beanName, beanClass, null);
    }

    public static boolean registerBeanDefinitionIfNotExists(BeanDefinitionRegistry registry, String beanName,
                                                            Class<?> beanClass,
                                                            Map<String, Object> extraPropertyValues) {
        if (registry.containsBeanDefinition(beanName)) {
            return false;
        }

        BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(beanClass).getBeanDefinition();

        if (extraPropertyValues != null) {
            for (Map.Entry<String, Object> entry : extraPropertyValues.entrySet()) {
                beanDefinition.getPropertyValues().add(entry.getKey(), entry.getValue());
            }
        }

        registry.registerBeanDefinition(beanName, beanDefinition);

        return true;
    }


}
