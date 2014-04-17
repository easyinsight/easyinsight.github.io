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
public class NthRecord extends Function {

    public Value evaluate() {
        // categorizing step, cycle time,

        // process()

        // next([Round ID], "", "")

        // next([Round ID], [Round Date], "filter1") )

        String instanceIDName = minusBrackets(getParameterName(1));
        String targetName = minusBrackets(getParameterName(4));
        String sortName = minusBrackets(getParameterName(3));
        AnalysisItem instanceIDField = findDataSourceItem(1);
        AnalysisItem targetField = findDataSourceItem(4);
        AnalysisItem sortField = findDataSourceItem(3);
        if (instanceIDField == null) {
            throw new FunctionException("Could not find the specified field " + instanceIDName);
        }
        if (targetField == null) {
            throw new FunctionException("Could not find the specified field " + targetName);
        }
        if (sortField == null) {
            throw new FunctionException("Could not find the specified field " + sortName);
        }
        String processName = minusQuotes(getParameter(1)).toString();
        ProcessCalculationCache processCalculationCache = (ProcessCalculationCache) calculationMetadata.getCache(new ProcessCacheBuilder(instanceIDField, sortField), processName);
        Value instanceValue = getParameter(1);
        int n = (int) Math.round(getParameter(0).toDouble());
        List<IRow> rows = processCalculationCache.rowsForValue(instanceValue);
        if (rows == null) {
            return new EmptyValue();
        }
        if(n < 0) {
            n = -n;
            if(rows.size() < n) {
                return new EmptyValue();
            }
            return rows.get(rows.size() - n).getValue(targetField);
        } else {
            if (rows.size() < n) {
                return new EmptyValue();
            }
            return rows.get(n - 1).getValue(targetField);
        }
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return 5;
    }
}
