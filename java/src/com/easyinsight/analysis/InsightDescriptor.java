package com.easyinsight.analysis;

/**
 * User: James Boe
 * Date: Jan 24, 2009
 * Time: 11:23:48 PM
 */
public class InsightDescriptor {
    private long insightID;
    private String name;
    private long dataFeedID;

    public InsightDescriptor() {
    }

    public InsightDescriptor(long insightID, String name, long dataFeedID) {
        this.insightID = insightID;
        this.name = name;
        this.dataFeedID = dataFeedID;
    }

    public long getDataFeedID() {
        return dataFeedID;
    }

    public void setDataFeedID(long dataFeedID) {
        this.dataFeedID = dataFeedID;
    }

    public long getInsightID() {
        return insightID;
    }

    public void setInsightID(long insightID) {
        this.insightID = insightID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
