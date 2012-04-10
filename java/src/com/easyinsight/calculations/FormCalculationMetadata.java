package com.easyinsight.calculations;

import com.easyinsight.analysis.ActualRowLayoutItem;
import com.easyinsight.analysis.AnalysisItem;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 4/4/12
 * Time: 10:45 AM
 */
public class FormCalculationMetadata extends CalculationMetadata {
    private List<AnalysisItem> analysisItemPool;
    private List<ActualRowLayoutItem> forms = new ArrayList<ActualRowLayoutItem>();

    public List<AnalysisItem> getAnalysisItemPool() {
        return analysisItemPool;
    }

    public void setAnalysisItemPool(List<AnalysisItem> analysisItemPool) {
        this.analysisItemPool = analysisItemPool;
    }

    public List<ActualRowLayoutItem> getForms() {
        return forms;
    }

    public void setForms(List<ActualRowLayoutItem> forms) {
        this.forms = forms;
    }
}
