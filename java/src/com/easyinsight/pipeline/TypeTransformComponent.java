package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Value;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
        Map<AnalysisItem, Calendar> map = new HashMap<AnalysisItem, Calendar>();
        for (AnalysisItem analysisItem : pipelineData.getReportItems()) {
            map.put(analysisItem, Calendar.getInstance());
        }
        for (IRow row : dataSet.getRows()) {
            //IRow targetRow = targetSet.createRow();
            for (AnalysisItem analysisItem : pipelineData.getReportItems()) {
                Value value = row.getValue(analysisItem.createAggregateKey());
                if (analysisItem.hasType(AnalysisItemTypes.MEASURE) && value.type() == Value.EMPTY) {
                    value = row.getValueNoAdd(analysisItem.createAggregateKey(false));
                }
                boolean shift = false;
                if (timeShift) {
                    if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                        shift = ((AnalysisDateDimension) analysisItem).isTimeshift();
                    }
                }
                Value transformedValue = analysisItem.transformValue(value, pipelineData.getInsightRequestMetadata(), shift, map.get(analysisItem));
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
