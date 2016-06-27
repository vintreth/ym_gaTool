package ru.names.ym_gaTool;

import org.apache.log4j.Logger;
import ru.names.ym_gaTool.configuration.ExceptionHandlerConfig;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Handling top level application exceptions and sending notifications
 *
 * @author kbogdanov 24.06.16
 */
class ExceptionHandler {

    private String errorMessage;
    private Throwable cause;

    private static Logger logger = Logger.getLogger("ExceptionHandler");

    /**
     * @param errorMessage error message
     * @param cause        any exception
     */
    public ExceptionHandler(String errorMessage, Throwable cause) {
        this.errorMessage = errorMessage;
        this.cause = cause;
    }

    /**
     * Handles an error
     */
    public void handleCause() {
        logger.error(errorMessage, cause);
        sendNotifications();
    }

    /**
     * Handles a fatal error
     * In that case the application must be stopped
     */
    public void handleFatalCause() {
        logger.fatal(errorMessage, cause);
        sendNotifications();

        logger.fatal("Exit 1");
        Application.getInstance().stop();
    }

    /**
     * Notifies about exceptions to mail addresses contained in exception handler config
     */
    public void sendNotifications() {
        ExceptionHandlerConfig config = Application.getInstance().getConfigurationManager().getExceptionHandlerConfig();
        if (config.isNotificationsEnabled()) {
            logger.debug("Sending notifications");

            StringWriter errors = new StringWriter();
            cause.printStackTrace(new PrintWriter(errors));
            String stackTrace = errors.toString();

            MailSender.Context context = new MailSender.Context(
                    config.getFromMail(),
                    config.getRecipients(),
                    String.format(config.getMailSubject(), cause.getClass().getName()),
                    String.format(config.getMailText(), stackTrace)
            );
            MailSender mailSender = new MailSender(
                    Application.getInstance().getConfigurationManager().getMailSenderConfig(),
                    context
            );

            try {
                mailSender.send();
                logger.debug("The mail has been sent successfully");
            } catch (MailSenderException e) {
                logger.error("Failure to send mails", e);
            }
        }
    }

}
