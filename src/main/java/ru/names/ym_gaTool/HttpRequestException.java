package ru.names.ym_gaTool;

/**
 * @author kbogdanov 17.03.16
 */
class HttpRequestException extends BaseException {

    public HttpRequestException(String message) {
        super(message);
    }

    public HttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpRequestException(Throwable cause) {
        super(cause);
    }
}
