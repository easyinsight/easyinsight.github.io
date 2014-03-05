package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.AnalysisTypes;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.HTMLReportMetadata;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * User: jamesboe
 * Date: 9/26/11
 * Time: 9:44 AM
 */
public class WSTrendGridDefinition extends WSKPIDefinition {

    private long trendReportID;
    private int sortIndex;
    private boolean sortAscending;

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public boolean isSortAscending() {
        return sortAscending;
    }

    public void setSortAscending(boolean sortAscending) {
        this.sortAscending = sortAscending;
    }

    public long getTrendReportID() {
        return trendReportID;
    }

    public void setTrendReportID(long trendReportID) {
        this.trendReportID = trendReportID;
    }

    @Override
    public String getDataFeedType() {
        return AnalysisTypes.TREND_GRID;
    }

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentDefinitions) throws JSONException {
        JSONObject list = super.toJSON(htmlReportMetadata, parentDefinitions);
        list.put("type", "trend_definition");
        list.put("key", getUrlKey());
        list.put("url", "/app/htmlExport");
        return list;
    }
}
