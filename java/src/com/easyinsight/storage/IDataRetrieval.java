package com.easyinsight.storage;

import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.dataset.ColumnSegment;
import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.FeedPersistenceMetadata;

import java.sql.Connection;

/**
 * User: James Boe
 * Date: Apr 16, 2008
 * Time: 4:25:36 PM
 */
public interface IDataRetrieval {

    void storeData(long dataFeedID, PersistableDataSetForm dataSet);

    void deleteData(long dataFeedID);

    ColumnSegment getSegment(long feedID, Key column);

    void appendData(long feedID, PersistableDataSetForm dataSet);

    //void addOrUpdateMetadata(long dataFeedID, FeedPersistenceMetadata metadata, Connection conn);
}
