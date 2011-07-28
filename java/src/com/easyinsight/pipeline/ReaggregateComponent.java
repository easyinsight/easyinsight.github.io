package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.core.StringValue;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: Aug 5, 2009
 * Time: 12:34:56 PM
 */
public class ReaggregateComponent implements IComponent {

    private Map<Value, List<IRow>> aggregationMap = new LinkedHashMap<Value, List<IRow>>();
    private Map<Value, Aggregation> resultMap = new LinkedHashMap<Value, Aggregation>();

    private ReaggregateAnalysisMeasure reaggregateAnalysisMeasure;

    public ReaggregateComponent(ReaggregateAnalysisMeasure reaggregateAnalysisMeasure) {
        this.reaggregateAnalysisMeasure = reaggregateAnalysisMeasure;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {

        for (IRow row : dataSet.getRows()) {
            Value value = row.getValue(reaggregateAnalysisMeasure.getAggregationItem());
            List<IRow> rows = aggregationMap.get(value);
            if (rows == null) {
                rows = new ArrayList<IRow>();
                aggregationMap.put(value, rows);
            }
            rows.add(row);
        }

        AnalysisMeasure wrappedMeasure = (AnalysisMeasure) reaggregateAnalysisMeasure.getWrappedMeasure();

        for (Map.Entry<Value, List<IRow>> entry : aggregationMap.entrySet()) {

            Aggregation aggregation = new AggregationFactory(reaggregateAnalysisMeasure, true).getAggregation();
            for (IRow row : entry.getValue()) {
                Value value = row.getValue(wrappedMeasure);
                aggregation.addValue(value);
            }
            resultMap.put(entry.getKey(), aggregation);
        }

        DataSet resultSet = new DataSet();

        for (Map.Entry<Value, List<IRow>> entry : aggregationMap.entrySet()) {
            for (IRow row : entry.getValue()) {
                IRow resultRow = resultSet.createRow();
                resultRow.addValues(row);
                resultRow.addValue(reaggregateAnalysisMeasure.createAggregateKey(), resultMap.get(entry.getKey()).getValue());
            }
        }

        return resultSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
