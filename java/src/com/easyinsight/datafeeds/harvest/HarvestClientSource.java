package com.easyinsight.datafeeds.harvest;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.FeedType;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 3/21/11
 * Time: 7:36 PM
 */
public class HarvestClientSource extends HarvestBaseSource {
    public HarvestClientSource() {
        setFeedName("Clients");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HARVEST_CLIENT;
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }
}
