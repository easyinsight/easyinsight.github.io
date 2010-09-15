package com.easyinsight.analysis;

import com.easyinsight.core.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: Sep 12, 2010
 * Time: 6:01:00 PM
 */
public class MaterializedOrderedFilterDefinition extends MaterializedFilterDefinition {

    private List<MaterializedFilterDefinition> filters = new ArrayList<MaterializedFilterDefinition>();

    public MaterializedOrderedFilterDefinition(AnalysisItem key, OrderedFilterDefinition orderedFilterDefinition,
                                               InsightRequestMetadata insightRequestMetadata) {
        super(key);
        for (FilterDefinition filterDefinition : orderedFilterDefinition.getFilters()) {
            filters.add(filterDefinition.materialize(insightRequestMetadata));
        }
    }

    @Override
    public boolean allows(Value value) {
        
        return false;
    }
}
