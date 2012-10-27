package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.calculations.*;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 10/25/12
 * Time: 1:41 PM
 */
public class FillInterval extends Function {
    public Value evaluate() {
        String instanceIDName = minusBrackets(getParameterName(0));
        String startDate = minusBrackets(getParameterName(1));
        String endDate = minusBrackets(getParameterName(2));
        AnalysisItem instanceIDField = null;
        AnalysisItem startField = null;
        AnalysisItem endField = null;
        for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
            if (instanceIDName.equals(analysisItem.toDisplay()) || instanceIDName.equals(analysisItem.getKey().toKeyString())) {
                instanceIDField = analysisItem;
            }
            if (startDate.equals(analysisItem.toDisplay()) || startDate.equals(analysisItem.getKey().toKeyString())) {
                startField = analysisItem;
            }
            if (endDate.equals(analysisItem.toDisplay()) || endDate.equals(analysisItem.getKey().toKeyString())) {
                endField = analysisItem;
            }
        }
        if (instanceIDField == null) {
            throw new FunctionException("Could not find the specified field " + instanceIDName);
        }
        if (startField == null) {
            throw new FunctionException("Could not find the specified field " + startDate);
        }
        if (endField == null) {
            throw new FunctionException("Could not find the specified field " + endDate);
        }
        String processName = getAnalysisItem().qualifiedName();
        calculationMetadata.getCache(new FillCacheBuilder(instanceIDField, startField, endField, calculationMetadata, (AnalysisDateDimension) getAnalysisItem()), processName);
        return new EmptyValue();
    }

    public int getParameterCount() {
        return 3;
    }

    @Override
    public boolean onDemand() {
        return true;
    }
}
