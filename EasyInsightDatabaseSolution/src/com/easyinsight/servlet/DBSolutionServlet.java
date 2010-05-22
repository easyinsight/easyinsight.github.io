package com.easyinsight.servlet;

import com.easyinsight.connections.database.data.Query;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import com.mysql.jdbc.Driver;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.sql.SQLException;
import java.sql.DriverManager;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: May 4, 2010
 * Time: 11:41:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBSolutionServlet extends HttpServlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        try {
            DriverManager.registerDriver(new SQLServerDriver());
            DriverManager.registerDriver(new Driver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Query.getScheduler();
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
