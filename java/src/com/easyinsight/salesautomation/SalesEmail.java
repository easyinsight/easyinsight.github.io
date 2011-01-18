package com.easyinsight.salesautomation;

import com.easyinsight.database.EIConnection;
import com.easyinsight.email.SendGridEmail;
import com.easyinsight.logging.LogClass;
import com.easyinsight.users.Account;
import com.easyinsight.users.User;

import javax.mail.MessagingException;
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

    public static void leadNurture(long userID, int number, EIConnection conn) throws SQLException, MessagingException, UnsupportedEncodingException {

        // does the user have any KPIs defined?

        PreparedStatement userStmt = conn.prepareStatement("SELECT USER.email, USER.first_name, ACCOUNT.account_type FROM USER, ACCOUNT WHERE USER.USER_ID = ? AND " +
                "USER.account_id = ACCOUNT.account_id");
        userStmt.setLong(1, userID);
        ResultSet rs = userStmt.executeQuery();
        rs.next();

        String email = rs.getString(1);
        String firstName = rs.getString(2);

        List<EmailBlock> emailBlocks = new ArrayList<EmailBlock>();

        emailBlocks.add(new ParagraphEmailBlock("Hello " + firstName + ","));

        String subject = null;

        if (number == ONE_DAY) {
            // nothing yet
        } else if (number == ONE_WEEK) {
            /*subject = "Tips for the best Easy Insight experience";
            emailBlocks.add(new ParagraphEmailBlock("You're one week into your trial with Easy Insight, so you might find the following screencast interesting:"));
            emailBlocks.add(new ParagraphEmailBlock("<a></a>"));
            emailBlocks.add(new ParagraphEmailBlock("This screencast provides you with guidance around how you can build compelling reports inside of Easy Insight. Take advantage of our wide range of report types to visualize your data in forms from simple bar and column charts up to heat maps and trees. Learn how to use different types of filters to quickly and easily slice data sets into the insights you need for your business."));
            emailBlocks.add(new ParagraphEmailBlock("Have any questions or comments? Please don't hesitate to contact us at sales@easy-insight.com or support@easy-insight.com!"));*/
        } else if (number == TWO_WEEKS) {
            /*subject = "Tips for the best Easy Insight experience";
            emailBlocks.add(new ParagraphEmailBlock("You're halfway through your trial with Easy Insight, so here's a few suggestions on how to take advantage of the wide variety of functionality we offer:"));
            emailBlocks.add(new ParagraphEmailBlock("You can greatly expand your capabilities for reporting on any particular SaaS system with custom fields. Your CRM system doesn't have an Expected Close Date or Target Revenue? Take advantage of Easy Insight to augment that CRM system. For a more detailed look at this functionality, you can check out the documentation at {0} or watch this screencast at YYY."));
            emailBlocks.add(new ParagraphEmailBlock("Want to calculate more complex metrics? Apply mathematical functions to your data? For explanation of what a calculation is, how you define one, and how you use them to create reports, you can check out the documentation at {0} or watch this screencast at YYY."));
            emailBlocks.add(new ParagraphEmailBlock("Once you have a handful of reports created on one of your data sources, you can start building out full dashboards to display many reports in a single screen. Detailed explanation and examples are available in our documentation at {0} or with the screencast at YYY."));
            emailBlocks.add(new ParagraphEmailBlock("Have any questions or comments? Please don't hesitate to contact us at sales@easy-insight.com or support@easy-insight.com!"));*/
        } else if (number == THREE_WEEKS) {
            // nothing yet
        } else if (number == END_OF_TRIAL) {
            /*subject = "Your Easy Insight trial is almost over";
            emailBlocks.add(new ParagraphEmailBlock("Your trial with Easy Insight is coming to an end on {0}. Pricing information is available at {1}. We'd love to have your business, so if you have any questions or concerns, please contact us at sales@easy-insight.com."));*/
        }

        if (emailBlocks.size() > 1) {
            StringBuilder sb = new StringBuilder();
            for (EmailBlock emailBlock : emailBlocks) {
                sb.append(emailBlock.toText());
            }
            new SendGridEmail().sendEmail(email, subject, sb.toString(), "sales@easy-insight.com", false, "Easy Insight Marketing");
        }
    }
}
