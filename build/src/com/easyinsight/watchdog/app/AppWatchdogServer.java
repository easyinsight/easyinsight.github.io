package com.easyinsight.watchdog.app;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 * User: James Boe
 * Date: Jan 27, 2009
 * Time: 1:32:05 AM
 */
public class AppWatchdogServer {
    private Server server;
    private boolean running = false;

    public static void main(String[] args) throws Exception {
        new AppWatchdogServer().initialize();

    }

    public void initialize() throws Exception {
        server = new Server(4000);
        Context update = new Context(server,"/update",Context.SESSIONS);
        update.addServlet(new ServletHolder(new AppWatchdogServlet()), "/*");
        server.start();
    }
}
