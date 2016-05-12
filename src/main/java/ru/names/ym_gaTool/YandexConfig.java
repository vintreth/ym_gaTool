package ru.names.ym_gaTool;

/**
 * Config implementation for yandex client
 *
 * @author kbogdanov 12.05.16
 */
class YandexConfig implements JsonConfiguration {

    /** Name of config file in the root directory */
    private static final String FILE_NAME = "yandex.cfg.json";

    /** Base url for authorization and getting token */
    private String authorizationUrl;
    /** Yandex application identifier */
    private String clientId;
    /** Yandex application password */
    private String password;
    /** Yandex metrika counter identifier */
    private int yandexMetrikaId;

    public YandexConfig() {}

    @Override
    public String getFileName() {
        return FILE_NAME;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getYandexMetrikaId() {
        return yandexMetrikaId;
    }

    public void setYandexMetrikaId(int yandexMetrikaId) {
        this.yandexMetrikaId = yandexMetrikaId;
    }
}
