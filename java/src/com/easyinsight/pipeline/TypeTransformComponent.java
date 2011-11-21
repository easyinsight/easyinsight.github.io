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

    private boolean timeShift = true;

    public TypeTransformComponent() {
    }

    public TypeTransformComponent(boolean timeShift) {
        this.timeShift = timeShift;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        //DataSet targetSet = new DataSet();
        for (IRow row : dataSet.getRows()) {
            //IRow targetRow = targetSet.createRow();
            for (AnalysisItem analysisItem : pipelineData.getReportItems()) {
                Value value = row.getValue(analysisItem.createAggregateKey());
                boolean shift = false;
                if (timeShift) {
                    if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                        shift = ((AnalysisDateDimension) analysisItem).isTimeshift();
                    }
                }
                Value transformedValue = analysisItem.transformValue(value, pipelineData.getInsightRequestMetadata(), shift);
                if (transformedValue != value) {
                    row.addValue(analysisItem.createAggregateKey(), transformedValue);
                }
            }
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
