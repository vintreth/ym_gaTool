package ru.names.ym_gaTool.configuration;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author kbogdanov 27.06.16
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MailSenderConfig implements JsonConfiguration {

    private String userName;

    private String password;

    public MailSenderConfig() {}

    @Override
    public String getFileName() {
        return "mailSender.cfg.json";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
