package xyz.hellothomas.jedi.demo.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {
    private String prefix;
    private String basePackage;
    private String title;
    private String description;
    private String version;
    private Contact contact;
    private String host;

    @Getter
    @Setter
    public static class Contact {
        private String name;
        private String url;
        private String email;
    }

}
