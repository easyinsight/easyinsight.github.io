package com.easyinsight.storage;

import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.dataset.ColumnSegment;
import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.FeedPersistenceMetadata;
import com.easyinsight.datafeeds.FeedDefinition;

import java.sql.Connection;

/**
 * User: James Boe
 * Date: Nov 8, 2008
 * Time: 10:29:28 AM
 */
public class SQLDataRetrieval implements IDataRetrieval {

    public void defineTable(FeedDefinition feedDefinition) {
        
    }

    public void update() {
        
    }

    public void delete() {
        
    }

    public void storeData(long dataFeedID, PersistableDataSetForm dataSet) {

    }

    public void deleteData(long dataFeedID) {

    }

    public ColumnSegment getSegment(long feedID, Key column) {
        return null;
    }

    public void appendData(long feedID, PersistableDataSetForm dataSet) {

    }

    public void addOrUpdateMetadata(long dataFeedID, FeedPersistenceMetadata metadata, Connection conn) {

    }
}
