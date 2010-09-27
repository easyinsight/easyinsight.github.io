package com.easyinsight.selenium;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.xerox.amazonws.sqs2.Message;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.SQSUtils;
import org.openqa.selenium.server.SeleniumServer;

/**
 * User: jamesboe
 * Date: Sep 17, 2010
 * Time: 11:53:02 AM
 */
public class SeleniumRunner {

    public static void main(String[] args) throws Exception {
        final String url = "https://www.easy-insight.com/";
        SeleniumServer seleniumServer = new SeleniumServer();
        seleniumServer.start();

        MessageQueue messageQueue = SQSUtils.connectToQueue("EISelenium", "0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");

        while (true) {
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
                        Selenium selenium = new DefaultSelenium("localhost", 4444, "*firefox", url);
                        selenium.start();
                        selenium.open(childURL);
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException e) {
                            // ignore
                        }
                        selenium.stop();
                    }
                }).start();

            }
        }
        //seleniumServer.stop();
    }
}
