package com.easyinsight.export;

import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;
import org.hibernate.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: Jun 6, 2010
 * Time: 10:58:02 AM
 */
public class ReportDelivery extends ScheduledDelivery {

    public static final int EXCEL = 1;
    public static final int PNG = 2;
    public static final int PDF = 3;
    public static final int HTML_TABLE = 4;

    private int reportFormat;
    private long reportID;
    private String reportName;
    private String subject;
    private String body;
    private boolean htmlEmail;
    private int timezoneOffset;
    private long senderID;
    private long dataSourceID;
    private String deliveryLabel;

    private List<FilterDefinition> customFilters;

    public String getDeliveryLabel() {
        return deliveryLabel;
    }

    public void setDeliveryLabel(String deliveryLabel) {
        this.deliveryLabel = deliveryLabel;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public List<FilterDefinition> getCustomFilters() {
        return customFilters;
    }

    public void setCustomFilters(List<FilterDefinition> customFilters) {
        this.customFilters = customFilters;
    }

    public long getSenderID() {
        return senderID;
    }

    public void setSenderID(long senderID) {
        this.senderID = senderID;
    }

    public int getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(int timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public boolean isHtmlEmail() {
        return htmlEmail;
    }

    public void setHtmlEmail(boolean htmlEmail) {
        this.htmlEmail = htmlEmail;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getReportID() {
        return reportID;
    }

    public void setReportID(long reportID) {
        this.reportID = reportID;
    }

    public int getReportFormat() {
        return reportFormat;
    }

    public void setReportFormat(int reportFormat) {
        this.reportFormat = reportFormat;
    }

    @Override
    public int retrieveType() {
        return ScheduledActivity.REPORT_DELIVERY;
    }

    protected void customSave(EIConnection conn, int utcOffset) throws SQLException {
        super.customSave(conn, utcOffset);
        setTimezoneOffset(utcOffset);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM REPORT_DELIVERY WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        clearStmt.setLong(1, getScheduledActivityID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO REPORT_DELIVERY (REPORT_ID, delivery_format, subject, body, " +
                "SCHEDULED_ACCOUNT_ACTIVITY_ID, html_email, timezone_offset, sender_user_id, delivery_label) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        insertStmt.setLong(1, reportID);
        insertStmt.setInt(2, reportFormat);
        insertStmt.setString(3, subject);
        insertStmt.setString(4, body);
        insertStmt.setLong(5, getScheduledActivityID());
        insertStmt.setBoolean(6, htmlEmail);
        insertStmt.setInt(7, timezoneOffset);
        if (senderID > 0) {
            insertStmt.setLong(8, senderID);
        } else {
            insertStmt.setNull(8, Types.BIGINT);
        }
        insertStmt.setString(9, deliveryLabel);
        insertStmt.execute();
        long deliveryID = Database.instance().getAutoGenKey(insertStmt);
        insertStmt.close();
        PreparedStatement insertFilterStmt = conn.prepareStatement("INSERT INTO DELIVERY_TO_FILTER_DEFINITION (REPORT_DELIVERY_ID, FILTER_ID) VALUES (?, ?)");
        Session session = Database.instance().createSession(conn);
        try {
            for (FilterDefinition customFilter : customFilters) {
                customFilter.beforeSave(session);
                session.saveOrUpdate(customFilter);
                session.flush();
                insertFilterStmt.setLong(1, deliveryID);
                insertFilterStmt.setLong(2, customFilter.getFilterID());
                insertFilterStmt.execute();
            }
        } finally {
            session.close();
        }
        insertFilterStmt.close();
    }

    protected void customLoad(EIConnection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DELIVERY_FORMAT, REPORT_ID, SUBJECT, BODY, HTML_EMAIL, ANALYSIS.TITLE, " +
                "timezone_offset, SENDER_USER_ID, REPORT_DELIVERY_ID, ANALYSIS.DATA_FEED_ID, DELIVERY_LABEL FROM " +
                "REPORT_DELIVERY, ANALYSIS WHERE " +
                "SCHEDULED_ACCOUNT_ACTIVITY_ID = ? AND REPORT_DELIVERY.REPORT_ID = ANALYSIS.ANALYSIS_ID");
        queryStmt.setLong(1, getScheduledActivityID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            reportFormat = rs.getInt(1);
            reportID = rs.getLong(2);
            subject = rs.getString(3);
            body = rs.getString(4);
            htmlEmail = rs.getBoolean(5);
            reportName = rs.getString(6);
            timezoneOffset = rs.getInt(7);
            long senderID = rs.getLong(8);
            if (!rs.wasNull()) {
                this.senderID = senderID;
            }
            customFilters = new ArrayList<FilterDefinition>();
            long reportDeliveryID = rs.getLong(9);
            dataSourceID = rs.getLong(10);
            deliveryLabel = rs.getString(11);
            Session session = Database.instance().createSession(conn);
            try {
                PreparedStatement filterStmt = conn.prepareStatement("SELECT FILTER_ID FROM DELIVERY_TO_FILTER_DEFINITION WHERE REPORT_DELIVERY_ID = ?");
                filterStmt.setLong(1, reportDeliveryID);
                ResultSet filterRS = filterStmt.executeQuery();
                while (filterRS.next()) {
                    long filterID = filterRS.getLong(1);
                    List results = session.createQuery("from FilterDefinition where filterID = ?").setLong(0, filterID).list();
                    if (results.size() > 0) {
                        FilterDefinition filter = (FilterDefinition) results.get(0);
                        filter.afterLoad();
                        customFilters.add(filter);
                    }
                }
            }  finally {
                session.close();
            }
            queryStmt.close();
        } else {
            throw new RuntimeException("Orphan activity");
        }
    }

    @Override
    public boolean authorize() {
        try {
            SecurityUtil.authorizeInsight(reportID);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void setup(EIConnection conn) throws SQLException {
        // nuke the existing generator
        PreparedStatement queryStmt = conn.prepareStatement("SELECT TASK_GENERATOR_ID FROM delivery_task_generator where SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        queryStmt.setLong(1, getScheduledActivityID());
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM TASK_GENERATOR WHERE TASK_GENERATOR_ID = ?");
            deleteStmt.setLong(1, rs.getLong(1));
            deleteStmt.executeUpdate();
            PreparedStatement arghStmt = conn.prepareStatement("DELETE FROM delivery_task_generator WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
            arghStmt.setLong(1, getScheduledActivityID());
            arghStmt.executeUpdate();
        }
        Session session = Database.instance().createSession(conn);
        try {
            DeliveryTaskGenerator generator = new DeliveryTaskGenerator();
            generator.setStartTaskDate(new Date());
            generator.setActivityID(getScheduledActivityID());
            generator.setTaskInterval(24 * 1000 * 60 * 60);
            session.save(generator);
            session.flush();
        } finally {
            session.close();
        }
    }
}
