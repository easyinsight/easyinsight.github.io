package com.easyinsight.datafeeds.composite;

import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.CompositeFeedConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.IServerDataSourceDefinition;

/**
 * User: James Boe
* Date: Jun 16, 2009
* Time: 10:39:52 AM
*/
public class ChildConnection {
    private FeedType sourceFeedType;
    private FeedType targetFeedType;
    private String sourceKey;
    private String targetKey;

    public ChildConnection(FeedType sourceFeedType, FeedType targetFeedType, String sourceKey, String targetKey) {
        this.sourceFeedType = sourceFeedType;
        this.targetFeedType = targetFeedType;
        this.sourceKey = sourceKey;
        this.targetKey = targetKey;
    }

    public FeedType getSourceFeedType() {
        return sourceFeedType;
    }

    public void setSourceFeedType(FeedType sourceFeedType) {
        this.sourceFeedType = sourceFeedType;
    }

    public FeedType getTargetFeedType() {
        return targetFeedType;
    }

    public void setTargetFeedType(FeedType targetFeedType) {
        this.targetFeedType = targetFeedType;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public String getTargetKey() {
        return targetKey;
    }

    public void setTargetKey(String targetKey) {
        this.targetKey = targetKey;
    }

    public CompositeFeedConnection createConnection(IServerDataSourceDefinition sourceDef, IServerDataSourceDefinition targetDef) {
        Key sourceKey = sourceDef.getField(getSourceKey());
        Key targetKey = targetDef.getField(getTargetKey());
        return new CompositeFeedConnection(sourceDef.getDataFeedID(), targetDef.getDataFeedID(),
                    sourceKey, targetKey, sourceDef.getFeedName(), targetDef.getFeedName());
    }
}
