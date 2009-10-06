package com.easyinsight.analysis;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: Aug 5, 2009
 * Time: 12:22:40 PM
 */
@Entity
@Table(name="last_value_filter")
public class LastValueFilter extends FilterDefinition {

    public LastValueFilter(AnalysisItem key) {
        super(key);
    }

    public LastValueFilter() {
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
