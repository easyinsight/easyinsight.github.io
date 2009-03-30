package com.easyinsight.core;

/**
 * User: James Boe
 * Date: Jan 24, 2009
 * Time: 11:23:48 PM
 */
public class InsightDescriptor extends EIDescriptor {

    private long dataFeedID;

    public InsightDescriptor() {
    }

    public InsightDescriptor(long id, String name, long dataFeedID) {
        super(name, id);
        this.dataFeedID = dataFeedID;
    }

    public long getDataFeedID() {
        return dataFeedID;
    }

    public void setDataFeedID(long dataFeedID) {
        this.dataFeedID = dataFeedID;
    }
}
