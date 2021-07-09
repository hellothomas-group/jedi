package xyz.hellothomas.jedi.biz.infrastructure.config;

import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@EnableSwagger2
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
@ConditionalOnMissingBean(Docket.class)
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public Docket createReadRestApi(SwaggerProperties swaggerProperties) {
        ApiInfo readApiInfo = this.apiInfo(swaggerProperties);
        return createRestApi(readApiInfo, swaggerProperties.getHost(), swaggerProperties.getPrefix(),
                swaggerProperties.getBasePackage());
    }


    public Docket createRestApi(ApiInfo apiInfo, String swaggerHost, String prefixPath, String basePackage) {
        ParameterBuilder pb = new ParameterBuilder();
        Parameter factorParam = pb
                .parameterType("header")
                .name("Authorization")
                .defaultValue("")
                .description("请输入token")
                .modelRef(new ModelRef("string"))
                .required(false).build();
        List<Parameter> parameterList = Lists.newArrayList(factorParam);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .globalOperationParameters(parameterList)
                .host(swaggerHost)
                .pathMapping(prefixPath)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(SwaggerProperties apiInfo) {
        return new ApiInfoBuilder()
                .title(apiInfo.getTitle())
                .description(apiInfo.getDescription())
                .version(apiInfo.getVersion())
                .contact(new Contact(apiInfo.getContact().getName(), apiInfo.getContact().getUrl(),
                        apiInfo.getContact().getEmail()))
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

    }

}
