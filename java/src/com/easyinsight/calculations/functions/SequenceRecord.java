package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.calculations.*;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

import java.util.List;

/**
 * User: jamesboe
 * Date: 11/6/14
 * Time: 1:25 PM
 */
public class SequenceRecord extends Function {
    @Override
    public Value evaluate() {
        String instanceIDName = minusBrackets(getParameterName(0));
        String sortFieldName = minusBrackets(getParameterName(1));
        AnalysisItem instanceIDField = findDataSourceItem(0);
        AnalysisItem sortField = findDataSourceItem(1);
        if (instanceIDField == null) {
            throw new FunctionException("Could not find the specified field " + instanceIDName);
        }
        if (sortField == null) {
            throw new FunctionException("Could not find the specified field " + sortFieldName);
        }
        ProcessCalculationCache simpleCache = (ProcessCalculationCache) calculationMetadata.getCache(new ProcessCacheBuilder(instanceIDField, sortField), instanceIDField.qualifiedName());
        Value instanceValue = getParameter(0);
        if (instanceValue.type() == Value.EMPTY) {
            return new EmptyValue();
        }
        List<IRow> rows = simpleCache.rowsForValue(instanceValue);
        if (rows != null) {
            for (int i = 0; i < rows.size(); i++) {
                IRow row = rows.get(i);
                if (row == this.getRow()) {
                    return new NumericValue(i);
                }
            }

        }
        return new EmptyValue();
    }

    @Override
    public int getParameterCount() {
        return 2;
    }

    @Override
    public boolean onDemand() {
        return true;
    }
}
