package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.definitions.WSVerticalListDefinition;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

import java.util.Iterator;

/**
 * User: jamesboe
 * Date: 9/17/11
 * Time: 3:42 PM
 */
public class RemoveField extends Function {
    public Value evaluate() {
        if (calculationMetadata.getReport() != null) {
            WSVerticalListDefinition verticalListDefinition = (WSVerticalListDefinition) calculationMetadata.getReport();
            String fieldName = minusQuotes(params.get(0)).toString().toLowerCase();
            Iterator<AnalysisItem> iter = verticalListDefinition.getMeasures().iterator();
            while (iter.hasNext()) {
                AnalysisItem item = iter.next();
                if (item.toDisplay().toLowerCase().equals(fieldName)) {
                    iter.remove();
                }
            }
        }
        return new EmptyValue();
    }

    public int getParameterCount() {
        return -1;
    }
}
