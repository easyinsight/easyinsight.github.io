package com.easyinsight.datafeeds.test;

import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.IServerDataSourceDefinition;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;

/**
 * User: James Boe
 * Date: Jun 16, 2009
 * Time: 10:39:26 AM
 */
public class TestCompositeDataSource extends CompositeServerDataSource {
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.TEST_ALPHA);
        types.add(FeedType.TEST_BETA);
        types.add(FeedType.TEST_GAMMA);
        return types;
    }

    protected Collection<ChildConnection> getChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.TEST_ALPHA, FeedType.TEST_BETA,
                TestAlphaDataSource.JOIN_DIM, TestBetaDataSource.JOIN_DIM),
                new ChildConnection(FeedType.TEST_BETA, FeedType.TEST_GAMMA, TestBetaDataSource.JOIN_DIM,
                        TestGammaDataSource.JOIN_DIM),
                new ChildConnection(FeedType.TEST_ALPHA, FeedType.TEST_GAMMA, TestAlphaDataSource.JOIN_DIM,
                        TestGammaDataSource.JOIN_DIM));
    }

    protected IServerDataSourceDefinition createForFeedType(FeedType feedType) {
        if (feedType.equals(FeedType.TEST_ALPHA)) {
            return new TestAlphaDataSource();
        } else if (feedType.equals(FeedType.TEST_BETA)) {
            return new TestBetaDataSource();
        } else if (feedType.equals(FeedType.TEST_GAMMA)){
            return new TestGammaDataSource();
        }
        throw new RuntimeException();
    }
}
