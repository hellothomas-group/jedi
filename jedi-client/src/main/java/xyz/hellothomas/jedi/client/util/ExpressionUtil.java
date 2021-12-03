package xyz.hellothomas.jedi.client.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.StringUtils;
import xyz.hellothomas.jedi.client.expression.ExpressionEvaluator;

import java.lang.reflect.Method;

/**
 * @author Thomas
 * @date 2021/8/26 11:23
 * @descripton
 * @version 1.0
 */
public class ExpressionUtil {

    private static ExpressionEvaluator evaluator = new ExpressionEvaluator();

    private ExpressionUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Object evaluateValue(JoinPoint joinPoint, String keyExpression, BeanFactory beanFactory) {
        return evaluateValue(joinPoint.getTarget(), joinPoint.getArgs(), joinPoint.getTarget().getClass(),
                ((MethodSignature) joinPoint.getSignature()).getMethod(), keyExpression, beanFactory);
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
