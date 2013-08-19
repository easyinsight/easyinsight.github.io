package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 8/19/13
 * Time: 11:40 AM
 */
public class OverrideFilter extends Function {
    public Value evaluate() {
        String filterName = minusQuotes(0);
        calculationMetadata.getInsightRequestMetadata().getFilterOverrideMap().put(filterName, true);
        return new EmptyValue();
    }

    public int getParameterCount() {
        return 1;
    }
}
