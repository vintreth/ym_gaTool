package ru.names.ym_gaTool;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author kbogdanov 14.03.16
 */
public class YandexClient {

    private static final String AUTHORIZATION_URL = "https://oauth.yandex.ru/authorize";
    private static final String OAUTH_PARAM_RESPONSE_TYPE = "code";
    private static final String CLIENT_ID = "NOT_A_PASSWORD_ACTUALLY";
    private static final String PASSWORD = "NOT_A_PASSWORD_ACTUALLY";
    private static final String CALLBACK_URL = "https://oauth.yandex.ru/verification_code";

    private static Logger logger = Logger.getLogger("YandexClient");

    public void authorize() throws YandexClientException {
        logger.debug("Starting to authorize");
        try {
            URL url = new URL(prepareOAuthUrl());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String line;
            while (null != (line = reader.readLine())) {
                System.out.println(line);
            }
        } catch (IOException e) {
            logger.error("Authorization error", e);
            throw new YandexClientException("Failure to client authorize", e);
        }
    }

    /**
     * Prepares OAuth url
     *
     * @return url
     */
    private String prepareOAuthUrl() {
        return AUTHORIZATION_URL + "?response_type=" + OAUTH_PARAM_RESPONSE_TYPE + "&client_id=" + CLIENT_ID;
    }

}
