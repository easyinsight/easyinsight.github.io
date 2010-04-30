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
            "You can access Easy Insight at http://www.easy-insight.com/app/#page=welcome\r\n" +
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
            "This email was sent from an automated account. Please do not reply to this address.";

    private static String remindUserNameText =
            "Your user name with Easy Insight is below:\r\n\r\n" +
            "User Name:  {0}\r\n\r\n"+
            "This email was sent from an automated account. Please do not reply to this address.";;

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

    private static String freeAccountConfirmationText =
            "You have created a new account with Easy Insight.\r\n"+
            "Please click the link below to activate your account:\r\n\r\n"+
            "{0}\r\n\r\n"+
            "This email was sent from an automated account. Please do not reply to this address.";

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
            new AuthSMTPConnection().sendSSLMessage(to, subject, body, accountOwner);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void sendActivationEmail(String to, String activation) {
        String url = "https://www.easy-insight.com/app/#activationID=" + activation;
        String body = MessageFormat.format(freeAccountConfirmationText, url);
        String subject = "Easy Insight Account Activation";
        try {
            new AuthSMTPConnection().sendSSLMessage(to, subject, body, "donotreply@easy-insight.com");
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
