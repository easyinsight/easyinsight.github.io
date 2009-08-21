package com.easyinsight.analysis;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: Aug 5, 2009
 * Time: 12:22:40 PM
 */
public class LastValueFilter extends FilterDefinition {

    public LastValueFilter(AnalysisItem key) {
        super(key);
    }

    public LastValueFilter() {
    }

    public PersistableFilterDefinition toPersistableFilterDefinition() {
        PersistableLastValueFilter date = new PersistableLastValueFilter();
        date.setEnabled(isEnabled());
        date.setFilterId(getFilterID());
        date.setApplyBeforeAggregation(isApplyBeforeAggregation());
        date.setField(getField());
        date.setIntrinsic(isIntrinsic());
        return date;
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedLastValueFilter(getField());
    }

    public String toQuerySQL(String tableName) {
        return null;
    }

    public boolean validForQuery() {
        return false;
    }

    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        return start;
    }
}
