package ru.names.ym_gaTool;

import org.apache.log4j.Logger;
import ru.names.ym_gaTool.api.yandex.response.Data;
import ru.names.ym_gaTool.api.yandex.response.Table;

import java.util.Date;
import java.util.List;

/**
 * @author kbogdanov 14.03.16
 */
public class Launcher {

    private static Logger logger = Logger.getLogger("Launcher");

    /**
     * Main method
     *
     * @param args input params
     */
    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        launcher.run();
    }

    /**
     * Launch the application
     */
    private void run() {
        logger.debug("Running the application");
        try {
            logger.debug("Getting data from yandex api");
            YandexClient yandexClient = new YandexClient();
            Date now = new Date();
            Date from = new Date(now.getTime() - 2 * 86400 * 1000);
            Date to = new Date(now.getTime() - 86400 * 1000);
            List<ClientPhrase> clientPhrases = yandexClient.getClientPhrases(from, to);

            logger.debug("Got result, rows count " + clientPhrases.size());
            logger.debug("Processing data");
            GoogleClient googleClient = new GoogleClient();
            for (ClientPhrase clientPhrase : clientPhrases) {
                //todo googleClient.sendEvent(clientPhrase);
            }
        } catch (ClientException | HttpConnectionException e) {
            logger.error(e.getMessage(), e);
        }

        logger.debug("Finish");
    }

}
