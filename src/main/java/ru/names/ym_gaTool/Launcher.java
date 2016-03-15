package ru.names.ym_gaTool;

import org.apache.log4j.Logger;

import java.util.Date;

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
            Date from = new Date();
            Date to = new Date(from.getTime() + 86400 * 1000);
            //todo write code
            yandexClient.getDataByTime(from, to);

        } catch (YandexClientException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
