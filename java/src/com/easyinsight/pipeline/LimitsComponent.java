package com.easyinsight.pipeline;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;
import com.easyinsight.analysis.ListDataResults;

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

    public void decorate(ListDataResults listDataResults) {
        listDataResults.setLimitedResults(limitsResults.isLimitedResults());
        listDataResults.setMaxResults(limitsResults.getMaxResults());
        listDataResults.setLimitResults(limitsResults.getLimitResults());
    }
}
