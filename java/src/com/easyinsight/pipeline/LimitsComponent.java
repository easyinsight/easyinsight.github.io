package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 2:51:37 PM
 */
public class LimitsComponent implements IComponent {

    private LimitsResults limitsResults;

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        this.limitsResults = pipelineData.getReport().applyLimits(dataSet);
        return dataSet;
    }

    public void decorate(DataResults dataResults) {
        if (dataResults instanceof ListDataResults) {
            ListDataResults listDataResults = (ListDataResults) dataResults;
            listDataResults.setLimitedResults(limitsResults.isLimitedResults());
            listDataResults.setMaxResults(limitsResults.getMaxResults());
            listDataResults.setLimitResults(limitsResults.getLimitResults());
        }
    }
}
