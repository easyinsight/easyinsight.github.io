package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.TextValueExtension;
import com.easyinsight.calculations.Function;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 4/3/14
 * Time: 2:45 PM
 */
public class AssignCurrency extends Function {
    public Value evaluate() {
        Value value = params.get(0);
        Value currency = params.get(1);
        TextValueExtension textValueExtension = (TextValueExtension) value.getValueExtension();
        if (textValueExtension == null) {
            textValueExtension = new TextValueExtension();
            value.setValueExtension(textValueExtension);
        }
        if ("USD".equals(currency.toString())) {
            textValueExtension.setCurrency(TextValueExtension.USD);
            textValueExtension.setCurrencyString("$");
        } else if ("EUR".equals(currency.toString())) {
            textValueExtension.setCurrency(TextValueExtension.EURO);
            textValueExtension.setCurrencyString("\u20AC");
        } else if ("GBP".equals(currency.toString())) {
            textValueExtension.setCurrency(TextValueExtension.GBP);
            textValueExtension.setCurrencyString("\u00A3");
        } else {

        }
        return null;
    }

    public int getParameterCount() {
        return 2;
    }
}
