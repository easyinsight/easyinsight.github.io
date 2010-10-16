package com.easyinsight.export;

import com.easyinsight.analysis.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.email.SendGridEmail;
import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.scorecard.*;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;

import javax.mail.MessagingException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: Jun 6, 2010
 * Time: 1:49:58 PM
 */
@Entity
@Table(name = "delivery_scheduled_task")
@PrimaryKeyJoinColumn(name = "scheduled_task_id")
public class DeliveryScheduledTask extends ScheduledTask {
    @Column(name = "scheduled_account_activity_id")
    private long activityID;

    public long getActivityID() {
        return activityID;
    }

    public void setActivityID(long activityID) {
        this.activityID = activityID;
    }

    protected void execute(Date now, EIConnection conn) throws Exception {

        // identify the type of activity

        PreparedStatement typeStmt = conn.prepareStatement("SELECT SCHEDULED_ACCOUNT_ACTIVITY.activity_type FROM SCHEDULED_ACCOUNT_ACTIVITY WHERE " +
                "SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        typeStmt.setLong(1, activityID);
        ResultSet typeRS = typeStmt.executeQuery();
        if (typeRS.next()) {
            int type = typeRS.getInt(1);
            if (type == ScheduledActivity.REPORT_DELIVERY) {
                reportDelivery(conn);        
            } else if (type == ScheduledActivity.SCORECARD_DELIVERY) {
                scorecardDelivery(conn);
            }
        }
    }

    private void scorecardDelivery(EIConnection conn) throws SQLException, IOException, MessagingException {
        PreparedStatement userStmt = conn.prepareStatement("SELECT EMAIL, USER_ID FROM USER, user_scorecard_display WHERE " +
                "user_scorecard_display.scheduled_account_activity_id = ? and user_scorecard_display.user_id = user.user_id");
        userStmt.setLong(1, activityID);
        ResultSet rs = userStmt.executeQuery();
        rs.next();
        String email = rs.getString(1);
        long userID = rs.getLong(2);
        StringBuilder stringBuilder = new StringBuilder();
        ScorecardList scorecardList = new ScorecardInternalService().getScorecardDescriptors(userID, true);
        /*for (ScorecardDescriptor scorecardDescriptor : scorecardList.getScorecardDescriptors()) {
            ScorecardWrapper scorecardWrapper = new ScorecardInternalService().getScorecard(scorecardDescriptor.getId(),
                    userID, new ArrayList<CredentialFulfillment>(), false);
            Scorecard scorecard = scorecardWrapper.getScorecard();
            for (KPI kpi : scorecard.getKpis()) {
                KPIOutcome kpiOutcome = kpi.getKpiOutcome();
                //kpiOutcome.
            }
        }*/
        //new SendGridEmail().sendAttachmentEmail(email, subjectLine, body, bytes, reportName, htmlEmail);
    }

    private void reportDelivery(EIConnection conn) throws SQLException, IOException, MessagingException {

        PreparedStatement getInfoStmt = conn.prepareStatement("SELECT DELIVERY_FORMAT, REPORT_ID, SUBJECT, BODY, HTML_EMAIL, REPORT_DELIVERY_ID FROM REPORT_DELIVERY WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        getInfoStmt.setLong(1, activityID);
        ResultSet deliveryInfoRS = getInfoStmt.executeQuery();
        if (deliveryInfoRS.next()) {
            int deliveryFormat = deliveryInfoRS.getInt(1);

            long reportID = deliveryInfoRS.getLong(2);
            long ownerID;
            PreparedStatement findOwnerStmt = conn.prepareStatement("SELECT USER_ID FROM user_to_analysis where analysis_id = ?");

            findOwnerStmt.setLong(1, reportID);
            ResultSet ownerRS = findOwnerStmt.executeQuery();
            if (ownerRS.next()) {
                ownerID = ownerRS.getLong(1);
            } else {
                throw new RuntimeException();
            }
            PreparedStatement queryStmt = conn.prepareStatement("SELECT USERNAME, USER_ID, USER.ACCOUNT_ID, ACCOUNT.ACCOUNT_TYPE, USER.account_admin FROM USER, ACCOUNT " +
                        "WHERE USER.ACCOUNT_ID = ACCOUNT.ACCOUNT_ID AND (ACCOUNT.account_state = ? OR ACCOUNT.ACCOUNT_STATE = ?) AND USER.USER_ID = ?");
            queryStmt.setInt(1, Account.ACTIVE);
            queryStmt.setInt(2, Account.TRIAL);
            queryStmt.setLong(3, ownerID);
            ResultSet queryRS = queryStmt.executeQuery();
            if (queryRS.next()) {
                String userName = queryRS.getString(1);
                long userID = queryRS.getLong(2);
                long accountID = queryRS.getLong(3);
                int accountType = queryRS.getInt(4);
                boolean accountAdmin = queryRS.getBoolean(5);
                SecurityUtil.populateThreadLocal(userName, userID, accountID, accountType, accountAdmin, false);

                if (deliveryFormat == ReportDelivery.EXCEL) {
                    WSAnalysisDefinition analysisDefinition = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
                    analysisDefinition.updateMetadata();
                    DataResults dataResults = new DataService().list(analysisDefinition, new InsightRequestMetadata());
                    if (dataResults instanceof ListDataResults) {
                        ListDataResults listDataResults = (ListDataResults) dataResults;
                        byte[] bytes = new ExportService().toExcel(analysisDefinition, listDataResults);
                        String reportName = analysisDefinition.getName();
                        sendEmails(conn, bytes, reportName + ".xls", accountID, "application/excel", activityID);
                    }
                } else if (deliveryFormat == ReportDelivery.PNG) {
                    new SeleniumLauncher().requestSeleniumDrawForEmail(activityID, userID, accountID, conn);
                }
            }
        }
    }

    private static class UserInfo {
        String email;
        String firstName;
        String lastName;

        private UserInfo(String email, String firstName, String lastName) {
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    public static void sendEmails(EIConnection conn, byte[] bytes, String attachmentName, long accountID, String encoding, long activityID) throws SQLException, MessagingException, UnsupportedEncodingException {
        PreparedStatement getInfoStmt = conn.prepareStatement("SELECT DELIVERY_FORMAT, REPORT_ID, SUBJECT, BODY, HTML_EMAIL, REPORT_DELIVERY_ID FROM REPORT_DELIVERY WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        getInfoStmt.setLong(1, activityID);
        ResultSet deliveryInfoRS = getInfoStmt.executeQuery();
        deliveryInfoRS.next();
        String subjectLine = deliveryInfoRS.getString(3);
        String body = deliveryInfoRS.getString(4);
        boolean htmlEmail = deliveryInfoRS.getBoolean(5);
        long deliveryID = deliveryInfoRS.getLong(6);
        PreparedStatement userStmt = conn.prepareStatement("SELECT EMAIL, FIRST_NAME, NAME FROM USER, delivery_to_user WHERE delivery_to_user.scheduled_account_activity_id = ? and " +
                "delivery_to_user.user_id = user.user_id");
        List<UserInfo> userStubs = new ArrayList<UserInfo>();
        userStmt.setLong(1, activityID);
        ResultSet rs = userStmt.executeQuery();
        while (rs.next()) {
            String email = rs.getString(1);
            String firstName = rs.getString(2);
            String lastName = rs.getString(3);
            userStubs.add(new UserInfo(email, firstName, lastName));
        }

        String senderName = null;
        String senderEmail = null;
        PreparedStatement getSernderStmt = conn.prepareStatement("SELECT EMAIL, FIRST_NAME, NAME FROM USER, REPORT_DELIVERY WHERE REPORT_DELIVERY.sender_user_id = user.user_id and " +
                "REPORT_DELIVERY.scheduled_account_activity_id = ?");
        getSernderStmt.setLong(1, activityID);
        ResultSet senderRS = getSernderStmt.executeQuery();
        if (senderRS.next()) {
            String email = senderRS.getString(1);
            String firstName = senderRS.getString(2);
            String lastName = senderRS.getString(3);
            senderName = firstName + " " + lastName;
            senderEmail = email;
        }

        PreparedStatement emailQueryStmt = conn.prepareStatement("SELECT EMAIL_ADDRESS FROM delivery_to_email where scheduled_account_activity_id = ?");
        emailQueryStmt.setLong(1, activityID);
        List<String> emails = new ArrayList<String>();
        ResultSet emailRS = emailQueryStmt.executeQuery();
        while (emailRS.next()) {
            String email = emailRS.getString(1);
            emails.add(email);
        }

        PreparedStatement deliveryAuditStmt = conn.prepareStatement("INSERT INTO REPORT_DELIVERY_AUDIT (ACCOUNT_ID," +
                "REPORT_DELIVERY_ID, SUCCESSFUL, MESSAGE, TARGET_EMAIL, SEND_DATE) VALUES (?, ?, ?, ?, ?, ?)");
        for (UserInfo userInfo : userStubs) {
            new SendGridEmail().sendAttachmentEmail(userInfo.email, subjectLine, body, bytes, attachmentName, htmlEmail, senderEmail, senderName, encoding);
            deliveryAuditStmt.setLong(1, accountID);
            deliveryAuditStmt.setLong(2, deliveryID);
            deliveryAuditStmt.setBoolean(3, true);
            deliveryAuditStmt.setString(4, null);
            deliveryAuditStmt.setString(5, userInfo.email);
            deliveryAuditStmt.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
            deliveryAuditStmt.execute();
        }
        for (String email : emails) {
            new SendGridEmail().sendAttachmentEmail(email, subjectLine, body, bytes, attachmentName, htmlEmail, senderEmail, senderName, encoding);
            deliveryAuditStmt.setLong(1, accountID);
            deliveryAuditStmt.setLong(2, deliveryID);
            deliveryAuditStmt.setBoolean(3, true);
            deliveryAuditStmt.setString(4, null);
            deliveryAuditStmt.setString(5, email);
            deliveryAuditStmt.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
            deliveryAuditStmt.execute();
        }
        deliveryAuditStmt.close();
    }
}
