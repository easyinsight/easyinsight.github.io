package com.easyinsight.export;

import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.email.SendGridEmail;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 2/28/13
 * Time: 1:58 PM
 */
public class ScorecardDeliveryUtils {
    public static void sendNoAttachEmails(EIConnection conn, String extraBody, long activityID,
                                          String subjectLine, String body, boolean htmlEmail, int type) throws SQLException, MessagingException, UnsupportedEncodingException {

        PreparedStatement userStmt = conn.prepareStatement("SELECT EMAIL, FIRST_NAME, NAME, USER.PERSONA_ID, USER.USER_ID, USER.ACCOUNT_ADMIN, USER.USERNAME FROM USER, delivery_to_user WHERE delivery_to_user.scheduled_account_activity_id = ? and " +
                "delivery_to_user.user_id = user.user_id");
        List<UserInfo> userStubs = new ArrayList<UserInfo>();
        userStmt.setLong(1, activityID);
        ResultSet rs = userStmt.executeQuery();
        while (rs.next()) {
            String email = rs.getString(1);
            String firstName = rs.getString(2);
            String lastName = rs.getString(3);
            userStubs.add(new UserInfo(email, firstName, lastName, rs.getString(7), rs.getLong(5), rs.getBoolean(6), rs.getLong(4)));
        }
        userStmt.close();

        PreparedStatement groupStmt = conn.prepareStatement("SELECT EMAIL, FIRST_NAME, NAME, USER.PERSONA_ID, USER.USER_ID, USER.ACCOUNT_ADMIN, USER.USERNAME FROM USER, delivery_to_group, group_to_user_join WHERE " +
                "delivery_to_group.scheduled_account_activity_id = ? AND delivery_to_group.group_id = group_to_user_join.group_id and group_to_user_join.user_id = user.user_id");
        groupStmt.setLong(1, activityID);
        ResultSet groupRS = groupStmt.executeQuery();
        while (groupRS.next()) {
            String email = groupRS.getString(1);
            String firstName = groupRS.getString(2);
            String lastName = groupRS.getString(3);
            userStubs.add(new UserInfo(email, firstName, lastName, rs.getString(7), rs.getLong(5), rs.getBoolean(6), rs.getLong(4)));
        }
        groupStmt.close();

        String senderName = null;
        String senderEmail = null;
        if (type == ScheduledActivity.REPORT_DELIVERY) {
            PreparedStatement getSenderStmt = conn.prepareStatement("SELECT EMAIL, FIRST_NAME, NAME FROM USER, REPORT_DELIVERY WHERE REPORT_DELIVERY.sender_user_id = user.user_id and " +
                    "REPORT_DELIVERY.scheduled_account_activity_id = ?");
            getSenderStmt.setLong(1, activityID);
            ResultSet senderRS = getSenderStmt.executeQuery();
            if (senderRS.next()) {
                String email = senderRS.getString(1);
                String firstName = senderRS.getString(2);
                String lastName = senderRS.getString(3);
                senderName = firstName + " " + lastName;
                senderEmail = email;
            }
            getSenderStmt.close();
        } else {
            PreparedStatement getSenderStmt = conn.prepareStatement("SELECT EMAIL, FIRST_NAME, NAME FROM USER, SCORECARD_DELIVERY WHERE SCORECARD_DELIVERY.sender_user_id = user.user_id and " +
                    "SCORECARD_DELIVERY.scheduled_account_activity_id = ?");
            getSenderStmt.setLong(1, activityID);
            ResultSet senderRS = getSenderStmt.executeQuery();
            if (senderRS.next()) {
                String email = senderRS.getString(1);
                String firstName = senderRS.getString(2);
                String lastName = senderRS.getString(3);
                senderName = firstName + " " + lastName;
                senderEmail = email;
            }
            getSenderStmt.close();
        }

        PreparedStatement emailQueryStmt = conn.prepareStatement("SELECT EMAIL_ADDRESS FROM delivery_to_email where scheduled_account_activity_id = ?");
        emailQueryStmt.setLong(1, activityID);
        List<String> emails = new ArrayList<String>();
        ResultSet emailRS = emailQueryStmt.executeQuery();
        while (emailRS.next()) {
            String email = emailRS.getString(1);
            emails.add(email);
        }
        emailQueryStmt.close();

        PreparedStatement deliveryAuditStmt = conn.prepareStatement("INSERT INTO REPORT_DELIVERY_AUDIT (" +
                "SCHEDULED_ACCOUNT_ACTIVITY_ID, SUCCESSFUL, TARGET_EMAIL, SEND_DATE) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        for (UserInfo userInfo : userStubs) {
            deliveryAuditStmt.setLong(1, activityID);
            deliveryAuditStmt.setInt(2, 0);
            deliveryAuditStmt.setString(3, userInfo.email);
            deliveryAuditStmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
            deliveryAuditStmt.execute();
            long auditID = Database.instance().getAutoGenKey(deliveryAuditStmt);
            new SendGridEmail().sendNoAttachmentEmail(userInfo.email, subjectLine, body + extraBody, htmlEmail, senderEmail, senderName, "ReportDelivery", auditID);
        }
        for (String email : emails) {
            deliveryAuditStmt.setLong(1, activityID);
            deliveryAuditStmt.setInt(2, 0);
            deliveryAuditStmt.setString(3, email);
            deliveryAuditStmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
            deliveryAuditStmt.execute();
            long auditID = Database.instance().getAutoGenKey(deliveryAuditStmt);
            new SendGridEmail().sendNoAttachmentEmail(email, subjectLine, body + extraBody, htmlEmail, senderEmail, senderName, "ReportDelivery", auditID);
        }
        deliveryAuditStmt.close();
    }

    public static void scorecardDelivery(EIConnection conn, long activityID) throws Exception {
        PreparedStatement userStmt = conn.prepareStatement("SELECT EMAIL, USER_ID, ACCOUNT_ID, DELIVERY_FORMAT, SCORECARD_ID," +
                "SUBJECT, BODY, HTML_EMAIL, TIMEZONE_OFFSET FROM USER, scorecard_delivery WHERE " +
                "scorecard_delivery.scheduled_account_activity_id = ? and scorecard_delivery.sender_user_id = user.user_id");
        userStmt.setLong(1, activityID);
        ResultSet rs = userStmt.executeQuery();
        if (rs.next()) {
            String email = rs.getString(1);
            //long userID = rs.getLong(2);
            long accountID = rs.getLong(3);
            int deliveryFormat = rs.getInt(4);
            long scorecardID = rs.getLong(5);
            String subject = rs.getString(6);
            String body = rs.getString(7);
            boolean htmlEmail = rs.getBoolean(8);
            int timezoneOffset = rs.getInt(9);
            PreparedStatement findOwnerStmt = conn.prepareStatement("SELECT USER_ID FROM scorecard where scorecard_id = ?");

            findOwnerStmt.setLong(1, scorecardID);
            ResultSet ownerRS = findOwnerStmt.executeQuery();
            long ownerID;
            if (ownerRS.next()) {
                ownerID = ownerRS.getLong(1);
            } else {
                return;
            }
            InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
            insightRequestMetadata.setUtcOffset(timezoneOffset);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT USERNAME, ACCOUNT.ACCOUNT_TYPE, USER.account_admin," +
                    "ACCOUNT.FIRST_DAY_OF_WEEK, USER.first_name, USER.name, USER.email FROM USER, ACCOUNT " +
                    "WHERE USER.ACCOUNT_ID = ACCOUNT.ACCOUNT_ID AND (ACCOUNT.account_state = ? OR ACCOUNT.ACCOUNT_STATE = ?) AND USER.USER_ID = ?");
            queryStmt.setInt(1, Account.ACTIVE);
            queryStmt.setInt(2, Account.TRIAL);
            queryStmt.setLong(3, ownerID);
            ResultSet queryRS = queryStmt.executeQuery();
            if (queryRS.next()) {
                String userName = queryRS.getString(1);
                int accountType = queryRS.getInt(2);
                boolean accountAdmin = queryRS.getBoolean(3);
                int firstDayOfWeek = queryRS.getInt(4);
                String firstName = queryRS.getString(5);
                String lastName = queryRS.getString(6);
                PreparedStatement stmt = conn.prepareStatement("SELECT PERSONA.persona_name FROM USER, PERSONA WHERE USER.PERSONA_ID = PERSONA.PERSONA_ID AND USER.USER_ID = ?");
                stmt.setLong(1, ownerID);
                ResultSet personaRS = stmt.executeQuery();

                String personaName = null;
                if (personaRS.next()) {
                    personaName = personaRS.getString(1);
                }
                stmt.close();
                SecurityUtil.populateThreadLocal(userName, ownerID, accountID, accountType, accountAdmin, firstDayOfWeek, personaName);
                try {
                    String scorecardHTML = ExportService.exportScorecard(scorecardID, insightRequestMetadata, conn);
                    sendNoAttachEmails(conn, scorecardHTML, activityID, subject, body, htmlEmail, ScheduledActivity.SCORECARD_DELIVERY);
                } finally {
                    SecurityUtil.clearThreadLocal();
                }
            }
        }
    }
}
