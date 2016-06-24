package ru.names.ym_gaTool;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * @author kbogdanov 14.03.16
 */
public class Launcher {

    private static ConfigurationManager configurationManager;

    private static Logger logger = Logger.getLogger("Launcher");

    /**
     * Main method
     *
     * @param args input params
     *             todo
     *             args[0] some string, this is a yandex api access token (it also may persists in .access_token file)
     *             args[1] 0 or 1, this is a test mode, no sending real data
     */
    public static void main(String[] args) {
        Launcher.run();
    }

    /**
     * Launches the application
     */
    private static void run() {
        logger.debug("Running the application");
        try {
            configurationManager = new ConfigurationManager();
            YandexConfig yandexConfig = configurationManager.getYandexConfig();

            logger.debug("Getting data from yandex api");
            YandexClient yandexClient = new YandexClient(yandexConfig);

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
            }
        } catch (ConfigurationManagerException e) {
            stop("Failure to load a configuration. Application will be stopped.", e);
        } catch (HttpException e) {
            onException("Caught HttpException: " + e.getStatus() + " " + e.getMessage(), e);
        } catch (BaseException e) {
            onException(e.getMessage(), e);
        }

        logger.debug("Finish");
    }

    /**
     * Stops the application in case of fatal error
     *
     * @param message error message
     */
    private static void stop(String message, Throwable cause) {
        logger.fatal(message, cause);
        logger.fatal("Exit 1");

        new ExceptionHandler(configurationManager.getExceptionHandlerConfig(), message, cause).sendNotifications();

        System.exit(1);
    }

    /**
     * Raises in case of exception
     *
     * @param cause cause object
     */
    private static void onException(String errorMessage, Throwable cause) {
        new ExceptionHandler(configurationManager.getExceptionHandlerConfig(), errorMessage, cause).handleCause();
    }

}
