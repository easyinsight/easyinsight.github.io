package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Value;

import java.util.*;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 3:18:51 PM
 */
public class TypeTransformComponent implements IComponent {

    private boolean timeShift = true;
    private List<AnalysisItem> items;

    public TypeTransformComponent() {
    }

    public TypeTransformComponent(boolean timeShift) {
        this.timeShift = timeShift;
    }

    public TypeTransformComponent(List<AnalysisItem> items) {
        this.items = items;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        //DataSet targetSet = new DataSet();
        Map<AnalysisItem, Calendar> map = new HashMap<AnalysisItem, Calendar>();
        Collection<AnalysisItem> targets;
        if (items == null) {
            targets = pipelineData.getReportItems();
        } else {
            targets = items;
        }
        for (AnalysisItem analysisItem : targets) {
            map.put(analysisItem, Calendar.getInstance());
            if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                boolean shift = ((AnalysisDateDimension) analysisItem).isTimeshift(pipelineData.getInsightRequestMetadata());
                if (shift) {
                    pipelineData.getInsightRequestMetadata().addAudit(analysisItem, "Time shifted " + analysisItem.toDisplay() + ".");
                } else {
                    pipelineData.getInsightRequestMetadata().addAudit(analysisItem, "Did not time shift " + analysisItem.toDisplay() + ".");
                }
            }
        }
        for (IRow row : dataSet.getRows()) {
            for (AnalysisItem analysisItem : targets) {
                Value value = row.getValueNullOnEmpty(analysisItem.createAggregateKey());
                if (value != null) {
                    if (analysisItem.hasType(AnalysisItemTypes.MEASURE) && value.type() == Value.EMPTY) {
                        value = row.getValueNoAdd(analysisItem.createAggregateKey(false));
                    }
                    boolean shift = false;
                    if (timeShift) {
                        if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                            shift = ((AnalysisDateDimension) analysisItem).isTimeshift(pipelineData.getInsightRequestMetadata());
                        }
                    }

                    Value transformedValue = analysisItem.transformValue(value, pipelineData.getInsightRequestMetadata(), shift, map.get(analysisItem));
                    if (transformedValue != value) {
                        row.addValue(analysisItem.createAggregateKey(), transformedValue);
                    }
                }
            }
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
