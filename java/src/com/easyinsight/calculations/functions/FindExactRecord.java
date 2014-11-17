package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.calculations.ProcessCacheBuilder;
import com.easyinsight.calculations.ProcessCalculationCache;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

import java.util.List;

/**
 * User: jamesboe
 * Date: 9/5/12
 * Time: 7:58 PM
 */
public class FindExactRecord extends Function {

    public Value evaluate() {
        String instanceIDName = minusBrackets(getParameterName(0));
        String targetName = minusBrackets(getParameterName(3));
        String findName = minusBrackets(getParameterName(2));
        String lookingForValue = minusQuotes(getParameter(4)).toString();
        AnalysisItem instanceIDField = findDataSourceItem(0);
        AnalysisItem targetField = findDataSourceItem(3);
        AnalysisItem sortField = findDataSourceItem(2);
        if (instanceIDField == null) {
            throw new FunctionException("Could not find the specified field " + instanceIDName);
        }
        if (targetField == null) {
            throw new FunctionException("Could not find the specified field " + targetName);
        }
        if (sortField == null) {
            throw new FunctionException("Could not find the specified field " + findName);
        }
        String processName = minusQuotes(getParameter(1)).toString();
        ProcessCalculationCache processCalculationCache = (ProcessCalculationCache) calculationMetadata.getCache(new ProcessCacheBuilder(instanceIDField, null), processName);
        Value instanceValue = getParameter(0);
        List<IRow> rows = processCalculationCache.rowsForValue(instanceValue);
        if (rows == null || rows.size() == 0) {
            return new EmptyValue();
        }
        for (IRow row : rows) {
            Value findValue = row.getValue(sortField);
            if (findValue.toString().equals(lookingForValue)) {
                return row.getValue(targetField);
            }
        }
        return new EmptyValue();
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return 5;
    }
}
