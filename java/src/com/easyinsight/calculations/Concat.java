package com.easyinsight.calculations;


import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 9:31:22 PM
 */
public class Concat extends Function {
    public Value evaluate() {
        String instanceIDName = minusBrackets(getParameterName(0));
        String targetName = minusBrackets(getParameterName(1));
        AnalysisItem instanceIDField = null;
        AnalysisItem targetField = null;
        for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
            if (instanceIDName.equals(analysisItem.toDisplay()) || instanceIDName.equals(analysisItem.getKey().toKeyString())) {
                instanceIDField = analysisItem;
            }
            if (targetName.equals(analysisItem.toDisplay()) || targetName.equals(analysisItem.getKey().toKeyString())) {
                targetField = analysisItem;
            }
        }
        if (instanceIDField == null) {
            throw new FunctionException("Could not find the specified field " + instanceIDName);
        }
        if (targetField == null) {
            throw new FunctionException("Could not find the specified field " + targetName);
        }
        String processName = getAnalysisItem().qualifiedName();
        SimpleCalculationCache simpleCache = (SimpleCalculationCache) calculationMetadata.getCache(new SimpleCacheBuilder(instanceIDField), processName);
        Value instanceValue = getParameter(0);
        List<IRow> rows = simpleCache.rowsForValue(instanceValue);
        StringBuilder sb = new StringBuilder();
        for (IRow row : rows) {
            Value value = row.getValue(targetField);
            if (value.type() == Value.EMPTY) {
                continue;
            }
            sb.append(row.getValue(targetField)).append(", ");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
        }
        return new StringValue(sb.toString());
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return 2;
    }
}