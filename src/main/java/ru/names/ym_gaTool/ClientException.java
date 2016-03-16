package ru.names.ym_gaTool;

/**
 * @author kbogdanov 14.03.16
 */
class ClientException extends BaseException {

    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientException(Throwable cause) {
        super(cause);
    }
}
