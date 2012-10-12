package com.easyinsight.analysis;

import com.easyinsight.core.XMLMetadata;
import nu.xom.Element;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    public int type() {
        return FilterDefinition.NULL;
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
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = super.toXML(xmlMetadata);
        return element;
    }

    @Override
    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        return start;
    }
}