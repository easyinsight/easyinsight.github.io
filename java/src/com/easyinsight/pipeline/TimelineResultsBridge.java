package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSTimeline;
import com.easyinsight.analysis.definitions.WSXAxisDefinition;
import com.easyinsight.core.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.sequence.Sequence;

import java.util.*;

/**
 * User: jamesboe
 * Date: Nov 30, 2009
 * Time: 9:32:47 AM
 */
public class TimelineResultsBridge implements ResultsBridge {

    private Sequence sequence;
    private WSTimeline timeline;
    private Map<Value, DataSet> dataSetMap = new HashMap<Value, DataSet>();
    private List<AnalysisDimension> dimensions;
    private List<AnalysisMeasure> measures;

    public TimelineResultsBridge(WSTimeline timeline, List<AnalysisDimension> dimensions, List<AnalysisMeasure> measures) {
        this.timeline = timeline;
        this.sequence = timeline.getSequence();
        this.dimensions = dimensions;
        this.measures = measures;
    }

    private Set<Value> getKeysForDataSet(AnalysisDimension analysisDimension, DataSet dataSet) {
        Set<Value> valueSet = new HashSet<Value>();
        for (IRow row : dataSet.getRows()) {
            Value value = row.getValue(analysisDimension.createAggregateKey());
            valueSet.add(value);
        }
        return valueSet;
    }

    public DataResults toDataResults(DataSet dataSet, List<AnalysisItem> columns, Map<AnalysisItem, AnalysisItem> aliases, WSAnalysisDefinition report) {
        // iterate the data set

        // this is granular
        // if it's cumulative...

        // step 1. identify all dimension pairs in a data set
        // step 2. if a data set doesn't have values for those pairs, create an empty row for those keys

        Map<AnalysisDimension, Set<Value>> argh = new HashMap<AnalysisDimension, Set<Value>>();

        if (timeline.getReport() instanceof WSXAxisDefinition) {
            WSXAxisDefinition xAxisDefinition = (WSXAxisDefinition) timeline.getReport();
            for (AnalysisMeasure measure : measures) {
                double minValue = Double.MAX_VALUE;
                double maxValue = Double.MIN_VALUE;
                for (IRow row : dataSet.getRows()) {
                    Value value = row.getValue(measure.createAggregateKey());
                    if (value.type() == Value.NUMBER) {
                        NumericValue numericValue = (NumericValue) value;
                        if (numericValue.toDouble() != null) {
                            minValue = Math.min(minValue, numericValue.toDouble());
                            maxValue = Math.max(maxValue, numericValue.toDouble());
                        }
                    }
                }
                xAxisDefinition.setYAxisMin(minValue);
                xAxisDefinition.setYAxisMax(maxValue);
            }
            for (AnalysisDimension dimension : dimensions) {
                Set<Value> values = getKeysForDataSet(dimension, dataSet);
                argh.put(dimension, values);
            }




        }

        for (IRow row : dataSet.getRows()) {
            DataSet targetDataSet = findOrCreateDataSet(row);
            targetDataSet.addRow(row);
        }

        /*if (timeline.getReport() instanceof WSXAxisDefinition) {
            for (AnalysisDimension dimension : dimensions) {
                for (DataSet childSet : dataSetMap.values()) {
                    List<IRow> placeHolderRows = new ArrayList<IRow>();
                    Set<Value> childValues = getKeysForDataSet(dimension, childSet);
                    Set<Value> masterValues = new HashSet<Value>(argh.get(dimension));
                    masterValues.removeAll(childValues);
                    for (Value value : masterValues) {
                        IRow placeHolderRow = new Row();
                        placeHolderRow.addValue(dimension.createAggregateKey(), value);
                        placeHolderRows.add(placeHolderRow);
                    }
                    childSet.getRows().addAll(placeHolderRows);
                }
            }
        }*/


        SeriesDataResults seriesDataResults = new SeriesDataResults();

        Set<Value> keySet = dataSetMap.keySet();
        List<Value> keyList = new ArrayList<Value>(keySet);
        Collections.sort(keyList, new Comparator<Value>() {

            public int compare(Value value, Value value1) {
                if (value instanceof DateValue && value1 instanceof DateValue) {
                    DateValue dateValue1 = (DateValue) value;
                    DateValue dateValue2 = (DateValue) value1;
                    return dateValue1.getDate().compareTo(dateValue2.getDate());
                } else if (value instanceof StringValue && value1 instanceof StringValue) {
                    StringValue stringValue1 = (StringValue) value;
                    StringValue stringValue2 = (StringValue) value1;
                    return stringValue1.toString().toLowerCase().compareTo(stringValue2.toString().toLowerCase());
                }
                return 0;
            }
        });

        List<ListDataResults> listDatas = new ArrayList<ListDataResults>();
        for (Value key : keyList) {
            ListDataResults listDataResults = (ListDataResults) new ListResultsBridge().toDataResults(dataSetMap.get(key), columns, aliases, report);
            listDatas.add(listDataResults);
        }
        seriesDataResults.setListDatas(listDatas);
        seriesDataResults.setSeriesValues(keyList);
        return seriesDataResults;
    }

    private DataSet findOrCreateDataSet(IRow row) {
        Value value = row.getValue(sequence.getAnalysisItem());
        DataSet dataSet = dataSetMap.get(value);
        if (dataSet == null) {
            dataSet = new DataSet();
            dataSetMap.put(value, dataSet);
        }
        return dataSet;
    }
}
