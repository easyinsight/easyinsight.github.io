package com.easyinsight.calculations;

import com.easyinsight.analysis.FunctionExplanation;
import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 8:57:49 PM
 */
public class NaturalLog extends Function {

    public Value evaluate() {
        return new NumericValue(Math.log(params.get(0).toDouble()));
    }

    public FunctionExplanation explain() {
        return new FunctionExplanation("ln(value)", "Calculate the natural logarithm of value", Arrays.asList("Value - what you want the ln of"));
    }
}
