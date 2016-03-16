package ru.names.ym_gaTool;

import org.apache.log4j.Logger;
import ru.names.ym_gaTool.api.response.yandex.Data;
import ru.names.ym_gaTool.api.response.yandex.Table;

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
        try {
            YandexClient yandexClient = new YandexClient();
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
        }
    }

}
