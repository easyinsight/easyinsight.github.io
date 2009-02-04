package com.easyinsight.dbservice.admin;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
                conn = DriverManager.getConnection(dbURL);
            } catch (SQLException e) {
                dbURL = "jdbc:derby:eijdbc;create=true";
                conn = DriverManager.getConnection(dbURL);
                newDatabase(conn);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    private void newDatabase(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        statement.execute("CREATE TABLE EI_CONFIG (USERNAME VARCHAR(100), PASSWORD VARCHAR(100))");
        statement.execute("CREATE TABLE DB_CONFIG (DB_TYPE VARCHAR(100))");
        statement.execute("CREATE TABLE MYSQL_CONFIG (HOST VARCHAR(100), PORT VARCHAR(10), DATABASE VARCHAR(100), " +
                "USERNAME VARCHAR(100), PASSWORD VARCHAR(100))");
        statement.execute("CREATE TABLE QUERY_CONFIG (DATA_SOURCE VARCHAR(100), QUERY VARCHAR(2000), QUERY_MODE INTEGER, AD_HOC SMALLINT, NAME VARCHAR(255)," +
                "QUERY_CONFIG_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1))");
    }
}
