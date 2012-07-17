package com.easyinsight.analysis;

import com.easyinsight.core.XMLMetadata;
import com.easyinsight.logging.LogClass;
import nu.xom.Element;
import org.hibernate.Session;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 2:23:40 PM
 */
@Entity
@Table(name="range_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class FilterRangeDefinition extends FilterDefinition {

    public static final int LESS_THAN = 1;
    public static final int LESS_THAN_EQUAL_TO = 2;

    @Column(name="low_value")
    private double startValue;
    @Column(name="low_value_defined")
    private boolean startValueDefined;
    @Column(name="high_value")
    private double endValue;
    @Column(name="high_value_defined")
    private boolean endValueDefined;
    @Column(name="current_low_value")
    private double currentStartValue;
    @Column(name="current_high_value")
    private double currentEndValue;
    @Column(name="current_low_value_defined")
    private boolean currentStartValueDefined;
    @Column(name="current_high_value_defined")
    private boolean currentEndValueDefined;
    @Column(name="lower_operator")
    private int lowerOperator;
    @Column(name="upper_operator")
    private int upperOperator;

    public FilterRangeDefinition() {
        setApplyBeforeAggregation(false);
    }

    @Override
    public void beforeSave(Session session) {
        super.beforeSave(session);
        if (Double.isNaN(startValue)) startValue = 0;
        if (Double.isNaN(endValue)) endValue = 0;
        if (Double.isNaN(currentStartValue)) currentStartValue = 0;
        if (Double.isNaN(currentEndValue)) currentEndValue = 0;
    }

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = super.toXML(xmlMetadata);
        return element;
    }

    public boolean isCurrentStartValueDefined() {
        return currentStartValueDefined;
    }

    public void setCurrentStartValueDefined(boolean currentStartValueDefined) {
        this.currentStartValueDefined = currentStartValueDefined;
    }

    public boolean isCurrentEndValueDefined() {
        return currentEndValueDefined;
    }

    public void setCurrentEndValueDefined(boolean currentEndValueDefined) {
        this.currentEndValueDefined = currentEndValueDefined;
    }

    public double getCurrentStartValue() {
        return currentStartValue;
    }

    public void setCurrentStartValue(double currentStartValue) {
        this.currentStartValue = currentStartValue;
    }

    public double getCurrentEndValue() {
        return currentEndValue;
    }

    public void setCurrentEndValue(double currentEndValue) {
        this.currentEndValue = currentEndValue;
    }

    public boolean isStartValueDefined() {
        return startValueDefined;
    }

    public void setStartValueDefined(boolean startValueDefined) {
        this.startValueDefined = startValueDefined;
    }

    public boolean isEndValueDefined() {
        return endValueDefined;
    }

    public void setEndValueDefined(boolean endValueDefined) {
        this.endValueDefined = endValueDefined;
    }

    public double getStartValue() {
        return startValue;
    }

    public void setStartValue(double startValue) {
        this.startValue = startValue;
    }

    public double getEndValue() {
        return endValue;
    }

    public void setEndValue(double endValue) {
        this.endValue = endValue;
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedFilterRangeDefinition(getField(), startValueDefined() ? startValue() : null, endValueDefined() ? endValue() : null, getLowerOperator(), getUpperOperator());
    }

    private boolean startValueDefined() {
        return currentStartValueDefined || startValueDefined;
    }

    private boolean endValueDefined() {
        return currentEndValueDefined || endValueDefined;
    }

    public int getLowerOperator() {
        return lowerOperator;
    }

    public void setLowerOperator(int lowerOperator) {
        this.lowerOperator = lowerOperator;
    }

    public int getUpperOperator() {
        return upperOperator;
    }

    public void setUpperOperator(int upperOperator) {
        this.upperOperator = upperOperator;
    }

    private double startValue() {
        if (currentStartValueDefined) {
            return currentStartValue;
        } else {
            return startValue;
        }
    }

    private double endValue() {
        if (currentEndValueDefined) {
            return currentEndValue;
        } else {
            return endValue;
        }
    }

    public String toQuerySQL(String tableName) {
        String lowerOperator = (getLowerOperator() == LESS_THAN) ? ">" : ">=";
        String upperOperator = (getUpperOperator() == LESS_THAN) ? "<" : "<=";
        StringBuilder queryBuilder = new StringBuilder();
        String columnName = getField().toKeySQL();
        queryBuilder.append(columnName);
        if (startValueDefined() && endValueDefined()) {
            queryBuilder.append(" ");
            queryBuilder.append(lowerOperator);
            queryBuilder.append(" ? AND ");
            queryBuilder.append(columnName);
            queryBuilder.append(" ");
            queryBuilder.append(upperOperator);
            queryBuilder.append(" ?");
        } else if (startValueDefined()) {
            queryBuilder.append(" ");
            queryBuilder.append(lowerOperator);
            queryBuilder.append(" ?");
        } else if (endValueDefined()) {
            queryBuilder.append(" ");
            queryBuilder.append(upperOperator);
            queryBuilder.append(" ?");

        }
        return queryBuilder.toString();
    }

    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        if (startValueDefined()) {
            preparedStatement.setDouble(start++, startValue());
        }
        if (endValueDefined()) {
            preparedStatement.setDouble(start++, endValue());
        }
        return start;
    }
}
