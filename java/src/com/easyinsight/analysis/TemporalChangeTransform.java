package com.easyinsight.analysis;

import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.core.DateValue;
import com.easyinsight.*;
import com.easyinsight.dataset.DataSet;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: James Boe
 * Date: Oct 18, 2008
 * Time: 2:30:10 PM
 */
public class TemporalChangeTransform {
    private Map<Map<Key, Value>, ITemporalAggregation> aggregationMap = new LinkedHashMap<Map<Key, Value>, ITemporalAggregation>();

    private TemporalAnalysisMeasure temporalAnalysisMeasure;
    private List<AnalysisItem> allRequestedAnalysisItems;

    public TemporalChangeTransform(TemporalAnalysisMeasure temporalAnalysisMeasure, List<AnalysisItem> allRequestedAnalysisItems) {
        this.temporalAnalysisMeasure = temporalAnalysisMeasure;
        this.allRequestedAnalysisItems = allRequestedAnalysisItems;
    }

    public DataSet blah(DataSet dataSet) {
        AnalysisDateDimension existingDate = null;
        for (AnalysisItem analysisItem : allRequestedAnalysisItems) {
            if (analysisItem.getKey().equals(temporalAnalysisMeasure.getAnalysisDimension().getKey()) && analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                existingDate = (AnalysisDateDimension) analysisItem;
                break;
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
            for (AnalysisItem analysisItem : allRequestedAnalysisItems) {
                if (analysisItem.hasType(AnalysisItemTypes.DIMENSION) && !analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                    AggregateKey aggregateKey = new AggregateKey(analysisItem.getKey(), analysisItem.getType());
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
        for (int i = 0; i < dataSet.getRows().size(); i++) {
            IRow row = dataSet.getRow(i);
            Map<Key, Value> dimensionKey = new HashMap<Key, Value>();
            for (AnalysisItem analysisItem : allRequestedAnalysisItems) {
                if (analysisItem.hasType(AnalysisItemTypes.DIMENSION) && !analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                    AggregateKey aggregateKey = new AggregateKey(analysisItem.getKey(), analysisItem.getType());
                    dimensionKey.put(aggregateKey, row.getValue(aggregateKey));
                }
            }
            ITemporalAggregation temporalAggregation = aggregationMap.get(dimensionKey);
            row.addValue(temporalAggregation.getNewAggregateKey(), temporalAggregation.getValue(i));
        }
        if (existingDate == null) {
            DateValue latestDate = null;
            AggregateKey dateKey = new AggregateKey(temporalAnalysisMeasure.getAnalysisDimension().getKey(), temporalAnalysisMeasure.getAnalysisDimension().getType());
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
                DataSet newDataSet = new DataSet();
                for (IRow row : dataSet.getRows()) {
                    if (row.getValue(dateKey) != null && row.getValue(dateKey).equals(latestDate)) {
                        newDataSet.addRow(row);
                    }
                }
                dataSet = newDataSet;
            }
        }
        return dataSet;
    }
}
