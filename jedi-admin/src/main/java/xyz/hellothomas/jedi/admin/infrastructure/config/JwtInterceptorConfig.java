package xyz.hellothomas.jedi.admin.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.hellothomas.jedi.admin.application.UserService;
import xyz.hellothomas.jedi.admin.infrastructure.inteceptor.JwtAuthenticationInterceptor;

/**
 * @author 80234613 唐圆
 * @date 2021/7/8 11:33
 * @descripton
 * @version 1.0
 */
@Slf4j
@Configuration
public class JwtInterceptorConfig implements WebMvcConfigurer {
    private final UserService userService;

    public JwtInterceptorConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //默认拦截所有路径
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**");
    }

    @Bean
    public JwtAuthenticationInterceptor authenticationInterceptor() {
        return new JwtAuthenticationInterceptor(this.userService);
    }
}
