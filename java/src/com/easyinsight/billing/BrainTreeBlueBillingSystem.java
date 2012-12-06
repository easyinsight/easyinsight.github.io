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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


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
        } else if(result.getErrors().size() > 0) {
            info.setSuccessful(false);
            info.setResponseString(result.getMessage());

        }
        info.setTransactionTime(result.getTarget().getCreatedAt().getTime());
        info.setAmount(String.valueOf(amount));

        return info;
    }

    public String getRedirect(Request req) {
        return gateway.transparentRedirect().trData(req, ConfigLoader.instance().getRedirectLocation() + "/app/billing/newSubmit.jsp");
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
        for(Customer cc : c) {
            i++;
        }
        if(i == 0)
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
}
