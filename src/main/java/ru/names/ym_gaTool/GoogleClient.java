package ru.names.ym_gaTool;

import org.apache.log4j.Logger;

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

    public void sendEvent(String clientId, String keyWord) throws ClientException {
        Map<String, String> httpQueryMap = new HashMap<>();
        httpQueryMap.put("v", "1"); // Version.
        httpQueryMap.put("tid", GOOGLE_ANALYTICS_ID); // Tracking ID / Property ID.
        httpQueryMap.put("t", HIT_TYPE_EVENT); // Hit Type.
        httpQueryMap.put("ec", EVENT_NAME); // Event name.
        httpQueryMap.put("ni", "1"); // Special param.
        httpQueryMap.put("cid", clientId); // todo Anonymous Client ID.
        httpQueryMap.put("ea", keyWord); // todo Key word.

        String response = makePostRequest(GOOGLE_ANALYTICS_COLLECT_URL, buildHttpQuery(httpQueryMap));
        System.out.println(response);
    }
}
