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
class HttpsConnection extends AbstractHttpConnection {

    private static Logger logger = Logger.getLogger("HttpsConnection");

    public HttpsConnection(String url) throws HttpConnectionException {
        super(url);
    }

    /**
     * @return https url connection object
     * @throws HttpConnectionException
     */
    @Override
    protected HttpURLConnection connection() throws HttpConnectionException {
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
        } catch (IOException e) {
            String msg = "Failure to open a connection";
            logger.error(msg, e);
            throw new HttpConnectionException(msg, e);
        }

        return connection;
    }
}
