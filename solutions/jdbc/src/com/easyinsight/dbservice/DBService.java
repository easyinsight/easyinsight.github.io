package com.easyinsight.dbservice;

import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.ContextHandlerCollection;

import java.util.Calendar;
import java.util.Timer;
import java.util.Date;

/**
 * User: James Boe
 * Date: Jan 16, 2009
 * Time: 9:43:14 PM
 */
public class DBService {

    private Server server;
    private Timer timer;

    public static void main(String[] args) {
        new DBService().start();
    }

    public DBService() {
    }
    
    public void start() {
        try {
            startWebServer();
            startTasks();
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
    }

    private void startWebServer() throws Exception {
        server = new Server();
        Connector connector=new SelectChannelConnector();
        connector.setPort(Integer.getInteger("jetty.port", 4040));
        server.setConnectors(new Connector[]{connector});

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar("web2");
        //webapp.setDefaultsDescriptor(jetty_home+"/etc/webdefault.xml");

        server.setHandler(webapp);

        server.start();
        server.join();

        /*ContextHandlerCollection contexts = new ContextHandlerCollection();        
        server.setHandler(contexts);                        
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("c:/easyinsight/solutions/dbservice/webcontent");
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler,new DefaultHandler()});
        Context root = new Context(contexts,"/",Context.SESSIONS);
        root.addHandler(handlers);
        Context forceRun = new Context(server,"/forcerun",Context.SESSIONS);
        forceRun.addServlet(new ServletHolder(new AdminServlet()), "/*");
        Context dbValidation = new Context(server,"/validateDB",Context.SESSIONS);
        dbValidation.addServlet(new ServletHolder(new DBValidationServlet()), "/*");
        Context executeQuery = new Context(server,"/executeQuery",Context.SESSIONS);
        executeQuery.addServlet(new ServletHolder(new ExecuteQueryServlet()), "/*");*/
    }
}
