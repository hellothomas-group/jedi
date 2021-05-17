package xyz.hellothomas.jedi.client.exception;

public class JediClientException extends RuntimeException {
    public JediClientException(String message) {
        super(message);
    }

    public JediClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
