package com.easyinsight.export;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.util.RandomTextGenerator;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.SQSException;
import com.xerox.amazonws.sqs2.SQSUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.MessageFormat;

/**
 * User: jamesboe
 * Date: Sep 23, 2010
 * Time: 11:55:38 AM
 */
public class SeleniumLauncher {

    private static final String URL = "/app/selenium/selenium.jsp?userName={0}&password={1}&seleniumID={2}&dataSourceID={3}&reportID={4}&reportType={5}&width={6}&height={7}";

    public void requestSeleniumDrawForEmail(long accountActivityID, long userID, long accountID) {
        // emit the JMS event or whatever we're using
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            EmailSeleniumPostProcessor processor = new EmailSeleniumPostProcessor(accountActivityID);
            long id = processor.save(conn);
            String userName = RandomTextGenerator.generateText(50);
            String password = RandomTextGenerator.generateText(50);
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO SELENIUM_REQUEST (SELENIUM_PROCESSOR_ID, " +
                    "USERNAME, PASSWORD, REQUEST_TIME, USER_ID, ACCOUNT_ID) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertStmt.setLong(1, id);
            insertStmt.setString(2, userName);
            insertStmt.setString(3, password);
            insertStmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
            insertStmt.setLong(5, userID);
            insertStmt.setLong(6, accountID);
            insertStmt.execute();
            String seleniumID = String.valueOf(Database.instance().getAutoGenKey(insertStmt));
            PreparedStatement queryStmt = conn.prepareStatement("SELECT ANALYSIS.title, ANALYSIS.data_feed_id, ANALYSIS.report_type, ANALYSIS.url_key FROM " +
                    "ANALYSIS, REPORT_DELIVERY, SCHEDULED_ACCOUNT_ACTIVITY WHERE ANALYSIS.analysis_id = REPORT_DELIVERY.report_id AND " +
                    "REPORT_DELIVERY.scheduled_account_activity_id = ?");
            queryStmt.setLong(1, accountActivityID);
            ResultSet rs = queryStmt.executeQuery();
            rs.next();
            String dataSourceID = String.valueOf(rs.getLong(2));
            String reportType = String.valueOf(rs.getInt(3));
            String urlKey = rs.getString(4);
            String url = MessageFormat.format(URL, userName, password, seleniumID, dataSourceID, urlKey, reportType, "1000", "800");
            System.out.println(url);
            conn.commit();
            launchRequest(url);
        } catch (Throwable e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    private void launchRequest(String url) {
        // send an SQS request
        try {
            MessageQueue msgQueue = SQSUtils.connectToQueue("EISelenium", "0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
            msgQueue.sendMessage(url);
        } catch (Exception e) {
            LogClass.error(e);
        }
    }
}
