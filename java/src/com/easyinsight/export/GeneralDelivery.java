package com.easyinsight.export;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * User: jamesboe
 * Date: Jun 10, 2010
 * Time: 2:46:01 PM
 */
public class GeneralDelivery extends ScheduledDelivery {

    private List<DeliveryInfo> deliveryInfos;

    private String subject;
    private String body;
    private boolean htmlEmail;
    private int timezoneOffset;
    private long senderID;

    public List<DeliveryInfo> getDeliveryInfos() {
        return deliveryInfos;
    }

    public void setDeliveryInfos(List<DeliveryInfo> deliveryInfos) {
        this.deliveryInfos = deliveryInfos;
    }

    public long getSenderID() {
        return senderID;
    }

    public void setSenderID(long senderID) {
        this.senderID = senderID;
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

    public boolean isHtmlEmail() {
        return htmlEmail;
    }

    public void setHtmlEmail(boolean htmlEmail) {
        this.htmlEmail = htmlEmail;
    }

    public int getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(int timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }



    @Override
    public int retrieveType() {
        return ScheduledActivity.GENERAL_DELIVERY;
    }

    @Override
    protected void customSave(EIConnection conn, int utcOffset) throws SQLException {
        super.customSave(conn, utcOffset);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM GENERAL_DELIVERY WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        clearStmt.setLong(1, getScheduledActivityID());
        clearStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO GENERAL_DELIVERY (SCHEDULED_ACCOUNT_ACTIVITY_ID, SENDER_USER_ID, SUBJECT," +
                "BODY, TIMEZONE_OFFSET, HTML_EMAIL) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        insertStmt.setLong(1, getScheduledActivityID());
        insertStmt.setLong(2, SecurityUtil.getUserID());
        insertStmt.setString(3, subject);
        insertStmt.setString(4, body);
        insertStmt.setInt(5, timezoneOffset);
        insertStmt.setBoolean(6, htmlEmail);
        insertStmt.execute();
        long id = Database.instance().getAutoGenKey(insertStmt);
        PreparedStatement insertReportStmt = conn.prepareStatement("INSERT INTO delivery_to_report (general_delivery_id, report_id, delivery_index, delivery_format) values (?, ?, ?, ?)");
        PreparedStatement insertScorecardStmt = conn.prepareStatement("INSERT INTO delivery_to_scorecard (general_delivery_id, scorecard_id, delivery_index, delivery_format) values (?, ?, ?, ?)");
        int index = 0;
        for (DeliveryInfo deliveryInfo : getDeliveryInfos()) {
            if (deliveryInfo.getType() == DeliveryInfo.REPORT) {
                insertReportStmt.setLong(1, id);
                insertReportStmt.setLong(2, deliveryInfo.getId());
                insertReportStmt.setInt(3, index++);
                insertReportStmt.setInt(4, deliveryInfo.getFormat());
                insertReportStmt.execute();
            } else if (deliveryInfo.getType() == DeliveryInfo.SCORECARD) {
                insertScorecardStmt.setLong(1, id);
                insertScorecardStmt.setLong(2, deliveryInfo.getId());
                insertScorecardStmt.setInt(3, index++);
                insertScorecardStmt.setInt(4, deliveryInfo.getFormat());
                insertScorecardStmt.execute();
            }
        }
    }

    @Override
    protected void customLoad(EIConnection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT SENDER_USER_ID, SUBJECT, BODY, TIMEZONE_OFFSET, HTML_EMAIL, GENERAL_DELIVERY_ID, USER_ID " +
                "FROM " +
                "GENERAL_DELIVERY, scheduled_account_activity WHERE general_delivery.SCHEDULED_ACCOUNT_ACTIVITY_ID = ? and " +
                "general_delivery.scheduled_account_activity_id = scheduled_account_activity.scheduled_account_activity_id");
        queryStmt.setLong(1, getScheduledActivityID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            senderID = rs.getLong(1);
            subject = rs.getString(2);
            body = rs.getString(3);
            timezoneOffset = rs.getInt(4);
            htmlEmail = rs.getBoolean(5);
            long id = rs.getLong(6);
            setUserID(rs.getLong(7));
            PreparedStatement getReportStmt = conn.prepareStatement("SELECT REPORT_ID, TITLE, DELIVERY_INDEX, DELIVERY_FORMAT FROM DELIVERY_TO_REPORT, ANALYSIS WHERE GENERAL_DELIVERY_ID = ? AND " +
                    "delivery_to_report.report_id = analysis.analysis_id");
            getReportStmt.setLong(1, id);
            List<DeliveryInfo> infos = new ArrayList<DeliveryInfo>();
            ResultSet reports = getReportStmt.executeQuery();
            while (reports.next()) {
                DeliveryInfo deliveryInfo = new DeliveryInfo();
                deliveryInfo.setId(reports.getLong(1));
                deliveryInfo.setName(reports.getString(2));
                deliveryInfo.setIndex(reports.getInt(3));
                deliveryInfo.setFormat(reports.getInt(4));
                deliveryInfo.setType(DeliveryInfo.REPORT);
                infos.add(deliveryInfo);
            }
            PreparedStatement getScorecardStmt = conn.prepareStatement("SELECT SCORECARD.SCORECARD_ID, SCORECARD_NAME, DELIVERY_INDEX, DELIVERY_FORMAT FROM delivery_to_scorecard, scorecard WHERE GENERAL_DELIVERY_ID = ? AND " +
                    "delivery_to_scorecard.scorecard_id = scorecard.scorecard_id");
            getScorecardStmt.setLong(1, id);
            ResultSet scorecards = getScorecardStmt.executeQuery();
            while (scorecards.next()) {
                DeliveryInfo deliveryInfo = new DeliveryInfo();
                deliveryInfo.setId(scorecards.getLong(1));
                deliveryInfo.setName(scorecards.getString(2));
                deliveryInfo.setIndex(scorecards.getInt(3));
                deliveryInfo.setFormat(scorecards.getInt(4));
                deliveryInfo.setType(DeliveryInfo.SCORECARD);
                infos.add(deliveryInfo);
            }
            Collections.sort(infos, new Comparator<DeliveryInfo>() {

                public int compare(DeliveryInfo deliveryInfo, DeliveryInfo deliveryInfo1) {
                    return ((Integer) deliveryInfo.getIndex()).compareTo(deliveryInfo1.getIndex());
                }
            });
            setDeliveryInfos(infos);
        }
    }

    @Override
    public void setup(EIConnection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT TASK_GENERATOR_ID FROM delivery_task_generator where SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        queryStmt.setLong(1, getScheduledActivityID());
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM TASK_GENERATOR WHERE TASK_GENERATOR_ID = ?");
            deleteStmt.setLong(1, rs.getLong(1));
            deleteStmt.executeUpdate();
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
