package com.easyinsight.datafeeds;

import com.easyinsight.storage.DataStorage;

/**
 * User: James Boe
 * Date: Nov 9, 2008
 * Time: 9:34:07 PM
 */
public class FeedCreationResult {
    private long feedID;
    private DataStorage dataStorage;

    public FeedCreationResult(long feedID, DataStorage dataStorage) {
        this.feedID = feedID;
        this.dataStorage = dataStorage;
    }

    public long getFeedID() {
        return feedID;
    }

    public DataStorage getTableDefinitionMetadata() {
        return dataStorage;
    }
}
