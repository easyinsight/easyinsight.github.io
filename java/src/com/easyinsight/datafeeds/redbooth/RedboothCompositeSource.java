package com.easyinsight.datafeeds.redbooth;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 2/19/14
 * Time: 7:09 PM
 */
public class RedboothCompositeSource extends CompositeServerDataSource {

    public RedboothCompositeSource() {
        setFeedName("Redbooth");
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.REDBOOTH_COMPOSITE;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.REDBOOTH_ORGANIZATION);
        types.add(FeedType.REDBOOTH_PROJECT);
        types.add(FeedType.REDBOOTH_TASK_LIST);
        types.add(FeedType.REDBOOTH_TASK);
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }
}
