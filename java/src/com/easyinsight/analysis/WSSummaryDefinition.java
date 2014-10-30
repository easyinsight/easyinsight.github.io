package com.easyinsight.analysis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * User: James Boe
 * Date: Jul 17, 2008
 * Time: 7:45:24 PM
 */
public class WSSummaryDefinition extends WSTreeDefinition {

    private long summaryDefinitionID;

    private List<AnalysisItem> subItems;

    public List<AnalysisItem> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<AnalysisItem> subItems) {
        this.subItems = subItems;
    }

    public long getSummaryDefinitionID() {
        return summaryDefinitionID;
    }

    public void setSummaryDefinitionID(long summaryDefinitionID) {
        this.summaryDefinitionID = summaryDefinitionID;
    }

    public String getDataFeedType() {
        return AnalysisTypes.SUMMARY;
    }

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentDefinitions) throws JSONException {
        JSONObject list = super.toJSON(htmlReportMetadata, parentDefinitions);
        list.put("type", "summary");
        list.put("key", getUrlKey());
        list.put("url", "/app/htmlExport");
        return list;
    }
}