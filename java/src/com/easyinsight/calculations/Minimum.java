package com.easyinsight.calculations;

import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Sep 24, 2009
 * Time: 10:57:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class Minimum extends Function {
    public Value evaluate() {
        Double min = null;
        for(Value v : params) {
            if(min == null || v.toDouble() < min) {
                min = v.toDouble();
            }
        }
        return new NumericValue(min);
    }
}
