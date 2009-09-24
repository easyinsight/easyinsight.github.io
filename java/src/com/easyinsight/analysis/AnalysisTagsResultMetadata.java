package com.easyinsight.analysis;

import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: Sep 24, 2009
 * Time: 3:35:53 PM
 */
public class AnalysisTagsResultMetadata extends AnalysisDimensionResultMetadata {
    @Override
    public void addValue(AnalysisItem analysisItem, Value value, InsightRequestMetadata insightRequestMetadata) {
        if (analysisItem.isMultipleTransform()) {
            Value[] transformedValues = analysisItem.transformToMultiple(value);
            for (Value transformedValue : transformedValues) {
                super.addValue(analysisItem, transformedValue, insightRequestMetadata);
            }
        }
    }
}
