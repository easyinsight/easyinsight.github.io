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
    private Key fixedKey;
    private boolean leftJoin;
    private boolean rightJoin;
    private boolean leftOriginal;
    private boolean rightOriginal;

    public ChildConnection(FeedType sourceFeedType, FeedType targetFeedType, String sourceKey, String targetKey) {
        this.sourceFeedType = sourceFeedType;
        this.targetFeedType = targetFeedType;
        this.sourceKey = sourceKey;
        this.targetKey = targetKey;
    }

    public ChildConnection(FeedType sourceFeedType, FeedType targetFeedType, String sourceKey, String targetKey, boolean leftJoin, boolean rightJoin,
                           boolean leftOriginal, boolean rightOriginal) {
        this.sourceFeedType = sourceFeedType;
        this.targetFeedType = targetFeedType;
        this.sourceKey = sourceKey;
        this.targetKey = targetKey;
        this.leftJoin = leftJoin;
        this.rightJoin = rightJoin;
        this.leftOriginal = leftOriginal;
        this.rightOriginal = rightOriginal;
    }

    public ChildConnection(FeedType sourceFeedType, FeedType targetFeedType, Key fixedKey) {
        this.sourceFeedType = sourceFeedType;
        this.targetFeedType = targetFeedType;
        this.fixedKey = fixedKey;
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
        if (fixedKey == null) {
            Key sourceKey = sourceDef.getField(getSourceKey());
            if (sourceKey == null) {
                return null;
            }
            Key targetKey = targetDef.getField(getTargetKey());
            if (targetKey == null) {
                return null;
            }
            return new CompositeFeedConnection(sourceDef.getDataFeedID(), targetDef.getDataFeedID(),
                        sourceKey, targetKey, sourceDef.getFeedName(), targetDef.getFeedName(), leftJoin, rightJoin, leftOriginal, rightOriginal);
        } else {
            return new CompositeFeedConnection(sourceDef.getDataFeedID(), targetDef.getDataFeedID(),
                    fixedKey, fixedKey, sourceDef.getFeedName(), targetDef.getFeedName(), leftJoin, rightJoin, leftOriginal, rightOriginal);
        }
    }
}
