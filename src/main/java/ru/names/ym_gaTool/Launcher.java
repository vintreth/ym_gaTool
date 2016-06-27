package ru.names.ym_gaTool;

import org.apache.log4j.Logger;
import ru.names.ym_gaTool.configuration.ConfigurationManager;
import ru.names.ym_gaTool.configuration.ConfigurationManagerException;
import ru.names.ym_gaTool.configuration.YandexConfig;

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
     *             todo
     *             args[0] some string, this is a yandex api access token (it also may persists in .access_token file)
     *             args[1] 0 or 1, this is a test mode, no sending real data
     */
    public static void main(String[] args) {
        logger.debug("Creating the application");
        Application.getInstance().run();
    }

}
