package com.easyinsight.datafeeds.composite;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.CompositeFeedConnection;
import com.easyinsight.datafeeds.FeedDefinition;
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
    private int sourceCardinality;
    private int targetCardinality;
    private boolean forceOuterJoin;

    public ChildConnection(FeedType sourceFeedType, FeedType targetFeedType, String sourceKey, String targetKey) {
        this.sourceFeedType = sourceFeedType;
        this.targetFeedType = targetFeedType;
        this.sourceKey = sourceKey;
        this.targetKey = targetKey;
    }

    public ChildConnection(FeedType sourceFeedType, FeedType targetFeedType, String sourceKey, String targetKey, boolean forceOuterJoin) {
        this.sourceFeedType = sourceFeedType;
        this.targetFeedType = targetFeedType;
        this.sourceKey = sourceKey;
        this.targetKey = targetKey;
        this.forceOuterJoin = forceOuterJoin;
    }

    public ChildConnection(FeedType sourceFeedType, FeedType targetFeedType, String sourceKey, String targetKey, int sourceCardinality, int targetCardinality) {
        this.sourceFeedType = sourceFeedType;
        this.targetFeedType = targetFeedType;
        this.sourceKey = sourceKey;
        this.targetKey = targetKey;
        this.sourceCardinality = sourceCardinality;
        this.targetCardinality = targetCardinality;
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

    public int getSourceCardinality() {
        return sourceCardinality;
    }

    public int getTargetCardinality() {
        return targetCardinality;
    }

    public boolean isForceOuterJoin() {
        return forceOuterJoin;
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

    private Key findKey(long id, String key, FeedDefinition dataSource) {
        for (AnalysisItem field : dataSource.getFields()) {
            if (field.getKey() instanceof DerivedKey) {
                DerivedKey derivedKey = (DerivedKey) field.getKey();
                if (derivedKey.getFeedID() == id) {
                    if (derivedKey.getParentKey().toKeyString().equals(key)) {
                        return derivedKey.getParentKey();
                    }
                }
            }
        }
        return null;
    }

    public CompositeFeedConnection createConnection(Long sourceID, String sourceName, Long targetID, String targetName, FeedDefinition parentSource) {
        if (fixedKey == null) {
            Key sourceKey = findKey(sourceID, getSourceKey(), parentSource);
            if (sourceKey == null) {
                return null;
            }
            Key targetKey = findKey(targetID, getTargetKey(), parentSource);
            if (targetKey == null) {
                return null;
            }
            CompositeFeedConnection conn = new CompositeFeedConnection(sourceID, targetID,
                        sourceKey, targetKey, sourceName, targetName, leftJoin, rightJoin, leftOriginal, rightOriginal);
            conn.setSourceCardinality(getSourceCardinality());
            conn.setTargetCardinality(getTargetCardinality());
            conn.setForceOuterJoin(forceOuterJoin ? 1 : 0);
            return conn;
        } else {
            return new CompositeFeedConnection(sourceID, targetID,
                    fixedKey, fixedKey, sourceName, targetName, leftJoin, rightJoin, leftOriginal, rightOriginal);
        }
    }
}
