package ru.names.ym_gaTool;

/**
 * Main data, that is exchanged from  yandex metrics to google analytics
 *
 * @author kbogdanov 18.03.16
 */
class ClientPhrase {

    private String clientId;
    private String keyWord;

    public ClientPhrase(String clientId, String keyWord) {
        this.clientId = clientId;
        this.keyWord = keyWord;
    }

    public String getClientId() {
        return clientId;
    }

    public String getKeyWord() {
        return keyWord;
    }
}
