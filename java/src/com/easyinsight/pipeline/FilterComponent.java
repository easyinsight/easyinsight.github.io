package com.easyinsight.pipeline;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 2:37:52 PM
 */
public class FilterComponent implements IComponent {

    private boolean beforeAggregation;

    public FilterComponent(boolean beforeAggregation) {
        this.beforeAggregation = beforeAggregation;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        Map<AnalysisItem, Collection<MaterializedFilterDefinition>> filterMap = new HashMap<AnalysisItem, Collection<MaterializedFilterDefinition>>();
        if (pipelineData.getReport().retrieveFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : pipelineData.getReport().retrieveFilterDefinitions()) {
                if (filterDefinition.isEnabled()) {
                    if (filterDefinition.isApplyBeforeAggregation() == beforeAggregation) {
                        Collection<MaterializedFilterDefinition> filters = filterMap.get(filterDefinition.getField());
                        if (filters == null) {
                            filters = new ArrayList<MaterializedFilterDefinition>();
                            filterMap.put(filterDefinition.getField(), filters);
                        }
                        filters.add(filterDefinition.materialize(pipelineData.getInsightRequestMetadata()));
                    }
                }
            }
        }
        DataSet resultDataSet = new DataSet();
        for (IRow row : dataSet.getRows()) {
            boolean rowValid = true;
            for (AnalysisItem analysisItem : filterMap.keySet()) {
                Value value = row.getValue(analysisItem);

                Collection<MaterializedFilterDefinition> filterDefinitions = filterMap.get(analysisItem);
                if (filterDefinitions != null) {
                    for (MaterializedFilterDefinition filter : filterDefinitions) {
                        if (!filter.allows(value)) {
                            rowValid = false;
                        }
                    }
                }
            }
            if (rowValid) {
                IRow newRow = resultDataSet.createRow();
                newRow.addValues(row.getValues());
            }
        }
        return resultDataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
