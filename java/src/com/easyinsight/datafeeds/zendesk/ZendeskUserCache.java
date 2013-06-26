package com.easyinsight.datafeeds.zendesk;

import com.easyinsight.datafeeds.FeedDefinition;
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
        String nextPage = zendeskCompositeSource.getUrl() + "/api/v2/users.json";
        while (nextPage != null) {
            Map ticketObjects = queryList(nextPage, zendeskCompositeSource, httpClient);
            List results = (List) ticketObjects.get("users");
            for (Object obj : results) {
                Map map = (Map) obj;
                String name = queryField(map, "name");
                String id = queryField(map, "id");
                String organization = queryField(map, "organization_id");
                String role = queryField(map, "role");
                String email = queryField(map, "email");
                users.put(id, new ZendeskUser(name, id, organization, role, email));
            }
            if (ticketObjects.get("next_page") != null) {
                nextPage = ticketObjects.get("next_page").toString();
            } else {
                nextPage = null;
            }
        }
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
