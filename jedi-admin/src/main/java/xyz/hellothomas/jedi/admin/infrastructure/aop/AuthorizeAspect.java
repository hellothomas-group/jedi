package xyz.hellothomas.jedi.admin.infrastructure.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationException;
import xyz.hellothomas.jedi.admin.common.enums.AdminErrorCodeEnum;
import xyz.hellothomas.jedi.admin.common.utils.ExpressionUtil;
import xyz.hellothomas.jedi.admin.infrastructure.annotation.PreAuthorize;
import xyz.hellothomas.jedi.core.exception.BusinessException;

/**
 * @author Thomas
 * @date 2021/11/30 22:40
 * @description
 * @version 1.0
 */
@Aspect
@Slf4j
public class AuthorizeAspect implements BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Pointcut("@annotation(xyz.hellothomas.jedi.admin.infrastructure.annotation.PreAuthorize)")
    public void annotationPointcut() {
        // 仅定义切点
    }

    @Before("annotationPointcut()")
    public void authorizeBefore(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        PreAuthorize preAuthorize = AnnotationUtils.getAnnotation(methodSignature.getMethod(), PreAuthorize.class);

        if (StringUtils.isBlank(preAuthorize.value())) {
            throw new IllegalArgumentException("@preAuthorize value is blank");
        }

        boolean isPassAuth;
        try {
            isPassAuth = (Boolean) ExpressionUtil.evaluateValue(joinPoint, preAuthorize.value(), beanFactory);
        } catch (EvaluationException e) {
            throw new IllegalArgumentException("Failed to evaluate expression '"
                    + preAuthorize.value() + "'", e);
        }

        if (!isPassAuth) {
            throw new BusinessException(AdminErrorCodeEnum.AUTHORIZATION_NOT_PASS);
        }

    }
}
