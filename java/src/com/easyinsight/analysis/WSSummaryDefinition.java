package com.easyinsight.analysis;

/**
 * User: James Boe
 * Date: Jul 17, 2008
 * Time: 7:45:24 PM
 */
public class WSSummaryDefinition extends WSTreeDefinition {

    private long summaryDefinitionID;

    public long getSummaryDefinitionID() {
        return summaryDefinitionID;
    }

    public void setSummaryDefinitionID(long summaryDefinitionID) {
        this.summaryDefinitionID = summaryDefinitionID;
    }

    public String getDataFeedType() {
        return AnalysisTypes.SUMMARY;
    }
}