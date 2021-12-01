package xyz.hellothomas.jedi.admin.common.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
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

    public static Object getKeyValue(JoinPoint joinPoint, String keyExpression, BeanFactory beanFactory) {
        return getKeyValue(joinPoint.getTarget(), joinPoint.getArgs(), joinPoint.getTarget().getClass(),
                ((MethodSignature) joinPoint.getSignature()).getMethod(), keyExpression, beanFactory);
    }

    public static boolean evaluateAsBoolean(JoinPoint joinPoint, String expressionString, BeanFactory beanFactory) {
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(expressionString);

        EvaluationContext evaluationContext = evaluator.createEvaluationContext(joinPoint.getTarget(),
                joinPoint.getTarget().getClass(), ((MethodSignature) joinPoint.getSignature()).getMethod(),
                joinPoint.getArgs(), beanFactory);

        try {
            return expression.getValue(evaluationContext, Boolean.class).booleanValue();
        } catch (EvaluationException e) {
            throw new IllegalArgumentException("Failed to evaluate expression '"
                    + expression.getExpressionString() + "'", e);
        }
    }

    private static Object getKeyValue(Object object, Object[] args, Class clazz, Method method,
                                      String keyExpression, BeanFactory beanFactory) {
        if (StringUtils.hasText(keyExpression)) {
            EvaluationContext evaluationContext = evaluator.createEvaluationContext(object, clazz, method,
                    args, beanFactory);
            AnnotatedElementKey methodKey = new AnnotatedElementKey(method, clazz);
            return evaluator.key(keyExpression, methodKey, evaluationContext);
        }

        return null;
    }
}
