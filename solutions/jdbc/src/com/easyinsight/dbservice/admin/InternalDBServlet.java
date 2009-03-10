package com.easyinsight.dbservice.admin;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.sql.*;

/**
 * User: James Boe
 * Date: Jan 31, 2009
 * Time: 10:55:29 PM
 */
public class InternalDBServlet extends HttpServlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            String dbURL = "jdbc:derby:eijdbc";
            Connection conn;
            try {
                System.out.println("Checking internal database...");
                conn = DriverManager.getConnection(dbURL);
                Statement queryVersionStmt = conn.createStatement();
                ResultSet versionRS = queryVersionStmt.executeQuery("SELECT DATABASE_VERSION FROM DATABASE_VERSION");
                versionRS.next();
                int version = versionRS.getInt(1);
                System.out.println("Checking database version...");
                applyMigrations(version);
            } catch (SQLException e) {
                System.out.println("No internal database found, creating new..");
                dbURL = "jdbc:derby:eijdbc;create=true";
                conn = DriverManager.getConnection(dbURL);
                conn.setAutoCommit(false);
                try {
                    newDatabase(conn);
                    conn.commit();
                    System.out.println("Created new internal database.");
                } catch (SQLException e1) {
                    conn.rollback();
                    throw e1;
                } finally {
                    conn.setAutoCommit(true);
                }
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    private void applyMigrations(int version) {
        System.out.println("Up to date.");
    }

    private void newDatabase(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        statement.execute("CREATE TABLE DATABASE_VERSION (DATABASE_VERSION INTEGER)");
        statement.execute("CREATE TABLE EI_CONFIG (USERNAME VARCHAR(100), PASSWORD VARCHAR(100))");
        statement.execute("CREATE TABLE DB_CONFIG (DB_TYPE VARCHAR(100))");
        statement.execute("CREATE TABLE MYSQL_CONFIG (HOST VARCHAR(100), PORT VARCHAR(10), DATABASE VARCHAR(100), " +
                "USERNAME VARCHAR(100), PASSWORD VARCHAR(100))");
        statement.execute("CREATE TABLE GENERIC_DB_CONFIG (DRIVER VARCHAR(255), JDBC_URL VARCHAR(255))");
        statement.execute("CREATE TABLE QUERY_CONFIG (DATA_SOURCE VARCHAR(100), QUERY VARCHAR(2000), QUERY_MODE INTEGER, AD_HOC SMALLINT, NAME VARCHAR(255)," +
                "QUERY_CONFIG_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1))");
        statement.execute("INSERT INTO DATABASE_VERSION (DATABASE_VERSION) VALUES (1)");
    }
}
