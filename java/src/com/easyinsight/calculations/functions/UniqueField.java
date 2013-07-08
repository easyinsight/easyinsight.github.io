package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.UniqueKey;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.core.*;

import java.util.HashMap;

/**
 * User: jamesboe
 * Date: 3/19/12
 * Time: 4:52 PM
 */
public class UniqueField extends Function {
    public Value evaluate() {
        WSAnalysisDefinition report = calculationMetadata.getReport();
        String field;
        try {
            Value param = getParameter(0);
            if (param.type() != Value.EMPTY) {
                field = minusQuotes(param).toString();
            } else {
                field = minusBrackets(getParameterName(0));
            }
        } catch (Exception e) {
            field = minusBrackets(getParameterName(0));
        }

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
        UniqueKey id = toID(match.getKey());
        if (report.getUniqueIteMap() == null) {
            report.setUniqueIteMap(new HashMap<UniqueKey, AnalysisItem>());
        }
        report.getUniqueIteMap().put(id, clone);
        return null;
    }

    private UniqueKey toID(Key key) {
        if (key instanceof DerivedKey) {
            DerivedKey derivedKey = (DerivedKey) key;
            Key next = derivedKey.getParentKey();
            if (next instanceof NamedKey) {
                return new UniqueKey(derivedKey.getFeedID(), UniqueKey.DERIVED);
            }
            return toID(next);
        } else if (key instanceof ReportKey) {
            ReportKey reportKey = (ReportKey) key;
            return new UniqueKey(reportKey.getReportID(), UniqueKey.REPORT);
        }
        return null;
    }

    public int getParameterCount() {
        return 1;
    }

    @Override
    public boolean onDemand() {
        return true;
    }
}
