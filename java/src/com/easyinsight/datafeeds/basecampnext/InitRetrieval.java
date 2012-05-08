package com.easyinsight.datafeeds.basecampnext;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.logging.LogClass;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String blah(BasecampNextCompositeSource parent) throws JSONException {
        JSONObject jsonObject = rawJSONRequestForObject("https://launchpad.37signals.com/authorization.json", parent);
        JSONArray accountsArray = jsonObject.getJSONArray("accounts");
        for (int i = 0; i < accountsArray.length(); i++) {
            JSONObject accountObject = accountsArray.getJSONObject(i);
            String product = accountObject.getString("product");
            if ("bcx".equals(product)) {
                return accountObject.getString("id");
            }
        }
        LogClass.error("No Basecamp Next product found - JSON: " + jsonObject.toString());
        throw new RuntimeException("No Basecamp account found");
//        return null;
    }
}
