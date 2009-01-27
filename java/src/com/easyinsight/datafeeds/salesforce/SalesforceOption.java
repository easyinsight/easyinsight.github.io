package com.easyinsight.datafeeds.salesforce;

/**
 * User: James Boe
 * Date: Jan 26, 2008
 * Time: 3:58:41 PM
 */
public class SalesforceOption {
    private String name;
    private long dataFeedID;        

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDataFeedID() {
        return dataFeedID;
    }

    public void setDataFeedID(long dataFeedID) {
        this.dataFeedID = dataFeedID;
    }
}
