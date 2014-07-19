package com.easyinsight.dashboard;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.ReplacementMap;

import java.util.List;

/**
 * User: jamesboe
 * Date: 7/7/14
 * Time: 10:46 AM
 */
public class DashboardSaveMetadata {
    private Dashboard dashboard;
    private ReplacementMap replacementMap;
    private List<AnalysisItem> fields;

    public DashboardSaveMetadata(Dashboard dashboard, ReplacementMap replacementMap, List<AnalysisItem> fields) {
        this.dashboard = dashboard;
        this.replacementMap = replacementMap;
        this.fields = fields;
    }

    public List<AnalysisItem> getFields() {
        return fields;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public ReplacementMap getReplacementMap() {
        return replacementMap;
    }
}
