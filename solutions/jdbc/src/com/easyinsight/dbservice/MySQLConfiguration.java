package com.easyinsight.dbservice;

import java.sql.*;
import java.util.Properties;
import java.text.MessageFormat;

/**
 * User: James Boe
 * Date: Jan 31, 2009
 * Time: 7:01:46 PM
 */
public class MySQLConfiguration extends DBConfiguration {
    private String databaseName;
    private String host;
    private String port;
    private String userName;
    private String password;
    private String connectionString = "jdbc:mysql://{0}:{1}/{2}?user={3}&password={4}";

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = MessageFormat.format(connectionString, host, port, databaseName, userName, password);
        return DriverManager.getConnection(url);
    }

    public String validate() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = MessageFormat.format(connectionString, host, port, databaseName, userName, password);
            Connection conn = DriverManager.getConnection(url);
            conn.getMetaData().getTables(null, null, null, null);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            return e.getMessage();
        }
        return null;
    }

    @Override
    public void save(Connection conn) throws SQLException, StringEncrypter.EncryptionException {        
        PreparedStatement insertUserStmt = conn.prepareStatement("INSERT INTO MYSQL_CONFIG (HOST, PORT, DATABASE, USERNAME, PASSWORD) VALUES (?, ?, ?, ?, ?)");
        insertUserStmt.setString(1, host);
        insertUserStmt.setString(2, port);
        insertUserStmt.setString(3, databaseName);
        insertUserStmt.setString(4, userName);
        insertUserStmt.setString(5, new StringEncrypter(StringEncrypter.DES_ENCRYPTION_SCHEME).encrypt(password));
        insertUserStmt.execute();
    }

    @Override
    public String getType() {
        return DBRemote.MYSQL;
    }

    @Override
    public void load(Connection conn) throws SQLException, StringEncrypter.EncryptionException {
        PreparedStatement loadStmt = conn.prepareStatement("SELECT HOST, PORT, DATABASE, USERNAME, PASSWORD FROM MYSQL_CONFIG");
        ResultSet rs = loadStmt.executeQuery();
        if (rs.next()) {
            host = rs.getString(1);
            port = rs.getString(2);
            databaseName = rs.getString(3);
            userName = rs.getString(4);
            password = new StringEncrypter(StringEncrypter.DES_ENCRYPTION_SCHEME).decrypt(rs.getString(5));
        } else {
            throw new RuntimeException("No MySQL record found!");
        }
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
