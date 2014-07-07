package com.easyinsight.dashboard;

import com.easyinsight.analysis.ReplacementMap;

/**
 * User: jamesboe
 * Date: 7/7/14
 * Time: 10:46 AM
 */
public class DashboardSaveMetadata {
    private Dashboard dashboard;
    private ReplacementMap replacementMap;

    public DashboardSaveMetadata(Dashboard dashboard, ReplacementMap replacementMap) {
        this.dashboard = dashboard;
        this.replacementMap = replacementMap;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public ReplacementMap getReplacementMap() {
        return replacementMap;
    }
}
