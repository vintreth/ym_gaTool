package ru.names.ym_gaTool;

import org.apache.log4j.Logger;

/**
 * Handling top level application exceptions and sending notifications
 *
 * @author kbogdanov 24.06.16
 */
class ExceptionHandler {

    private ExceptionHandlerConfig config;
    private String errorMessage;
    private Throwable cause;

    private static Logger logger = Logger.getLogger("ExceptionHandler");

    public ExceptionHandler(ExceptionHandlerConfig config, String errorMessage, Throwable cause) {
        this.config = config;
        this.errorMessage = errorMessage;
        this.cause = cause;
    }

    public void handleCause() {
        logger.error(errorMessage, cause);
        sendNotifications();
    }

    public void sendNotifications() {
        if (config.isNotificationsEnabled()) {
            //MailSender mailSender = new MailSender();
        }
    }

}
