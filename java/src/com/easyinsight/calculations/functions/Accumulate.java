package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.calculations.*;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 11/6/13
 * Time: 11:46 AM
 */
public class Accumulate extends Function {
    public Value evaluate() {
        // accumulate([Avg TTC], [Week], 4)
        String measureName = minusBrackets(getParameterName(0));
        String dateName = minusBrackets(getParameterName(1));
        int interval = getParameter(2).toDouble().intValue();
        AnalysisItem measure = null;
        AnalysisItem date = null;
        for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
            if (measureName.equals(analysisItem.toDisplay()) || measureName.equals(analysisItem.getKey().toKeyString())) {
                measure = analysisItem;
            }
            if (dateName.equals(analysisItem.toDisplay()) || dateName.equals(analysisItem.getKey().toKeyString())) {
                date = analysisItem;
            }
        }
        if (measure == null) {
            throw new FunctionException("Could not find the specified field " + measureName);
        }
        if (date == null) {
            throw new FunctionException("Could not find the specified field " + dateName);
        }
        Value dateValue = getParameter(1);
        CumulativeCalculationCache calculationCache = (CumulativeCalculationCache)
                calculationMetadata.getCache(new CumulativeCacheBuilder(date, (AnalysisMeasure) measure, interval), "ph");
        return calculationCache.forValue(dateValue);
    }

    public int getParameterCount() {
        return 3;
    }

    @Override
    public boolean onDemand() {
        return true;
    }
}
