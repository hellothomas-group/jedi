package xyz.hellothomas.jedi.collector;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Thomas
 * @date 2020/12/27 21:25
 * @description
 * @version 1.0
 */
@EnableCaching
@EnableAsync
@EnableScheduling
@SpringBootApplication(scanBasePackages = "xyz.hellothomas.jedi.*")
@MapperScan(basePackages = {"xyz.hellothomas.jedi.*.infrastructure.mapper"})
public class CollectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CollectorApplication.class, args);
    }
}
