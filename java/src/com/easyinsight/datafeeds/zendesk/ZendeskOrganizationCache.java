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
public class ZendeskOrganizationCache extends ZendeskBaseSource {
    private Map<String, String> organizations = new HashMap<>();

    public Map<String, String> getOrganizations() {
        return organizations;
    }

    public void populate(HttpClient httpClient, ZendeskCompositeSource zendeskCompositeSource) throws InterruptedException {
        String nextPage = zendeskCompositeSource.getUrl() + "/api/v2/organizations.json";
        while (nextPage != null) {
            Map ticketObjects = queryList(nextPage, zendeskCompositeSource, httpClient);
            List results = (List) ticketObjects.get("organizations");
            if (results == null) {
                return;
            }
            for (Object obj : results) {
                Map map = (Map) obj;
                String name = queryField(map, "name");
                String id = queryField(map, "id");
                organizations.put(name, id);
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
