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
public class Floor extends Function {

    public Value evaluate() {
        return new NumericValue(Math.floor(params.get(0).toDouble()));
    }

    public FunctionExplanation explain() {
        return new FunctionExplanation("floor(Number)", "Calculate the floor of Number", Arrays.asList("Number - what you want the floor of"));
    }
}