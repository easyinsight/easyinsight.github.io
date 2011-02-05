package com.easyinsight.calculations;


import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 8:57:49 PM
 */
public class Sine extends Function {

    public Value evaluate() {
        return new NumericValue(Math.sin(params.get(0).toDouble()));
    }

    public int getParameterCount() {
        return 1;
    }
}