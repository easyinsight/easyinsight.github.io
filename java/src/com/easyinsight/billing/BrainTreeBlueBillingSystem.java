package com.easyinsight.billing;

import com.braintreegateway.*;
import com.easyinsight.config.ConfigLoader;
import com.easyinsight.logging.LogClass;
import com.easyinsight.users.Account;
import com.easyinsight.users.AccountActivityStorage;
import com.easyinsight.users.AccountCreditCardBillingInfo;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


/**
 * User: abaldwin
 * Date: Jun 26, 2009
 * Time: 10:13:10 AM
 */
public class BrainTreeBlueBillingSystem implements BillingSystem {

    private static BraintreeGateway gateway = new BraintreeGateway(ConfigLoader.instance().isProduction() ? Environment.PRODUCTION : Environment.SANDBOX, ConfigLoader.instance().getMerchantID(), ConfigLoader.instance().getBillingPublicKey(), ConfigLoader.instance().getBillingPrivateKey());

    public void cancelPlan(long accountID) {
        gateway.customer().delete(String.valueOf(accountID));
    }

    public AccountCreditCardBillingInfo billAccount(long accountID, double amount) {
        return billAccount(accountID, amount, false);
    }

    public AccountCreditCardBillingInfo billAccount(long accountID, double amount, boolean auth) {
        TransactionRequest request = new TransactionRequest()
                .customerId(String.valueOf(accountID))
                .amount(new BigDecimal(String.valueOf(amount)))
                .creditCard().done().options().submitForSettlement(!auth).done();

        Result<Transaction> result = gateway.transaction().sale(request);

        return createInfo(accountID, amount, result);
    }

    private AccountCreditCardBillingInfo createInfo(long accountID, double amount, Result<Transaction> result) {
        AccountCreditCardBillingInfo info = new AccountCreditCardBillingInfo();
        info.setAccountId(accountID);
        if (result.getTarget() != null) {
            info.setResponse(result.getTarget().getProcessorAuthorizationCode());
            info.setResponseCode(result.getTarget().getProcessorResponseCode());
            info.setResponseString(result.getTarget().getProcessorResponseText());
            info.setTransactionID(result.getTarget().getId());
            info.setSuccessful(result.getTarget().getProcessorResponseCode().equals("1000"));
            info.setTransactionTime(result.getTarget().getCreatedAt().getTime());
        } else if (result.getErrors().size() > 0) {
            info.setSuccessful(false);
            info.setResponseString(result.getMessage());
            info.setTransactionTime(new Date());
        }

        info.setAmount(String.valueOf(amount));

        return info;
    }

    public String getRedirect(Request req, int pricingModel) {
        switch (pricingModel) {
            case Account.TIERED:
                return gateway.transparentRedirect().trData(req, ConfigLoader.instance().getRedirectLocation() + "/app/billing/newSubmit.jsp");
            case Account.NEW:
                return gateway.transparentRedirect().trData(req, ConfigLoader.instance().getRedirectLocation() + "/app/billing/newModelSubmit.jsp");
            default:
                throw new RuntimeException("There was a problem with the pricing model, please contact Easy Insight.");
        }
    }

    public String getTargetUrl() {
        return gateway.transparentRedirect().url();
    }

    public Result<Customer> confirmCustomer(String queryString) {
        return gateway.transparentRedirect().confirmCustomer(queryString);
    }

    public Customer getCustomer(Account account) {
        CustomerSearchRequest r = new CustomerSearchRequest().id().is(String.valueOf(account.getAccountID()));
        ResourceCollection<Customer> c = gateway.customer().search(r);
        int i = 0;
        for (Customer cc : c) {
            i++;
        }
        if (i == 0)
            return null;
        else
            return c.getFirst();
    }

    public void deleteCard(CreditCard cc) {

        gateway.creditCard().delete(cc.getToken());
    }

    public void deleteAddress(Address aa) {
        gateway.address().delete(aa.getCustomerId(), aa.getId());
    }

    public Result<Subscription> subscribeMonthly(Account account, int numDesigners, int numStorage, int numConnections) {
        Customer c = getCustomer(account);
        SubscriptionRequest sr = new SubscriptionRequest();
        CreditCard dc = null;
        for (CreditCard cc : c.getCreditCards()) {
            if (cc.isDefault())
                dc = cc;
        }


        sr.planId("1").paymentMethodToken(dc.getToken()).options().prorateCharges(true);
        if (numDesigners > 0)
            sr.addOns().add().inheritedFromId("11").quantity(numDesigners).done().done();
        if (numStorage > 0)
            sr.addOns().add().inheritedFromId("12").quantity(numStorage).done().done();
        if (numConnections > 0)
            sr.addOns().add().inheritedFromId("13").quantity(numConnections).done().done();

        Result<Subscription> r = gateway.subscription().create(sr);
        return r;
    }


    public Result<Subscription> subscribeYearly(Account account, int numDesigners, int numStorage, int numConnections) {
        Customer c = getCustomer(account);
        SubscriptionRequest sr = new SubscriptionRequest();
        CreditCard dc = null;
        for (CreditCard cc : c.getCreditCards()) {
            if (cc.isDefault())
                dc = cc;
        }
        sr.planId("2").paymentMethodToken(dc.getToken());

        if (numDesigners > 0)
            sr.addOns().add().inheritedFromId("21").quantity(numDesigners).done().done();
        if (numStorage > 0)
            sr.addOns().add().inheritedFromId("22").quantity(numStorage).done().done();
        if (numConnections > 0)
            sr.addOns().add().inheritedFromId("23").quantity(numConnections).done().done();

        Result<Subscription> r = gateway.subscription().create(sr);
        return r;
    }

    public Result<Subscription> updateMonthly(Subscription s, int numDesigners, int numStorage, int numConnections) {
        SubscriptionRequest sr = new SubscriptionRequest().id(s.getId());
        sr.options().prorateCharges(true);
        sr.options().replaceAllAddOnsAndDiscounts(true).done();
        if (numDesigners > 0)
            sr.addOns().add().inheritedFromId("11").quantity(numDesigners).done().done();
        if (numStorage > 0)
            sr.addOns().add().inheritedFromId("12").quantity(numStorage).done().done();
        if (numConnections > 0)
            sr.addOns().add().inheritedFromId("13").quantity(numConnections).done().done();
        return gateway.subscription().update(s.getId(), sr);
    }

    public Result<Subscription> updateYearly(Subscription s, int numDesigners, int numStorage, int numConnections) {
        SubscriptionRequest sr = new SubscriptionRequest().id(s.getId());
        sr.options().prorateCharges(true);
        sr.options().replaceAllAddOnsAndDiscounts(true).done();
        if (numDesigners > 0)
            sr.addOns().add().inheritedFromId("21").quantity(numDesigners).done().done();
        if (numStorage > 0)
            sr.addOns().add().inheritedFromId("22").quantity(numStorage).done().done();
        if (numConnections > 0)
            sr.addOns().add().inheritedFromId("23").quantity(numConnections).done().done();
        return gateway.subscription().update(s.getId(), sr);
    }

    public void setSubscribedStatus(Account account) {
        Customer c = getCustomer(account);
        Set<String> transactions = new HashSet<String>();
        for (AccountCreditCardBillingInfo b : account.getBillingInfo()) {
            transactions.add(b.getTransactionID());
        }

        System.out.println("Account ID: " + account.getAccountID());
        if (c != null) {
            System.out.println("customer exists");
            CreditCard dc = null;
            for (CreditCard cc : c.getCreditCards()) {
                if (cc.isDefault())
                    dc = cc;
            }
            if (dc != null) {
                for (Subscription ss : dc.getSubscriptions()) {
                    for (Transaction t : ss.getTransactions()) {
                        if (!transactions.contains(t.getId())) {
                            AccountCreditCardBillingInfo info = new AccountCreditCardBillingInfo();
                            info.setAccountId(account.getAccountID());
                            info.setResponse(t.getProcessorAuthorizationCode());
                            info.setResponseCode(t.getProcessorResponseCode());
                            info.setResponseString(t.getProcessorResponseText());
                            info.setTransactionID(t.getId());
                            info.setSuccessful(t.getProcessorResponseCode().equals("1000"));
                            info.setTransactionTime(t.getCreatedAt().getTime());
                            info.setAmount(t.getAmount().toString());
                            account.getBillingInfo().add(info);
                            transactions.add(t.getId());
                        }
                    }
                    if (ss.getStatus() == Subscription.Status.ACTIVE) {
                        account.setAccountState(Account.ACTIVE);
                    } else if (ss.getStatus() == Subscription.Status.PAST_DUE) {
                        account.setAccountState(Account.BILLING_FAILED);
                    } else if (ss.getStatus() == Subscription.Status.EXPIRED) {
                        account.setAccountState(Account.BILLING_FAILED);
                    } else if (ss.getStatus() == Subscription.Status.CANCELED) {
                        account.setAccountState(Account.CLOSED);
                    } else {
                        account.setAccountState(Account.DELINQUENT);
                    }
                }

            }
        }

    }


    public void updateSubscriptionCard(CreditCard curCC, Subscription currentSubscription) {
        SubscriptionRequest sr = new SubscriptionRequest().paymentMethodToken(curCC.getToken());
        gateway.subscription().update(currentSubscription.getId(), sr);
    }
}
