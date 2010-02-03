package com.easyinsight.analysis;

import com.easyinsight.core.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Feb 29, 2008
 * Time: 10:52:57 AM
 */
@Entity
@Table(name="analysis_range")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class AnalysisRangeDimension extends AnalysisDimension {
    private static final int THRESHOLD = 10;
    private transient Range range;

    

    public AnalysisRangeDimension(Key key, boolean group) {
        super(key, group);
    }

    public AnalysisRangeDimension() {
    }

    public int getType() {
        return super.getType() | AnalysisItemTypes.RANGE_DIMENSION;
    }

    public Value transformValue(Value value, InsightRequestMetadata insightRequestMetadata) {
        Value transformedValue;
        try {
            Double doubleValue = value.toDouble();
            transformedValue = new StringValue(range.getRange(doubleValue));
        } catch (NumberFormatException e) {
            transformedValue = new EmptyValue();
        }
        return transformedValue;
    }

    public boolean requiresDataEarly() {
        return true;
    }

    public void handleEarlyData(List<IRow> rows) {
        Collection<Double> doubleValues = new ArrayList<Double>();
        for (IRow row : rows) {
            Value value = row.getValue(this);
            Double doubleValue = value.toDouble();
            if (doubleValue != null) {
                doubleValues.add(doubleValue);
            }            
        }
        range = new Range(doubleValues, THRESHOLD);
    }

    public boolean equals(Object o) {
        return this == o || o instanceof AnalysisRangeDimension && super.equals(o);

    }
}

