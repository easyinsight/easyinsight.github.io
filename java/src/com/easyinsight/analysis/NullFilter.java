package com.easyinsight.analysis;

import com.easyinsight.pipeline.FirstValueComponent;
import com.easyinsight.pipeline.IComponent;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * User: jamesboe
 * Date: Sep 12, 2010
 * Time: 5:09:44 PM
 */
@Entity
@Table(name="null_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class NullFilter extends FilterDefinition {
    @Override
    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedNullFilter(getField());
    }

    @Override
    public String toQuerySQL(String tableName) {
        return null;
    }

    @Override
    public boolean validForQuery() {
        return false;
    }

    @Override
    public String toXML() {
        String xml = "<nullFilter>";
        xml += getField().toXML();
        xml += "</nullFilter>";
        return xml;
    }

    @Override
    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        return start;
    }
}