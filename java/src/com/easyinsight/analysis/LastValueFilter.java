package com.easyinsight.analysis;

import com.easyinsight.pipeline.IComponent;
import com.easyinsight.pipeline.LastValueComponent;

import javax.persistence.Column;
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

    @Column(name="apply_across_report")
    private boolean absolute;

    @Column(name="threshold")
    private int threshold;

    public LastValueFilter(AnalysisItem key) {
        super(key);
    }

    public LastValueFilter() {
    }

    public boolean isAbsolute() {
        return absolute;
    }

    public void setAbsolute(boolean absolute) {
        this.absolute = absolute;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
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

    public List<IComponent> createComponents(boolean beforeAggregation, IFilterProcessor filterProcessor, AnalysisItem sourceItem, boolean columnLevel) {
        if (isEnabled() && beforeAggregation == isApplyBeforeAggregation()) {
            return Arrays.asList((IComponent) new LastValueComponent(this));
        } else {
            return new ArrayList<IComponent>();
        }
    }

    @Override
    public String toXML() {
        String xml = "<lastValueFilter>";
        xml += getField().toXML();
        xml += "</lastValueFilter>";
        return xml;
    }
}
