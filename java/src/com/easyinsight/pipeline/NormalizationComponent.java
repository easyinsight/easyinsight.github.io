package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.dataset.DataSet;

/**
 * User: jamesboe
 * Date: 1/17/11
 * Time: 12:21 PM
 */
public class NormalizationComponent implements IComponent {
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        dataSet.normalize();
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
