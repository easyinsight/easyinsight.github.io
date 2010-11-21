package com.easyinsight.email;

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

    private static String groupInviteText =
            "You have been added as a member of an account administered by {0} {1} on Easy Insight. " +
            "Your login information is the following:\r\n\r\n"+
            "User Name: {2}\r\n" +
            "Password: {3}\r\n\r\n" +
            "You can access Easy Insight at http://www.easy-insight.com/app/#page=home\r\n" +
            "Once there and logged in, you can change your password through Account - Change my Password.";

    private static String newProAccountText =
            "A new professional account for your organization has been created on Easy Insight.\r\nYou can access the application at\r\n\r\n" +
            "http://www.easy-insight.com/app/#page=welcome\r\n\r\n"+
            "Your administrator user credentials are:\r\n\r\n"+
            "User Name: {0}\r\n"+
            "Password:  {1}\r\n\r\n"+
            "Once logged in, you can change your password through Account - Change my Password.\r\n" +
            "Tutorials are available through the Help button on each application page to help you get started.\r\n\r\nWelcome to Easy Insight!";

    /*private static String resetPasswordText =
            "Your password with Easy Insight has been reset:\r\n\r\n" +
            "Password:  {0}\r\n\r\n"+
            "This email was sent from an automated account. Please do not reply to this address.";*/
    private static String resetPasswordText =
            "You are receiving this email because you have requested to reset your password.:\r\n\r\n" +
            "To reset your password, use the following link:  https://www.easy-insight.com/app/#resetPassword={0}\r\n\r\n"+
            "Or enter the value of {0} into Manually Enter Reset Key in the Forgot Password section of the login page on Easy Insight at https://www.easy-insight.com/app.\r\n\rn"+
            "This email was sent from an automated account. Please do not reply to this address.";

    private static String remindUserNameText =
            "Your user name with Easy Insight is below:\r\n\r\n" +
            "User Name:  {0}\r\n\r\n"+
            "This email was sent from an automated account. Please do not reply to this address.";

    private static String newConsultantProAccountText =
            "A new professional account for your organization has been created on Easy Insight.\r\nYou can access the application at\r\n\r\n" +
            "http://www.easy-insight.com/app/#page=welcome\r\n\r\n"+
            "Your administrator user credentials are:\r\n\r\n"+
            "User Name: {0}\r\n"+
            "Password:  {1}\r\n\r\n"+
            "Once logged in, you can change your password through Account - Change my Password.\r\n" +
            "Tutorials are available through the Help button on each application page to help you get started.\r\n\r\n" +
            "Your account has been set up with one initial Easy Insight consultant user:\r\n\r\n"+
            "Consultant: {3} {4}\r\n\r\n" +
            "This consultant will be responsible for assisting you with data source, report, and goal creation,\r\n"+
            "as well as any further questions or support issues you may have.\r\n\r\nWelcome to Easy Insight!";

    private static String welcomeEmailText =
             "Hi {0},\r\n\r\n"+
             "Welcome to Easy Insight and thanks for signing up!\r\n\r\n"+
            "Please click the link below to activate your account:\r\n\r\n"+
            "{1}\r\n\r\n"+
            "Once you've activated your account, you can always log back in to your account at:\r\n\r\n"+
            "https://www.easy-insight.com/app\r\n\r\n"+
            "Thank you for choosing Easy Insight. Introductory screencasts that may help you out are available at http://www.youtube.com/user/easyinsight. In particular, you may find the following screencasts helpful as starting points:\r\n\r\n"+
            "Easy Insight and Basecamp - http://www.youtube.com/watch?v=XISV8DLN2XA\r\n"+
            "Easy Insight and Highrise - http://www.youtube.com/watch?v=XISV8DLN2XA\r\n"+
            "Easy Insight and Flat Files - http://www.youtube.com/watch?v=XISV8DLN2XA\r\n\r\n"+
            "Documentation is available at http://www.easy-insight.com/documentation/toc.html\r\n\r\n"+
            "If you have any questions around use of the service, please don't hesitant to contact us at support@easy-insight.com.\r\n\r\nThanks and welcome again!\r\n\r\nThe Team at Easy Insight";

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

    public void newProAccount(String to, String userName, String password) {
        String body = MessageFormat.format(newProAccountText, userName, password);
        String subject = "Easy Insight Account Creation";
        try {
            new AuthSMTPConnection().sendSSLMessage(to, subject, body, "donotreply@easy-insight.com");
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void resetPassword(String to, String password) {
        String body = MessageFormat.format(resetPasswordText, password);
        String subject = "Easy Insight Password Reset";
        try {
            new AuthSMTPConnection().sendSSLMessage(to, subject, body, "donotreply@easy-insight.com");
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void remindUserName(String to, String userName) {
        String body = MessageFormat.format(remindUserNameText, userName);
        String subject = "Easy Insight User Name Reminder";
        try {
            new AuthSMTPConnection().sendSSLMessage(to, subject, body, "donotreply@easy-insight.com");
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void newProAccountWithConsultant(String to, String userName, String password, String consultant, String consultantEMail) {
        String body = MessageFormat.format(newProAccountText, userName, password, consultant, consultantEMail);
        String subject = "Easy Insight Account Creation";
        try {
            new AuthSMTPConnection().sendSSLMessage(to, subject, body, "donotreply@easy-insight.com");
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void sendAccountEmail(String to, String adminFirstName, String accountOwner, String userName, String password) {
        String body = MessageFormat.format(groupInviteText, adminFirstName != null ? adminFirstName : "", accountOwner, userName, password);
        String subject = "Easy Insight Account Creation";
        try {
            new AuthSMTPConnection().sendSSLMessage(to, subject, body, adminFirstName + " " + accountOwner);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void sendActivationEmail(String to, String firstName, String activation) {
        String url = "https://www.easy-insight.com/app/accountactivation?activationID=" + activation;
        String body = welcomeEmailText.replace("{0}", firstName).replace("{1}", url);
        String subject = "Welcome to Easy Insight!";
        try {
            new AuthSMTPConnection().sendSSLMessage(to, subject, body, "Easy Insight");
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void salesNotification(String userName, String email, String company, String additionalInfo) throws MessagingException, UnsupportedEncodingException {
        String body = MessageFormat.format(salesText, userName, email, company, additionalInfo);
        String subject = "Sales Info Request";
        new AuthSMTPConnection().sendSSLMessage("sales@easy-insight.com", subject, body, "donotreply@easy-insight.com");
    }
}
