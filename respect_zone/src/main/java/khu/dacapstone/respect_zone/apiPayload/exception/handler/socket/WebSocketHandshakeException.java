package khu.dacapstone.respect_zone.apiPayload.exception.handler.socket;

public class WebSocketHandshakeException extends RuntimeException {
    public WebSocketHandshakeException(String message) {
        super(message);
    }

    public WebSocketHandshakeException(String message, Throwable cause) {
        super(message, cause);
    }
}