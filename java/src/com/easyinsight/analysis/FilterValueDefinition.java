package com.easyinsight.analysis;

import com.easyinsight.MaterializedFilterDefinition;
import com.easyinsight.AnalysisItem;
import com.easyinsight.core.PersistableValueFactory;
import com.easyinsight.core.PersistableValue;
import com.easyinsight.core.Value;
import com.easyinsight.core.StringValue;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 2:57:56 PM
 */
public class FilterValueDefinition extends FilterDefinition {
    private boolean inclusive;
    private List<String> filteredValues;

    public FilterValueDefinition() {        
    }

    public FilterValueDefinition(AnalysisItem field, boolean inclusive, List<String> filteredValues) {
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

     public List<String> getFilteredValues() {
        return filteredValues;
    }

    public void setFilteredValues(List<String> filteredValues) {
        this.filteredValues = filteredValues;
    }

    public PersistableFilterDefinition toPersistableFilterDefinition() {
        PersistableValueFilterDefinition persistableFilterDefinition = new PersistableValueFilterDefinition();
        persistableFilterDefinition.setInclusive(inclusive);
        persistableFilterDefinition.setApplyBeforeAggregation(isApplyBeforeAggregation());
        persistableFilterDefinition.setField(getField());
        Set<Value> valueSet = new HashSet<Value>();
        for (String stringValue : filteredValues) {
            valueSet.add(new StringValue(stringValue));
        }
        Set<PersistableValue> filterDefinitionValues = PersistableValueFactory.fromValue(valueSet);
        persistableFilterDefinition.setFilterValues(filterDefinitionValues);
        return persistableFilterDefinition;
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedValueFilterDefinition(getField(), filteredValues, inclusive);
    }
}
