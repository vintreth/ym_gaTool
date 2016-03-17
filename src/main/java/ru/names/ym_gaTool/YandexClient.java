package ru.names.ym_gaTool;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import ru.names.ym_gaTool.api.yandex.error.ErrorResponse;
import ru.names.ym_gaTool.api.yandex.response.Table;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kbogdanov 14.03.16
 */
class YandexClient extends AbstractClient {

    private static final String AUTHORIZATION_URL = "https://oauth.yandex.ru/authorize";
    private static final String CLIENT_ID = "NOT_A_PASSWORD_ACTUALLY";
    private static final String PASSWORD = "NOT_A_PASSWORD_ACTUALLY";

    private static final String TOKEN = "253d7248653e4a0fa2d78a6070fa56e6";

    private static final String API_URL_STAT = "https://api-metrika.yandex.ru/stat/v1/data/";
    private static final String API_METHOD_BYTIME = "bytime";
    private static final String API_METHOD_TABLE = "";

    private static final int YA_METRIKA_ID = NOT_A_PASSWORD_ACTUALLY;

    private static Logger logger = Logger.getLogger("YandexClient");

    private boolean authorized = false;

    private void authorize() throws ConnectionException, ClientException {
        logger.debug("Starting to authorize");
        HttpsConnection connection = new HttpsConnection(AUTHORIZATION_URL);
        connection.addHeaders("Content-type", "application/x-www-form-urlencoded");

        byte[] secret = Base64.getEncoder().encode((CLIENT_ID + ":" + PASSWORD).getBytes());
        String s = new String(secret);
        connection.addHeaders("Authorization", "Basic " + new String(secret));

        logger.debug("Sending authorization request");
        connection.doPost("");
        String r = getResponse(connection.getInputStream());

        System.out.println(r);
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
    public Table getClientPhraseTable(Date from, Date to) throws ClientException, HttpException, ConnectionException {
        logger.debug("Retrieving client phrases");
        if (!authorized) {
            logger.debug("Not authorized");
            authorize();
        }

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
        HttpURLConnection connection = makeGetRequest(apiUrl);
        String response = getResponse(connection);
        try {
            if (HTTP_STATUS_OK != connection.getResponseCode()) {
                throw new HttpException(connection.getResponseCode(), response);
            }
        } catch (IOException e) {
            String msg = "Failure to get response code";
            logger.error(msg, e);
            throw new ClientException(msg, e);
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

    /**
     * Retrieves ErrorResponse object
     *
     * @param json json-formatted string
     */
    public ErrorResponse getErrorResponse(String json) {
        try {
            logger.debug("Parsing error json");
            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(json, ErrorResponse.class);
        } catch (IOException e) {
            String msg = "Failure to parse error json";
            logger.error(msg, e);
        }

        return null;
    }

}
