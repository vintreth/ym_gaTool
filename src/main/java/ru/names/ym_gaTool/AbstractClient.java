package ru.names.ym_gaTool;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author kbogdanov 16.03.16
 */
class AbstractClient {

    private static final String HTTP_METHOD_GET = "GET";
    private static final String HTTP_METHOD_POST = "POST";

    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/48.0.2564.116 Chrome/48.0.2564.116 Safari/537.36";

    private static Logger logger = Logger.getLogger("AbstractClient");

    /**
     * Sending a request to current api url
     *
     * @param link api url
     * @return string represented response
     * @throws ClientException
     */
    protected String makeGetRequest(String link) throws ClientException {
        logger.debug("Making GET request to " + link);
        HttpURLConnection connection = null;
        try {
            URL url = new URL(link);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HTTP_METHOD_GET);
            connection.setRequestProperty("User-Agent", USER_AGENT);

            logger.debug("Response code " + connection.getResponseCode());
        } catch (IOException e) {
            String msg = "Failure to make a request";
            logger.error(msg, e);
            throw new ClientException(msg, e);
        }

        return getResponse(connection);
    }

    /**
     * Makes POST-request to current url
     *
     * @param link      url
     * @param httpQuery string represented query params
     * @return server response
     * @throws ClientException
     */
    public String makePostRequest(String link, String httpQuery) throws ClientException {
        logger.debug("Making POST request to " + link);
        HttpURLConnection connection = null;
        try {
            URL url = new URL(link);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HTTP_METHOD_POST);
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(httpQuery);
            dataOutputStream.flush();
            dataOutputStream.close();

            logger.debug("Response code " + connection.getResponseCode());
        } catch (IOException e) {
            String msg = "Failure to make a request";
            logger.error(msg, e);
            throw new ClientException(msg, e);
        }

        return getResponse(connection);
    }

    /**
     * Return response for current connection
     *
     * @param connection current connection
     * @return server response
     * @throws ClientException
     */
    private String getResponse(HttpURLConnection connection) throws ClientException {
        logger.debug("Retrieving response for the connection");
        StringBuilder response = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while (null != (line = reader.readLine())) {
                response.append(line);
            }
        } catch (IOException e) {
            String msg = "Failure to read a response";
            logger.error(msg, e);
            throw new ClientException(msg, e);
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error("Failure to close the reader", e);
            }
        }

        return response.toString();
    }

    /**
     * Generates url-encoded string with params
     *
     * @param httpQueryMap pair param name - value
     * @throws ClientException
     */
    protected String buildHttpQuery(Map<String, String> httpQueryMap) throws ClientException {
        List<String> httpQueryList = new ArrayList<>();
        for (Map.Entry<String, String> entry : httpQueryMap.entrySet()) {
            try {
                httpQueryList.add(
                        URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8")
                );
            } catch (UnsupportedEncodingException e) {
                logger.error("Failure to encode params", e);
                throw new ClientException("Failure to encode params", e);
            }
        }

        return String.join("&", httpQueryList);
    }
}
