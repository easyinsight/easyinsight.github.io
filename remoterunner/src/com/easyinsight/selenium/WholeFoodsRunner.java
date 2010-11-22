package com.easyinsight.selenium;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.xerox.amazonws.sqs2.Message;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.SQSUtils;

import java.rmi.RemoteException;

/**
 * User: jamesboe
 * Date: Oct 9, 2010
 * Time: 9:28:47 AM
 */
public class WholeFoodsRunner implements Runnable {

    private boolean running;

    public WholeFoodsRunner(boolean running) {
        this.running = running;
    }

    public void run() {

        try {
            MessageQueue messageQueue = SQSUtils.connectToQueue("EIWFReport", "0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");

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
                        final String messageBody = message.getMessageBody();
                        messageQueue.deleteMessage(message);
                        processMessage(messageBody);
                    }
                } catch (Exception e) {
                    e.printStackTrace(); 
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processMessage(String messageBody) throws InterruptedException, RemoteException {
        String[] messageTokens = messageBody.split(",");
        String userID = messageTokens[0];
        String password = messageTokens[1];
        String apiKey = messageTokens[2];
        String apiSecretKey = messageTokens[3];
        String dataSourceName = messageTokens[4];
        String refreshMode = messageTokens[5];
        {
            Selenium selenium = createSelenium(userID, password);
            IWholeFoodsParse parseHandler;
            populateFormForHistory(selenium);
            parseHandler = new WholeFoodsXMLParse(apiKey, apiSecretKey, dataSourceName);
            boolean loadedReport = false;
            while (!loadedReport) {
                try {
                    selenium.getText("//div[@id='rb_ReportStyleIncrementalFetch_2']/div/span[6]");
                    loadedReport = true;
                } catch (Exception e) {
                    Thread.sleep(30000);
                }
            }
            String pageString = selenium.getText("//div[@id='rb_ReportStyleIncrementalFetch_2']/div/span[6]");
            String[] tokens = pageString.split(" ");
            parseHandler.addPage(selenium.getHtmlSource());
            int pages = Integer.parseInt(tokens[1]);
            for (int k = 2; k <= pages; k++) {
                if ((k - 1) % 5 == 0) {
                    selenium.click("//img[@alt='Next 5 Pages']");
                } else {
                    selenium.click("link=" + k);
                }
                selenium.waitForPageToLoad("30000");
                parseHandler.addPage(selenium.getHtmlSource());
            }
            parseHandler.done();
            selenium.stop();
        }
        {
            Selenium selenium = createSelenium(userID, password);
            IWholeFoodsParse parseHandler;

            populateFormForLatest(selenium);
            parseHandler = new WholeFoodsLatestParse(apiKey, apiSecretKey, dataSourceName);
            
            boolean loadedReport = false;
            while (!loadedReport) {
                try {
                    selenium.getText("//div[@id='rb_ReportStyleIncrementalFetch_2']/div/span[6]");
                    loadedReport = true;
                } catch (Exception e) {
                    Thread.sleep(30000);
                }
            }
            String pageString = selenium.getText("//div[@id='rb_ReportStyleIncrementalFetch_2']/div/span[6]");
            String[] tokens = pageString.split(" ");
            parseHandler.addPage(selenium.getHtmlSource());
            int pages = Integer.parseInt(tokens[1]);
            for (int k = 2; k <= pages; k++) {
                if ((k - 1) % 5 == 0) {
                    selenium.click("//img[@alt='Next 5 Pages']");
                } else {
                    selenium.click("link=" + k);
                }
                selenium.waitForPageToLoad("30000");
                parseHandler.addPage(selenium.getHtmlSource());
            }
            parseHandler.done();
            selenium.stop();
        }
    }

    private static void populateFormForHistory(Selenium selenium) {
        selenium.removeSelection("unitsToAdd", "label=Company");
		selenium.addSelection("unitsToAdd", "label=Region");
		selenium.click("//img[@alt='Add to selections']");
		selenium.click("//img[@alt='Add to selections']");
		selenium.removeSelection("unitsToAdd", "label=Category");
		selenium.addSelection("unitsToAdd", "label=Item");
		selenium.click("//img[@alt='Add to selections']");
		selenium.removeSelection("unitsToAdd", "label=Brand");
		selenium.addSelection("unitsToAdd", "label=Week");
		selenium.click("//img[@alt='Add to selections']");
		selenium.click("//div[@id='mstrPromptsBody']/div[2]/div[6]/table/tbody/tr/td[2]/span[1]/img");
		selenium.click("//div[@id='mstrPromptsBody']/div[2]/div[6]/table/tbody/tr/td[2]/span[1]/img");
		selenium.removeSelection("unitsToAdd_1", "label=% $ Sales - YOY");
		selenium.addSelection("unitsToAdd_1", "label=Unit Sales");
		selenium.click("//div[@id='mstrPromptsBody']/div[2]/div[6]/table/tbody/tr/td[2]/span[1]/img");
		selenium.click("//div[@id='mstrPromptsBody']/div[2]/div[6]/table/tbody/tr/td[2]/span[1]/img");
		selenium.addSelection("unitsToAdd_1", "label=Avg Rtl Price");
		selenium.removeSelection("unitsToAdd_1", "label=% Unit Sales - YOY");
		selenium.click("//div[@id='mstrPromptsBody']/div[2]/div[6]/table/tbody/tr/td[2]/span[1]/img");
		selenium.removeSelection("unitsToAdd_3", "label=Year ... Week");
		selenium.addSelection("unitsToAdd_3", "label=Last X Full Quarters - Fiscal");
		selenium.addSelection("unitsToAdd_3", "label=Last X Full Weeks - Fiscal");
		selenium.removeSelection("unitsToAdd_3", "label=Last X Full Quarters - Fiscal");
		selenium.click("//div[@id='mstrPromptsBody']/div[3]/div[6]/table/tbody/tr/td[2]/span[1]/img");
		selenium.click("//div[@id='mstrPromptsBody']/div[5]/div[6]/table/tbody/tr/td[2]/span[1]/img");
        selenium.click("answerButton");
		selenium.waitForPageToLoad("30000");
		selenium.type("answer_8", "1");
		selenium.click("answerButton");
		selenium.waitForPageToLoad("30000");
    }

    private static void populateFormForLatest(Selenium selenium) {
        selenium.removeSelection("unitsToAdd", "label=Company");
		selenium.addSelection("unitsToAdd", "label=Region");
		selenium.click("//img[@alt='Add to selections']");
		selenium.click("//img[@alt='Add to selections']");
		selenium.removeSelection("unitsToAdd", "label=Category");
		selenium.addSelection("unitsToAdd", "label=Item");
		selenium.click("//img[@alt='Add to selections']");
		selenium.removeSelection("unitsToAdd", "label=Brand");
		selenium.addSelection("unitsToAdd", "label=Week");
		selenium.click("//img[@alt='Add to selections']");
		selenium.click("//div[@id='mstrPromptsBody']/div[2]/div[6]/table/tbody/tr/td[2]/span[1]/img");
		selenium.click("//div[@id='mstrPromptsBody']/div[2]/div[6]/table/tbody/tr/td[2]/span[1]/img");
		selenium.removeSelection("unitsToAdd_1", "label=% $ Sales - YOY");
		selenium.addSelection("unitsToAdd_1", "label=Unit Sales");
		selenium.click("//div[@id='mstrPromptsBody']/div[2]/div[6]/table/tbody/tr/td[2]/span[1]/img");
		selenium.click("//div[@id='mstrPromptsBody']/div[2]/div[6]/table/tbody/tr/td[2]/span[1]/img");
		selenium.addSelection("unitsToAdd_1", "label=Avg Rtl Price");
		selenium.removeSelection("unitsToAdd_1", "label=% Unit Sales - YOY");
		selenium.click("//div[@id='mstrPromptsBody']/div[2]/div[6]/table/tbody/tr/td[2]/span[1]/img");
		selenium.removeSelection("unitsToAdd_3", "label=Year ... Week");
		selenium.addSelection("unitsToAdd_3", "label=This Week - Fiscal");
		selenium.click("//div[@id='mstrPromptsBody']/div[3]/div[6]/table/tbody/tr/td[2]/span[1]/img");
		selenium.click("//div[@id='mstrPromptsBody']/div[5]/div[6]/table/tbody/tr/td[2]/span[1]/img");
		selenium.click("answerButton");
		selenium.waitForPageToLoad("30000");
    }

    private static Selenium createSelenium(String userName, String password) {
        String url = "http://vendor-reporting.wholefoods.com/";
        Selenium selenium = new DefaultSelenium("localhost", 4444, "*firefox", url);
        selenium.start();
        selenium.open("/microstrategy/asp/");
		selenium.type("Uid", userName);
		selenium.type("Pwd", password);
		selenium.click("3054");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Vendor Reporting");
		selenium.waitForPageToLoad("30000");
		selenium.click("//table[@id='FolderSummaryIcons']/tbody/tr[3]/td[4]/a/span");
		selenium.waitForPageToLoad("30000");
        return selenium;
    }    
}
