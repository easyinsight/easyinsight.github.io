package com.easyinsight.dbservice;

import org.w3c.dom.Node;

import java.sql.*;
import java.text.MessageFormat;

/**
 * User: jamesboe
 * Date: Aug 31, 2009
 * Time: 2:47:05 PM
 */
public class OracleConfiguration extends DBConfiguration {
    private String databaseName;
    private String host;
    private String port;
    private String userName;
    private String password;
    private String connectionString = "jdbc:oracle:thin:@{0}:{1}:{2}";
    private String xmlString = "<mysql host=\"{0}\" port=\"{1}\" databaseName=\"{2}\" userName=\"{3}\"><![CDATA[{4}]]></mysql>";

    public void loadFromXML(Node node, StringEncrypter stringEncrypter) throws StringEncrypter.EncryptionException {
        this.host = node.getAttributes().getNamedItem("host").getNodeValue();
        this.port = node.getAttributes().getNamedItem("port").getNodeValue();
        this.databaseName = node.getAttributes().getNamedItem("databaseName").getNodeValue();
        this.userName = node.getAttributes().getNamedItem("userName").getNodeValue();
        Node attributeItem = node.getAttributes().getNamedItem("password");
        if (attributeItem != null) {
            this.password = attributeItem.getFirstChild().getNodeValue();
        } else {
            this.password = stringEncrypter.decrypt(node.getFirstChild().getNodeValue());
        }
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
        String url = MessageFormat.format(connectionString, host, port, databaseName);
        return DriverManager.getConnection(url, userName, password);
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
            throw new RuntimeException("No Oracle record found!");
        }
    }

    public String toXML(StringEncrypter stringEncrypter) throws StringEncrypter.EncryptionException {
        return MessageFormat.format(xmlString, host, port, databaseName, userName, stringEncrypter.encrypt(password));
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
