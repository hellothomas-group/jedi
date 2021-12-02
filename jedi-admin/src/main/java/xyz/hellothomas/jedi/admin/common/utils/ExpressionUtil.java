package xyz.hellothomas.jedi.admin.common.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.StringUtils;
import xyz.hellothomas.jedi.admin.infrastructure.expression.ExpressionEvaluator;

import java.lang.reflect.Method;

/**
 * @author Thomas
 * @date 2021/11/30 23:13
 * @description
 * @version 1.0
 */
public class ExpressionUtil {
    private static ExpressionEvaluator evaluator = new ExpressionEvaluator();

    private ExpressionUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Object evaluateValue(JoinPoint joinPoint, String expressionString, BeanFactory beanFactory) {
        return evaluateValue(joinPoint.getTarget(), joinPoint.getArgs(), joinPoint.getTarget().getClass(),
                ((MethodSignature) joinPoint.getSignature()).getMethod(), expressionString, beanFactory);
    }

    private static Object evaluateValue(Object object, Object[] args, Class clazz, Method method,
                                        String expressionString, BeanFactory beanFactory) {
        if (StringUtils.hasText(expressionString)) {
            EvaluationContext evaluationContext = evaluator.createEvaluationContext(object, clazz, method,
                    args, beanFactory);
            AnnotatedElementKey methodKey = new AnnotatedElementKey(method, clazz);
            return evaluator.key(expressionString, methodKey, evaluationContext);
        }

        return null;
    }
}
