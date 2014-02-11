package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.*;

/**
 * User: jamesboe
 * Date: 2/11/14
 * Time: 3:11 PM
 */
public class StringFormat extends Function {

    public Value evaluate() {
        try {
            int number = Integer.parseInt(params.get(0).toString());
            int digits = params.get(1).toDouble().intValue();
            return new StringValue(String.format("%0"+digits +"d", number));
        } catch (Exception e) {
            return new EmptyValue();
        }
    }

    public int getParameterCount() {
        return 2;
    }
}
