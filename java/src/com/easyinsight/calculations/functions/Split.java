package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 9/26/14
 * Time: 6:25 AM
 */
public class Split extends Function {
    @Override
    public Value evaluate() {
        try {
            String baseValue = params.get(0).toString();
            String delimiter = minusQuotes(1);
            String[] tokens = baseValue.split(delimiter);
            int tokenIndex = params.get(2).toDouble().intValue();
            return new com.easyinsight.core.StringValue(tokens[tokenIndex]);
        } catch (Exception e) {
            return new EmptyValue();
        }
    }

    @Override
    public int getParameterCount() {
        return 3;
    }
}
