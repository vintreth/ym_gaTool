package ru.names.ym_gaTool;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import ru.names.ym_gaTool.api.response.yandex.Table;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author kbogdanov 14.03.16
 */
class YandexClient extends AbstractClient {

    private static final String AUTHORIZATION_URL = "https://oauth.yandex.ru/authorize";
    private static final String OAUTH_PARAM_RESPONSE_TYPE = "token";
    private static final String CLIENT_ID = "4d3195c45b994736bf868c4b493f7a17";

    private static final String TOKEN = "253d7248653e4a0fa2d78a6070fa56e6";

    private static final String API_URL_STAT = "https://api-metrika.yandex.ru/stat/v1/data/";
    private static final String API_METHOD_BYTIME = "bytime";
    private static final String API_METHOD_TABLE = "";

    private static final int YA_METRIKA_ID = 17520310;

    private static Logger logger = Logger.getLogger("YandexClient");

    private void authorize() {
    }

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
    private String buildApiUrl(String apiMethod, Map<String, String> httpQuery) throws ClientException {
        String apiUrl = API_URL_STAT + apiMethod + '?';
        httpQuery.put("id", String.valueOf(YA_METRIKA_ID));
        httpQuery.put("oauth_token", TOKEN);

        return apiUrl + buildHttpQuery(httpQuery);
    }

    /**
     * Getting client ids and search phrases from api
     *
     * @throws ClientException
     */
    public Table getClientPhraseTable(Date from, Date to) throws ClientException {
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
        //todo debug
        httpQuery.put("limit", "3");

        logger.debug(
                "Preparing to get data by time from api. From: "
                        + httpQuery.get("date1")
                        + ", to: " + httpQuery.get("date2")
        );

        String apiUrl = buildApiUrl(API_METHOD_TABLE, httpQuery);
        String response = makeGetRequest(apiUrl);
        if (response.isEmpty()) {
            throw new ClientException("Empty content");
        }

        logger.debug("Parsing a json");
        ObjectMapper objectMapper = new ObjectMapper();
        Table table;
        try {
            table = objectMapper.readValue(response, Table.class);
        } catch (IOException e) {
            String msg = "Failure to parse json";
            logger.error(msg, e);
            throw new ClientException(msg, e);
        }

        return table;
    }

}
