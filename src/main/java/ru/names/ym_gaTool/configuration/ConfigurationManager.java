package ru.names.ym_gaTool.configuration;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Class for loading and getting configuration instances
 *
 * @author kbogdanov 12.05.16
 */
public class ConfigurationManager {

    private Map<String, JsonConfiguration> configurations = new HashMap<>();

    {
        configurations.put("yandexConfig", new YandexConfig());
        configurations.put("exceptionHandlerConfig", new ExceptionHandlerConfig());
        configurations.put("mailSenderConfig", new MailSenderConfig());
    }

    private static Logger logger = Logger.getLogger("ConfigurationManager");

    public ConfigurationManager() throws ConfigurationManagerException {
        loadAll();
    }

    /**
     * Loads all configs from configurations list
     *
     * @throws ConfigurationManagerException
     */
    private void loadAll() throws ConfigurationManagerException {
        for (Map.Entry<String, JsonConfiguration> entry : configurations.entrySet()) {
            JsonConfiguration configuration = entry.getValue();
            logger.debug("Initializing " + configuration.getClass().getName() + " configuration");

            configurations.put(entry.getKey(), loadJsonConfiguration(configuration));
        }
    }

    /**
     * Retrieves configuration file and load config data
     *
     * @param emptyConfiguration empty config instance
     *
     * @return loaded config instance
     *
     * @throws ConfigurationManagerException
     */
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
     * @return data loaded YandexClient instance
     */
    public YandexConfig getYandexConfig() {
        return (YandexConfig) configurations.get("yandexConfig");
    }

    /**
     * @return data loaded config instance
     */
    public ExceptionHandlerConfig getExceptionHandlerConfig() {
        return (ExceptionHandlerConfig) configurations.get("exceptionHandlerConfig");
    }

    /**
     * @return data loaded MailSenderConfig instance
     */
    public MailSenderConfig getMailSenderConfig() {
        return (MailSenderConfig) configurations.get("mailSenderConfig");
    }
}
