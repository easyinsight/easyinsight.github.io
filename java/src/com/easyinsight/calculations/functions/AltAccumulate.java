package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.calculations.AltCumulativeCacheBuilder;
import com.easyinsight.calculations.FillCacheBuilder;
import com.easyinsight.calculations.Function;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 5/6/15
 * Time: 3:25 PM
 */
public class AltAccumulate extends Function {
    @Override
    public Value evaluate() {
        // altaccumulate(date)
        AnalysisItem dateField = findDataSourceItem(0);

        WSAnalysisDefinition report = calculationMetadata.getReport();
        calculationMetadata.getCache(new AltCumulativeCacheBuilder(dateField, report), "x");
        Value value = getParameter(0);
        return value;
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
