package ru.names.ym_gaTool;

/**
 * @author kbogdanov 24.06.16
 */
class MailSenderException extends BaseException {

    public MailSenderException(String message) {
        super(message);
    }

    public MailSenderException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailSenderException(Throwable cause) {
        super(cause);
    }

}
