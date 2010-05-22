package com.easyinsight.connections.database.data;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Apr 27, 2010
 * Time: 9:56:08 PM
 */
@Entity
public class JdbcConnectionInfo extends ConnectionInfo {
    private static final String JDBC_JSON_FORMAT = "'{' \"id\": {0}, \"name\": \"{1}\", \"username\": \"{2}\", \"password\": \"{3}\", \"connectionString\": \"{4}\", \"type\": \"jdbc\" '}'";

    private String connectionString;

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(getConnectionString(), getUsername(), getPassword()); 
    }

    @Override
    public void update(Map<String, String[]> parameterMap) {
        setConnectionString(parameterMap.get("connectionString")[0]);
        super.update(parameterMap);
    }

    public String typeName() {
        return "Raw JDBC";
    }

    public String sourceInfo() {
        return getConnectionString();
    }

    public String toJSON() {
        return MessageFormat.format(JDBC_JSON_FORMAT, getId(), getName(), getUsername(), getPassword(), getConnectionString());
    }
}
