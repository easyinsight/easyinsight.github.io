package com.easyinsight.analysis;

import com.easyinsight.core.XMLMetadata;
import com.easyinsight.pipeline.FirstValueComponent;
import com.easyinsight.pipeline.IComponent;
import nu.xom.Element;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: jamesboe
 * Date: Sep 12, 2010
 * Time: 5:09:44 PM
 */
@Entity
@Table(name="first_value_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class FirstValueFilter extends FilterDefinition {

    @Column(name="apply_across_report")
    private boolean absolute;

    @Column(name="threshold")
    private int threshold;

    @Override
    public int type() {
        return FilterDefinition.FIRST_VALUE;
    }

    @Override
    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedFirstValueFilter(getField());
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

    @Override
    public String toQuerySQL(String tableName) {
        return null;
    }

    @Override
    public boolean validForQuery() {
        return false;
    }

    @Override
    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        return start;
    }

    public List<IComponent> createComponents(String pipelineName, IFilterProcessor filterProcessor, AnalysisItem sourceItem, boolean columnLevel) {
        if (isEnabled() && pipelineName.equals(getPipelineName())) {
            return Arrays.asList((IComponent) new FirstValueComponent(this, sourceItem));
        } else {
            return new ArrayList<IComponent>();
        }
    }

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = super.toXML(xmlMetadata);
        return element;
    }
}
