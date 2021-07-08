package xyz.hellothomas.jedi.admin.infrastructure.exception;

/**
 * @author 80234613 唐圆
 * @date 2021/7/8 9:52
 * @descripton
 * @version 1.0
 */
public class TokenUnavailable extends RuntimeException {
    public TokenUnavailable() {
        super();
    }

    public TokenUnavailable(String message) {
        super(message);
    }

    public TokenUnavailable(String message, Throwable cause) {
        super(message, cause);
    }
}
