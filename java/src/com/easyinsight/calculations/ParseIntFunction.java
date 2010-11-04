package com.easyinsight.calculations;

import com.easyinsight.analysis.FunctionExplanation;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: Jul 12, 2010
 * Time: 8:32:10 PM
 */
public class ParseIntFunction extends Function {
    public Value evaluate() {
        Value value = params.get(0);
        return new NumericValue(value.toDouble());
    }

    public int getParameterCount() {
        return -1;
    }
    
    public FunctionExplanation explain() {
        return new FunctionExplanation("", "");
    }
}
