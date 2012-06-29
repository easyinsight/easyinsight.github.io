package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * User: jamesboe
 * Date: 3/19/12
 * Time: 4:52 PM
 */
public class UniqueField extends Function {
    public Value evaluate() {
        WSAnalysisDefinition report = calculationMetadata.getReport();
        String field = minusQuotes(0);
        AnalysisItem match = null;
        for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
            if (field.equals(analysisItem.toDisplay())) {
                match = analysisItem;
            }
        }
        if (match == null) {
            throw new FunctionException("Could not find field " + field + ".");
        }
        AnalysisItem clone;
        try {
            clone = match.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        long id = toID(match.getKey());
        if (report.getUniqueIteMap() == null) {
            report.setUniqueIteMap(new HashMap<Long, AnalysisItem>());
        }
        report.getUniqueIteMap().put(id, clone);
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
        return 1;
    }
}
