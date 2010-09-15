package com.easyinsight.pipeline;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 2:37:52 PM
 */
public class FilterComponent implements IComponent {

    private FilterDefinition filterDefinition;
    private IFilterProcessor filterProcessor;

    public FilterComponent(FilterDefinition filterDefinition, IFilterProcessor filterProcessor) {
        this.filterDefinition = filterDefinition;
        this.filterProcessor = filterProcessor;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        MaterializedFilterDefinition filter = filterDefinition.materialize(pipelineData.getInsightRequestMetadata());
        DataSet resultDataSet = new DataSet();
        for (IRow row : dataSet.getRows()) {
            boolean rowValid = true;
            Value value = row.getValue(filter.getKey());
            if (!filter.allows(value)) {
                rowValid = false;
            }
            filterProcessor.createRow(resultDataSet, row, filterDefinition, rowValid);
        }
        return resultDataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
