package ru.names.ym_gaTool;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kbogdanov 14.03.16
 */
public class YandexClient {

    private static final String AUTHORIZATION_URL = "https://oauth.yandex.ru/authorize";
    private static final String OAUTH_PARAM_RESPONSE_TYPE = "token";
    private static final String CLIENT_ID = "4d3195c45b994736bf868c4b493f7a17";

    private static final String PASSWORD = "0df451083b7941e597ea5d5c5b971ac2";
    private static final String CALLBACK_URL = "https://oauth.yandex.ru/verification_code";
    private static final String TOKEN = "253d7248653e4a0fa2d78a6070fa56e6";

    private static final String API_URL_STAT = "https://api-metrika.yandex.ru/stat/v1/data/";
    private static final String API_METHOD_BYTIME = "bytime";

    private static final int YA_METRIKA_ID = 17520310;

    private static Logger logger = Logger.getLogger("YandexClient");

    //todo write code
    public void authorize() throws YandexClientException {
        logger.debug("Starting to authorize");
        try {
            String OAuthUrl = prepareOAuthUrl();
            logger.debug("Sending a request to " + OAuthUrl);

            URL url = new URL(OAuthUrl);
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

    /**
     * Builds full url
     *
     * @param apiMethod method in api
     * @param httpQuery map of query parameters
     * @return full url
     */
    private String buildApiUrl(String apiMethod, Map<String, String> httpQuery) {
        String apiUrl = API_URL_STAT + apiMethod + '?';
        httpQuery.put("id", String.valueOf(YA_METRIKA_ID));
        httpQuery.put("oauth_token", TOKEN);

        List<String> httpQueryParams = new ArrayList<>();
        for (Map.Entry<String, String> entry : httpQuery.entrySet()) {
            httpQueryParams.add(entry.getKey() + "=" + entry.getValue());
        }

        return apiUrl + String.join("&", httpQueryParams);
    }

    /**
     * Getting data by time from api
     *
     * @throws YandexClientException
     */
    public void getDataByTime() throws YandexClientException {
        logger.debug("Preparing to get data from api");

        Map<String, String> httpQuery = new HashMap<>();
        try {
            httpQuery.put("dimensions", URLEncoder.encode("ym:s:<attribution>SearchPhrase", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("Failure to encode params", e);
            throw new YandexClientException("Failure to encode params", e);
        }

        String apiUrl = buildApiUrl(API_METHOD_BYTIME, httpQuery);

        logger.debug(getData(apiUrl));
    }

    /**
     * Sending a request to current api url
     *
     * @param apiUrl full url to the server
     * @return string formatted response from server
     * @throws YandexClientException
     */
    private String getData(String apiUrl) throws YandexClientException {
        String data = "";
        try {
            logger.debug("Sending a request to " + apiUrl);
            URL url = new URL(apiUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String line;
            while (null != (line = reader.readLine())) {
                data += line;
            }
        } catch (IOException e) {
            String msg = "Failure to get data from api";
            logger.error(msg, e);
            throw new YandexClientException(msg, e);
        }

        return data;
    }

}
