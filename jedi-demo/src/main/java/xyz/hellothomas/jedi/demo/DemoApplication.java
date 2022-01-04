package xyz.hellothomas.jedi.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import xyz.hellothomas.jedi.client.model.JediProperty;

/**
 * @author Thomas
 * @date 2021/11/3 21:34
 * @description
 * @version 1.0
 */
//@EnableScheduling
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        System.out.println(context.getBean(JediProperty.class));
    }
}
