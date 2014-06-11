package com.easyinsight.salesautomation;

import com.easyinsight.admin.ConstantContactSync;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.email.SendGridEmail;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
import com.easyinsight.users.User;
import com.easyinsight.util.RandomTextGenerator;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONException;
import org.json.JSONObject;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: Jan 8, 2010
 * Time: 4:45:03 PM
 */
public class SalesEmail implements Runnable {

    public static final int ONE_DAY = 1;
    public static final int ONE_WEEK = 2;
    public static final int TWO_WEEKS = 3;
    public static final int THREE_WEEKS = 4;
    public static final int END_OF_TRIAL = 5;
    public static final int ONE_DAY_BACK = 6;
    public static final int ONE_WEEK_BACK = 7;
    public static final int TWO_WEEKS_BACK = 8;

    private static String newAccountNotification =
            "New account created:\r\n\r\n" +
            "Account Type: {0}\r\n" +
            "User Name: {1}\r\n" +
            "First Name: {2}\r\n" +
            "Last Name: {3}\r\n" +
            "Email Address: {4}";

    private Account account;
    private User user;

    public SalesEmail(Account account, User user) {
        this.account = account;
        this.user = user;
    }

    public static void forceRun() {
        EIConnection conn = Database.instance().getConnection();
        try {
            leadNurture(SecurityUtil.getUserID(), ONE_DAY, conn);
            leadNurture(SecurityUtil.getUserID(), ONE_WEEK, conn);
            leadNurture(SecurityUtil.getUserID(), TWO_WEEKS, conn);
            leadNurture(SecurityUtil.getUserID(), THREE_WEEKS, conn);
            leadNurture(SecurityUtil.getUserID(), END_OF_TRIAL, conn);
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void run() {
        String accountType;
        if (account.getAccountType() == Account.PERSONAL) {
            accountType = "Personal";
        } else if (account.getAccountType() == Account.BASIC) {
            accountType = "Basic";
        }  else if (account.getAccountType() == Account.PLUS) {
            accountType = "Plus";
        } else if (account.getAccountType() == Account.PROFESSIONAL) {
            accountType = "Professional";
        } else if (account.getAccountType() == Account.PREMIUM) {
            accountType = "Premium";
        } else {
            accountType = "Enterprise";
        }
        String body = MessageFormat.format(newAccountNotification, accountType, user.getUserName(), user.getFirstName(), user.getName(), user.getEmail());
        String subject = "New " + accountType + " Account Created";
        try {
            new SendGridEmail().sendEmail("sales@easy-insight.com", subject, body, "donotreply@easy-insight.com", false, "Easy Insight");
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public static void leadNurture(long userID, int number, EIConnection conn) throws SQLException, MessagingException, IOException, JSONException, ParseException {

        // does the user have any KPIs defined?

        PreparedStatement userStmt = conn.prepareStatement("SELECT USER.email, USER.first_name, USER.name, ACCOUNT.account_type, ACCOUNT.account_state FROM USER, ACCOUNT WHERE USER.USER_ID = ? AND " +
                "USER.account_id = ACCOUNT.account_id and user.opt_in_email = ?");
        userStmt.setLong(1, userID);
        userStmt.setBoolean(2, true);
        ResultSet rs = userStmt.executeQuery();
        if (!rs.next()) {
            return;
        }

        String email = rs.getString(1);
        String firstName = rs.getString(2);
        String lastName = rs.getString(3);
        int accountState = rs.getInt(5);

        if (accountState != Account.TRIAL) return;

        String templateID = null;
        if (number == ONE_DAY) {
            return;
        } else if (number == ONE_WEEK) {
            templateID = "KDHcbfhYgAzzknYb8qoc4T";
        } else if (number == TWO_WEEKS) {
            templateID = "tem_mKA67sxJGVmjwqpGPDRcAi";
        } else if (number == THREE_WEEKS) {
            templateID = "tem_RF7aHmobQnESaT7jeiAuzj";
        } else if (number == END_OF_TRIAL) {
            templateID = "tem_TEaKcEQXdzqzFkxBCJSPh4";
        }

        if (templateID != null) {
            PreparedStatement queryUnsubscribeStmt = conn.prepareStatement("SELECT unsubscribe_key from user_unsubscribe_key WHERE USER_ID = ?");
            PreparedStatement insertKeyStmt = conn.prepareStatement("INSERT INTO USER_UNSUBSCRIBE_KEY (USER_ID, UNSUBSCRIBE_KEY) VALUES (?, ?)");
            queryUnsubscribeStmt.setLong(1, userID);
            ResultSet unsubscribeRS = queryUnsubscribeStmt.executeQuery();
            String unsubscribeKey;
            if (unsubscribeRS.next()) {
                unsubscribeKey = unsubscribeRS.getString(1);
            } else {
                unsubscribeKey = RandomTextGenerator.generateText(25);
                insertKeyStmt.setLong(1, userID);
                insertKeyStmt.setString(2, unsubscribeKey);
                insertKeyStmt.execute();
            }
            queryUnsubscribeStmt.close();
            insertKeyStmt.close();

            HttpClient sendWithUsClient = new HttpClient();
            sendWithUsClient.getParams().setAuthenticationPreemptive(true);
            Credentials defaultcreds = new UsernamePasswordCredentials("live_2d56944c0596aea84504bd0947e0421ab3e1c56c", "");
            sendWithUsClient.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);

            JSONObject json = new JSONObject();
            json.put("email_id", templateID);
            JSONObject recipient = new JSONObject();
            recipient.put("address", email);
            json.put("recipient", recipient);
            JSONObject sender = new JSONObject();
            sender.put("address", "sales@easy-insight.com");
            sender.put("reply_to", "sales@easy-insight.com");
            sender.put("name", "Easy Insight Marketing");
            json.put("sender", sender);

            JSONObject emailData = new JSONObject();
            emailData.put("user_name", firstName);
            emailData.put("unsubscribeLink", "https://www.easy-insight.com/app/unsubscribe?user=" + unsubscribeKey);
            json.put("email_data", emailData);

            PostMethod method = new PostMethod("https://api.sendwithus.com/api/v1/send");
            StringRequestEntity entity = new StringRequestEntity(json.toString(), "application/json", "UTF-8");
            method.setRequestEntity(entity);
            sendWithUsClient.executeMethod(method);
            Object obj = new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(method.getResponseBodyAsStream());
            System.out.println(obj);
        }
    }
}
