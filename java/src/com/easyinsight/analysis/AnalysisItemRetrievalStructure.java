package com.easyinsight.analysis;

/**
 * User: jamesboe
 * Date: 4/11/12
 * Time: 10:02 AM
 */
public class AnalysisItemRetrievalStructure {
    private boolean onStorage;
    private WSAnalysisDefinition report;

    public WSAnalysisDefinition getReport() {
        return report;
    }

    public void setReport(WSAnalysisDefinition report) {
        this.report = report;
    }

    public boolean isOnStorage() {
        return onStorage;
    }

    public void setOnStorage(boolean onStorage) {
        this.onStorage = onStorage;
    }
}
