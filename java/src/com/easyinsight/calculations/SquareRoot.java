package com.easyinsight.calculations;

import com.easyinsight.analysis.FunctionExplanation;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 8:57:49 PM
 */
public class SquareRoot extends Function {

    public Value evaluate() {
        return new NumericValue(Math.sqrt(params.get(0).toDouble()));
    }

    public FunctionExplanation explain() {
        return new FunctionExplanation("sqrt(Number)", "Calculate the square root of Number", Arrays.asList("Number - what you want the square root of"));
    }
}