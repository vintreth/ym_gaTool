package ru.names.ym_gaTool;

import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kbogdanov 16.03.16
 */
class GoogleClient extends AbstractClient {

    private static final String GOOGLE_ANALYTICS_COLLECT_URL = "http://www.google-analytics.com/collect";
    private static final String GOOGLE_ANALYTICS_ID = "UA-26865693-1";

    private static final String HIT_TYPE_EVENT = "event";
    private static final String EVENT_NAME = "Keyword";

    private static Logger logger = Logger.getLogger("GoogleClient");

    public void sendEvent(ClientPhrase clientPhrase) throws ClientException, HttpConnectionException, HttpException {
        logger.debug("Preparing to send event to google analytics");
        Map<String, String> httpQueryMap = new HashMap<>();
        // default params
        httpQueryMap.put("v", "1"); // Version.
        httpQueryMap.put("tid", GOOGLE_ANALYTICS_ID); // Tracking ID / Property ID.
        httpQueryMap.put("t", HIT_TYPE_EVENT); // Hit Type.
        httpQueryMap.put("ec", EVENT_NAME); // Event name.
        httpQueryMap.put("ni", "1"); // Special param.

        logger.debug("Client id: " + clientPhrase.getClientId() + "; Key word: \"" + clientPhrase.getKeyWord() + "\"");
        httpQueryMap.put("cid", clientPhrase.getClientId());
        httpQueryMap.put("ea", clientPhrase.getKeyWord());

        AbstractHttpConnection connection = new HttpsConnection(GOOGLE_ANALYTICS_COLLECT_URL);
        connection.doPost(buildHttpQuery(httpQueryMap));
        if (connection.isError()) {
            throw new HttpException(connection.getResponseCode(), "Google api returned error");
        }

        InputStream inputStream = connection.getInputStream();
        if (null != inputStream) {
            String response = getResponse(inputStream);
            logger.debug("Response: " + response);
        }

        logger.debug("Sent");
    }
}
