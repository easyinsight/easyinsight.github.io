package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.datafeeds.FeedDefinition;
import nu.xom.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: 1/5/12
 * Time: 10:36 AM
 */
public class CampaignCache extends ConstantContactBaseSource {
    private List<Campaign> campaigns;

    public List<Campaign> getOrCreateCampaigns(ConstantContactCompositeSource ccSource) throws Exception {
        if (campaigns == null) {
            campaigns = new ArrayList<Campaign>();
            org.apache.http.client.HttpClient client = new DefaultHttpClient();
            Document doc = query("https://api.constantcontact.com/ws/customers/" + ccSource.getCcUserName() + "/campaigns", ccSource.getTokenKey(), ccSource.getTokenSecret(), ccSource, client);
            boolean hasMoreData;
            do {
                hasMoreData = false;
                Nodes nodes = doc.query("/feed/entry");
                for (int i = 0; i < nodes.size(); i++) {
    
                    Node node = nodes.get(i);
                    Date date = null;
                    String dateString = queryField(node, "content/Campaign/Date/text()");
                    if (dateString != null) {
                        date = DATE_FORMAT.parse(dateString);
                        long time = date.getTime();
    
                        long delta = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 90;
                        if (time < delta) {
                            continue;
                        }
    
                    }
                    
                    String idString = node.query("id/text()").get(0).getValue();
                    String id = idString.split("/")[7];
                    String name = node.query("content/Campaign/Name/text()").get(0).getValue();
                    String status = node.query("content/Campaign/Status/text()").get(0).getValue();
                    campaigns.add(new Campaign(name, id, status, date, null));
                }
                Nodes links = doc.query("/feed/link");
    
                for (int i = 0; i < links.size(); i++) {
                    Element link = (Element) links.get(i);
                    Attribute attribute = link.getAttribute("rel");
                    if (attribute != null && "next".equals(attribute.getValue())) {
                        String linkURL = link.getAttribute("href").getValue();
                        hasMoreData = true;
                        String linkURLString = "https://api.constantcontact.com" + linkURL;
                        linkURLString = linkURLString.substring(0, 45) + ccSource.getCcUserName() + linkURLString.substring(linkURLString.indexOf("/campaigns"));
                        doc = query(linkURLString, ccSource.getTokenKey(), ccSource.getTokenSecret(), ccSource, client);
                        break;
                    }
                }
            } while (hasMoreData);
        }
        return campaigns;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        throw new UnsupportedOperationException();
    }
}
