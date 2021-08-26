package xyz.hellothomas.jedi.client.expression;

/**
 * @author 80234613 唐圆
 * @date 2021/8/26 11:08
 * @descripton
 * @version 1.0
 */
public class ExpressionRootObject {

    private final Object object;
    private final Object[] args;

    public ExpressionRootObject(Object object, Object[] args) {
        this.object = object;
        this.args = args;
    }

    public Object getObject() {
        return object;
    }

    public Object[] getArgs() {
        return args;
    }
}
