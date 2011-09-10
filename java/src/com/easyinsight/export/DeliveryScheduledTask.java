package com.easyinsight.export;

import com.easyinsight.analysis.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.email.SendGridEmail;
import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
import com.itextpdf.text.DocumentException;

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
import java.util.*;

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
            } else if (type == ScheduledActivity.GENERAL_DELIVERY) {
                generalDelivery(conn);
            }
        }
        typeStmt.close();
    }

    private void generalDelivery(EIConnection conn) throws Exception {
        PreparedStatement getInfoStmt = conn.prepareStatement("SELECT SUBJECT, BODY, HTML_EMAIL, TIMEZONE_OFFSET, GENERAL_DELIVERY_ID, USER_ID FROM GENERAL_DELIVERY, " +
                "SCHEDULED_ACCOUNT_ACTIVITY WHERE GENERAL_DELIVERY.SCHEDULED_ACCOUNT_ACTIVITY_ID = ? AND GENERAL_DELIVERY.scheduled_account_activity_id = SCHEDULED_ACCOUNT_ACTIVITY.SCHEDULED_ACCOUNT_ACTIVITY_ID");
        getInfoStmt.setLong(1, activityID);
        ResultSet deliveryInfoRS = getInfoStmt.executeQuery();
        if (deliveryInfoRS.next()) {
            final String subject = deliveryInfoRS.getString(1);
            String body = deliveryInfoRS.getString(2);
            final boolean htmlEmail = deliveryInfoRS.getBoolean(3);
            int timezoneOffset = deliveryInfoRS.getInt(4);
            long id = deliveryInfoRS.getLong(5);
            long ownerID = deliveryInfoRS.getLong(6);
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

            PreparedStatement queryStmt = conn.prepareStatement("SELECT USERNAME, ACCOUNT.ACCOUNT_TYPE, USER.account_admin," +
                    "ACCOUNT.FIRST_DAY_OF_WEEK, USER.first_name, USER.name, USER.email, USER.ACCOUNT_ID FROM USER, ACCOUNT " +
                        "WHERE USER.ACCOUNT_ID = ACCOUNT.ACCOUNT_ID AND (ACCOUNT.account_state = ? OR ACCOUNT.ACCOUNT_STATE = ?) AND USER.USER_ID = ?");
            queryStmt.setInt(1, Account.ACTIVE);
            queryStmt.setInt(2, Account.TRIAL);
            queryStmt.setLong(3, ownerID);
            ResultSet queryRS = queryStmt.executeQuery();
            if (!queryRS.next()) {
                return;
            }

            String userName = queryRS.getString(1);
            int accountType = queryRS.getInt(2);
            boolean accountAdmin = queryRS.getBoolean(3);
            int firstDayOfWeek = queryRS.getInt(4);
            String firstName = queryRS.getString(5);
            String lastName = queryRS.getString(6);
            long accountID = queryRS.getLong(8);

            SecurityUtil.populateThreadLocal(userName, ownerID, accountID, accountType, accountAdmin, false, firstDayOfWeek);
            try {

                List<AttachmentInfo> attachmentInfos = new ArrayList<AttachmentInfo>();
                for (DeliveryInfo deliveryInfo : infos) {
                    body = handleDeliveryInfo(deliveryInfo, body, attachmentInfos, conn, timezoneOffset);
                }

                PreparedStatement userStmt = conn.prepareStatement("SELECT EMAIL, FIRST_NAME, NAME FROM USER, delivery_to_user WHERE delivery_to_user.scheduled_account_activity_id = ? and " +
                    "delivery_to_user.user_id = user.user_id");
                List<UserInfo> userStubs = new ArrayList<UserInfo>();
                userStmt.setLong(1, activityID);
                ResultSet rs = userStmt.executeQuery();
                while (rs.next()) {
                    String email = rs.getString(1);
                    String userFirstName = rs.getString(2);
                    String userLastName = rs.getString(3);
                    userStubs.add(new UserInfo(email, userFirstName, userLastName));
                }
                userStmt.close();

                String senderName = null;
                String senderEmail = null;
                PreparedStatement getSernderStmt = conn.prepareStatement("SELECT EMAIL, FIRST_NAME, NAME FROM USER, REPORT_DELIVERY WHERE REPORT_DELIVERY.sender_user_id = user.user_id and " +
                        "REPORT_DELIVERY.scheduled_account_activity_id = ?");
                getSernderStmt.setLong(1, activityID);
                ResultSet senderRS = getSernderStmt.executeQuery();
                if (senderRS.next()) {
                    String email = senderRS.getString(1);
                    String userFirstName = senderRS.getString(2);
                    String userLastName = senderRS.getString(3);
                    senderName = userFirstName + " " + userLastName;
                    senderEmail = email;
                }
                getSernderStmt.close();

                PreparedStatement emailQueryStmt = conn.prepareStatement("SELECT EMAIL_ADDRESS FROM delivery_to_email where scheduled_account_activity_id = ?");
                emailQueryStmt.setLong(1, activityID);
                List<String> emails = new ArrayList<String>();
                ResultSet emailRS = emailQueryStmt.executeQuery();
                while (emailRS.next()) {
                    String email = emailRS.getString(1);
                    emails.add(email);
                }
                emailQueryStmt.close();


                for (UserInfo userInfo : userStubs) {
                    new SendGridEmail().sendMultipleAttachmentsEmail(userInfo.email, subject, body, htmlEmail, senderEmail, senderName, attachmentInfos);
                }
                for (String email : emails) {
                    new SendGridEmail().sendMultipleAttachmentsEmail(email, subject, body, htmlEmail, senderEmail, senderName, attachmentInfos);
                }
            } finally {
                SecurityUtil.clearThreadLocal();
            }
        }
    }

    private String handleDeliveryInfo(DeliveryInfo deliveryInfo, String body, List<AttachmentInfo> attachmentInfos, EIConnection conn, int timezoneOffset) throws Exception {
        InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
        insightRequestMetadata.setUtcOffset(timezoneOffset);
        if (deliveryInfo.getFormat() == ReportDelivery.EXCEL) {
            WSAnalysisDefinition analysisDefinition = new AnalysisStorage().getAnalysisDefinition(deliveryInfo.getId(), conn);
            analysisDefinition.updateMetadata();
            byte[] bytes = new ExportService().toExcelEmail(analysisDefinition, conn, insightRequestMetadata);
            attachmentInfos.add(new AttachmentInfo(bytes, deliveryInfo.getName() + ".xls", "application/xls"));
        } else if (deliveryInfo.getFormat() == ReportDelivery.HTML_TABLE) {
            if (deliveryInfo.getType() == DeliveryInfo.REPORT) {
                 WSAnalysisDefinition analysisDefinition = new AnalysisStorage().getAnalysisDefinition(deliveryInfo.getId(), conn);
                        analysisDefinition.updateMetadata();
                        DataResults dataResults = DataService.list(analysisDefinition, insightRequestMetadata, conn);
                        ListDataResults listDataResults = (ListDataResults) dataResults;
                        String table = ExportService.toTable(analysisDefinition, listDataResults, conn, insightRequestMetadata);
                body += table;
            } else if (deliveryInfo.getType() == DeliveryInfo.SCORECARD) {
                body += ExportService.exportScorecard(deliveryInfo.getId(), insightRequestMetadata, conn);
            }
        }
        return body;
    }

    private void scorecardDelivery(EIConnection conn) throws Exception {
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
                SecurityUtil.populateThreadLocal(userName, ownerID, accountID, accountType, accountAdmin, false, firstDayOfWeek);
                try {
                    String scorecardHTML = ExportService.exportScorecard(scorecardID, insightRequestMetadata, conn);
                    sendNoAttachEmails(conn, scorecardHTML, activityID, subject, body, htmlEmail, ScheduledActivity.SCORECARD_DELIVERY);
                } finally {
                    SecurityUtil.clearThreadLocal();
                }
            }
        }
    }

    private void reportDelivery(EIConnection conn) throws SQLException, IOException, MessagingException, DocumentException {

        PreparedStatement getInfoStmt = conn.prepareStatement("SELECT DELIVERY_FORMAT, REPORT_ID, SUBJECT, BODY, HTML_EMAIL, REPORT_DELIVERY_ID, TIMEZONE_OFFSET, SENDER_USER_ID FROM REPORT_DELIVERY WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        getInfoStmt.setLong(1, activityID);
        ResultSet deliveryInfoRS = getInfoStmt.executeQuery();
        if (deliveryInfoRS.next()) {
            final int deliveryFormat = deliveryInfoRS.getInt(1);
            long reportID = deliveryInfoRS.getLong(2);
            final String subject = deliveryInfoRS.getString(3);
            final String body = deliveryInfoRS.getString(4);
            final boolean htmlEmail = deliveryInfoRS.getBoolean(5);
            int timezoneOffset = deliveryInfoRS.getInt(7);
            final long senderID = deliveryInfoRS.getLong(8);
            PreparedStatement findOwnerStmt = conn.prepareStatement("SELECT USER_ID FROM user_to_analysis where analysis_id = ?");

            findOwnerStmt.setLong(1, reportID);
            ResultSet ownerRS = findOwnerStmt.executeQuery();
            long ownerID;
            if (ownerRS.next()) {
                ownerID = ownerRS.getLong(1);
            } else {
                return;
            }
            PreparedStatement queryStmt = conn.prepareStatement("SELECT USERNAME, USER_ID, USER.ACCOUNT_ID, ACCOUNT.ACCOUNT_TYPE, USER.account_admin," +
                    "ACCOUNT.FIRST_DAY_OF_WEEK, USER.first_name, USER.name, USER.email FROM USER, ACCOUNT " +
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
                int firstDayOfWeek = queryRS.getInt(6);
                String firstName = queryRS.getString(7);
                String lastName = queryRS.getString(8);
                String email = queryRS.getString(9);
                SecurityUtil.populateThreadLocal(userName, userID, accountID, accountType, accountAdmin, false, firstDayOfWeek);

                try {
                    if (deliveryFormat == ReportDelivery.EXCEL) {
                        WSAnalysisDefinition analysisDefinition = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
                        analysisDefinition.updateMetadata();
                        InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
                        insightRequestMetadata.setUtcOffset(timezoneOffset);
                        byte[] bytes = new ExportService().toExcelEmail(analysisDefinition, conn, insightRequestMetadata);
                        String reportName = analysisDefinition.getName();
                        sendEmails(conn, bytes, reportName + ".xls", accountID, "application/excel", activityID);
                    } else if (deliveryFormat == ReportDelivery.HTML_TABLE) {
                        WSAnalysisDefinition analysisDefinition = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
                        analysisDefinition.updateMetadata();
                        InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
                        insightRequestMetadata.setUtcOffset(timezoneOffset);
                        DataResults dataResults = DataService.list(analysisDefinition, insightRequestMetadata, conn);
                        if (dataResults instanceof ListDataResults) {
                            ListDataResults listDataResults = (ListDataResults) dataResults;
                            String table = ExportService.toTable(analysisDefinition, listDataResults, conn, insightRequestMetadata);
                            sendNoAttachEmails(conn, table, activityID, subject, body, htmlEmail, ScheduledActivity.REPORT_DELIVERY);
                        }
                    } else if (deliveryFormat == ReportDelivery.PNG) {
                        new SeleniumLauncher().requestSeleniumDrawForEmail(activityID, userID, accountID, conn);
                    } else if (deliveryFormat == ReportDelivery.PDF) {
                        WSAnalysisDefinition analysisDefinition = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
                        if (analysisDefinition.getReportType() == WSAnalysisDefinition.LIST) {
                            analysisDefinition.updateMetadata();
                            InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
                            insightRequestMetadata.setUtcOffset(timezoneOffset);
                            DataResults dataResults = DataService.list(analysisDefinition, insightRequestMetadata, conn);
                            if (dataResults instanceof ListDataResults) {
                                ListDataResults listDataResults = (ListDataResults) dataResults;
                                byte[] bytes = new ExportService().toListPDF(analysisDefinition, listDataResults, conn, insightRequestMetadata);
                                String reportName = analysisDefinition.getName();
                                sendEmails(conn, bytes, reportName + ".pdf", accountID, "application/pdf", activityID);
                            }
                        } else {
                            new SeleniumLauncher().requestSeleniumDrawForEmail(activityID, userID, accountID, conn);
                        }
                    }
                } finally {
                    SecurityUtil.clearThreadLocal();
                }
            }
            queryStmt.close();
        }
        getInfoStmt.close();
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
        getInfoStmt.close();
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
        userStmt.close();

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
        getSernderStmt.close();

        PreparedStatement emailQueryStmt = conn.prepareStatement("SELECT EMAIL_ADDRESS FROM delivery_to_email where scheduled_account_activity_id = ?");
        emailQueryStmt.setLong(1, activityID);
        List<String> emails = new ArrayList<String>();
        ResultSet emailRS = emailQueryStmt.executeQuery();
        while (emailRS.next()) {
            String email = emailRS.getString(1);
            emails.add(email);
        }
        emailQueryStmt.close();

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

    public static void sendNoAttachEmails(EIConnection conn, String extraBody, long activityID,
                                          String subjectLine, String body, boolean htmlEmail, int type) throws SQLException, MessagingException, UnsupportedEncodingException {

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
        userStmt.close();

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

        for (UserInfo userInfo : userStubs) {
            new SendGridEmail().sendNoAttachmentEmail(userInfo.email, subjectLine, body + extraBody, htmlEmail, senderEmail, senderName);
        }
        for (String email : emails) {
            new SendGridEmail().sendNoAttachmentEmail(email, subjectLine, body + extraBody, htmlEmail, senderEmail, senderName);
        }
    }
}
