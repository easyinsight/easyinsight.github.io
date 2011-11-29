package com.easyinsight.datafeeds.zendesk;

import com.easyinsight.datafeeds.FeedDefinition;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 11/29/11
 * Time: 10:16 AM
 */
public class ZendeskUserCache extends ZendeskBaseSource {
    private Map<String, ZendeskUser> users = new HashMap<String, ZendeskUser>();

    public Map<String, ZendeskUser> getUsers() {
        return users;
    }

    public void setUsers(Map<String, ZendeskUser> users) {
        this.users = users;
    }

    public void populate(HttpClient httpClient, String url, ZendeskCompositeSource zendeskCompositeSource) throws InterruptedException {
        Builder builder = new Builder();
        Document doc = runRestRequest(zendeskCompositeSource, httpClient, "/users.xml", builder);
        Nodes userNodes = doc.query("/users/user");
        for (int i = 0; i < userNodes.size(); i++) {
            Node userNode = userNodes.get(i);
            String name = queryField(userNode, "name/text()");
            String id = queryField(userNode, "id/text()");
            String organization = queryField(userNode, "organization-id/text()");
            String role = queryField(userNode, "roles/text()");
            String email = queryField(userNode, "email/text()");
            users.put(id, new ZendeskUser(name, id, organization, role, email));
        }
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
