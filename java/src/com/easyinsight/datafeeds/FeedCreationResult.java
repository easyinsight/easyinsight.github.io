package com.easyinsight.datafeeds;

import com.easyinsight.storage.TableDefinitionMetadata;

/**
 * User: James Boe
 * Date: Nov 9, 2008
 * Time: 9:34:07 PM
 */
public class FeedCreationResult {
    private long feedID;
    private TableDefinitionMetadata tableDefinitionMetadata;

    public FeedCreationResult(long feedID, TableDefinitionMetadata tableDefinitionMetadata) {
        this.feedID = feedID;
        this.tableDefinitionMetadata = tableDefinitionMetadata;
    }

    public long getFeedID() {
        return feedID;
    }

    public TableDefinitionMetadata getTableDefinitionMetadata() {
        return tableDefinitionMetadata;
    }
}
