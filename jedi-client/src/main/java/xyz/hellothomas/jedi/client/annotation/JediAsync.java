package xyz.hellothomas.jedi.client.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JediAsync {

    /**
     * 线程池名称
     * @return
     */
    @AliasFor("executorName")
    String value() default "";

    /**
     * 线程池名称
     * @return
     */
    @AliasFor("value")
    String executorName() default "";

    /**
     * 任务名称
     * @return
     */
    String taskName() default "";

    /**
     * 任务附加信息
     * @return
     */
    String taskExtraData() default "";
}
