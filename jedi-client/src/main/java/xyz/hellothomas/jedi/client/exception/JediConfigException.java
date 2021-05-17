package xyz.hellothomas.jedi.client.exception;

public class JediConfigException extends RuntimeException {
    public JediConfigException(String message) {
        super(message);
    }

    public JediConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
