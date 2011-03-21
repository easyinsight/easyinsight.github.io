package com.easyinsight.datafeeds.campaignmonitor;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.dataset.DataSet;
import nu.xom.Document;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * User: jamesboe
 * Date: 12/28/10
 * Time: 2:00 PM
 */
public class CMListSource extends CampaignMonitorBaseSource {
    public boolean defineCustomFields(FeedDefinition parentDefinition) {
        try {
            CampaignMonitorDataSource ccSource = (CampaignMonitorDataSource) parentDefinition;
            Document doc = query("customfields.xml", ccSource.getCmApiKey(), parentDefinition);
            // for each custom field...
            // if the custom field does not exist as is, add it
            Nodes customFields = doc.query("/CustomFields/CustomField");
            for (int i = 0; i < customFields.size(); i++) {

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
