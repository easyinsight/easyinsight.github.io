package com.easyinsight.datafeeds.harvest;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 3/21/11
 * Time: 7:37 PM
 */
public class HarvestTimeSource extends HarvestBaseSource {
    public HarvestTimeSource() {
        setFeedName("Time Tracking");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HARVEST_TIME;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }
}
