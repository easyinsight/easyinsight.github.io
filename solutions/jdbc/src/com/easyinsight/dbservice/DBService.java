package com.easyinsight.dbservice;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.jetty.security.ConstraintMapping;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.jetty.nio.SelectChannelConnector;

import java.util.*;
import java.net.URL;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

/**
 * User: James Boe
 * Date: Jan 16, 2009
 * Time: 9:43:14 PM
 */
public class DBService {

    private Server server;
    private Timer timer;

    public static void main(String[] args) throws IOException {
        new DBService().start();
    }

    public DBService() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(new File("config.properties")));
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            System.setProperty((String) entry.getKey(), (String) entry.getValue());
        }
    }
    
    public void start() {
        try {
            startTasks();
            startWebServer();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            if (timer != null) {
                timer.cancel();
            }
            if (server != null) {
                server.stop();
            }
        } catch (Exception e) {
            throw new RuntimeException(e); 
        }
    }

    private void startTasks() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.add(Calendar.DAY_OF_YEAR, 1);
        Date midnight = cal.getTime();
        timer = new Timer();
        timer.schedule(new DBTask(), midnight, 24 * 60 * 60 * 1000);
        //timer.schedule(new DBTask(), new Date(), 60000);
    }

    private void startWebServer() throws Exception {
        server = new Server();
        Connector connector=new SelectChannelConnector();
        connector.setPort(Integer.getInteger("jetty.port", 4040));
        server.setConnectors(new Connector[]{connector});

        /*Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(new String[]{"admin"});
        constraint.setAuthenticate(true);

        ConstraintMapping cm = new ConstraintMapping();
        cm.setConstraint(constraint);
        cm.setPathSpec("*//*");

        SecurityHandler sh = new SecurityHandler();
        sh.setUserRealm(new HashUserRealm("MyRealm","realm.properties"));
        sh.setConstraintMappings(new ConstraintMapping[]{cm});*/

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar("../web2");

        //webapp.addHandler(sh);

        server.setHandler(webapp);

        server.start();
        server.join();
    }
}
