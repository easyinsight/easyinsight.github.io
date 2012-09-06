package com.easyinsight.selenium;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.xerox.amazonws.sqs2.Message;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.SQSException;
import com.xerox.amazonws.sqs2.SQSUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.openqa.selenium.server.SeleniumServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * User: jamesboe
 * Date: Sep 17, 2010
 * Time: 11:53:02 AM
 */
public class SeleniumRunner {

    private static Date lastDate = new Date();
    public static final String url = "https://www.easy-insight.com/";

    public static void main(String[] args) throws Exception {
        SeleniumServer seleniumServer = new SeleniumServer();
        seleniumServer.start();

        boolean running = true;
        new Thread(new ReportListener(running)).start();
        Server server = new Server(5050);
        server.setHandler(new LocalHandler());
        server.start();
        while (running) {
            Thread.sleep(3000000);
        }
        //seleniumServer.stop();
    }

    private static class LocalHandler extends AbstractHandler {

        public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException, ServletException {
            System.out.println("Responding...");
            Date curDate = new Date();
            if(curDate.getTime() - lastDate.getTime() > (12 * 60 * 60 * 1000))
                response.sendError(503);
            else {
                response.setStatus(200);
                response.setContentType("text/plain");
                response.getOutputStream().write("All ok!".getBytes());
                response.getOutputStream().flush();
            }
        }
    }

    private static class ReportListener implements Runnable {

        private boolean running;

        private ReportListener(boolean running) {
            this.running = running;
        }

        public void run() {
            try {
                MessageQueue messageQueue = SQSUtils.connectToQueue("EISelenium", "0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
                while (running) {
                    try {
                        Message message = messageQueue.receiveMessage();
                        if (message == null) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                // ignore
                            }
                        } else {
                            final String childURL = message.getMessageBody();
                            messageQueue.deleteMessage(message);
                            new Thread(new Runnable() {

                                public void run() {
                                    try {
                                        Selenium selenium = new DefaultSelenium("localhost", 4444, "*firefox", url);
                                        selenium.start();
                                        selenium.open(childURL);
                                        try {
                                            Thread.sleep(30000);
                                        } catch (InterruptedException e) {
                                            // ignore
                                        }
                                        selenium.stop();
                                        lastDate = new Date();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Thread.sleep(10000);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
