package xyz.hellothomas.jedi.collector.domain.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @className CaffeineProperty
 * @author Thomas
 * @date 2021/1/7 11:18
 * @descripton
 * @version 1.0
 */
@Getter
@Setter
@ToString
@ConfigurationProperties("collector-service.caffeine")
@Component
public class CaffeineProperty {
    private int initialCapacity = 100;

    private long maximumSize = 1000;

    /**
     * 单位:s
     */
    private long expireAfterWrite = 3600;
}
