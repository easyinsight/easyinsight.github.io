package com.easyinsight.datafeeds.internal;

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
 * Date: 10/27/14
 * Time: 1:55 PM
 */
public class InternalCompositeSource extends CompositeServerDataSource {

    public InternalCompositeSource() {
        setFeedName("Internal");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INTERNAL_COMPOSITE;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.LIVE;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<>();
        types.add(FeedType.INTERNAL_DATA_SOURCE);
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<>();
    }
}
