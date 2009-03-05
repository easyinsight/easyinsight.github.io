package com.easyinsight.email;

import com.easyinsight.logging.LogClass;

import java.text.MessageFormat;

/**
 * User: James Boe
 * Date: Jan 24, 2009
 * Time: 9:19:05 PM
 */
public class AccountMemberInvitation {

    private static String groupInviteText =
            "You have been added as a member of an account administered by {0} on Easy Insight. " +
            "Your login information is the following:\r\n\r\n"+
            "User Name: {1}\r\n" +
            "Password: {2}\r\n\r\n" +
            "You can access Easy Insight at http://www.easy-insight.com/app\r\n" +
            "Once there and logged in, you can change your password through Account - Change my Password.";

    private static String newProAccountText =
            "A new professional account for your organization has been created on Easy Insight.\r\nYou can access the application at\r\n\r\n" +
            "http://www.easy-insight.com/app\r\n\r\n"+
            "Your administrator user credentials are:\r\n\r\n"+
            "User Name: {0}\r\n"+
            "Password:  {1}\r\n\r\n"+
            "Once logged in, you can change your password through Account - Change my Password.\r\n" +
            "Tutorials are available through the Help button on each application page to help you get started.\r\n\r\nWelcome to Easy Insight!";

    private static String newConsultantProAccountText =
            "A new professional account for your organization has been created on Easy Insight.\r\nYou can access the application at\r\n\r\n" +
            "http://www.easy-insight.com/app\r\n\r\n"+
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
            "You have created a new free account with Easy Insight.\r\n"+
            "Please click the link below to activate your account:\r\n\r\n"+
            "{0}\r\n\r\n"+
            "This email was sent from an automated account. Please do not reply to this address.";

    private static String individualAccountCreationText =
            "Welcome to Easy Insight!\r\n\r\n" +
            "You have created an individual account.";

    private static String professionalAccountCreationText =
            "";

    public void newProAccount(String to, String userName, String password) {
        String body = MessageFormat.format(newProAccountText, userName, password);
        String subject = "Easy Insight Account Creation";
        try {
            new GMailConnection().sendSSLMessage(to, subject, body, "accounts@easy-insight.com");
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void newProAccountWithConsultant(String to, String userName, String password, String consultant, String consultantEMail) {
        String body = MessageFormat.format(newProAccountText, userName, password, consultant, consultantEMail);
        String subject = "Easy Insight Account Creation";
        try {
            new GMailConnection().sendSSLMessage(to, subject, body, "accounts@easy-insight.com");
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void sendAccountEmail(String to, String accountOwner, String userName, String password) {
        String body = MessageFormat.format(groupInviteText, accountOwner, userName, password);
        String subject = "Easy Insight Account Creation";
        try {
            new GMailConnection().sendSSLMessage(to, subject, body, accountOwner);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void sendActivationEmail(String to, String activation) {
        String url = "http://www.easy-insight.com/accounts?activation=" + activation;
        String body = MessageFormat.format(freeAccountConfirmationText, url);
        String subject = "Easy Insight Account Activation";
        try {
            new GMailConnection().sendSSLMessage(to, subject, body, "donotreply@easy-insight.com");
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
