package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

import java.util.List;

/**
 * User: jamesboe
 * Date: 9/5/12
 * Time: 7:58 PM
 */
public class PreviousRecord extends Function {

    public Value evaluate() {
        // categorizing step, cycle time,

        // process()

        // next([Round ID], "", "")

        // next([Round ID], [Round Date], "filter1") )
        String instanceIDName = minusBrackets(getParameterName(0));
        String targetName = minusBrackets(getParameterName(3));
        String sortName = minusBrackets(getParameterName(2));
        AnalysisItem instanceIDField = null;
        AnalysisItem targetField = null;
        AnalysisItem sortField = null;
        for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
            if (instanceIDName.equals(analysisItem.toDisplay()) || instanceIDName.equals(analysisItem.getKey().toKeyString())) {
                instanceIDField = analysisItem;
            }
            if (targetName.equals(analysisItem.toDisplay()) || targetName.equals(analysisItem.getKey().toKeyString())) {
                targetField = analysisItem;
            }
            if (sortName.equals(analysisItem.toDisplay()) || sortName.equals(analysisItem.getKey().toKeyString())) {
                sortField = analysisItem;
            }
        }
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
        List<IRow> rows = processCalculationCache.rowsForValue(instanceValue);
        for (int i = 0; i < rows.size(); i++) {
            IRow row = rows.get(i);
            if (row == getRow()) {
                if ((i - 1) > 0) {
                    IRow nextRow = rows.get(i - 1);
                    return nextRow.getValue(targetField);
                } else {
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
