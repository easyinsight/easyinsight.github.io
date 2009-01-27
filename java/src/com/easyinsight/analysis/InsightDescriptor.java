package com.easyinsight.analysis;

/**
 * User: James Boe
 * Date: Jan 24, 2009
 * Time: 11:23:48 PM
 */
public class InsightDescriptor {
    private long insightID;
    private String insightName;

    public InsightDescriptor() {
    }

    public InsightDescriptor(long insightID, String insightName) {
        this.insightID = insightID;
        this.insightName = insightName;
    }

    public long getInsightID() {
        return insightID;
    }

    public void setInsightID(long insightID) {
        this.insightID = insightID;
    }

    public String getInsightName() {
        return insightName;
    }

    public void setInsightName(String insightName) {
        this.insightName = insightName;
    }
}
