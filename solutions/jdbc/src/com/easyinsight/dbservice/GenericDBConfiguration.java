package com.easyinsight.dbservice;

import java.sql.*;

/**
 * User: James Boe
 * Date: Mar 7, 2009
 * Time: 10:16:17 AM
 */
public class GenericDBConfiguration extends DBConfiguration {

    private String driver;
    private String jdbcURL;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getJdbcURL() {
        return jdbcURL;
    }

    public void setJdbcURL(String jdbcURL) {
        this.jdbcURL = jdbcURL;
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(jdbcURL);
    }

    public String validate() {
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(jdbcURL);
            conn.getMetaData().getTables(null, null, null, null);
        } catch (ClassNotFoundException e) {
            return e.getMessage();
        } catch (SQLException e) {
            return e.getMessage();
        }
        return null;
    }

    public void save(Connection conn) throws SQLException, StringEncrypter.EncryptionException {
        PreparedStatement insertUserStmt = conn.prepareStatement("INSERT INTO GENERIC_DB_CONFIG (DRIVER, JDBC_URL) VALUES (?, ?)");
        insertUserStmt.setString(1, driver);
        insertUserStmt.setString(2, jdbcURL);
        insertUserStmt.execute();
    }

    public String getType() {
        return DBRemote.GENERIC;
    }

    public void load(Connection conn) throws SQLException, StringEncrypter.EncryptionException {
        PreparedStatement loadStmt = conn.prepareStatement("SELECT DRIVER, JDBC_URL FROM GENERIC_DB_CONFIG");
        ResultSet rs = loadStmt.executeQuery();
        if (rs.next()) {
            driver = rs.getString(1);
            jdbcURL = rs.getString(2);
        } else {
            throw new RuntimeException("No generic database record found!");
        }
    }
}
