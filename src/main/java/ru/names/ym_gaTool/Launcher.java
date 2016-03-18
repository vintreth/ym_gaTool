package ru.names.ym_gaTool;

import org.apache.log4j.Logger;

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
            try {
                for (ClientPhrase clientPhrase : clientPhrases) {
                    googleClient.sendEvent(clientPhrase);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
                throw new BaseException(e.getMessage(), e);
            }
        } catch (HttpException e) {
            logger.error("Caught HttpException: " + e.getStatus() + " " + e.getMessage(), e);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        logger.debug("Finish");
    }

}
