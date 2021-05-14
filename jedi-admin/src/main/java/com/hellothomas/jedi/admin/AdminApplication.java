package com.hellothomas.jedi.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @classname AdminApplication
 * @author Thomas
 * @date 2020/12/27 21:25
 * @description
 * @version 1.0
 */
@SpringBootApplication(scanBasePackages = "com.hellothomas.jedi.*")
@MapperScan(basePackages = {"com.hellothomas.jedi.*.infrastructure.mapper"})
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
