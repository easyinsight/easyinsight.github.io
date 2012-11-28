package com.easyinsight.billing;

import com.easyinsight.users.AccountCreditCardBillingInfo;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 11/19/12
 * Time: 8:58 AM
 * To change this template use File | Settings | File Templates.
 */
public interface BillingSystem {
    void cancelPlan(long accountID);

    AccountCreditCardBillingInfo billAccount(long accountID, double amount);

    AccountCreditCardBillingInfo billAccount(long accountID, double amount, boolean auth);
}
