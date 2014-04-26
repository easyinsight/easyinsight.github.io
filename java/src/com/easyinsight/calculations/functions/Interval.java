package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.DerivedAnalysisDateDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.IntervalCacheBuilder;
import com.easyinsight.calculations.IntervalCalculationCache;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

import java.util.Date;

/**
 * User: jamesboe
 * Date: 4/18/14
 * Time: 7:04 PM
 */
public class Interval extends Function {
    public Value evaluate() {

        // interval([Comparison Date], "Deal Interval", "Deals Won")

        AnalysisItem dateField = findDataSourceItem(0);

        String string = minusQuotes(getParameter(1)).toString();
        String measureString = minusQuotes(getParameter(2)).toString();

        DerivedAnalysisDateDimension derivedAnalysisDimension = null;
        AnalysisMeasure measureField = null;
        for (AnalysisItem field : calculationMetadata.getDataSourceFields()) {
            if (string.equals(field.toDisplay())) {
                derivedAnalysisDimension = (DerivedAnalysisDateDimension) field;
            } else if (measureString.equals(field.toDisplay())) {
                measureField = (AnalysisMeasure) field;
            }
        }

        calculationMetadata.getCache(
                new IntervalCacheBuilder(getAnalysisItem(), dateField, measureField, derivedAnalysisDimension, calculationMetadata), "xyz");
        return getRow().getValue(getAnalysisItem().createAggregateKey());
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return 3;
    }
}
