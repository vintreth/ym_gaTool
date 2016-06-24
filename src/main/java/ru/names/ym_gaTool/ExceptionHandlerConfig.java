package ru.names.ym_gaTool;

/**
 * @author kbogdanov 24.06.16
 */
class ExceptionHandlerConfig implements JsonConfiguration {

    /**
     * Name of config file in the root directory
     */
    private static final String FILE_NAME = "exceptionHandler.cfg.json";

    private boolean notificationsEnabled;

    private String[] mails = new String[]{"development@names.ru"};

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

    public String[] getMails() {
        return mails;
    }

    public void setMails(String[] mails) {
        this.mails = mails;
    }
}
