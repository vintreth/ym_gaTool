package ru.names.ym_gaTool;

import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kbogdanov 16.03.16
 */
class GoogleClient extends AbstractClient {

    public static final String GOOGLE_ANALYTICS_COLLECT_URL = "https://www.google-analytics.com/collect";

    private static final String GOOGLE_ANALYTICS_ID = "UA-26865693-1";
    private static final String HIT_TYPE_EVENT = "event";
    private static final String EVENT_NAME = "Keyword";

    private static Logger logger = Logger.getLogger("GoogleClient");

    public void sendEvent(ClientPhrase clientPhrase) throws ClientException, HttpRequestException, HttpException {
        logger.debug("Preparing to send event to google analytics");

        AbstractHttpRequest request = new HttpsRequest(GOOGLE_ANALYTICS_COLLECT_URL);
        String httpQuery = AbstractHttpRequest.buildHttpQuery(getHttpQueryMap(clientPhrase));

        request.doPost(httpQuery);
        if (request.isError()) {
            throw new HttpException(request.getResponseCode(), "Google api returned error");
        }

        InputStream inputStream = request.getInputStream();
        if (null != inputStream) {
            String response = getResponse(inputStream);
            logger.debug("Response: " + response);
        }

        logger.debug("Sent");
    }

    /**
     * Building request params
     * @param clientPhrase
     * @return
     */
    public static Map<String, String> getHttpQueryMap(ClientPhrase clientPhrase) {
        Map<String, String> httpQueryMap = new HashMap<>();
        // default params
        httpQueryMap.put("v", "1"); // Version
        httpQueryMap.put("tid", GOOGLE_ANALYTICS_ID); // Tracking ID / Property ID
        httpQueryMap.put("t", HIT_TYPE_EVENT); // Hit Type
        httpQueryMap.put("ec", EVENT_NAME); // Event name
        httpQueryMap.put("ni", "1"); // Special param. Specifies that a hit be considered non-interactive
        httpQueryMap.put("ds", "[mp]metrika_key"); // Data source

        logger.debug("Client id: " + clientPhrase.getClientId() + "; Key word: \"" + clientPhrase.getKeyWord() + "\"");
        httpQueryMap.put("cid", clientPhrase.getClientId());
        httpQueryMap.put("cd6", clientPhrase.getClientId()); // Custom dimension 1
        httpQueryMap.put("ea", clientPhrase.getKeyWord());

        return httpQueryMap;
    }
}
