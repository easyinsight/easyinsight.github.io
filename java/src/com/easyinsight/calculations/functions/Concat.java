package com.easyinsight.calculations.functions;


import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.calculations.SimpleCacheBuilder;
import com.easyinsight.calculations.SimpleCalculationCache;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;
import com.easyinsight.servlet.SystemSettings;

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
        AnalysisItem instanceIDField = findDataSourceItem(0);
        AnalysisItem targetField = findDataSourceItem(1);
        if (instanceIDField == null) {
            throw new FunctionException("Could not find the specified field " + instanceIDName);
        }
        if (targetField == null) {
            throw new FunctionException("Could not find the specified field " + targetName);
        }
        String processName = getAnalysisItem().qualifiedName();
        SimpleCalculationCache simpleCache = (SimpleCalculationCache) calculationMetadata.getCache(new SimpleCacheBuilder(instanceIDField), processName);
        Value instanceValue = getParameter(0);
        if (instanceValue.type() == Value.EMPTY) {
            return new EmptyValue();
        }
        if (calculationMetadata.getDataSet().getRows().size() > SystemSettings.instance().getSequenceLimit()) {
            throw new FunctionException("Concat complexity exceeds limits.");
        }
        List<IRow> rows = simpleCache.rowsForValue(instanceValue);

        System.out.println("data set size = " + calculationMetadata.getDataSet().getRows().size() + " for " + instanceIDName + " and " + instanceValue);
        if (rows != null) {
            System.out.println("merging with " + rows.size());
            StringBuilder sb = new StringBuilder();
            for (IRow row : rows) {
                Value value = row.getValue(targetField);
                if (value.type() == Value.EMPTY) {
                    continue;
                }
                String str = value.toString();
                if ("".equals(str)) {
                    continue;
                }
                sb.append(str).append(", ");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
                sb.deleteCharAt(sb.length() - 1);
            }
            return new StringValue(sb.toString());
        } else {
            return new EmptyValue();
        }
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return 2;
    }
}