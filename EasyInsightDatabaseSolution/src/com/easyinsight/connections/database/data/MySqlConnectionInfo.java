package com.easyinsight.connections.database.data;

import javax.persistence.Entity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.text.MessageFormat;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Apr 29, 2010
 * Time: 11:51:18 AM
 */
@Entity
public class MySqlConnectionInfo extends ConnectionInfo {

    private String hostname;
    private int port;
    private String databaseName;

    private static final String MYSQL_CONNECTION_STRING = "jdbc:mysql://{0}:{1}/{2}";
    private static final String INFO_STRING = "//{0}:{1}/{2}";
    private static final String MYSQL_JSON_STRING = "'{' \"id\": {0}, \"name\": \"{1}\", \"username\": \"{2}\", \"password\": \"{3}\", \"hostname\": \"{4}\", \"port\": {5}, \"databaseName\": \"{6}\", \"type\": \"mysql\" '}'";

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(MessageFormat.format(MYSQL_CONNECTION_STRING, getHostname(), String.valueOf(getPort()), getDatabaseName()), 
                getUsername(), getPassword());
    }

    public String typeName() {
        return "mySQL Database";
    }

    public String sourceInfo() {
        return MessageFormat.format(INFO_STRING, getHostname(), String.valueOf(getPort()), getDatabaseName());
    }

    public String toJSON() {
        return MessageFormat.format(MYSQL_JSON_STRING, getId(), getName(), getUsername(), getPassword(), getHostname(), String.valueOf(getPort()), getDatabaseName());
    }

    @Override
    public void update(Map<String, String[]> parameterMap) {
        setDatabaseName(parameterMap.get("mysqlDbName")[0]);
        setPort(Integer.parseInt(parameterMap.get("mysqlPort")[0]));
        setHostname(parameterMap.get("mysqlHostName")[0]);
        super.update(parameterMap);
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
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

    @Override
    public void alterStatement(PreparedStatement statement) throws SQLException {
        super.alterStatement(statement);
        statement.setFetchSize(Integer.MIN_VALUE);
    }
}
