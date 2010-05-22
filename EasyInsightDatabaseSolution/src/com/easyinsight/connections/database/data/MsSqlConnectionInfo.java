package com.easyinsight.connections.database.data;

import javax.persistence.Entity;
import java.sql.Connection;
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
public class MsSqlConnectionInfo extends ConnectionInfo {

    private String hostname;
    private int port;
    private String instanceName;

    private static final String SQL_SERVER_CONNECTION_STRING = "jdbc:sqlserver://{0}{1}:{2}";
    private static final String INFO_STRING = "//{0}:{1}/{2}";
    private static final String SQL_SERVER_JSON_STRING = "'{' \"id\": {0}, \"name\": \"{1}\", \"username\": \"{2}\", \"password\": \"{3}\", \"hostname\": \"{4}\", \"port\": {5}, \"instanceName\": \"{6}\", \"type\": \"mssql\" '}'";

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(MessageFormat.format(SQL_SERVER_CONNECTION_STRING, getHostname(), getInstanceName().isEmpty() ? "" : "\\" + getInstanceName(), String.valueOf(getPort())),
                getUsername(), getPassword());
    }

    public String typeName() {
        return "SQL Server Database";
    }

    public String sourceInfo() {
        return MessageFormat.format(INFO_STRING, getHostname(), String.valueOf(getPort()), getInstanceName());
    }

    public String toJSON() {
        return MessageFormat.format(SQL_SERVER_JSON_STRING, getId(), getName(), getUsername(), getPassword(), getHostname(), String.valueOf(getPort()), getInstanceName());
    }

    @Override
    public void update(Map<String, String[]> parameterMap) {
        setInstanceName(parameterMap.get("mssqlInstanceName")[0]);
        setPort(Integer.parseInt(parameterMap.get("mssqlPort")[0]));
        setHostname(parameterMap.get("mssqlHostName")[0]);
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

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
}