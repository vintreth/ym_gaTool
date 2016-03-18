package ru.names.ym_gaTool;

import org.apache.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Abstract Http Connection
 *
 * @author kbogdanov 17.03.16
 */
abstract class AbstractHttpConnection {

    private static final String HTTP_METHOD_GET = "GET";
    private static final String HTTP_METHOD_POST = "POST";

    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/48.0.2564.116 Chrome/48.0.2564.116 Safari/537.36";

    protected URL url;
    protected HttpURLConnection connection;

    /**
     * response code will be set after request
     */
    protected int responseCode = 0;

    private static Logger logger = Logger.getLogger("AbstractHttpConnection");

    public AbstractHttpConnection(String url) throws HttpConnectionException {
        try {
            logger.debug("Creating abstract connection. Url: " + url);
            this.url = new URL(url);
            connection = connection();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new HttpConnectionException(e.getMessage(), e);
        }
    }

    /**
     * @return Actual connection implementation
     * @throws HttpConnectionException
     */
    protected abstract HttpURLConnection connection() throws HttpConnectionException;

    public void addHeaders(String name, String value) {
        logger.debug("Adding headers {name: " + name + ", value: " + value + "}");
        connection.setRequestProperty(name, value);
    }

    /**
     * Prepares GET request
     * @throws HttpConnectionException
     */
    public void doGet() throws HttpConnectionException {
        try {
            connection.setRequestMethod(HTTP_METHOD_GET);
            connection.setRequestProperty("User-Agent", USER_AGENT);
        } catch (ProtocolException e) {
            logger.error(e.getMessage(), e);
            throw new HttpConnectionException(e.getMessage(), e);
        }
        processRequest();
    }

    /**
     * Prepares POST request
     * @param body request body
     * @throws HttpConnectionException
     */
    public void doPost(String body) throws HttpConnectionException {
        try {
            connection.setRequestMethod(HTTP_METHOD_POST);
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(body);
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new HttpConnectionException(e.getMessage(), e);
        }
        processRequest();
    }

    /**
     * Sets additional params before request
     * @throws HttpConnectionException
     */
    private void processRequest() throws HttpConnectionException {
        try {
            logger.debug("Processing the request");
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new HttpConnectionException(e.getMessage(), e);
        }
    }

    /**
     * @return input stream from the connection
     * @throws HttpConnectionException
     */
    public InputStream getInputStream() throws HttpConnectionException {
        logger.debug("Getting input stream");
        InputStream inputStream = null;
        if (isSuccess()) {
            try {
                inputStream = connection.getInputStream();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new HttpConnectionException(e.getMessage(), e);
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
     * @return boolean
     */
    public boolean isError() {
        return 400 <= responseCode;
    }
}
