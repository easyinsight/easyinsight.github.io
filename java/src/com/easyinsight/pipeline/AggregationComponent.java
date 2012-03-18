package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.ListTransform;
import com.easyinsight.analysis.AnalysisItem;

import java.util.*;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 2:48:47 PM
 */
public class AggregationComponent implements IComponent {

    private Set<Integer> skipAggregations;

    public AggregationComponent(Integer... skipAggregations) {
        this.skipAggregations = new HashSet<Integer>();
        if (skipAggregations != null && skipAggregations.length > 0) {
            this.skipAggregations.addAll(Arrays.asList(skipAggregations));
        }
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        List<AnalysisItem> derivedItems = new ArrayList<AnalysisItem>();
        for (AnalysisItem item : pipelineData.getAllRequestedItems()) {
            derivedItems.addAll(item.getDerivedItems());
        }
        List<AnalysisItem> list = new ArrayList<AnalysisItem>(pipelineData.getReportItems());
        ListTransform listTransform = dataSet.listTransform(list, skipAggregations, pipelineData.getUniqueItems());
        return listTransform.aggregate(derivedItems);
    }

    public void decorate(DataResults listDataResults) {
    }
}
