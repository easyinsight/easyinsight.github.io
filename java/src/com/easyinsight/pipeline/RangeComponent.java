package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.AnalysisItem;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 2:45:33 PM
 */
public class RangeComponent implements IComponent {
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        for (AnalysisItem analysisItem : pipelineData.getReportItems()) {
            if (analysisItem.requiresDataEarly()) {
                analysisItem.handleEarlyData(dataSet.getRows());
            }
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
