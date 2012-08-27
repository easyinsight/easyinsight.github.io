package com.easyinsight.pipeline;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 2:37:52 PM
 */
public class FilterComponent implements IComponent, DescribableComponent {

    private FilterDefinition filterDefinition;
    private IFilterProcessor filterProcessor;

    public FilterComponent(FilterDefinition filterDefinition, IFilterProcessor filterProcessor) {
        this.filterDefinition = filterDefinition;
        this.filterProcessor = filterProcessor;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        MaterializedFilterDefinition filter = filterDefinition.materialize(pipelineData.getInsightRequestMetadata());
        return filter.processDataSet(dataSet, filterProcessor, filterDefinition);
    }

    public void decorate(DataResults listDataResults) {
    }

    public String getDescription() {
        return "FilterComponent on " + filterDefinition.label(false);
    }
}
