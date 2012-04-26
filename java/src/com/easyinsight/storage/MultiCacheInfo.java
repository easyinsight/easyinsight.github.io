package com.easyinsight.storage;

import com.easyinsight.analysis.AnalysisCalculation;
import com.easyinsight.analysis.AnalysisItem;

/**
 * User: jamesboe
 * Date: 4/26/12
 * Time: 4:30 PM
 */
public class MultiCacheInfo {
    private AnalysisCalculation calculation;
    private AnalysisItem endField;

    public MultiCacheInfo(AnalysisCalculation calculation, AnalysisItem endField) {
        this.calculation = calculation;
        this.endField = endField;
    }

    public AnalysisCalculation getCalculation() {
        return calculation;
    }

    public AnalysisItem getEndField() {
        return endField;
    }
}
