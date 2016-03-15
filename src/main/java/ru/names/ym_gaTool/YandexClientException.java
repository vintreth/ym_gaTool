package ru.names.ym_gaTool;

/**
 * @author kbogdanov 14.03.16
 */
public class YandexClientException extends Throwable {
    public YandexClientException(String s) {
        super(s);
    }

    public YandexClientException(String s, Throwable t) {
        super(s, t);
    }

    public YandexClientException(Throwable t) {
        super(t);
    }
}
