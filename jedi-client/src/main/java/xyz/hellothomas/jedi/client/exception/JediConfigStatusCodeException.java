package xyz.hellothomas.jedi.client.exception;

public class JediConfigStatusCodeException extends RuntimeException {
    private final int m_statusCode;

    public JediConfigStatusCodeException(int statusCode, String message) {
        super(String.format("[status code: %d] %s", statusCode, message));
        this.m_statusCode = statusCode;
    }

    public JediConfigStatusCodeException(int statusCode, Throwable cause) {
        super(cause);
        this.m_statusCode = statusCode;
    }

    public int getStatusCode() {
        return m_statusCode;
    }
}
