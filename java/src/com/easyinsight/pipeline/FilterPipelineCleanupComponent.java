package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.FilterDefinition;


/**
 * User: James Boe
 * Date: May 28, 2009
 * Time: 9:05:37 AM
 */
public class FilterPipelineCleanupComponent implements IComponent {

    private FilterDefinition filterDefinition;

    public FilterPipelineCleanupComponent(FilterDefinition filterDefinition) {
        this.filterDefinition = filterDefinition;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        /*if (pipelineData.decrementReferenceCount(filterDefinition.getField())) {
            pipelineData.getReportItems().remove(filterDefinition.getField());
        }*/
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
