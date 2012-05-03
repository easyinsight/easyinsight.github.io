package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Value;

import java.util.HashMap;

/**
 * User: jamesboe
 * Date: 3/19/12
 * Time: 4:52 PM
 */
public class AssignUniqueField extends Function {
    public Value evaluate() {
        WSAnalysisDefinition report = calculationMetadata.getReport();
        String source = minusQuotes(0);
        String field = minusQuotes(1);
        AnalysisItem match = null;
        for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
            if (field.equals(analysisItem.toDisplay())) {
                match = analysisItem;
            }
        }
        if (match == null) {
            throw new FunctionException("Could not find field " + field + ".");
        }
        long id = toID(match.getKey());
        if (report.getFieldToUniqueMap() == null) {
            report.setFieldToUniqueMap(new HashMap<String, AnalysisItem>());
        }
        report.getFieldToUniqueMap().put(source, match);
        return null;
    }

    private long toID(Key key) {
        if (key instanceof DerivedKey) {
            DerivedKey derivedKey = (DerivedKey) key;
            Key next = derivedKey.getParentKey();
            if (next instanceof NamedKey) {
                return derivedKey.getFeedID();
            }
            return toID(next);
        }
        return 0;
    }

    public int getParameterCount() {
        return 2;
    }
}
