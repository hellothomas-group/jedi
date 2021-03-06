package xyz.hellothomas.jedi.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Thomas
 * @date 2020/12/27 21:25
 * @description
 * @version 1.0
 */
@SpringBootApplication(scanBasePackages = "xyz.hellothomas.jedi.*")
@MapperScan(basePackages = {"xyz.hellothomas.jedi.*.infrastructure.mapper"})
public class ConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication.class, args);
    }
}
