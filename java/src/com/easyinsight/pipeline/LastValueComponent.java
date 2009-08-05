package com.easyinsight.pipeline;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.HashMap;

/**
 * User: jamesboe
 * Date: Aug 5, 2009
 * Time: 12:34:56 PM
 */
public class LastValueComponent implements IComponent {

    private Map<Map<Key, Value>, IRow> aggregationMap = new LinkedHashMap<Map<Key, Value>, IRow>();

    private LastValueFilter lastValueFilter;

    public LastValueComponent(LastValueFilter lastValueFilter) {
        this.lastValueFilter = lastValueFilter;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        DataSet producedSet = new DataSet();
        AnalysisDimension existingDate = null;
        AnalysisDateDimension sortDim = (AnalysisDateDimension) lastValueFilter.getField();
        for (AnalysisItem analysisItem : pipelineData.getReport().getAllAnalysisItems()) {
            if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                AnalysisDateDimension dimension = (AnalysisDateDimension) analysisItem;
                if (analysisItem.getKey().equals(lastValueFilter.getField().getKey()) && dimension.getDateLevel() == sortDim.getDateLevel()) {
                    existingDate = (AnalysisDateDimension) analysisItem;
                    break;
                }
            }
        }
        // first question...
        // sort the values by the date sort item in an ascending fashion
        if (existingDate == null) {
            dataSet.sort(lastValueFilter.getField(), false);
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
            aggregationMap.put(dimensionKey, row);
            /*Value value = row.getValue(temporalAnalysisMeasure.getAggregateKey());
            ITemporalAggregation temporalAggregation = aggregationMap.get(dimensionKey);
            if (temporalAggregation == null) {
                temporalAggregation = temporalAnalysisMeasure.createAggregation();
                aggregationMap.put(dimensionKey, temporalAggregation);
            }
            temporalAggregation.addValue(value, i);*/
        }

        for (Map.Entry<Map<Key, Value>, IRow> entry : aggregationMap.entrySet()) {
            //Map<Key, Value> dimensionKey = entry.getKey();
            IRow row = entry.getValue();
            IRow newRow = producedSet.createRow();
            newRow.addValues(row.getValues());
            /*for (Map.Entry<Key, Value> keyPair : dimensionKey.entrySet()) {
                newRow.addValue(keyPair.getKey(), keyPair.getValue());
            }
            newRow.addValue(temporalAnalysisMeasure.createAggregateKey(), entry.getValue().getValue(0));*/
        }

        /*if (existingDate == null) {
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
        }*/
        return producedSet;
    }

    public void decorate(ListDataResults listDataResults) {
    }
}
