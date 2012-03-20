package com.easyinsight.analysis;

import java.util.List;

/**
 * User: jamesboe
 * Date: 3/12/12
 * Time: 2:06 PM
 */
public class ActualRowSet {
    private List<ActualRow> rows;
    private List<AnalysisItem> analysisItems;
    private long dataSourceID;

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public List<ActualRow> getRows() {
        return rows;
    }

    public void setRows(List<ActualRow> rows) {
        this.rows = rows;
    }

    public List<AnalysisItem> getAnalysisItems() {
        return analysisItems;
    }

    public void setAnalysisItems(List<AnalysisItem> analysisItems) {
        this.analysisItems = analysisItems;
    }
}
