package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.scrubbing.DataSetScrubber;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 1:42:59 PM
 */
public class DataScrubComponent implements IComponent {
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        if (pipelineData.getReport().getDataScrubs() != null && !pipelineData.getReport().getDataScrubs().isEmpty()) {
            new DataSetScrubber().scrub(dataSet, pipelineData.getReport().getDataScrubs());
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
