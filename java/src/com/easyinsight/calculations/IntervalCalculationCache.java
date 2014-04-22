package com.easyinsight.calculations;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: 9/11/12
 * Time: 2:16 PM
 */
public class IntervalCalculationCache implements ICalculationCache {

    private AnalysisDateDimension baseDate;
    private AnalysisMeasure measure;
    private DerivedAnalysisDateDimension date;
    private CalculationMetadata calculationMetadata;
    private AnalysisItem destination;
    private Map<Value, IRow> map = new HashMap<Value, IRow>();

    public IntervalCalculationCache(AnalysisItem destination,
                                    AnalysisItem baseDate, AnalysisMeasure measure, DerivedAnalysisDateDimension intervalDate, CalculationMetadata calculationMetadata) {
        this.destination = destination;
        this.baseDate = (AnalysisDateDimension) baseDate;
        this.measure = measure;
        this.date = intervalDate;
        this.calculationMetadata = calculationMetadata;
    }

    public IRow getRow(Value value) {
        return map.get(value);
    }

    public void fromDataSet(DataSet dataSet) {

        WSListDefinition tempReport = new WSListDefinition();
        WSAnalysisDefinition report = calculationMetadata.getReport();
        tempReport.setDataFeedID(report.getDataFeedID());

        List<AnalysisItem> columns = new ArrayList<AnalysisItem>();
        List<AnalysisItem> additionalDimensions = new ArrayList<AnalysisItem>();
        List<AnalysisItem> additionalItems = new ArrayList<AnalysisItem>(calculationMetadata.getReport().getAddedItems());
        for (AnalysisItem item : calculationMetadata.getReport().getAllAnalysisItems()) {
            if (item.toDisplay().equals(baseDate.toDisplay())) {
                AnalysisDateDimension d = (AnalysisDateDimension) item;
                baseDate = d;
            } else if (item.hasType(AnalysisItemTypes.DIMENSION) && !item.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                additionalDimensions.add(item);
            }
        }
        columns.addAll(additionalDimensions);
        columns.add(measure);
        AnalysisDateDimension dClone;
        try {
            dClone = (AnalysisDateDimension) date.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        dClone.setDateLevel(baseDate.getDateLevel());
        additionalItems.add(dClone);
        tempReport.setAddedItems(additionalItems);
        columns.add(dClone);
        tempReport.setColumns(columns);
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        for (FilterDefinition filter : report.getFilterDefinitions()) {
            if (filter.getField() != null && filter.getField().hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                continue;
            }
            filters.add(filter);
        }

        tempReport.setFilterDefinitions(filters);

        DataSet dataSet1 = DataService.listDataSet(tempReport, calculationMetadata.getInsightRequestMetadata(), calculationMetadata.getConnection());
        Map<List<Value>, IRow> map = new HashMap<List<Value>, IRow>();
        for (IRow row : dataSet1.getRows()) {

            List<Value> values = new ArrayList<Value>(additionalDimensions.size() + 1);
            Value dateValue = row.getValue(dClone.createAggregateKey());
            values.add(dateValue);
            for (AnalysisItem item : additionalDimensions) {
                Value val = row.getValue(item);
                values.add(val);
            }
            map.put(values, row);
        }

        for (IRow row : dataSet.getRows()) {
            Value value = row.getValue(baseDate.createAggregateKey());
            List<Value> key = new ArrayList<Value>(additionalDimensions.size() + 1);
            key.add(value);
            for (AnalysisItem item : additionalDimensions) {
                Value val = row.getValue(item);
                key.add(val);
            }
            IRow mergeRow = map.remove(key);
            if (mergeRow != null) {
                row.addValues(mergeRow);
                //row.addValue(baseDate.createAggregateKey(), value);
            }
        }
        for (IRow row : map.values()) {
            Value value = row.getValue(dClone.createAggregateKey());
            if (value.type() != Value.EMPTY) {
                dataSet.addRow(row);
                row.addValue(baseDate.createAggregateKey(), value);
            }
        }
        for (IRow row : dataSet.getRows()) {
            Value val = row.getValue(baseDate.createAggregateKey());
            if (val.type() != Value.EMPTY) {
                row.addValue(destination.createAggregateKey(), row.getValue(measure.createAggregateKey()));
            }
        }
    }
}
