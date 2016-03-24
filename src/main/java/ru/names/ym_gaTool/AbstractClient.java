package ru.names.ym_gaTool;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author kbogdanov 16.03.16
 */
class AbstractClient {

    private static Logger logger = Logger.getLogger("AbstractClient");

    /**
     * Retrieves response from input stream
     *
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
}
