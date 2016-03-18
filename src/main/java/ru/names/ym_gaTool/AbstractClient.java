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

    private static Logger logger = Logger.getLogger("AbstractClient");

    /**
     * Retrieves response from input stream
     * @param inputStream current input stream
     * @return server response
     * @throws ClientException
     */
    protected String getResponse(InputStream inputStream) throws ClientException {
        logger.debug("Retrieving response from the input stream");
        StringBuilder response = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
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
