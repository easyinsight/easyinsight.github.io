package com.easyinsight.analysis;

/**
 * User: jamesboe
 * Date: Nov 5, 2010
 * Time: 10:41:01 AM
 */
public class AnalysisItemFault extends ReportFault {
    private String message;
    private AnalysisItem analysisItem;

    public AnalysisItemFault(String message, AnalysisItem analysisItem) {
        this.message = message;
        this.analysisItem = analysisItem;
    }

    public AnalysisItemFault() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public void setAnalysisItem(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }
}
