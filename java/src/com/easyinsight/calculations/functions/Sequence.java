package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.IRow;
import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.calculations.ProcessCacheBuilder;
import com.easyinsight.calculations.ProcessCalculationCache;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

import java.util.List;

/**
 * User: jamesboe
 * Date: 9/5/12
 * Time: 7:58 PM
 */
public class Sequence extends Function {

    public Value evaluate() {
        // categorizing step, cycle time,

        // process()

        // next([Round ID], "", "")

        // next([Round ID], [Round Date], "filter1") )
        String instanceIDName = minusBrackets(getParameterName(0));
        String targetName = minusBrackets(getParameterName(3));
        String sortName = minusBrackets(getParameterName(2));
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
            throw new FunctionException("Could not find the specified field " + sortName);
        }
        String processName = minusQuotes(getParameter(1)).toString();
        ProcessCalculationCache processCalculationCache = (ProcessCalculationCache) calculationMetadata.getCache(new ProcessCacheBuilder(instanceIDField, sortField), processName);
        Value instanceValue = getParameter(0);
        Value sortValue = getParameter(2);
        List<IRow> rows = processCalculationCache.rowsForValue(instanceValue);
        if (rows == null || rows.size() == 0) {
            return new EmptyValue();
        }
        for (int i = 0; i < rows.size(); i++) {
            IRow row = rows.get(i);
            if (row == getRow()) {
                if (i == 0) {
                    // we're the first row, nothing to do
                    return new EmptyValue();
                } else {
                    Value value = row.getValue(targetField);
                    if (value.type() == Value.DATE) {
                        DateValue dateValue = (DateValue) value;
                        for (int j = i - 1; j >= 0; j--) {
                            IRow previousRow = rows.get(j);
                            Value previousValue = previousRow.getValue(targetField);
                            if (value.type() == Value.DATE && previousValue.type() == Value.DATE) {

                                DateValue previousDateValue = (DateValue) previousValue;
                                if (!dateValue.getDate().equals(previousDateValue.getDate())) {
                                    return new NumericValue(dateValue.getDate().getTime() - previousDateValue.getDate().getTime());
                                }
                            }
                        }
                    }
                    return new EmptyValue();
                }
            }
        }
        return new EmptyValue();
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return 4;
    }
}
