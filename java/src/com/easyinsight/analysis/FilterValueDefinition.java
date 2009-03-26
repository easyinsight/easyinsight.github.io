package com.easyinsight.analysis;

import com.easyinsight.core.*;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 2:57:56 PM
 */
public class FilterValueDefinition extends FilterDefinition {
    private boolean inclusive;
    private List<Object> filteredValues;

    public FilterValueDefinition() {        
    }

    public FilterValueDefinition(AnalysisItem field, boolean inclusive, List<Object> filteredValues) {
        super(field);
        this.inclusive = inclusive;
        this.filteredValues = filteredValues;
    }

    public boolean isInclusive() {
        return inclusive;
    }

    public void setInclusive(boolean inclusive) {
        this.inclusive = inclusive;
    }

     public List<Object> getFilteredValues() {
        return filteredValues;
    }

    public void setFilteredValues(List<Object> filteredValues) {
        this.filteredValues = filteredValues;
    }

    public PersistableFilterDefinition toPersistableFilterDefinition() {
        PersistableValueFilterDefinition persistableFilterDefinition = new PersistableValueFilterDefinition();
        persistableFilterDefinition.setInclusive(inclusive);
        persistableFilterDefinition.setApplyBeforeAggregation(isApplyBeforeAggregation());
        persistableFilterDefinition.setField(getField());
        Set<Value> valueSet = new HashSet<Value>();
        for (Object valueObject : filteredValues) {
            Value value;
            if (valueObject instanceof Value) {
                value = (Value) valueObject;
            } else if (valueObject instanceof String) {
                value = new StringValue((String) valueObject);
            } else if (valueObject instanceof Number) {
                value = new NumericValue((Number) valueObject);
            } else if (valueObject instanceof Date) {
                value = new DateValue((Date) valueObject);
            } else {
                throw new RuntimeException("Unexpected value class " + valueObject.getClass().getName());
            }
            valueSet.add(value);
        }
        Set<PersistableValue> filterDefinitionValues = PersistableValueFactory.fromValue(valueSet);
        persistableFilterDefinition.setFilterValues(filterDefinitionValues);
        return persistableFilterDefinition;
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        Set<Value> valueSet = new HashSet<Value>();
        for (Object valueObject : filteredValues) {
            Value value;
            if (valueObject instanceof String) {
                value = new StringValue((String) valueObject);
            } else if (valueObject instanceof Number) {
                value = new NumericValue((Number) valueObject);
            } else if (valueObject instanceof Date) {
                value = new DateValue((Date) valueObject);
            } else {
                throw new RuntimeException("Unexpected value class " + valueObject.getClass().getName());
            }
            valueSet.add(value);
        }
        return new MaterializedValueFilterDefinition(getField(), valueSet, inclusive);
    }
}
