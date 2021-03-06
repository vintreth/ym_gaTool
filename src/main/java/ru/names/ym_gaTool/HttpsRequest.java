package ru.names.ym_gaTool;

import org.apache.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Https connection
 *
 * @author kbogdanov 17.03.16
 */
class HttpsRequest extends AbstractHttpRequest {

    private static Logger logger = Logger.getLogger("HttpsRequest");

    public HttpsRequest(String url) throws HttpRequestException {
        super(url);
    }

    /**
     * @return https url connection object
     * @throws HttpRequestException
     */
    @Override
    protected HttpURLConnection connection() throws HttpRequestException {
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
        } catch (IOException e) {
            String msg = "Failure to open a connection";
            logger.error(msg, e);
            throw new HttpRequestException(msg, e);
        }

        return connection;
    }
}
