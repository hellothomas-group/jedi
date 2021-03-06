package xyz.hellothomas.jedi.admin.infrastructure.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface UserLoginToken {
    boolean required() default true;
}
