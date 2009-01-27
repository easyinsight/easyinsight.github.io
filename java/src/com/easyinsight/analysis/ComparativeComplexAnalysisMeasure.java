package com.easyinsight.analysis;

import com.easyinsight.AnalysisMeasure;

/**
 * User: James Boe
 * Date: Oct 17, 2008
 * Time: 11:07:54 AM
 */
public class ComparativeComplexAnalysisMeasure extends ComplexAnalysisMeasure {
    private AnalysisMeasure compareToMeasure;

    public AnalysisMeasure getCompareToMeasure() {
        return compareToMeasure;
    }

    public void setCompareToMeasure(AnalysisMeasure compareToMeasure) {
        this.compareToMeasure = compareToMeasure;
    }
}
