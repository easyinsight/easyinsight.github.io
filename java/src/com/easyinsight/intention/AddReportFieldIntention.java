package com.easyinsight.intention;

import com.easyinsight.analysis.AnalysisItem;

/**
 * User: jamesboe
 * Date: 9/18/11
 * Time: 5:00 PM
 */
public class AddReportFieldIntention extends Intention {
    private AnalysisItem analysisItem;

    public AddReportFieldIntention(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    public AddReportFieldIntention() {
    }

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public void setAnalysisItem(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }
}
