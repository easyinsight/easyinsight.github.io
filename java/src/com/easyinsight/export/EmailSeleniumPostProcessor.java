package com.easyinsight.export;

import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: Sep 15, 2010
 * Time: 4:30:37 PM
 */
public class EmailSeleniumPostProcessor extends SeleniumPostProcessor {

    private long accountActivityID;

    public EmailSeleniumPostProcessor(long accountActivityID) {
        this.accountActivityID = accountActivityID;
    }

    public long save(EIConnection conn) throws SQLException {
        long id = super.save(conn);
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO email_selenium_processor (scheduled_account_activity_id, " +
                "selenium_processor_id) values (?, ?)");
        insertStmt.setLong(1, accountActivityID);
        insertStmt.setLong(2, id);
        insertStmt.execute();
        return id;
    }

    @Override
    public int getProcessorType() {
        return SeleniumPostProcessor.EMAIL;
    }

    @Override
    public void process(byte[] bytes, EIConnection conn, long accountID) {
        try {
            ReportDelivery reportDelivery = (ReportDelivery) ScheduledActivity.createActivity(ScheduledActivity.REPORT_DELIVERY, accountActivityID, conn);
            if (reportDelivery.getReportFormat() == ReportDelivery.PNG) {
                DeliveryScheduledTask.sendEmails(conn, bytes, reportDelivery.getReportName() + ".png", accountID, "image/png", accountActivityID);
            } else if (reportDelivery.getReportFormat() == ReportDelivery.PDF) {
                byte[] pdf = new ExportService().toImagePDF(bytes, 1000, 800);
                DeliveryScheduledTask.sendEmails(conn, pdf, reportDelivery.getReportName() + ".png", accountID, "application/pdf", accountActivityID);
            }
        } catch (Exception e) {
            LogClass.error(e);
        }
    }

    public static SeleniumPostProcessor load(long processorID, EIConnection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT scheduled_account_activity_id FROM EMAIL_SELENIUM_PROCESSOR WHERE " +
                "SELENIUM_PROCESSOR_ID = ?");
        queryStmt.setLong(1, processorID);
        ResultSet rs = queryStmt.executeQuery();
        rs.next();
        long reportDeliveryID = rs.getLong(1);
        return new EmailSeleniumPostProcessor(reportDeliveryID);
    }
}
