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

    private static String freeAccountConfirmationText =
            "You have created a new free account with Easy Insight.\r\n"+
            "Please click the link below to activate your account:\r\n\r\n"+
            "{0}";

    private static String individualAccountCreationText =
            "Welcome to Easy Insight!\r\n\r\n" +
            "You have created an individual account.";

    private static String professionalAccountCreationText =
            "";

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
}
