package ru.names.ym_gaTool.configuration;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author kbogdanov 24.06.16
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExceptionHandlerConfig implements JsonConfiguration {

    /**
     * Name of config file in the root directory
     */
    private static final String FILE_NAME = "exceptionHandler.cfg.json";

    private boolean notificationsEnabled;

    private String[] recipients = new String[]{"development@names.ru"};

    private String fromMail;

    private String mailSubject;

    private String mailText;

    public ExceptionHandlerConfig() {}

    @Override
    public String getFileName() {
        return FILE_NAME;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public String[] getRecipients() {
        return recipients;
    }

    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }

    public String getFromMail() {
        return fromMail;
    }

    public void setFromMail(String fromMail) {
        this.fromMail = fromMail;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailText() {
        return mailText;
    }

    public void setMailText(String mailText) {
        this.mailText = mailText;
    }
}
