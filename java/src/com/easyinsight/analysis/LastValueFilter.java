package com.easyinsight.analysis;

import com.easyinsight.pipeline.FirstValueComponent;
import com.easyinsight.pipeline.IComponent;
import com.easyinsight.pipeline.LastValueComponent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: jamesboe
 * Date: Aug 5, 2009
 * Time: 12:22:40 PM
 */
@Entity
@Table(name="last_value_filter")
@PrimaryKeyJoinColumn(name="filter_id")
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

    public List<IComponent> createComponents(boolean beforeAggregation, IFilterProcessor filterProcessor) {
        if (isEnabled() && beforeAggregation == isApplyBeforeAggregation()) {
            return Arrays.asList((IComponent) new LastValueComponent(this));
        } else {
            return new ArrayList<IComponent>();
        }
    }
}
