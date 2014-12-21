package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.calculations.Function;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 12/21/14
 * Time: 10:26 AM
 */
public class Sum extends Function {
    @Override
    public Value evaluate() {
        AnalysisItem field = findDataSourceItem(0);

        double sum = 0;
        for (IRow row : calculationMetadata.getDataSet().getRows()) {
            Value value = row.getValue(field);
            sum += value.toDouble();
        }

        return new NumericValue(sum);
    }

    @Override
    public int getParameterCount() {
        return 1;
    }

    @Override
    public boolean onDemand() {
        return true;
    }
}
