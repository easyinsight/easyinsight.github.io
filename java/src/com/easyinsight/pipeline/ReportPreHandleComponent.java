package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.dataset.DataSet;

/**
 * User: jamesboe
 * Date: Mar 24, 2010
 * Time: 5:17:57 PM
 */
public class ReportPreHandleComponent implements IComponent {
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        pipelineData.getReport().tweakReport(null);
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
        
    }
}
