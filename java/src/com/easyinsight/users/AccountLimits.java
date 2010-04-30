package com.easyinsight.users;

/**
 * User: jamesboe
 * Date: Apr 29, 2010
 * Time: 11:05:14 AM
 */
public class AccountLimits {
    public static void configureAccount(Account account) {
        if (account.getAccountType() == Account.ENTERPRISE) {
            account.setMaxUsers(500);
            account.setMaxSize(1000000000);
        } else if (account.getAccountType() == Account.PREMIUM) {
            account.setMaxUsers(50);
            account.setMaxSize(500000000);
        } else if (account.getAccountType() == Account.BASIC) {
            account.setMaxUsers(5);
            account.setMaxSize(100000000);
        } else if (account.getAccountType() == Account.PERSONAL) {
            account.setMaxUsers(1);
            account.setMaxSize(1000000);
        } else if (account.getAccountType() == Account.PROFESSIONAL) {
            account.setMaxUsers(50);
            account.setMaxSize(500000000);
        }
    }
}
