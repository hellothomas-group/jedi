package xyz.hellothomas.jedi.client.annotation;

import org.springframework.core.annotation.AliasFor;
import xyz.hellothomas.jedi.client.constants.Constants;

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
    String value() default Constants.JEDI_DEFAULT_EXECUTOR_NAME;

    /**
     * 线程池名称
     * @return
     */
    @AliasFor("value")
    String executorName() default Constants.JEDI_DEFAULT_EXECUTOR_NAME;

    /**
     * 任务名称
     * @return
     */
    String taskName() default "";

    /**
     * 任务附件信息
     * @return
     */
    String taskExtraData() default "";
}
