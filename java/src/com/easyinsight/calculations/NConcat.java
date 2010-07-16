package com.easyinsight.calculations;

import com.easyinsight.analysis.FunctionExplanation;
import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 9:31:22 PM
 */
public class NConcat extends Function {
    public Value evaluate() {
        StringBuilder sb = new StringBuilder();
        for(Value v : params) {
            sb.append(v.toDouble().intValue());
        }
        return new NumericValue(Double.parseDouble(sb.toString()));
    }

    public FunctionExplanation explain() {
        return new FunctionExplanation("nconcat(value...)", "Combines the numeric parameters passed in");
    }
}
