package com.easyinsight.datafeeds.highrise;

import com.easyinsight.datafeeds.FeedDefinition;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Jun 7, 2010
 * Time: 9:09:36 AM
 */
public class HighriseCache extends HighRiseBaseSource {
    Map<String, String> userCache = new HashMap<String, String>();

    public void populateCaches(HttpClient client, String url, FeedDefinition parentDefinition) throws HighRiseLoginException, ParsingException {
        Builder builder = new Builder();

        Document userDoc = runRestRequest("/users.xml", client, builder, url, true, false, parentDefinition);
        Nodes usersDoc = userDoc.query("/users/user");
        for (int i = 0; i < usersDoc.size(); i++) {
            Node userNode = usersDoc.get(i);
            String userName = queryField(userNode, "name/text()");
            String userID = queryField(userNode, "id/text()");
            userCache.put(userID, userName);
        }
    }

    public String getUserName(String userID) {
        String name = userCache.get(userID);
        if (name == null) {
            name = "";
        }
        return name;
    }

    public Map<String, String> getUserCache() {
        return userCache;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
