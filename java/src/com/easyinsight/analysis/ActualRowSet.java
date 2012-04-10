package com.easyinsight.analysis;

import com.easyinsight.core.Value;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 3/12/12
 * Time: 2:06 PM
 */
public class ActualRowSet {
    private List<ActualRow> rows;
    private List<AnalysisItem> analysisItems;
    private List<ActualRowLayoutItem> forms;
    private long dataSourceID;

    private Map<String, Collection<JoinLabelOption>> options;

    public List<ActualRowLayoutItem> getForms() {
        return forms;
    }

    public void setForms(List<ActualRowLayoutItem> forms) {
        this.forms = forms;
    }

    public Map<String, Collection<JoinLabelOption>> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Collection<JoinLabelOption>> options) {
        this.options = options;
    }

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
