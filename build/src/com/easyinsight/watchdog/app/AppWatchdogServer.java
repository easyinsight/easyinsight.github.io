package com.easyinsight.watchdog.app;


import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.security.Constraint;

import java.util.Collections;

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
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(AppWatchdogServlet.class, "/*");

        HashLoginService hls = new HashLoginService("EI Watchdog");
        hls.setConfig("jar:file:watchdog.jar!/realm.properties");
        server.addBean(hls);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(AppWatchdogServlet.class, "/*");

        Constraint constraint = new Constraint();

        constraint.setName(Constraint.__BASIC_AUTH);

        constraint.setRoles(new String[]{"admin"});
        constraint.setAuthenticate(true);

        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setPathSpec("/*");
        constraintMapping.setConstraint(constraint);

        ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
        securityHandler.setLoginService(hls);
        securityHandler.setAuthenticator(new BasicAuthenticator());
        securityHandler.setConstraintMappings(Collections.singletonList(constraintMapping));
        server.setHandler(securityHandler);
        securityHandler.setHandler(handler);

        server.dumpStdErr();
        server.start();
        server.dumpStdErr();
        securityHandler.dumpStdErr();
    }
}
