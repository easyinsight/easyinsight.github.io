package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.dataset.DataSet;

/**
 * User: jamesboe
 * Date: 8/27/12
 * Time: 12:39 PM
 */
public class PipelinePlaceholderComponent implements IComponent, DescribableComponent {

    private String name;

    public PipelinePlaceholderComponent(String name) {
        this.name = name;
    }

    public String getDescription() {
        return name;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
