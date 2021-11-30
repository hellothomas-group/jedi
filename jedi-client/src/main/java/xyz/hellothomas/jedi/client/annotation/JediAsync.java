package xyz.hellothomas.jedi.client.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface JediAsync {

    /**
     * must specify when multiple jediExecutors in ioc
     * <p>Default is {@code ""}, use the only one executorName in ioc (default executorName
     * {@link xyz.hellothomas.jedi.client.constants.Constants#JEDI_DEFAULT_EXECUTOR_NAME}
     * when no jediExecutor config)
     *
     * 线程池名称
     * @return
     */
    @AliasFor("executorName")
    String value() default "";

    /**
     * must specify when multiple jediExecutors in ioc
     * <p>Default is {@code ""}, use the only one executorName in ioc (default executorName
     * {@link xyz.hellothomas.jedi.client.constants.Constants#JEDI_DEFAULT_EXECUTOR_NAME}
     * when no jediExecutor config)
     *
     * 线程池名称
     * @return
     */
    @AliasFor("value")
    String executorName() default "";

    /**
     * Spring Expression Language (SpEL) expression for computing the key dynamically.
     * <p>Default is {@code ""}, use the default taskName
     * {@link xyz.hellothomas.jedi.core.constants.Constants#JEDI_DEFAULT_TASK_NAME}.
     *
     * 任务名称
     * @return
     */
    String taskName() default "";

    /**
     * Spring Expression Language (SpEL) expression for computing the key dynamically.
     *
     * 任务附加信息
     * @return
     */
    String taskExtraData() default "";
}
