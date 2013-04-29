package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.logging.LogClass;
import nu.xom.ParsingException;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
            HttpClient client = new HttpClient();
            Map result = query("https://api.constantcontact.com/v2/emailmarketing/campaigns?api_key=" + ConstantContactCompositeSource.KEY, ccSource, client);
            Map meta = (Map) result.get("meta");
            String nextLink = null;
            if (meta != null) {
                Map pagination = (Map) meta.get("pagination");
                if (pagination != null) {
                    Object nextLinkObject = pagination.get("next_link");
                    if (nextLinkObject != null) {
                        nextLink = nextLinkObject.toString();
                    }
                }
            }

            boolean hasMoreData;
            do {
                List campaigns = (List) result.get("results");
                hasMoreData = false;
                for (Object obj : campaigns) {
                    Map node = (Map) obj;
                    String id = node.get("id").toString();
                    String name = node.get("name").toString();
                    String status = node.get("status").toString();
                    Map xyz = query("https://api.constantcontact.com/v2/emailmarketing/campaigns/"+id+"?api_key=" + ConstantContactCompositeSource.KEY, ccSource, client);
                    List clickthroughDetails = (List) xyz.get("click_through_details");
                    List<CCCampaignLink> links = new ArrayList<CCCampaignLink>();
                    if (clickthroughDetails != null) {
                        for (Object clickObj : clickthroughDetails) {
                            Map clickthroughMap = (Map) clickObj;
                            String clickID = clickthroughMap.get("url_uid").toString();
                            String url = clickthroughMap.get("url").toString();
                            CCCampaignLink link = new CCCampaignLink();
                            link.setId(clickID);
                            link.setUrl(url);
                            links.add(link);
                        }
                    }
                    Date lastRun;
                    if (node.get("last_run_date") != null) {
                        lastRun = DATE_FORMAT.parse(node.get("last_run_date").toString());
                    } else {
                        lastRun = new Date(1);
                    }
                    this.campaigns.add(new Campaign(name, id, status, lastRun, null, links));
                }
                if (nextLink != null) {
                    try {
                        result = query(nextLink, ccSource, client);
                    } catch (IllegalArgumentException iae) {
                        LogClass.error(iae.getMessage() + " on " + nextLink);
                        return this.campaigns;
                    }
                    meta = (Map) result.get("meta");
                    nextLink = null;
                    if (meta != null) {
                        Map pagination = (Map) meta.get("pagination");
                        if (pagination != null) {
                            Object nextLinkObject = pagination.get("next_link");
                            if (nextLinkObject != null) {
                                nextLink = nextLinkObject.toString();
                            }
                        }
                    }
                    hasMoreData = true;
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
