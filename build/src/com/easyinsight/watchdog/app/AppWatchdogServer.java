package com.easyinsight.watchdog.app;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.security.ConstraintMapping;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.security.Constraint;
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
        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(new String[]{"admin"});
        constraint.setAuthenticate(true);

        ConstraintMapping cm = new ConstraintMapping();
        cm.setConstraint(constraint);
        cm.setPathSpec("/*");

        SecurityHandler sh = new SecurityHandler();
        sh.setUserRealm(new HashUserRealm("MyRealm","realm.properties"));
        sh.setConstraintMappings(new ConstraintMapping[]{cm});

        Context update = new Context(server,"/",Context.SESSIONS);
        update.addHandler(sh);
        update.addServlet(new ServletHolder(new AppWatchdogServlet()), "/*");
        server.start();
    }
}
