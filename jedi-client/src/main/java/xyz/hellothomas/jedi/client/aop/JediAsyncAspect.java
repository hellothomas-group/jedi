package xyz.hellothomas.jedi.client.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import xyz.hellothomas.jedi.client.annotation.JediAsync;
import xyz.hellothomas.jedi.client.model.JediConfig;
import xyz.hellothomas.jedi.client.exception.JediClientException;
import xyz.hellothomas.jedi.client.util.ExpressionUtil;
import xyz.hellothomas.jedi.core.internals.executor.JediRunnable;
import xyz.hellothomas.jedi.core.internals.executor.JediThreadPoolExecutor;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static xyz.hellothomas.jedi.core.constants.Constants.JEDI_DEFAULT_TASK_NAME;

@Aspect
@Slf4j
public class JediAsyncAspect implements ApplicationContextAware, InitializingBean, Ordered {
    private Map<String, JediThreadPoolExecutor> executorMap;
    private JediThreadPoolExecutor uniqueExecutor;
    private int order;
    private ApplicationContext applicationContext;

    @Pointcut("@annotation(xyz.hellothomas.jedi.client.annotation.JediAsync)")
    public void annotationPointcut() {
        // 仅定义切点
    }

    @Around("annotationPointcut()")
    public void jediAsyncAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        JediAsync jediAsync = AnnotationUtils.getAnnotation(methodSignature.getMethod(), JediAsync.class);

        JediThreadPoolExecutor jediThreadPoolExecutor = extractJediThreadPoolExecutor(jediAsync);
        String taskName = extractTaskName(joinPoint, jediAsync);
        String taskExtraData = extractTaskExtraData(joinPoint, jediAsync);

        jediThreadPoolExecutor.execute(new JediRunnable(jediThreadPoolExecutor, taskName, taskExtraData,
                () -> {
                    try {
                        joinPoint.proceed();
                    } catch (Throwable throwable) {
                        log.error("Exception in {}.{}() with cause = \'{}\' and exception = \'{}\'",
                                joinPoint.getSignature().getDeclaringTypeName(),
                                joinPoint.getSignature().getName(), null != throwable.getCause() ?
                                        throwable.getCause() :
                                        "NULL", throwable.getMessage(), throwable);
                    }
                }));
    }

    private JediThreadPoolExecutor extractJediThreadPoolExecutor(JediAsync jediAsync) {
        JediThreadPoolExecutor jediThreadPoolExecutor;
        if (StringUtils.isNotBlank(jediAsync.executorName())) {
            jediThreadPoolExecutor = executorMap.get(jediAsync.executorName());
            if (jediThreadPoolExecutor == null) {
                throw new JediClientException(String.format("未配置@JediAsync指定的线程池:%s", jediAsync.executorName()));
            }
        } else {
            // only one executor
            if (uniqueExecutor != null) {
                jediThreadPoolExecutor = uniqueExecutor;
            } else {
                throw new JediClientException(String.format("容器中有 %d 个线程池, 需在@JediAsync中指定", executorMap.size()));
            }
        }
        return jediThreadPoolExecutor;
    }

    private String extractTaskName(ProceedingJoinPoint joinPoint, JediAsync jediAsync) {
        String taskName;
        if (StringUtils.isBlank(jediAsync.taskName())) {
            taskName = JEDI_DEFAULT_TASK_NAME;
        } else {
            try {
                Object taskNameObject = ExpressionUtil.evaluateValue(joinPoint, jediAsync.taskName(),
                        this.applicationContext);
                if (taskNameObject == null) {
                    throw new JediClientException(String.format("@JediAsync taskName:%s, cannot be null",
                            jediAsync.taskName()));
                }
                taskName = StringUtils.isBlank(taskNameObject.toString()) ? JEDI_DEFAULT_TASK_NAME :
                        taskNameObject.toString();
            } catch (JediClientException e1) {
                throw e1;
            } catch (Exception e) {
                throw new JediClientException(String.format("@JediAsync taskName:%s, SpEL Expression exception:%s",
                        jediAsync.taskName(), e));
            }
        }
        return taskName;
    }

    private String extractTaskExtraData(ProceedingJoinPoint joinPoint, JediAsync jediAsync) {
        String taskExtraData;
        if (StringUtils.isBlank(jediAsync.taskExtraData())) {
            taskExtraData = EMPTY;
        } else {
            try {
                Object taskExtraDataObject = ExpressionUtil.evaluateValue(joinPoint, jediAsync.taskExtraData(),
                        this.applicationContext);
                if (taskExtraDataObject == null) {
                    throw new JediClientException(String.format("@JediAsync taskExtraData:%s, cannot be null",
                            jediAsync.taskExtraData()));
                }
                taskExtraData = StringUtils.isBlank(taskExtraDataObject.toString()) ? EMPTY :
                        taskExtraDataObject.toString();
            } catch (JediClientException e1) {
                throw e1;
            } catch (Exception e) {
                throw new JediClientException(String.format("@JediAsync taskExtraData:%s, SpEL Expression exception:%s",
                        jediAsync.taskExtraData(), e));
            }
        }
        return taskExtraData;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.executorMap =
                this.applicationContext.getBeansOfType(JediThreadPoolExecutor.class);
        JediConfig jediConfig = this.applicationContext.getBean(JediConfig.class);
        this.order = jediConfig.getOrder();
        if (executorMap.size() == 1) {
            uniqueExecutor = executorMap.values().stream().findFirst().get();
        } else {
            uniqueExecutor = null;
        }
    }
}
