package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.AnalysisItem;

/**
 * User: jamesboe
 * Date: Sep 4, 2009
 * Time: 11:10:39 AM
 */
public class SortComponent implements IComponent {
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        for (AnalysisItem analysisItem : pipelineData.getReportItems()) {
            if (analysisItem.getSort() > 0) {
                if (analysisItem.getSort() == 1) {
                    dataSet.sort(analysisItem, false);
                } else if (analysisItem.getSort() == 2) {
                    dataSet.sort(analysisItem, true);
                }
            }
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
        
    }
}
