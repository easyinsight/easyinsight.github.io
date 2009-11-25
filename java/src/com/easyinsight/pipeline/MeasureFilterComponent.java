package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 2:37:52 PM
 */
public class MeasureFilterComponent implements IComponent {

    public MeasureFilterComponent() {
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        Map<AnalysisItem, Collection<MaterializedFilterDefinition>> filterMap = new HashMap<AnalysisItem, Collection<MaterializedFilterDefinition>>();
        Map<MaterializedFilterDefinition, AnalysisItem> reverseMap = new HashMap<MaterializedFilterDefinition, AnalysisItem>();
        for (AnalysisItem item : pipelineData.getReportItems()) {
            if (item.getFilters().size() > 0) {
                for (FilterDefinition filterDefinition : item.getFilters()) {
                    Collection<MaterializedFilterDefinition> filters = filterMap.get(filterDefinition.getField());
                    if (filters == null) {
                        filters = new ArrayList<MaterializedFilterDefinition>();
                        filterMap.put(filterDefinition.getField(), filters);
                    }
                    MaterializedFilterDefinition filter = filterDefinition.materialize(pipelineData.getInsightRequestMetadata());
                    filters.add(filter);
                    reverseMap.put(filter, item);
                }
            }
        }
        for (IRow row : dataSet.getRows()) {

            for (AnalysisItem analysisItem : filterMap.keySet()) {
                Value value = row.getValue(analysisItem);

                Collection<MaterializedFilterDefinition> filterDefinitions = filterMap.get(analysisItem);
                if (filterDefinitions != null) {
                    for (MaterializedFilterDefinition filter : filterDefinitions) {
                        if (!filter.allows(value)) {
                            row.addValue(reverseMap.get(filter).createAggregateKey(), new EmptyValue());
                        }
                    }
                }
            }
        }
        return dataSet;
    }

    public void decorate(ListDataResults listDataResults) {
    }
}