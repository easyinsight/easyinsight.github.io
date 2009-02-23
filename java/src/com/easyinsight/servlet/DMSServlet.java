package com.easyinsight.servlet;

import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.storage.DataRetrieval;
import com.easyinsight.dataengine.EngineRequestHandler;
import com.easyinsight.dataengine.DataEngine;
import com.easyinsight.dataengine.MessagingBroker;
import com.easyinsight.api.APIManager;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.DefaultSecurityProvider;
import com.easyinsight.logging.LogClass;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * User: James Boe
 * Date: Jan 11, 2008
 * Time: 2:53:14 PM
 */
public class DMSServlet extends HttpServlet {
    public void init(ServletConfig servletConfig) throws ServletException {
        try {
            LogClass.info("Starting the core Easy Insight server...");
            if (Database.instance() == null) {
                SecurityUtil.setSecurityProvider(new DefaultSecurityProvider());
                Database.initialize();
                FeedRegistry.initialize();
                new DataRetrieval();
                new MessagingBroker();
                new EngineRequestHandler();
                new DataEngine();
                new APIManager().start();
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
        try {
            EngineRequestHandler.instance().stop();
            MessagingBroker.instance().stop();
        } catch (Exception e) {
            LogClass.error(e);
        }
        //System.exit(0);
    }
}
