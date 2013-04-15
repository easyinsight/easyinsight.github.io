package com.easyinsight.export;

import com.easyinsight.config.ConfigLoader;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.util.RandomTextGenerator;
import com.xerox.amazonws.sns.NotificationService;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.List;

/**
 * User: jamesboe
 * Date: Sep 15, 2010
 * Time: 4:30:07 PM
 */
public abstract class SeleniumPostProcessor {

    public static final int EMAIL = 1;
    public static final int HTML = 2;

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long save(EIConnection conn) throws SQLException {
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO selenium_processor (processor_type) values (?)",
                Statement.RETURN_GENERATED_KEYS);
        insertStmt.setInt(1, getProcessorType());
        insertStmt.execute();
        return Database.instance().getAutoGenKey(insertStmt);
    }

    public abstract int getProcessorType();

    public static SeleniumPostProcessor loadProcessor(long processorID, EIConnection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT PROCESSOR_TYPE FROM SELENIUM_PROCESSOR WHERE " +
                "SELENIUM_PROCESSOR_ID = ?");
        queryStmt.setLong(1, processorID);
        ResultSet rs = queryStmt.executeQuery();
        rs.next();
        int type = rs.getInt(1);
        SeleniumPostProcessor processor;
        if (type == EMAIL) {
            processor = EmailSeleniumPostProcessor.load(processorID, conn);
        } else if (type == HTML) {
            processor = HtmlSeleniumPostProcessor.load(processorID, conn);
        } else {
            throw new RuntimeException();
        }
        processor.setId(processorID);
        return processor;
    }

    public abstract void process(byte[] bytes, EIConnection conn, long accountID, long requestID, long processorID);

    public void processDashboardPDF(List<Page> pages, EIConnection conn, long requestID, long processorID, boolean landscapeOrientation) {
        try {
            byte[] pdf = new ExportService().toImagePDF(pages, landscapeOrientation);
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO PNG_EXPORT (USER_ID, PNG_IMAGE, REPORT_NAME, ANONYMOUS_ID) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            System.out.println("on request " + requestID);
            String topicName = ConfigLoader.instance().getReportDeliveryQueue();

            ByteArrayInputStream bais = new ByteArrayInputStream(pdf);
            BufferedInputStream bis = new BufferedInputStream(bais, 1024);
            long userID = SecurityUtil.getUserID(false);
            if (userID == 0) {
                insertStmt.setNull(1, Types.BIGINT);
            } else {
                insertStmt.setLong(1, SecurityUtil.getUserID());
            }
            insertStmt.setBinaryStream(2, bis, pdf.length);
            insertStmt.setString(3, "export");
            String anonID = RandomTextGenerator.generateText(20);
            insertStmt.setString(4, anonID);
            insertStmt.execute();
            long id = Database.instance().getAutoGenKey(insertStmt);
            System.out.println("sending PDF of " + requestID + " to queue");
            NotificationService ns = new NotificationService("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
            ns.createTopic(topicName);
            System.out.println("notifying " + processorID + "|" + id);
            ns.publish("arn:aws:sns:us-east-1:808335860417:" + topicName, processorID + "|" + id, null);

        } catch (Exception e) {
            LogClass.error(e);
        }
    }
}
