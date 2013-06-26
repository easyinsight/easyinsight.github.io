package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.UniqueKey;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 3/19/12
 * Time: 4:52 PM
 */
public class AssignUniqueField extends Function {
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
        String source = minusQuotes(getParameter(1)).toString();
        AnalysisItem match = null;
        for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
            if (field.equals(analysisItem.toDisplay())) {
                match = analysisItem;
            }
        }
        if (match == null) {
            throw new FunctionException("Could not find field " + field + ".");
        }
        UniqueKey id;
        try {
            id = findID(report.getDataFeedID(), source,  calculationMetadata.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (id == null) {
            throw new FunctionException("Could not find data source " + source + ".");
        }
        //long id = toID(match.getKey());
        if (report.getFieldToUniqueMap() == null) {
            report.setFieldToUniqueMap(new HashMap<String, UniqueKey>());
        }
        report.getFieldToUniqueMap().put(field, id);
        return null;
    }

    private UniqueKey findID(long dataSourceID, String name, EIConnection conn) throws SQLException {
        PreparedStatement getCompStmt = conn.prepareStatement("SELECT COMPOSITE_FEED_ID FROM COMPOSITE_FEED WHERE DATA_FEED_ID = ?");
        getCompStmt.setLong(1, dataSourceID);
        ResultSet compRS = getCompStmt.executeQuery();
        long compID = 0;
        if (compRS.next()) {
            compID = compRS.getLong(1);
        }
        getCompStmt.close();
        if (compID > 0) {
            PreparedStatement stmt = conn.prepareStatement("SELECT COMPOSITE_NODE.DATA_FEED_ID, FEED_NAME FROM COMPOSITE_NODE, DATA_FEED WHERE " +
                    "COMPOSITE_NODE.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID AND COMPOSITE_NODE.COMPOSITE_FEED_ID = ?");
            stmt.setLong(1, compID);
            ResultSet rs = stmt.executeQuery();
            Set<Long> ids = new HashSet<Long>();
            while (rs.next()) {
                long childID = rs.getLong(1);
                ids.add(childID);
                String childName = rs.getString(2);
                if (childName.equals(name)) {
                    return new UniqueKey(childID, UniqueKey.DERIVED);
                }
            }
            stmt.close();
            for (Long id : ids) {
                UniqueKey match = findID(id, name, conn);
                if (match != null) {
                    return match;
                }
            }
        }
        return null;
    }

    public int getParameterCount() {
        return 2;
    }

    @Override
    public boolean onDemand() {
        return true;
    }
}
