package com.easyinsight.salesautomation;

import com.easyinsight.email.AuthSMTPConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.users.Account;
import com.easyinsight.users.User;

import java.text.MessageFormat;

/**
 * User: jamesboe
 * Date: Jan 8, 2010
 * Time: 4:45:03 PM
 */
public class SalesEmail implements Runnable {

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
            new AuthSMTPConnection().sendSSLMessage("sales@easy-insight.com", subject, body, "donotreply@easy-insight.com");
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
