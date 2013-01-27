package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.PostProcessOperation;
import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.calculations.PostProcessCalculationMetadata;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.CompositeFeedConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 12/17/12
 * Time: 12:57 PM
 */
public class LoadFromJoin extends Function {
    public Value evaluate() {
        // post processing, you have field X (you're loading into), field Y (you're loading from), and connection C (what connection to use)
        // loadfromjoin([Comment Author], [])
        if (!(calculationMetadata instanceof  PostProcessCalculationMetadata)) {
            IRow row = getRow();
            return row.getValue(getAnalysisItem().createAggregateKey());
        }

        PostProcessCalculationMetadata metadata = (PostProcessCalculationMetadata) calculationMetadata;
        PostProcessOperation op = metadata.getOp();
        String field = minusBrackets(getParameterName(0));
        String joinSourceName = minusBrackets(getParameterName(2));
        String joinTargetName = minusBrackets(getParameterName(3));
        AnalysisItem sourceItem = null;
        AnalysisItem joinSourceItem = null;
        AnalysisItem joinTargetItem = null;
        for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
            if (field.equals(analysisItem.toDisplay())) {
                sourceItem = analysisItem;
            } else if (joinSourceName.equals(analysisItem.toDisplay())) {
                joinSourceItem = analysisItem;
            } else if (joinTargetName.equals(analysisItem.toDisplay())) {
                joinTargetItem = analysisItem;
            }
        }
        if (sourceItem == null) {
            throw new FunctionException("Could not find field " + field + ".");
        }
        if (joinSourceItem == null) {
            throw new FunctionException("Could not find field " + joinSourceName + ".");
        }
        if (joinTargetItem == null) {
            throw new FunctionException("Could not find field " + joinTargetName + ".");
        }
        op.setFromField(sourceItem);
        String source = minusQuotes(getParameter(1)).toString();
        Long id;
        try {
            id = findID(metadata.getDataSourceID(), source,  calculationMetadata.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DerivedKey derivedKey = (DerivedKey) sourceItem.getKey();
        CompositeFeedConnection connection = new CompositeFeedConnection(id, derivedKey.getFeedID(),
                joinSourceItem, joinTargetItem, null, null, false, false, false, false);
        op.setConnection(connection);
        return new EmptyValue();
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    private Long findID(long dataSourceID, String name, EIConnection conn) throws SQLException {
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
                    return childID;
                }
            }
            stmt.close();
            for (Long id : ids) {
                Long match = findID(id, name, conn);
                if (match != null) {
                    return match;
                }
            }
        }
        return null;
    }

    public int getParameterCount() {
        return 4;
    }
}
