package ru.names.ym_gaTool;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author kbogdanov 14.03.16
 */
public class YandexClient {

    private static final String AUTHORIZATION_URL = "https://oauth.yandex.ru/authorize";
    private static final String OAUTH_PARAM_RESPONSE_TYPE = "token";
    private static final String CLIENT_ID = "NOT_A_PASSWORD_ACTUALLY";

    private static final String TOKEN = "253d7248653e4a0fa2d78a6070fa56e6";

    private static final String API_URL_STAT = "https://api-metrika.yandex.ru/stat/v1/data/";
    private static final String API_METHOD_BYTIME = "bytime";

    private static final int YA_METRIKA_ID = NOT_A_PASSWORD_ACTUALLY;

    private static Logger logger = Logger.getLogger("YandexClient");

    /**
     * Retrieves token from api
     *
     * @return url
     */
    public String getToken() {
        return AUTHORIZATION_URL + "?response_type=" + OAUTH_PARAM_RESPONSE_TYPE + "&client_id=" + CLIENT_ID;
    }

    /**
     * Builds full url
     *
     * @param apiMethod method in api
     * @param httpQuery map of query parameters
     * @return full url
     */
    private String buildApiUrl(String apiMethod, Map<String, String> httpQuery) throws YandexClientException {
        String apiUrl = API_URL_STAT + apiMethod + '?';
        httpQuery.put("id", String.valueOf(YA_METRIKA_ID));
        httpQuery.put("oauth_token", TOKEN);

        List<String> httpQueryParams = new ArrayList<>();
        for (Map.Entry<String, String> entry : httpQuery.entrySet()) {
            try {
                httpQueryParams.add(
                        URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8")
                );
            } catch (UnsupportedEncodingException e) {
                logger.error("Failure to encode params", e);
                throw new YandexClientException("Failure to encode params", e);
            }
        }

        return apiUrl + String.join("&", httpQueryParams);
    }

    /**
     * Getting data by time from api
     *
     * @throws YandexClientException
     */
    public void getDataByTime(Date from, Date to) throws YandexClientException {
        Map<String, String> httpQuery = new HashMap<>();

        httpQuery.put("dimensions", "ym:s:searchPhrase,ym:s:paramsLevel2");
        httpQuery.put("metrics", "ym:s:visits");
        httpQuery.put(
                "filters",
                "ym:s:<attribution>SourceEngineName=='Яндекс' AND ym:s:paramsLevel1=='gaClientId'"
        );

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        httpQuery.put("date1", dateFormat.format(from));
        httpQuery.put("date2", dateFormat.format(to));
        httpQuery.put("top_keys", "30");

        logger.debug(
                "Preparing to get data by time from api. From: "
                        + httpQuery.get("date1")
                        + ", to: " + httpQuery.get("date2")
        );

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
