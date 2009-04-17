package com.easyinsight.analysis;

import java.util.Map;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

/**
 * User: James Boe
 * Date: Jul 17, 2008
 * Time: 7:45:16 PM
 */
public class WSYahooMapDefinition extends WSAnalysisDefinition {

    private long yahooMapDefinitionID;

    public long getYahooMapDefinitionID() {
        return yahooMapDefinitionID;
    }

    public void setYahooMapDefinitionID(long yahooMapDefinitionID) {
        this.yahooMapDefinitionID = yahooMapDefinitionID;
    }

    public String getDataFeedType() {
        return AnalysisTypes.YAHOO_MAP;
    }

    private AnalysisItem measure;
    private AnalysisItem geography;

    protected void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("geography", Arrays.asList(geography), structure);
        addItems("measure", Arrays.asList(measure), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        geography = firstItem("geography", structure);
        measure = firstItem("measure", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        columnList.add(measure);
        columnList.add(geography);
        return columnList;
    }
}
