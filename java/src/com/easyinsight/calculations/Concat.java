package com.easyinsight.calculations;

import com.easyinsight.analysis.FunctionExplanation;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 9:31:22 PM
 */
public class Concat extends Function {
    public Value evaluate() {
        StringBuilder sb = new StringBuilder();
        for(Value v : params) {
            sb.append(v.toString());
        }
        return new StringValue(sb.toString());
    }

    public FunctionExplanation explain() {
        return new FunctionExplanation("nconcat(value...)", "Combines the numeric parameters passed in");
    }
}