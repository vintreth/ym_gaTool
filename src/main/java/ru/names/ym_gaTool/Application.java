package ru.names.ym_gaTool;

import org.apache.log4j.Logger;
import ru.names.ym_gaTool.api.yandex.error.E;
import ru.names.ym_gaTool.api.yandex.error.ErrorResponse;
import ru.names.ym_gaTool.configuration.ConfigurationManager;
import ru.names.ym_gaTool.configuration.ConfigurationManagerException;
import ru.names.ym_gaTool.configuration.YandexConfig;

import java.util.Date;
import java.util.List;

/**
 * @author kbogdanov 27.06.16
 */
class Application {

    private ConfigurationManager configurationManager;

    private static Application instance;
    private static Logger logger = Logger.getLogger("Application");

    private Application() {}

    /**
     * @return instance of the application
     */
    public static Application getInstance() {
        Application localInstance = instance;
        if (localInstance == null) {
            synchronized (Application.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Application();
                }
            }
        }

        return localInstance;
    }

    /**
     * Launches the application
     */
    public void run() {
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
            new ExceptionHandler("Failure to load a configuration. Application will be stopped.", e).handleFatalCause();
        } catch (HttpException e) {
            onException("Caught HttpException: " + e.getStatus() + " " + e.getMessage(), e);
        } catch (ErrorResponseException e) {
            logger.fatal(e.getMessage());
            ExceptionHandler handler = new ExceptionHandler("Empty response", e);
            if (!e.getResponse().isEmpty()) {
                ErrorResponse errorResponse = YandexClient.getErrorResponse(e.getResponse());
                if (null != errorResponse) {
                    String errors = "[";
                    for (E error : errorResponse.getErrors()) {
                        errors += error.toString() + ",";
                    }
                    errors += "]";
                    handler = new ExceptionHandler(
                            "Code: " + errorResponse.getCode()
                                    + ";\n\tMessage: \"" + errorResponse.getMessage()
                                    + "\";\n\t" + errors,
                            e
                    );
                }
            }

            handler.handleFatalCause();
        } catch (BaseException e) {
            onException(e.getMessage(), e);
        }

        logger.debug("Finish");
    }

    /**
     * Stops the application in case of fatal error
     */
    public void stop() {
        System.exit(1);
    }

    /**
     * Raises in case of exception
     *
     * @param cause cause object
     */
    private void onException(String errorMessage, Throwable cause) {
        new ExceptionHandler(errorMessage, cause).handleCause();
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }
}
