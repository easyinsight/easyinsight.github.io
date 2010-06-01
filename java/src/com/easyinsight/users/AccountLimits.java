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
            account.setMaxSize(Account.ENTERPRISE_MAX);
        } else if (account.getAccountType() == Account.PREMIUM) {
            account.setMaxUsers(50);
            account.setMaxSize(Account.PREMIUM_MAX);
        } else if (account.getAccountType() == Account.BASIC) {
            account.setMaxUsers(5);
            account.setMaxSize(Account.BASIC_MAX);
        } else if (account.getAccountType() == Account.PERSONAL) {
            account.setMaxUsers(1);
            account.setMaxSize(Account.PERSONAL_MAX);
        } else if (account.getAccountType() == Account.PROFESSIONAL) {
            account.setMaxUsers(500);
            account.setMaxSize(Account.PROFESSIONAL_MAX);
        }  else if (account.getAccountType() == Account.PLUS) {
            account.setMaxUsers(500);
            account.setMaxSize(Account.PLUS_MAX);
        }
    }
}
