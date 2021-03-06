package ru.names.ym_gaTool;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import ru.names.ym_gaTool.api.yandex.error.E;
import ru.names.ym_gaTool.api.yandex.error.ErrorResponse;
import ru.names.ym_gaTool.api.yandex.response.Data;
import ru.names.ym_gaTool.api.yandex.response.Table;
import ru.names.ym_gaTool.configuration.YandexConfig;

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

    private static final String API_URL_STAT = "https://api-metrika.yandex.ru/stat/v1/data/";
    private static final String API_METHOD_BYTIME = "bytime";
    private static final String API_METHOD_TABLE = "";

    private YandexConfig config;

    public YandexClient(YandexConfig config) {
        this.config = config;
    }

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
    private String buildApiUrl(String apiMethod, Map<String, String> httpQuery)
            throws ClientException, HttpRequestException {
        String apiUrl = API_URL_STAT + apiMethod + '?';
        httpQuery.put("id", String.valueOf(config.getYandexMetrikaId()));
        httpQuery.put("oauth_token", token());

        return apiUrl + AbstractHttpRequest.buildHttpQuery(httpQuery);
    }

    /**
     * Getting client ids and search phrases from api
     *
     * @throws ClientException
     */
    public List<ClientPhrase> getClientPhrases(Date from, Date to)
            throws ClientException, HttpRequestException, ErrorResponseException {
        logger.debug("Retrieving client phrases");

        Map<String, String> httpQueryMap = getHttpQueryMap(from, to);
        logger.debug(
                "Preparing to get data from api. From: "
                        + httpQueryMap.get("date1")
                        + ", to: " + httpQueryMap.get("date2")
        );

        List<Table> tables = new ArrayList<>();
        int offset = 1;
        int limit = 100;
        int count = 0; // our count of retrieved results
        int totalRows = 0; // total count from api
        do {
            httpQueryMap.put("offset", String.valueOf(offset));
            httpQueryMap.put("limit", String.valueOf(limit));

            AbstractHttpRequest request = new HttpsRequest(buildApiUrl(API_METHOD_TABLE, httpQueryMap));
            request.doGet();
            InputStream inputStream = request.getInputStream();
            if (null != inputStream) {
                String response = getResponse(inputStream);

                if (request.isError()) {
                    throw new ErrorResponseException(
                            "Got error response from yandex api. Application will be stopped.",
                            response
                    );
                }

                Table table = getTableResponse(response);
                tables.add(table);

                // getting totalRows from api at first iteration
                if (0 == count) {
                    totalRows = table.getTotalRows();
                    logger.debug("Total rows " + totalRows);
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
            String msg = "Failure to parse json " + json;
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
    public static ErrorResponse getErrorResponse(String json) {
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
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while (null != (line = reader.readLine())) {
                    stringBuilder.append(line);
                }
                token = stringBuilder.toString().trim();
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

    /**
     * Build request params
     * @return
     */
    public static Map<String, String> getHttpQueryMap(Date from, Date to) {
        Map<String, String> httpQueryMap = new HashMap<>();
        httpQueryMap.put("dimensions", "ym:s:searchPhrase,ym:s:paramsLevel2");
        httpQueryMap.put("metrics", "ym:s:visits");
        httpQueryMap.put(
                "filters",
                "ym:s:<attribution>SourceEngineName=='Яндекс' AND ym:s:paramsLevel1=='gaClientId'"
        );

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        httpQueryMap.put("date1", dateFormat.format(from));
        httpQueryMap.put("date2", dateFormat.format(to));

        return httpQueryMap;
    }
}
