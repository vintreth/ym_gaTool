package ru.names.ym_gaTool;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import ru.names.ym_gaTool.api.yandex.error.E;
import ru.names.ym_gaTool.api.yandex.error.ErrorResponse;
import ru.names.ym_gaTool.api.yandex.response.Data;
import ru.names.ym_gaTool.api.yandex.response.Table;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author kbogdanov 14.03.16
 */
class YandexClient extends AbstractClient {

    private static final String AUTHORIZATION_URL = "https://oauth.yandex.ru/authorize";
    private static final String CLIENT_ID = "NOT_A_PASSWORD_ACTUALLY";
    private static final String PASSWORD = "NOT_A_PASSWORD_ACTUALLY";

    public static final String API_URL_STAT = "https://api-metrika.yandex.ru/stat/v1/data/";
    public static final String API_METHOD_BYTIME = "bytime";
    public static final String API_METHOD_TABLE = "";

    public static final int YA_METRIKA_ID = NOT_A_PASSWORD_ACTUALLY;

    /**
     * This property must be accessed from token() method
     */
    private String token;

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
        httpQuery.put("oauth_token", token());

        return apiUrl + buildHttpQuery(httpQuery);
    }

    /**
     * Getting client ids and search phrases from api
     *
     * @throws ClientException
     */
    public List<ClientPhrase> getClientPhrases(Date from, Date to) throws ClientException, HttpConnectionException {
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

        logger.debug(
                "Preparing to get data by time from api. From: "
                        + httpQuery.get("date1")
                        + ", to: " + httpQuery.get("date2")
        );

        List<Table> tables = new ArrayList<>();
        int offset = 1;
        int limit = 100;
        int count = 0; // our count of retrieved results
        int totalRows = 0; // total count from api
        do {
            httpQuery.put("offset", String.valueOf(offset));
            httpQuery.put("limit", String.valueOf(limit));

            AbstractHttpConnection connection = new HttpsConnection(buildApiUrl(API_METHOD_TABLE, httpQuery));
            connection.doGet();
            InputStream inputStream = connection.getInputStream();
            if (null != inputStream) {
                String response = getResponse(inputStream);

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

                Table table = getTableResponse(response);
                tables.add(table);

                // getting totalRows from api at first iteration
                if (0 == count) {
                    totalRows = table.getTotalRows();
                }
                count += table.getData().length;
                offset += limit;
            }
        } while (count < totalRows);

        return getClientPhrasesByTables(tables);
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
     * Converts list of tables to list of client phrases
     *
     * @param tables
     * @return
     */
    private List<ClientPhrase> getClientPhrasesByTables(List<Table> tables) {
        List<ClientPhrase> clientPhrases = new ArrayList<>();
        for (Table table : tables) {
            for (Data data : table.getData()) {
                String clientId = data.getClientId();
                String keyWord = data.getKeyWord();
                if (null != clientId && null != keyWord) {
                    clientPhrases.add(new ClientPhrase(clientId, keyWord));
                }
            }
        }

        return clientPhrases;
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

    /**
     * Retrieves yandex api access token
     *
     * @return .access_token file content
     * @throws ClientException
     */
    protected String token() throws ClientException {
        if (null == token) {
            token = "";
            try {
                String projectDirPath = System.getProperty("user.dir");
                String tokenFilePath = projectDirPath + "/.access_token";
                BufferedReader reader = new BufferedReader(new FileReader(tokenFilePath));
                String line;
                while (null != (line = reader.readLine())) {
                    token += line;
                }
                token = token.trim();
            } catch (IOException e) {
                logger.debug(e.getMessage(), e);
                throw new ClientException(e.getMessage(), e);
            }

            if (token.isEmpty()) {
                throw new ClientException("Failure to accept empty token");
            }
        }

        return token;
    }
}
