package com.easyinsight.calculations;

import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Sep 24, 2009
 * Time: 10:55:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class AbsoluteValue extends Function {
    public Value evaluate() {
        return new NumericValue(Math.abs(this.params.get(0).toDouble()));
    }
}
