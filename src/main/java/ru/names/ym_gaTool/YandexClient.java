package ru.names.ym_gaTool;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import ru.names.ym_gaTool.api.yandex.error.E;
import ru.names.ym_gaTool.api.yandex.error.ErrorResponse;
import ru.names.ym_gaTool.api.yandex.response.Table;

import java.io.IOException;
import java.io.InputStream;
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
    private static final String CLIENT_ID = "4d3195c45b994736bf868c4b493f7a17";
    private static final String PASSWORD = "0df451083b7941e597ea5d5c5b971ac2";

    private static final String TOKEN = "253d7248653e4a0fa2d78a6070fa56e6";

    private static final String API_URL_STAT = "https://api-metrika.yandex.ru/stat/v1/data/";
    private static final String API_METHOD_BYTIME = "bytime";
    private static final String API_METHOD_TABLE = "";

    private static final int YA_METRIKA_ID = 17520310;

    private static Logger logger = Logger.getLogger("YandexClient");

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
    public Table getClientPhraseTable(Date from, Date to) throws ClientException, HttpConnectionException {
        logger.debug("Retrieving client phrases");

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
        //todo debug httpQuery.put("limit", "3");

        logger.debug(
                "Preparing to get data by time from api. From: "
                        + httpQuery.get("date1")
                        + ", to: " + httpQuery.get("date2")
        );

        AbstractHttpConnection connection = new HttpsConnection(buildApiUrl(API_METHOD_TABLE, httpQuery));
        connection.doGet();
        InputStream inputStream = connection.getInputStream();
        if (null != inputStream) {
            String response = getResponse(connection.getInputStream());

            if (connection.isError()) {
                logger.fatal("Got error response from yandex api. Application will be stopped.");
                if (!response.isEmpty()) {
                    ErrorResponse errorResponse = getErrorResponse(response);
                    if (null != errorResponse) {
                        String errors = "[";
                        for (E error : errorResponse.getErrors()) {
                            errors += error.toString() + ",";
                        }
                        errors += "]";
                        logger.error(
                                "Code: " + errorResponse.getCode()
                                        + ";\n\tMessage: \"" + errorResponse.getMessage()
                                        + "\";\n\t" + errors
                        );
                    }
                }
                // exiting with error
                System.exit(1);
            }

            return getTableResponse(response);
        }

        return null;
    }

    private Table getTableResponse(String json) throws ClientException {
        logger.debug("Parsing a json");
        ObjectMapper objectMapper = new ObjectMapper();
        Table table;
        try {
            table = objectMapper.readValue(json, Table.class);
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
    private ErrorResponse getErrorResponse(String json) {
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
