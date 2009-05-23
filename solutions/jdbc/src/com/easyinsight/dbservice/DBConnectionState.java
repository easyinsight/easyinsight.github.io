package com.easyinsight.dbservice;

import java.net.URL;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

/**
 * User: James Boe
 * Date: Jan 15, 2009
 * Time: 10:03:49 PM
 */
public class DBConnectionState {
    private String databaseHost;
    private String databasePort;
    private String databaseName;
    private String userName;
    private String password;
    private String query;
    private String eiUserName;
    private String dataSourceKey;
    private String eiPassword;

    public DBConnectionState() {

    }

    public void load() {
        try {
            URL url = getClass().getClassLoader().getResource("eiconfig.properties");
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(url.getFile())));
            databaseHost = (String) properties.get("database.host");
            databasePort = (String) properties.get("database.port");
            databaseName = (String) properties.get("database.name");
            userName = (String) properties.get("database.username");
            password = (String) properties.get("database.password");
            query = (String) properties.get("query");
            eiUserName = (String) properties.get("ei.username");
            eiPassword = (String) properties.get("ei.password");
            dataSourceKey = (String) properties.get("datasource.key");
        } catch (IOException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void save() throws IOException {
        Properties properties = new Properties();
        properties.put("database.host", databaseHost);
        properties.put("database.port", databasePort);
        properties.put("database.name", databaseName);
        properties.put("database.username", userName);
        properties.put("database.password", password);
        properties.put("query", query);
        properties.put("ei.username", eiUserName);
        properties.put("ei.password", eiPassword);
        properties.put("dataSource.key", dataSourceKey);
        FileWriter fileWriter = new FileWriter("database.properties");
        properties.store(fileWriter, null);
        fileWriter.close();
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    public String getDatabasePort() {
        return databasePort;
    }

    public void setDatabasePort(String databasePort) {
        this.databasePort = databasePort;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getEiUserName() {
        return eiUserName;
    }

    public void setEiUserName(String eiUserName) {
        this.eiUserName = eiUserName;
    }

    public String getDataSourceKey() {
        return dataSourceKey;
    }

    public void setDataSourceKey(String dataSourceKey) {
        this.dataSourceKey = dataSourceKey;
    }

    public String getEiPassword() {
        return eiPassword;
    }

    public void setEiPassword(String eiPassword) {
        this.eiPassword = eiPassword;
    }
}
