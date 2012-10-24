package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.*;
import com.easyinsight.calculations.DrillthroughCalculationMetadata;
import com.easyinsight.calculations.Function;
import com.easyinsight.core.Value;

import java.util.Arrays;

/**
 * User: jamesboe
 * Date: 1/26/12
 * Time: 2:54 PM
 */
public class DrillthroughAddFilters extends Function {
    public Value evaluate() {
        DrillthroughCalculationMetadata drillthroughCalculationMetadata = (DrillthroughCalculationMetadata) calculationMetadata;

        for (AnalysisItem analysisItem : drillthroughCalculationMetadata.getAnalysisItems()) {
            if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
                FilterValueDefinition filterValueDefinition = new FilterValueDefinition();
                filterValueDefinition.setField(analysisItem);
                filterValueDefinition.setSingleValue(true);
                filterValueDefinition.setEnabled(true);
                filterValueDefinition.setShowOnReportView(true);
                filterValueDefinition.setToggleEnabled(false);
                filterValueDefinition.setInclusive(true);
                filterValueDefinition.setFilteredValues(Arrays.asList(drillthroughCalculationMetadata.getData().get(analysisItem.qualifiedName())));
                drillthroughCalculationMetadata.getDrillThroughFilters().add(filterValueDefinition);
            }
        }

        for (FilterDefinition filterDefinition : drillthroughCalculationMetadata.getReport().getFilterDefinitions()) {
            if (filterDefinition.isShowOnReportView() && (filterDefinition instanceof FilterValueDefinition ||
                    filterDefinition instanceof RollingFilterDefinition || filterDefinition instanceof FilterDateRangeDefinition ||
                    filterDefinition instanceof FilterPatternDefinition || filterDefinition instanceof FlatDateFilter ||
                    filterDefinition instanceof MultiFlatDateFilter)) {
                FilterDefinition clone;
                try {
                    clone = filterDefinition.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
                drillthroughCalculationMetadata.getDrillThroughFilters().add(clone);
            }
        }

        return null;
    }

    public int getParameterCount() {
        return 0;
    }
}
