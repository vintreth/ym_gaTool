package ru.names.ym_gaTool;

import org.apache.log4j.Logger;

/**
 * @author kbogdanov 14.03.16
 */
public class Launcher {

    private static Logger logger = Logger.getLogger("Launcher");

    /**
     * Main method
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
            YandexClient yandexClient = new YandexClient();
            logger.debug("Trying to authorize yandex client");
            yandexClient.authorize();
        } catch (YandexClientException e) {
            System.err.println(e.getMessage());
        }
    }

}
