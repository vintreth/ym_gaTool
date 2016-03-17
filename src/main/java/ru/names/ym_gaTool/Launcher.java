package ru.names.ym_gaTool;

import org.apache.log4j.Logger;
import ru.names.ym_gaTool.api.yandex.error.E;
import ru.names.ym_gaTool.api.yandex.error.ErrorResponse;
import ru.names.ym_gaTool.api.yandex.response.Data;
import ru.names.ym_gaTool.api.yandex.response.Table;

import java.util.Arrays;
import java.util.Date;

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
        YandexClient yandexClient = new YandexClient();
        try {
            Date to = new Date();
            Date from = new Date(to.getTime() - 86400 * 1000);
            //todo write code
            logger.debug("Getting data from yandex api");
            Table table = yandexClient.getClientPhraseTable(from, to);

            logger.debug("Got result, rows count " + table.getData().length);

            logger.debug("Processing data");
            GoogleClient googleClient = new GoogleClient();
            for (Data data : table.getData()) {
                String clientId = data.getClientId();
                String keyWord = data.getKeyWord();

                if (null != clientId && null != keyWord) {
                    googleClient.sendEvent(clientId, keyWord);
                }
            }
        } catch (ClientException e) {
            logger.error(e.getMessage(), e);
        } catch (HttpException e) {
            logger.error("Caught HttpException " + e.getStatus(), e);
            if (!e.getMessage().isEmpty()) {
                ErrorResponse errorResponse = yandexClient.getErrorResponse(e.getMessage());
                if (null != errorResponse) {
                    String errors = "[";
                    for (E error : errorResponse.getErrors()) {
                        errors += error.toString() + ",";
                    }
                    errors += "]";
                    logger.error(
                            "Code: " + errorResponse.getCode()
                                    + ";\nMessage: \"" + errorResponse.getMessage()
                                    + "\";\n" + errors
                    );
                }
            }
        }
    }

}
