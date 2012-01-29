package com.easyinsight.email;

import com.easyinsight.admin.ConstantContactSync;
import com.easyinsight.logging.LogClass;

import javax.mail.MessagingException;
import java.text.MessageFormat;
import java.io.UnsupportedEncodingException;

/**
 * User: James Boe
 * Date: Jan 24, 2009
 * Time: 9:19:05 PM
 */
public class AccountMemberInvitation {

    public static final String newUserInviteText = "<p>{0} has invited you to the {1} Easy Insight account.</p><p>All you need to do get started is sign in at:</p><a href=\"{2}\">{2}</a><hr/><p>You can log with the following credentials:</p><p>User Name: {3}</p><p>Password: {4}</p>{5}<hr/><p>Have questions? Contact {0} at {6}</p>";
    public static final String subjectText = "{0} has invited you to Easy Insight";

    private static final String resetPasswordText =
            "You are receiving this email because you have requested to reset your password.:\r\n\r\n" +
            "To reset your password, use the following link:  https://www.easy-insight.com/app/#resetPassword={0}\r\n\r\n"+
            "Or enter the value of {0} into Manually Enter Reset Key in the Forgot Password section of the login page on Easy Insight at https://www.easy-insight.com/app.\r\n\rn"+
            "This email was sent from an automated account. Please do not reply to this address.";

    private static final String remindUserNameText =
            "Your user name with Easy Insight is below:\r\n\r\n" +
            "User Name:  {0}\r\n\r\n"+
            "This email was sent from an automated account. Please do not reply to this address.";

    private static String welcomeEmailText =
             "Hi {0},\r\n\r\n"+
             "Welcome to Easy Insight and thanks for signing up!\r\n\r\n"+
            "Please click the link below to activate your account:\r\n\r\n"+
            "{1}\r\n\r\n"+
            "Once you've activated your account, you can always log back in to your account at:\r\n\r\n"+
            "https://www.easy-insight.com/app/";
            /*"Thank you for choosing Easy Insight. Introductory screencasts that may help you out are available at http://www.youtube.com/user/easyinsight. In particular, you may find the following screencasts helpful as starting points:\r\n\r\n"+
            "Easy Insight and Basecamp - http://www.youtube.com/watch?v=XISV8DLN2XA\r\n"+
            "Easy Insight and Highrise - http://www.youtube.com/watch?v=XISV8DLN2XA\r\n"+
            "Easy Insight and Flat Files - http://www.youtube.com/watch?v=XISV8DLN2XA\r\n\r\n"+
            "Documentation is available at http://www.easy-insight.com/documentation/toc.html\r\n\r\n"+
            "If you have any questions around use of the service, please don't hesitant to contact us at support@easy-insight.com.\r\n\r\nThanks and welcome again!\r\n\r\nThe Team at Easy Insight";*/

    private static String salesText =
            "The following user requested sales info:\r\n\r\n" +
                    "Name: {0}\r\n"+
                    "Email: {1}\r\n"+
                    "Company Name: {2}\r\n"+
                    "Additional Info: {3}\r\n";

    private static String individualAccountCreationText =
            "Welcome to Easy Insight!\r\n\r\n" +
            "You have created an individual account.";

    private static String professionalAccountCreationText =
            "";


    public void resetPassword(String to, String password) {
        String body = MessageFormat.format(resetPasswordText, password);
        String subject = "Easy Insight Password Reset";
        try {
            new SendGridEmail().sendEmail(to, subject, body, "donotreply@easy-insight.com", false, "Easy Insight");
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void remindUserName(String to, String userName) {
        String body = MessageFormat.format(remindUserNameText, userName);
        String subject = "Easy Insight User Name Reminder";
        try {
            new SendGridEmail().sendEmail(to, subject, body, "donotreply@easy-insight.com", false, "Easy Insight");
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void sendAccountEmail(String to, String adminFirstName, String accountOwner, String userName, String password, String companyName, String loginURL, String adminEmail, String sso) {
        String body = MessageFormat.format(newUserInviteText, (adminFirstName != null ? adminFirstName : "") + " " + accountOwner, companyName, loginURL, userName, password, sso, adminEmail);
        String subject = MessageFormat.format(subjectText, (adminFirstName != null ? adminFirstName : "") + " " + accountOwner);
        try {
            new SendGridEmail().sendEmail(to, subject, body, "support@easy-insight.com", true, "Easy Insight on behalf of " +
                    (adminFirstName == null ? accountOwner : adminFirstName + " " + accountOwner));
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void sendActivationEmail(String to, String firstName, String activation) {
        String url = "https://www.easy-insight.com/app/accountactivation?activationID=" + activation;
        String body = welcomeEmailText.replace("{0}", firstName).replace("{1}", url);
        String subject = "Activate your Easy Insight account";
        try {
            new SendGridEmail().sendEmail(to, subject, body, "sales@easy-insight.com", false, "Easy Insight");
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void sendWelcomeEmail(String to) {
        String body = ConstantContactSync.getContent(ConstantContactSync.CREATION_DAY);
        String subject = "Welcome to Easy Insight!";
        try {
            new SendGridEmail().sendEmail(to, subject, body, "sales@easy-insight.com", true, "Easy Insight");
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void salesNotification(String userName, String email, String company, String additionalInfo) throws MessagingException, UnsupportedEncodingException {
        String body = MessageFormat.format(salesText, userName, email, company, additionalInfo);
        String subject = "Sales Info Request";
        new SendGridEmail().sendEmail("sales@easy-insight.com", subject, body, "donotreply@easy-insight.com", false, "Easy Insight");
    }
}
