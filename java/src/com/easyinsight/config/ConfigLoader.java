package com.easyinsight.config;

import com.easyinsight.logging.LogClass;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * User: James Boe
 * Date: Sep 7, 2008
 * Time: 10:53:19 AM
 */
public class ConfigLoader {

    private String databaseHost;
    private String databasePort;
    private String databaseName;
    private String databaseUserName;
    private String databasePassword;

    private static ConfigLoader instance;

    public static ConfigLoader instance() {
        if (instance == null) {
            instance = new ConfigLoader();
        }
        return instance;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public String getDatabasePort() {
        return databasePort;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseUserName() {
        return databaseUserName;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    private ConfigLoader() {
        try {
            URL url = getClass().getClassLoader().getResource("eiconfig.properties");
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(url.getFile())));
            databaseHost = (String) properties.get("database.host");
            databasePort = (String) properties.get("database.port");
            databaseName = (String) properties.get("database.name");
            databaseUserName = (String) properties.get("database.username");
            databasePassword = (String) properties.get("database.password");
        } catch (IOException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
