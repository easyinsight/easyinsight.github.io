package com.easyinsight.userupload;

import com.easyinsight.dataset.PersistableDataSetForm;

/**
 * User: James Boe
 * Date: Jul 23, 2008
 * Time: 7:49:38 PM
 */
public class AsyncPersistence {
    private PersistableDataSetForm dataSet;
    private long feedID;

    public AsyncPersistence(PersistableDataSetForm dataSet, long feedID) {
        this.dataSet = dataSet;
        this.feedID = feedID;
    }

    public PersistableDataSetForm getDataSet() {
        return dataSet;
    }

    public void setDataSet(PersistableDataSetForm dataSet) {
        this.dataSet = dataSet;
    }

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }
}
