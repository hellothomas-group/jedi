package xyz.hellothomas.jedi.consumer.infrastructure.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.hellothomas.jedi.consumer.common.constants.Constants;
import xyz.hellothomas.jedi.consumer.domain.pojo.CaffeineProperty;
import xyz.hellothomas.jedi.consumer.infrastructure.exception.IgnoreExceptionCacheErrorHandler;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @className CacheConfig
 * @author Thomas
 * @date 2020/11/29 22:49
 * @description
 * @version 1.0
 */
@Slf4j
@Configuration
public class CacheConfig extends CachingConfigurerSupport {
    private final CaffeineProperty caffeineProperty;

    public CacheConfig(CaffeineProperty caffeineProperty) {
        this.caffeineProperty = caffeineProperty;
    }

    @Bean
    public CacheManager caffeineCacheManager() {
        log.info("caffeineProperty: {}", caffeineProperty);
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        Caffeine caffeine = Caffeine.newBuilder()
                //cache的初始容量值
                .initialCapacity(caffeineProperty.getInitialCapacity())
                //maximumSize用来控制cache的最大缓存数量，maximumSize和maximumWeight(最大权重)不可以同时使用，
                .maximumSize(caffeineProperty.getMaximumSize())
                //最后一次写入多久过期
                .expireAfterWrite(caffeineProperty.getExpireAfterWrite(), TimeUnit.SECONDS);
        cacheManager.setCaffeine(caffeine);
        //根据名字可以创建多个cache，但是多个cache使用相同的策略
        cacheManager.setCacheNames(Arrays.asList(Constants.CAFFEINE_CACHE_NAME_EXECUTOR_ALARM,
                Constants.CAFFEINE_CACHE_NAME_EXECUTOR_TASK,
                Constants.CAFFEINE_CACHE_NAME_EXECUTOR_INSTANCE));
        return cacheManager;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new IgnoreExceptionCacheErrorHandler();
    }
}
