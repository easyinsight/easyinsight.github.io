package com.easyinsight.calculations;

import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.FilterValueDefinition;
import com.easyinsight.core.*;

/**
 * User: jamesboe
 * Date: 7/10/12
 * Time: 11:07 AM
 */
public class FilterValue extends Function {
    public Value evaluate() {

        // fixedjoin("Therapy Works", "Benchmarks 2", case(filtervalue("Discipline"), "PT", "PT", "HT", "HT", "OT", "HT", "OT", "?", "PT"), "Discipline")

        String filterName = minusQuotes(0);
        CompositeCalculationMetadata compositeCalculationMetadata = (CompositeCalculationMetadata) calculationMetadata;
        FilterDefinition filterToUse = null;
        for (FilterDefinition filterDefinition : compositeCalculationMetadata.getFilters()) {
            if (filterName.equals(filterDefinition.getFilterName()) && filterDefinition instanceof FilterValueDefinition) {
                filterToUse = filterDefinition;
                break;
            }
        }
        if (filterToUse == null) {
            return new EmptyValue();
        }
        FilterValueDefinition filterValueDefinition = (FilterValueDefinition) filterToUse;
        return new StringValue(filterValueDefinition.getFilteredValues().get(0).toString());
    }

    public int getParameterCount() {
        return 1;
    }
}
