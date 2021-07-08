package xyz.hellothomas.jedi.admin.infrastructure.exception;

/**
 * @author 80234613 唐圆
 * @date 2021/7/8 9:52
 * @descripton
 * @version 1.0
 */
public class NeedToLogin extends RuntimeException {
    public NeedToLogin() {
        super();
    }

    public NeedToLogin(String message) {
        super(message);
    }

    public NeedToLogin(String message, Throwable cause) {
        super(message, cause);
    }
}
