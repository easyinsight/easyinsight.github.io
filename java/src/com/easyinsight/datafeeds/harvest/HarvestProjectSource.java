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
public class HarvestProjectSource extends HarvestBaseSource {
    public HarvestProjectSource() {
        setFeedName("Projects");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HARVEST_PROJECT;
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }
}
