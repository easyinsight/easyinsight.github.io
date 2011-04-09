package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 3:18:51 PM
 */
public class DateTransformComponent implements IComponent {

    private AnalysisItem analysisItem;

    public DateTransformComponent(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        DataSet targetSet = new DataSet();
        for (IRow row : dataSet.getRows()) {
            IRow targetRow = targetSet.createRow();
            Value value = row.getValue(analysisItem.createAggregateKey());
            /*boolean shift = false;
            if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                shift = ((AnalysisDateDimension) analysisItem).isTimeshift();
            }*/
            Value transformedValue = analysisItem.transformValue(value, pipelineData.getInsightRequestMetadata(), false);

            targetRow.addValue(analysisItem.createAggregateKey(), transformedValue);

        }
        return targetSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
