package xyz.hellothomas.jedi.admin.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.hellothomas.jedi.admin.infrastructure.aop.AuthorizeAspect;

@Slf4j
@Configuration
public class AuthorizeAspectConfig implements BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Bean
    public AuthorizeAspect authorizeAspect() {
        return new AuthorizeAspect(beanFactory);
    }
}
