package xyz.hellothomas.jedi.client.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Slf4j
public class JediAsyncAspect {

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
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        return result;
    }
}
