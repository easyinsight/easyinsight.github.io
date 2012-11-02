package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.Value;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 10/23/12
 * Time: 9:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class IfFunction extends Function {
    public Value evaluate() {

        if(params.get(0).toDouble() > 0)
            return minusQuotes(params.get(1));
        else
            return minusQuotes(params.get(2));
    }

    public int getParameterCount() {
        return 3;
    }
}