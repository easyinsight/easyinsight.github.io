package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Value;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.AnalysisItem;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 3:18:51 PM
 */
public class TypeTransformComponent implements IComponent {
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        DataSet targetSet = new DataSet();
        for (IRow row : dataSet.getRows()) {
            IRow targetRow = targetSet.createRow();
            for (AnalysisItem analysisItem : pipelineData.getReportItems()) {
                Value value = row.getValue(analysisItem.createAggregateKey());
                // TODO: why was this here?
                //Value preFilterValue = analysisItem.renameMeLater(value);
                Value transformedValue = analysisItem.transformValue(value, pipelineData.getInsightRequestMetadata());

                targetRow.addValue(analysisItem.createAggregateKey(), transformedValue);
            }
        }
        return targetSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
