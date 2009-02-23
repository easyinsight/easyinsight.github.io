package com.easyinsight.analysis;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Jan 13, 2008
 * Time: 6:08:58 PM
 */
public class FeedMetadata implements Serializable {
    private AnalysisItem[] fields;
    private long dataFeedID;
    private int version;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getDataFeedID() {
        return dataFeedID;
    }

    public void setDataFeedID(long dataFeedID) {
        this.dataFeedID = dataFeedID;
    }

    public AnalysisItem[] getFields() {
        return fields;
    }

    public void setFields(AnalysisItem[] fields) {
        this.fields = fields;
    }
}
