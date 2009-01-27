package com.easyinsight.calculations;

import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;

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
}
