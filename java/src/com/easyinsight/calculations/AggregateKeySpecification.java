package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisMeasure;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Feb 8, 2010
 * Time: 11:05:12 AM
 */
public class AggregateKeySpecification implements KeySpecification {
    private String key;
    private int aggregationType;

    public AggregateKeySpecification(String key, int aggregationType) {
        this.key = key;
        this.aggregationType = aggregationType;
    }

    @Nullable
    public AnalysisItem findAnalysisItem(Map<String, List<AnalysisItem>> keyMap, Map<String, List<AnalysisItem>> displayMap) throws CloneNotSupportedException {
        List<AnalysisItem> analysisItems = keyMap.get(key);
        if (analysisItems == null) {
            analysisItems = displayMap.get(key);
        }
        if (analysisItems != null) {
            for (AnalysisItem item : analysisItems) {
                if (item.getType() == AnalysisItemTypes.MEASURE) {
                    AnalysisMeasure analysisMeasure = (AnalysisMeasure) item;
                    if (analysisMeasure.getAggregation() == aggregationType) {
                        return item;
                    } else {
                        AnalysisMeasure clonedMeasure = (AnalysisMeasure) analysisMeasure.clone();
                        clonedMeasure.setAggregation(aggregationType);
                        return clonedMeasure;
                    }
                }
            }
        }
        return null;
    }
}
