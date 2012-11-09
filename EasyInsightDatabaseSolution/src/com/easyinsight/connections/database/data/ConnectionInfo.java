package com.easyinsight.connections.database.data;

import org.hibernate.Session;

import javax.persistence.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.List;

import com.easyinsight.connections.database.DataConnection;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Apr 27, 2010
 * Time: 9:45:33 PM
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ConnectionInfo {

    public static ConnectionInfo instance() throws SQLException {
        Session session = DataConnection.getSession();
        try {
            List vals = session.createQuery("from ConnectionInfo").list();
            if (vals.size() == 0)
                return null;
            else {
                System.out.println(vals.size());
                return (ConnectionInfo) vals.get(0);
            }
        } finally {
            session.close();
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String password;
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "connectionInfo", cascade = CascadeType.ALL)
    private List<Query> queries;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void update(Map<String, String[]> parameterMap) {
        setUsername(parameterMap.get("dbUsername")[0]);
        setPassword(parameterMap.get("dbPassword")[0]);
        setName(parameterMap.get("dbUsername")[0]);
    }

    public abstract Connection createConnection() throws SQLException;

    public abstract String typeName();

    public abstract String sourceInfo();

    public abstract String toJSON();


    public void alterStatement(PreparedStatement statement) throws SQLException {

    }

    public static ConnectionInfo createConnectionInfo(Map<String, String[]> parameterMap) {
        ConnectionInfo connectionInfo = null;
        if ("mysql".equals(parameterMap.get("dbType")[0])) {
            connectionInfo = new MySqlConnectionInfo();
        } else if ("oracle".equals(parameterMap.get("dbType")[0])) {
            connectionInfo = new OracleConnectionInfo();
        } else if ("mssql".equals(parameterMap.get("dbType")[0])) {
            connectionInfo = new MsSqlConnectionInfo();
        } else if ("jdbc".equals(parameterMap.get("dbType")[0])) {
            connectionInfo = new JdbcConnectionInfo();
        }
        connectionInfo.update(parameterMap);
        return connectionInfo;
    }

    public List<Query> getQueries() {
        return queries;
    }

    public void setQueries(List<Query> queries) {
        this.queries = queries;
    }

    public static List<ConnectionInfo> all() throws SQLException {
        Session session = DataConnection.getSession();
        try {
            return session.createQuery("from ConnectionInfo").list();
        } finally {
            session.close();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}