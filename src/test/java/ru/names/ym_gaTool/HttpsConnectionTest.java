package ru.names.ym_gaTool;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kbogdanov 18.03.16
 */
public class HttpsConnectionTest extends YandexClient {

    @Test(timeout = 10000)
    public void testDoGet() throws Exception {
        Map<String, String> httpQueryMap = new HashMap<>();
        httpQueryMap.put("id", String.valueOf(YandexClient.YA_METRIKA_ID));
        httpQueryMap.put("oauth_token", token());
        httpQueryMap.put("dimensions", "ym:s:searchPhrase,ym:s:paramsLevel2");
        httpQueryMap.put("metrics", "ym:s:visits");
        httpQueryMap.put(
                "filters",
                "ym:s:<attribution>SourceEngineName=='Яндекс' AND ym:s:paramsLevel1=='gaClientId'"
        );

        Date now = new Date();
        Date from = new Date(now.getTime() - 2 * 86400 * 1000);
        Date to = new Date(now.getTime() - 86400 * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        httpQueryMap.put("date1", dateFormat.format(from));
        httpQueryMap.put("date2", dateFormat.format(to));
        AbstractHttpConnection yandexConnection = new HttpsConnection("https://api-metrika.yandex.ru/stat/v1/data/?"
                + buildHttpQuery(httpQueryMap)
        );
        yandexConnection.doGet();
    }

    @Test(timeout = 10000)
    public void testDoPost() throws Exception {
        Map<String, String> httpQueryMap = new HashMap<>();
        httpQueryMap.put("v", "1"); // Version.
        httpQueryMap.put("tid", GoogleClient.GOOGLE_ANALYTICS_ID); // Tracking ID / Property ID.
        httpQueryMap.put("t", GoogleClient.HIT_TYPE_EVENT); // Hit Type.
        httpQueryMap.put("ec", GoogleClient.EVENT_NAME); // Event name.
        httpQueryMap.put("ni", "1"); // Special param.

        httpQueryMap.put("cid", "1746780259.1458051378");
        httpQueryMap.put("ea", "яркие туфли-лодочки - купить в интернет-магазине");

        AbstractHttpConnection googleConnection = new HttpsConnection("https://www.google-analytics.com/collect");
        googleConnection.doPost(buildHttpQuery(httpQueryMap));
    }
}