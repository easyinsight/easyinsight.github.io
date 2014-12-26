package com.easyinsight.datafeeds.basecampnext;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.security.SecurityUtil;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 3/29/12
 * Time: 4:08 PM
 */
public class InitRetrieval extends BasecampNextBaseSource {

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        throw new UnsupportedOperationException();
    }

    public List<BasecampNextAccount> getAccounts(BasecampNextCompositeSource parent) {
        JSONObject jsonObject = rawJSONRequestForObject("https://launchpad.37signals.com/authorization.json", parent);
        Object accountsArrayObject = jsonObject.get("accounts");
        List<BasecampNextAccount> accounts = new ArrayList<BasecampNextAccount>();
        if (accountsArrayObject instanceof JSONArray) {
            JSONArray accountsArray = (JSONArray) accountsArrayObject;
            for (int i = 0; i < accountsArray.size(); i++) {
                JSONObject accountObject = (JSONObject) accountsArray.get(i);
                String product = (String) accountObject.get("product");
                if ("bcx".equals(product)) {
                    String s = accountObject.get("id").toString();
                    BasecampNextAccount account = new BasecampNextAccount();
                    account.setName((String) accountObject.get("name"));
                    account.setId(s);
                    System.out.println(SecurityUtil.getUserName() + " found " + s + " - " + accountObject.get("name"));
                    accounts.add(account);
                }
            }
        }

        return accounts;
    }
}
