package com.easyinsight.calculations;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: 9/11/12
 * Time: 2:16 PM
 */
public class AltCumulativeCalculationCache implements ICalculationCache {

    private AnalysisItem dateField;

    private WSAnalysisDefinition report;

    public AltCumulativeCalculationCache(AnalysisItem dateField, WSAnalysisDefinition report) {
        this.dateField = dateField;
        this.report = report;
    }

    public void fromDataSet(DataSet dataSet) {
        Map<Integer, List<IRow>> map = new HashMap<>();
        for (IRow row : dataSet.getRows()) {
            Value val1 = row.getValue(dateField);
            if (val1.type() == Value.DATE) {
                DateValue dateValue = (DateValue) val1;
                int year = dateValue.getLocalDate().getYear();
                List<IRow> rows = map.get(year);
                if (rows == null) {
                    rows = new ArrayList<>();
                    map.put(year, rows);
                }
                rows.add(row);
            }
        }
        List<AnalysisItem> items = new ArrayList<>(report.getAllAnalysisItems());
        for (Map.Entry<Integer, List<IRow>> entry : map.entrySet()) {
            entry.getValue().sort((o1, o2) -> o1.getValue(dateField).compareTo(o2.getValue(dateField)));
            Map<AnalysisItem, Aggregation> aggregations = new HashMap<>();
            items.stream().filter(analysisItem -> analysisItem.hasType(AnalysisItemTypes.MEASURE)).forEach(analysisItem -> {
                aggregations.put(analysisItem, new AggregationFactory((AnalysisMeasure) analysisItem, false).getAggregation());
            });
            for (IRow row : entry.getValue()) {
                for (AnalysisItem item : items) {
                    Aggregation aggregation = aggregations.get(item);
                    if (aggregation != null) {
                        Value value = row.getValue(item);
                        aggregation.addValue(value);
                        row.addValue(item.createAggregateKey(), aggregation.getValue());
                    }
                }
            }
        }
    }
}
