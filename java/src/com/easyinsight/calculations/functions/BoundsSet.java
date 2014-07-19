package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.definitions.WSMap;
import com.easyinsight.calculations.Function;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 7/19/14
 * Time: 12:06 PM
 */
public class BoundsSet extends Function {
    @Override
    public Value evaluate() {
        String string = minusQuotes(0);
        String[] parts = string.split("\\,");
        WSMap map = (WSMap) calculationMetadata.getReport();
        map.setBoundSet(parts);
        return null;
    }

    @Override
    public int getParameterCount() {
        return 1;
    }
}
