package com.easyinsight.analysis;

import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: May 8, 2010
 * Time: 12:12:41 PM
 */
public class AnalysisRangeResultMetadata extends AnalysisDimensionResultMetadata {
    @Override
    public void addValue(AnalysisItem analysisItem, Value value, InsightRequestMetadata insightRequestMetadata) {
        AnalysisRangeDimension analysisRangeDimension = (AnalysisRangeDimension) analysisItem;
        Value rangeValue = analysisRangeDimension.toRange(value);
        super.addValue(analysisItem, rangeValue, insightRequestMetadata);
    }
}
