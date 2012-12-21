package com.easyinsight.users;

/**
 * User: jamesboe
 * Date: Apr 29, 2010
 * Time: 11:05:14 AM
 */
public class AccountLimits {
    public static void configureAccount(Account account) {
        account.setCoreDesigners(1);
        account.setCoreSmallBizConnections(2);
        account.setCoreStorage(50000000);
    }
}
