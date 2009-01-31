package com.easyinsight.database.watchdog;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import com.easyinsight.watchdog.app.AppWatchdogServlet;
import com.easyinsight.watchdog.app.AppWatchdogServer;

/**
 * User: James Boe
 * Date: Jan 30, 2009
 * Time: 6:12:48 PM
 */
public class DatabaseWatchdogServer {
    private Server server;
    private boolean running = false;

    public static void main(String[] args) throws Exception {
        new AppWatchdogServer().initialize();
    }

    public void initialize() throws Exception {
        server = new Server(4500);
        Context update = new Context(server,"/update",Context.SESSIONS);
        update.addServlet(new ServletHolder(new DatabaseWatchdogServlet()), "/*");
        server.start();
    }
}
