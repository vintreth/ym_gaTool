package ru.names.ym_gaTool;

/**
 * @author kbogdanov 17.03.16
 */
class HttpConnectionException extends BaseException {

    public HttpConnectionException(String message) {
        super(message);
    }

    public HttpConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpConnectionException(Throwable cause) {
        super(cause);
    }
}
