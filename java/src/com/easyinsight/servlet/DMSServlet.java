package com.easyinsight.servlet;

import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.api.APIManager;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.DefaultSecurityProvider;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.DatabaseManager;
import com.easyinsight.scheduler.Scheduler;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * User: James Boe
 * Date: Jan 11, 2008
 * Time: 2:53:14 PM
 */
public class DMSServlet extends HttpServlet {

    public static final long HOUR = 1000 * 60 * 60;

    private Scheduler scheduler;

    public void init(ServletConfig servletConfig) throws ServletException {
        try {
            LogClass.info("Starting the core Easy Insight server...");
            if (Database.instance() == null) {
                SecurityUtil.setSecurityProvider(new DefaultSecurityProvider());
                Database.initialize();
                DatabaseManager.instance();
                FeedRegistry.initialize();
                new APIManager().start();
                scheduler = new Scheduler();
                scheduler.start();
            }
            LogClass.info("Started the server.");
        } catch (Throwable e) {
            LogClass.error(e);
            throw new ServletException(e);
        }
    }

    public void destroy() {
        LogClass.info("Shutting down...");
        super.destroy();
        Database.instance().shutdown();
        DatabaseManager.instance().shutdown();
        scheduler.stop();
    }
}
