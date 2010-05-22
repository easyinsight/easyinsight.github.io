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
public class OracleConnectionInfo extends ConnectionInfo {

    private String hostname;
    private int port;
    private String schemaName;

    private static final String ORACLE_CONNECTION_STRING = "jdbc:oracle://{0}:{1}/{2}";
    private static final String INFO_STRING = "//{0}:{1}/{2}";
    private static final String ORACLE_JSON_STRING = "'{' \"id\": {0}, \"name\": \"{1}\", \"username\": \"{2}\", \"password\": \"{3}\", \"hostname\": \"{4}\", \"port\": {5}, \"schema\": \"{6}\", \"type\": \"oracle\" '}'";

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(MessageFormat.format(ORACLE_CONNECTION_STRING, getHostname(), String.valueOf(getPort()), getSchemaName()),
                getUsername(), getPassword());
    }

    public String typeName() {
        return "Oracle Database";
    }

    public String sourceInfo() {
        return MessageFormat.format(INFO_STRING, getHostname(), String.valueOf(getPort()), getSchemaName());
    }

    public String toJSON() {
        return MessageFormat.format(ORACLE_JSON_STRING, getId(), getName(), getUsername(), getPassword(), getHostname(), String.valueOf(getPort()), getSchemaName());
    }

    @Override
    public void update(Map<String, String[]> parameterMap) {
        setSchemaName(parameterMap.get("oracleSchema")[0]);
        setPort(Integer.parseInt(parameterMap.get("oraclePort")[0]));
        setHostname(parameterMap.get("oracleHostName")[0]);
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

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
}