package com.easyinsight.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 4/11/12
 * Time: 10:02 AM
 */
public class AnalysisItemRetrievalStructure {
    private boolean onStorage;
    private WSAnalysisDefinition report;
    private AnalysisDefinition baseReport;

    public void setBaseReport(AnalysisDefinition baseReport) {
        this.baseReport = baseReport;
    }

    public WSAnalysisDefinition getReport() {
        return report;
    }

    public void setReport(WSAnalysisDefinition report) {
        this.report = report;
    }

    public List<FilterDefinition> getFilters() {
        if (report != null) {
            return report.getFilterDefinitions();
        }
        if (baseReport != null) {
            return baseReport.getFilterDefinitions();
        }
        return new ArrayList<FilterDefinition>();
    }

    public boolean isOnStorage() {
        return onStorage;
    }

    public void setOnStorage(boolean onStorage) {
        this.onStorage = onStorage;
    }
}
