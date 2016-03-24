package ru.names.ym_gaTool;

import org.apache.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Abstract Http request
 *
 * @author kbogdanov 17.03.16
 */
abstract class AbstractHttpRequest {

    private static final String HTTP_METHOD_GET = "GET";
    private static final String HTTP_METHOD_POST = "POST";

    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/48.0.2564.116 Chrome/48.0.2564.116 Safari/537.36";

    protected URL url;
    protected HttpURLConnection connection;
    protected String urlAddress;

    /**
     * response code will be set after request
     */
    protected int responseCode = 0;

    private static Logger logger = Logger.getLogger("AbstractHttpRequest");

    public AbstractHttpRequest(String urlAddress) throws HttpRequestException {
        try {
            logger.debug("Creating abstract connection");
            this.url = new URL(urlAddress);
            this.urlAddress = urlAddress;
            connection = connection();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new HttpRequestException(e.getMessage(), e);
        }
    }

    /**
     * @return Actual connection implementation
     * @throws HttpRequestException
     */
    protected abstract HttpURLConnection connection() throws HttpRequestException;

    public void addHeaders(String name, String value) {
        connection.setRequestProperty(name, value);
    }

    /**
     * Prepares GET request
     *
     * @throws HttpRequestException
     */
    public void doGet() throws HttpRequestException {
        try {
            logger.debug(HTTP_METHOD_GET + " " + urlAddress);
            connection.setRequestMethod(HTTP_METHOD_GET);
            connection.setRequestProperty("User-Agent", USER_AGENT);
        } catch (ProtocolException e) {
            logger.error(e.getMessage(), e);
            throw new HttpRequestException(e.getMessage(), e);
        }
        processRequest();
    }

    /**
     * Prepares POST request
     *
     * @param body request body
     * @throws HttpRequestException
     */
    public void doPost(String body) throws HttpRequestException {
        try {
            logger.debug(HTTP_METHOD_POST + " " + urlAddress + " " + body);
            connection.setRequestMethod(HTTP_METHOD_POST);
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(body);
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new HttpRequestException(e.getMessage(), e);
        }
        processRequest();
    }

    /**
     * Sets additional params before request
     *
     * @throws HttpRequestException
     */
    private void processRequest() throws HttpRequestException {
        try {
            logger.debug("Processing the request");
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new HttpRequestException(e.getMessage(), e);
        }
    }

    /**
     * @return input stream from the connection
     * @throws HttpRequestException
     */
    public InputStream getInputStream() throws HttpRequestException {
        logger.debug("Getting input stream");
        InputStream inputStream = null;
        if (isSuccess()) {
            try {
                inputStream = connection.getInputStream();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new HttpRequestException(e.getMessage(), e);
            }
        } else if (isError()) {
            inputStream = connection.getErrorStream();
        }

        return inputStream;
    }

    /**
     * @return is request success
     */
    public boolean isSuccess() {
        return 200 <= responseCode && responseCode <= 299;
    }

    /**
     * Is request error
     *
     * @return boolean
     */
    public boolean isError() {
        return 400 <= responseCode;
    }

    /**
     * response code will be set after request
     *
     * @return
     */
    public int getResponseCode() {
        return responseCode;
    }


    /**
     * Generates url-encoded string with params
     *
     * @param httpQueryMap pair param name - value
     * @throws HttpRequestException
     */
    public static String buildHttpQuery(Map<String, String> httpQueryMap) throws HttpRequestException {
        List<String> httpQueryList = new ArrayList<>();
        for (Map.Entry<String, String> entry : httpQueryMap.entrySet()) {
            try {
                httpQueryList.add(
                        URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8")
                );
            } catch (UnsupportedEncodingException e) {
                logger.error("Failure to encode params", e);
                throw new HttpRequestException("Failure to encode params", e);
            }
        }

        return String.join("&", httpQueryList);
    }
}
