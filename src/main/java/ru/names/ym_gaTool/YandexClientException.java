package ru.names.ym_gaTool;

/**
 * @author kbogdanov 14.03.16
 */
class YandexClientException extends BaseException {

    public YandexClientException(String message) {
        super(message);
    }

    public YandexClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public YandexClientException(Throwable cause) {
        super(cause);
    }
}
