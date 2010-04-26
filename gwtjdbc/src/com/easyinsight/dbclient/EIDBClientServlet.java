package com.easyinsight.dbclient;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * User: jamesboe
 * Date: Apr 12, 2010
 * Time: 5:12:16 PM
 */
public class EIDBClientServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        Database database = new Database();
        database.createSchema();
        
    }

    @Override
    public void destroy() {
        super.destroy();
        if (Database.instance() != null) {
            Database.instance().shutdown();
        }
    }
}
