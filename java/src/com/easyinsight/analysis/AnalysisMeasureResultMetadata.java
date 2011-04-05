package com.easyinsight.analysis;

import com.easyinsight.core.Value;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 4:35:34 PM
 */
public class AnalysisMeasureResultMetadata extends AnalysisItemResultMetadata {
    private Double lowestValue;
    private Double highestValue;

    public double getLowestValue() {
        return lowestValue;
    }

    public void setLowestValue(double lowestValue) {
        this.lowestValue = lowestValue;
    }

    public double getHighestValue() {
        return highestValue;
    }

    public void setHighestValue(double highestValue) {
        this.highestValue = highestValue;
    }

   public void addValue(AnalysisItem analysisItem, Value value, InsightRequestMetadata insightRequestMetadata) {
        Double doubleValue = analysisItem.transformValue(value, insightRequestMetadata, false).toDouble();
        if (doubleValue != null) {
            if (lowestValue == null) {
                lowestValue = doubleValue;
                highestValue = doubleValue;
            } else {
                lowestValue = Math.min(lowestValue, doubleValue);
                highestValue = Math.max(highestValue, doubleValue);
            }
        }
    }
}
