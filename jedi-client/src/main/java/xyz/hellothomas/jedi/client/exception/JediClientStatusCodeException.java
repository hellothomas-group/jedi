package xyz.hellothomas.jedi.client.exception;

public class JediClientStatusCodeException extends RuntimeException {
    private final int m_statusCode;

    public JediClientStatusCodeException(int statusCode, String message) {
        super(String.format("[status code: %d] %s", statusCode, message));
        this.m_statusCode = statusCode;
    }

    public JediClientStatusCodeException(int statusCode, Throwable cause) {
        super(cause);
        this.m_statusCode = statusCode;
    }

    public int getStatusCode() {
        return m_statusCode;
    }
}
