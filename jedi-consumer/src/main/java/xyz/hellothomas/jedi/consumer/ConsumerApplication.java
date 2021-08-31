package xyz.hellothomas.jedi.consumer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Thomas
 * @date 2020/12/27 21:25
 * @description
 * @version 1.0
 */
@EnableAsync
@EnableScheduling
@SpringBootApplication
@MapperScan(basePackages = {"xyz.hellothomas.jedi.consumer.infrastructure.mapper"})
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
