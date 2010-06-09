package com.easyinsight.export;

import com.easyinsight.analysis.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.email.SendGridEmail;
import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

        PreparedStatement getInfoStmt = conn.prepareStatement("SELECT DELIVERY_FORMAT, REPORT_ID, SUBJECT, BODY, HTML_EMAIL FROM REPORT_DELIVERY WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        getInfoStmt.setLong(1, activityID);
        ResultSet deliveryInfoRS = getInfoStmt.executeQuery();
        if (deliveryInfoRS.next()) {
            int deliveryFormat = deliveryInfoRS.getInt(1);
            long reportID = deliveryInfoRS.getLong(2);
            String subjectLine = deliveryInfoRS.getString(3);
            String body = deliveryInfoRS.getString(4);
            boolean htmlEmail = deliveryInfoRS.getBoolean(5);
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
                SecurityUtil.populateThreadLocal(userName, userID, accountID, accountType, accountAdmin);

                WSAnalysisDefinition analysisDefinition = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
                analysisDefinition.updateMetadata();
                ListDataResults listDataResults = (ListDataResults) new DataService().list(analysisDefinition, new InsightRequestMetadata());
                byte[] bytes = new ExportService().toExcel(analysisDefinition, listDataResults);
                String reportName = analysisDefinition.getName();
                for (UserInfo userInfo : userStubs) {
                    new SendGridEmail().sendAttachmentEmail(userInfo.email, subjectLine, body, bytes, reportName, htmlEmail);
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
}
