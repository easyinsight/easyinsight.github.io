package com.easyinsight.calculations;

import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 8/26/11
 * Time: 12:38 PM
 */
public class FirstValueFunction extends Function {
    public Value evaluate() {
        for(Value v : params) {
            if (v.type() != Value.EMPTY && !"".equals(v.toString().trim())) {
                return v;
            }
        }
        return EmptyValue.EMPTY_VALUE;
    }

    public int getParameterCount() {
        return -1;
    }
}
