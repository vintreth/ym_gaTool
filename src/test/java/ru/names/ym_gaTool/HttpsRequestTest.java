package ru.names.ym_gaTool;

import org.junit.Test;

import java.util.Date;

/**
 * @author kbogdanov 18.03.16
 */
public class HttpsRequestTest {

    @Test(timeout = 10000)
    public void testDoGet() throws Exception {
        Date now = new Date();
        Date from = new Date(now.getTime() - 2 * 86400 * 1000);
        Date to = new Date(now.getTime() - 86400 * 1000);

        String httpQuery = AbstractHttpRequest.buildHttpQuery(YandexClient.getHttpQueryMap(from, to));
        AbstractHttpRequest yandexRequest = new HttpsRequest(YandexClient.API_URL_STAT + "?" + httpQuery);
        yandexRequest.doGet();
    }

    @Test(timeout = 10000)
    public void testDoPost() throws Exception {
        ClientPhrase clientPhrase = new ClientPhrase(
                "1746780259.1458051378",
                "яркие туфли-лодочки - купить в интернет-магазине"
        );

        AbstractHttpRequest googleRequest = new HttpsRequest(GoogleClient.GOOGLE_ANALYTICS_COLLECT_URL);
        googleRequest.doPost(AbstractHttpRequest.buildHttpQuery(GoogleClient.getHttpQueryMap(clientPhrase)));
    }
}