package com.easyinsight.billing;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import com.easyinsight.users.Account;
import com.easyinsight.users.AccountActivityStorage;
import com.easyinsight.logging.LogClass;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.IOException;


/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 26, 2009
 * Time: 10:13:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class BrainTreeBillingSystem {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String username;
    private String password;
    private static final String INDIVIDUAL_AMOUNT = "10.00";
    private static final String GROUP_AMOUNT = "100.00";

    public void cancelPlan(long accountID) {
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod("https://secure.braintreepaymentgateway.com/api/transact.php");
        String removeUserQueryString = "username=" + username + "&password=" +password + "&customer_vault=delete_customer&customer_vault_id=" + accountID;
        method.setQueryString(removeUserQueryString);
        try {
            client.executeMethod(method);
        } catch(Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> billAccount(long accountID, double amount) {
        return billAccount(accountID, amount, false);
    }

    public Map<String, String> billAccount(long accountID, double amount, boolean auth) {
        HttpClient client = new HttpClient();

        AccountActivityStorage storage = new AccountActivityStorage();

        String queryString = "username=" + username + "&password=" + password + "&customer_vault_id=" + accountID;
        queryString += "&type=" + (auth ? "auth":"sale") + "&amount=" + String.valueOf(amount);

        PostMethod method = new PostMethod("https://secure.braintreepaymentgateway.com/api/transact.php");
        method.setQueryString(queryString);
        Map<String, String> params = new HashMap<String, String>();
        try {
            client.executeMethod(method);
            String[] pairs = method.getResponseBodyAsString().split("&");
            for(String pair : pairs) {
                String[] pairVals = pair.split("=");
                if(pairVals.length > 1)
                    params.put(pairVals[0], pairVals[1]);
                else
                    params.put(pairVals[0], "");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return params;
    }

}
