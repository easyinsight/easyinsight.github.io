package com.easyinsight.dbclient;

import org.hibernate.annotations.Type;

/**
 * User: jamesboe
 * Date: Apr 12, 2010
 * Time: 5:28:37 PM
 */
public class MySQLDataSource extends DataSource {
    private String host;
    private int port;
    private String databaseName;
    private String dbUserName;
    @Type(type="encrypted_string")
    private String dbPassword;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
}
