package com.easyinsight.datafeeds.basecampnext;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.logging.LogClass;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public List<BasecampNextAccount> getAccounts(BasecampNextCompositeSource parent) throws JSONException {
        JSONObject jsonObject = rawJSONRequestForObject("https://launchpad.37signals.com/authorization.json", parent);
        Object accountsArrayObject = jsonObject.getJSONArray("accounts");
        List<BasecampNextAccount> accounts = new ArrayList<BasecampNextAccount>();
        if (accountsArrayObject instanceof JSONArray) {
            JSONArray accountsArray = (JSONArray) accountsArrayObject;
            for (int i = 0; i < accountsArray.length(); i++) {
                JSONObject accountObject = accountsArray.getJSONObject(i);
                String product = accountObject.getString("product");
                if ("bcx".equals(product)) {
                    String s = accountObject.getString("id");
                    BasecampNextAccount account = new BasecampNextAccount();
                    account.setName(accountObject.getString("name"));
                    account.setId(s);
                    accounts.add(account);
                }
            }
        }

        return accounts;
    }
}
