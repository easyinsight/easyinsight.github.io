package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.TextValueExtension;
import com.easyinsight.calculations.Function;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 10/6/11
 * Time: 12:01 PM
 */
public class ColorTextAll extends Function {
    public Value evaluate() {
        IRow row = getRow();
        String colorString = minusQuotes(getParameter(0)).toString();
        int color = Integer.parseInt(colorString, 16);
        for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
            if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                Value target = row.getValue(analysisItem);
                if (target.type() == Value.NUMBER) {
                    if (target.toDouble() < 0) {
                        TextValueExtension textValueExtension = (TextValueExtension) target.getValueExtension();
                        if (textValueExtension == null) {
                            textValueExtension = new TextValueExtension();
                            target.setValueExtension(textValueExtension);
                        }
                        textValueExtension.setColor(color);
                    }
                }
            }
        }
        return new EmptyValue();
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return 1;
    }
}
