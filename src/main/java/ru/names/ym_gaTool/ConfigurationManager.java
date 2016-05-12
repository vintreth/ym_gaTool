package ru.names.ym_gaTool;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class for loading and getting configuration instances
 *
 * @author kbogdanov 12.05.16
 */
class ConfigurationManager {

    private static Logger logger = Logger.getLogger("ConfigurationManager");

    private JsonConfiguration loadJsonConfiguration(JsonConfiguration emptyConfiguration)
            throws ConfigurationManagerException {

        JsonConfiguration loadedConfiguration = null;
        logger.debug("Starting to load configuration file");
        // root directory
        String projectDirPath = System.getProperty("user.dir");
        String configFilePath = projectDirPath + '/' + emptyConfiguration.getFileName();
        try {
            logger.debug("Getting file content");
            BufferedReader reader = new BufferedReader(new FileReader(configFilePath));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while (null != (line = reader.readLine())) {
                stringBuilder.append(line);
            }

            String json = stringBuilder.toString();
            if (json.isEmpty()) {
                throw new ConfigurationManagerException(
                        "No configuration file content " + emptyConfiguration.getFileName()
                );
            }

            logger.debug("Parsing json content");
            ObjectMapper objectMapper = new ObjectMapper();
            loadedConfiguration = objectMapper.readValue(json, emptyConfiguration.getClass());
            logger.debug(emptyConfiguration.getClass());

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ConfigurationManagerException(e.getMessage(), e);
        }

        return loadedConfiguration;
    }

    /**
     * Retrieves yandex configuration file and creates YandexConfig instance
     *
     * @return data loaded YandexClient instance
     */
    public YandexConfig getYandexConfig() throws ConfigurationManagerException {
        logger.debug("Initializing yandex configuration");

        return (YandexConfig) loadJsonConfiguration(new YandexConfig());
    }
}
