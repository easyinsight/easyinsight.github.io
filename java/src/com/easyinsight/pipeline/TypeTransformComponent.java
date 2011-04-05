package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Value;

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
                boolean shift = false;
                if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                    shift = pipelineData.isDateTime((AnalysisDateDimension) analysisItem);
                }
                Value transformedValue = analysisItem.transformValue(value, pipelineData.getInsightRequestMetadata(), shift);

                targetRow.addValue(analysisItem.createAggregateKey(), transformedValue);
            }
        }
        return targetSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
