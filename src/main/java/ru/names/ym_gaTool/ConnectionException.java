package ru.names.ym_gaTool;

/**
 * @author kbogdanov 17.03.16
 */
class ConnectionException extends BaseException {

    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionException(Throwable cause) {
        super(cause);
    }
}
