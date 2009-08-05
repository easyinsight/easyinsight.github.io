package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.core.DateValue;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.HashMap;

/**
 * User: jamesboe
 * Date: Aug 5, 2009
 * Time: 9:37:40 AM
 */
public class TemporalComponent implements IComponent {

    private TemporalAnalysisMeasure temporalAnalysisMeasure;

    private Map<Map<Key, Value>, ITemporalAggregation> aggregationMap = new LinkedHashMap<Map<Key, Value>, ITemporalAggregation>();

    public TemporalComponent(TemporalAnalysisMeasure temporalAnalysisMeasure) {
        this.temporalAnalysisMeasure = temporalAnalysisMeasure;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        DataSet producedSet = new DataSet();
        AnalysisDimension existingDate = null;
        AnalysisDateDimension sortDim = (AnalysisDateDimension) temporalAnalysisMeasure.getAnalysisDimension();
        for (AnalysisItem analysisItem : pipelineData.getReport().getAllAnalysisItems()) {
            if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                AnalysisDateDimension dimension = (AnalysisDateDimension) analysisItem;
                if (analysisItem.getKey().equals(temporalAnalysisMeasure.getAnalysisDimension().getKey()) && dimension.getDateLevel() == sortDim.getDateLevel()) {
                    existingDate = (AnalysisDateDimension) analysisItem;
                    break;
                }
            }
        }
        // first question...
        // sort the values by the date sort item in an ascending fashion
        if (existingDate == null) {
            dataSet.sort(temporalAnalysisMeasure.getAnalysisDimension(), false);
        } else {
            dataSet.sort(existingDate, false);
        }
        
        for (int i = 0; i < dataSet.getRows().size(); i++) {
            IRow row = dataSet.getRow(i);
            // create the key map here...
            Map<Key, Value> dimensionKey = new HashMap<Key, Value>();
            for (AnalysisItem analysisItem : pipelineData.getReportItems()) {
                if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
                    AggregateKey aggregateKey = analysisItem.createAggregateKey();
                    dimensionKey.put(aggregateKey, row.getValue(aggregateKey));
                }
            }
            Value value = row.getValue(temporalAnalysisMeasure.getAggregateKey());
            ITemporalAggregation temporalAggregation = aggregationMap.get(dimensionKey);
            if (temporalAggregation == null) {
                temporalAggregation = temporalAnalysisMeasure.createAggregation();
                aggregationMap.put(dimensionKey, temporalAggregation);
            }
            temporalAggregation.addValue(value, i);
        }

        for (Map.Entry<Map<Key, Value>, ITemporalAggregation> entry : aggregationMap.entrySet()) {
            Map<Key, Value> dimensionKey = entry.getKey();
            IRow newRow = producedSet.createRow();
            for (Map.Entry<Key, Value> keyPair : dimensionKey.entrySet()) {
                newRow.addValue(keyPair.getKey(), keyPair.getValue());
            }
            newRow.addValue(temporalAnalysisMeasure.createAggregateKey(), entry.getValue().getValue(0));
        }

        if (existingDate == null) {
            DateValue latestDate = null;
            AggregateKey dateKey = temporalAnalysisMeasure.getAnalysisDimension().createAggregateKey();
            for (IRow row : dataSet.getRows()) {
                Value value = row.getValue(dateKey);
                if (value.type() == Value.DATE) {
                    DateValue dateValue = (DateValue) value;
                    if (latestDate == null || dateValue.getDate().compareTo(latestDate.getDate()) > 0) {
                        latestDate = dateValue;
                    }
                }
            }
            if (latestDate != null) {
                DataSet anotherDataSet = new DataSet();
                for (IRow row : producedSet.getRows()) {
                    if (row.getValue(dateKey) != null && row.getValue(dateKey).equals(latestDate)) {
                        anotherDataSet.addRow(row);
                    }
                }
                producedSet = anotherDataSet;
            }
        }
        return producedSet;
    }

    public void decorate(ListDataResults listDataResults) {
    }
}
