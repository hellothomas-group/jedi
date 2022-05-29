package xyz.hellothomas.jedi.collector.infrastructure.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

/**
 * @className IgnoreExceptionCacheErrorHandler
 * @author Thomas
 * @date 2020/12/7 22:22
 * @description
 * @version 1.0
 */
@Slf4j
public class IgnoreExceptionCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        handleCacheException(exception, key);
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        handleCacheException(exception, key);
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        handleCacheException(exception, key);
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        handleCacheException(exception, null);
    }

    private void handleCacheException(Exception exception, Object key) {
        log.error("cache异常：key=[{}]", key, exception);
    }

}
