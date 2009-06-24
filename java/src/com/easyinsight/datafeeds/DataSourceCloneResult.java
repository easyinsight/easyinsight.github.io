package com.easyinsight.datafeeds;

import com.easyinsight.core.Key;

import java.util.Map;

/**
 * User: James Boe
 * Date: Jun 24, 2009
 * Time: 10:47:19 AM
 */
public class DataSourceCloneResult {
    private FeedDefinition feedDefinition;
    private Map<Key, Key> keyReplacementMap;

    public DataSourceCloneResult(FeedDefinition feedDefinition, Map<Key, Key> keyReplacementMap) {
        this.feedDefinition = feedDefinition;
        this.keyReplacementMap = keyReplacementMap;
    }

    public FeedDefinition getFeedDefinition() {
        return feedDefinition;
    }

    public Map<Key, Key> getKeyReplacementMap() {
        return keyReplacementMap;
    }
}
