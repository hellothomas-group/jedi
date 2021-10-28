package xyz.hellothomas.jedi.consumer.domain.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @className CaffeineProperty
 * @author 80234613 唐圆
 * @date 2021/1/7 11:18
 * @descripton
 * @version 1.0
 */
@Getter
@Setter
@ToString
@ConfigurationProperties("consumer-service.caffeine")
@Component
public class CaffeineProperty {
    private int initialCapacity = 100;

    private long maximumSize = 1000;

    /**
     * 单位:s
     */
    private long expireAfterWrite = 120;
}
