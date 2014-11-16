package com.easyinsight.calculations.functions;

import cern.jet.stat.Descriptive;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.calculations.*;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

import java.util.List;

/**
 * User: jamesboe
 * Date: 2/24/14
 * Time: 6:44 PM
 */
public class StandardDeviation extends Function {
    public Value evaluate() {
        String instanceIDName = minusBrackets(getParameterName(0));
        String statName = minusBrackets(getParameterName(1));
        AnalysisItem instanceIDField = findDataSourceItem(0);

        AnalysisItem statMeasure = findDataSourceItem(1);
        if (instanceIDField == null) {
            throw new FunctionException("Could not find the specified field " + instanceIDName);
        }
        if (statMeasure == null) {
            throw new FunctionException("Could not find the specified field " + statName);
        }
        String processName = getAnalysisItem().qualifiedName();
        StatCalculationCache statCache = (StatCalculationCache) calculationMetadata.getCache(new StatCacheBuilder(instanceIDField, statMeasure,
                d -> Descriptive.standardDeviation(Descriptive.variance(d.size(), Descriptive.sum(d), Descriptive.sumOfSquares(d)))), processName);
        Value instanceValue = getParameter(0);
        Double result = (Double) statCache.getResultForValue(instanceValue);
        if (result == null) {
            return new EmptyValue();
        }
        return new NumericValue(result);
    }

    public int getParameterCount() {
        return 2;
    }

    @Override
    public boolean onDemand() {
        return true;
    }
}
