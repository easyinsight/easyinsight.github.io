package com.easyinsight.export;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * User: jamesboe
 * Date: Jun 10, 2010
 * Time: 2:46:01 PM
 */
public class ScorecardDelivery extends ScheduledDelivery {

    private long scorecardID;
    private String scorecardName;
    private String subject;
    private String body;
    private boolean htmlEmail;
    private int timezoneOffset;
    private int deliveryFormat;
    private long senderID;

    public long getSenderID() {
        return senderID;
    }

    public void setSenderID(long senderID) {
        this.senderID = senderID;
    }

    public int getDeliveryFormat() {
        return deliveryFormat;
    }

    public void setDeliveryFormat(int deliveryFormat) {
        this.deliveryFormat = deliveryFormat;
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

    public String getScorecardName() {
        return scorecardName;
    }

    public void setScorecardName(String scorecardName) {
        this.scorecardName = scorecardName;
    }

    public long getScorecardID() {
        return scorecardID;
    }

    public void setScorecardID(long scorecardID) {
        this.scorecardID = scorecardID;
    }

    @Override
    public int retrieveType() {
        return ScheduledActivity.SCORECARD_DELIVERY;
    }

    @Override
    protected void customSave(EIConnection conn, int utcOffset) throws SQLException {
        super.customSave(conn, utcOffset);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM SCORECARD_DELIVERY WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        clearStmt.setLong(1, getScheduledActivityID());
        clearStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO SCORECARD_DELIVERY (SCHEDULED_ACCOUNT_ACTIVITY_ID, SENDER_USER_ID, SUBJECT," +
                "BODY, TIMEZONE_OFFSET, SCORECARD_ID, HTML_EMAIL, DELIVERY_FORMAT) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        insertStmt.setLong(1, getScheduledActivityID());
        insertStmt.setLong(2, SecurityUtil.getUserID());
        insertStmt.setString(3, subject);
        insertStmt.setString(4, body);
        insertStmt.setInt(5, timezoneOffset);
        insertStmt.setLong(6, scorecardID);
        insertStmt.setBoolean(7, htmlEmail);
        insertStmt.setInt(8, deliveryFormat);
        insertStmt.execute();
    }

    @Override
    protected void customLoad(EIConnection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT SENDER_USER_ID, SUBJECT, BODY, TIMEZONE_OFFSET, SCORECARD.SCORECARD_ID, HTML_EMAIL, DELIVERY_FORMAT," +
                "SCORECARD.SCORECARD_NAME FROM " +
                "SCORECARD_DELIVERY, SCORECARD WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ? AND SCORECARD.scorecard_id = scorecard_delivery.scorecard_id");
        queryStmt.setLong(1, getScheduledActivityID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            senderID = rs.getLong(1);
            subject = rs.getString(2);
            body = rs.getString(3);
            timezoneOffset = rs.getInt(4);
            scorecardID = rs.getLong(5);
            htmlEmail = rs.getBoolean(6);
            deliveryFormat = rs.getInt(7);
            scorecardName = rs.getString(8);
        }
    }

    @Override
    public boolean authorize() {
        try {
            SecurityUtil.authorizeScorecard(scorecardID);
            return true;
        } catch (Exception e) {
            return false;
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
