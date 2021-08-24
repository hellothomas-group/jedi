package xyz.hellothomas.jedi.client.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import xyz.hellothomas.jedi.client.annotation.JediAsync;
import xyz.hellothomas.jedi.client.constants.Constants;
import xyz.hellothomas.jedi.core.internals.executor.JediRunnable;
import xyz.hellothomas.jedi.core.internals.executor.JediThreadPoolExecutor;

import java.util.Map;

@Aspect
@Slf4j
public class JediAsyncAspect {
    private final Map<String, JediThreadPoolExecutor> executorMap;
    private final JediThreadPoolExecutor uniqueExecutor;

    public JediAsyncAspect(Map<String, JediThreadPoolExecutor> executorMap) {
        this.executorMap = executorMap;
        if (executorMap.size() == 1) {
            uniqueExecutor = executorMap.values().stream().findFirst().get();
        } else {
            uniqueExecutor = null;
        }
    }

    @Pointcut("@annotation(xyz.hellothomas.jedi.client.annotation.JediAsync))")
    public void jediAsyncAnnotationPointcut() {
        // 仅定义切点
    }

    @AfterThrowing(pointcut = "jediAsyncAnnotationPointcut()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error("Exception in {}.{}() with cause = \'{}\' and exception = \'{}\'",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), null != ex.getCause() ? ex.getCause() : "NULL", ex.getMessage(),
                ex);
    }

    @Around("jediAsyncAnnotationPointcut()")
    public void logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        JediAsync jediAsync = AnnotationUtils.getAnnotation(methodSignature.getMethod(), JediAsync.class);

        JediThreadPoolExecutor jediThreadPoolExecutor;
        if (StringUtils.isNotBlank(jediAsync.executorName())) {
            jediThreadPoolExecutor = executorMap.get(jediAsync.executorName());
            if (jediThreadPoolExecutor == null) {
                throw new RuntimeException(String.format("未配置@JediAsync指定的线程池:%s", jediAsync.executorName()));
            }
        } else {
            // only one executor
            if (uniqueExecutor != null) {
                jediThreadPoolExecutor = uniqueExecutor;
            } else {
                throw new RuntimeException(String.format("容器中有 %d 个线程池, 需在@JediAsync中指定", executorMap.size()));
            }
        }

        String taskName = StringUtils.isBlank(jediAsync.taskName()) ? Constants.JEDI_DEFAULT_TASK_NAME :
                jediAsync.taskName();

        jediThreadPoolExecutor.execute(new JediRunnable(jediThreadPoolExecutor, taskName, jediAsync.taskExtraData(),
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
}
