package com.easyinsight.dataset;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.logging.LogClass;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.scrubbing.DataSetScrubber;
import com.easyinsight.storage.IWhere;

import java.util.*;
import java.io.Serializable;

/**
 * User: jboe
* Date: Dec 22, 2007
* Time: 10:35:21 AM
*/
public class DataSet implements Serializable {

    private List<IRow> rows;
    //private List<FeedItem> fields;

    public DataSet() {
        rows = new ArrayList<IRow>();
    }

    public DataSet(int rowSize) {
        rows = new ArrayList<IRow>(rowSize);
    }

    public void replaceKey(Key existingKey, Key newKey) {
        for (IRow row : rows) {
            row.replaceKey(existingKey, newKey);
        }
    }
    
    /*public List<FeedItem> getFields() {
        return fields;
    }

    public void setFields(List<FeedItem> fields) {
        this.fields = fields;
    }*/

    public IRow createRow() {
        IRow row = new Row();
        rows.add(row);
        return row;
    }

    public void removeRow(IRow row) {
        rows.remove(row);
    }

    public IRow createRow(int size) {
        IRow row = new Row(size);
        rows.add(row);
        return row;
    }

    public IRow getRow(int index) {
        return rows.get(index);
    }

    /*public PersistableDataSetForm toPersistableForm() {
        String[] keys = new String[fields.size()];
        int i = 0;
        String[][] valueArrayMap = new String[keys.length][rows.size()];
        for (FeedItem feedItem : fields) {
            keys[i] = feedItem.getKey();
            int j = 0;
            for (IRow row : rows) {
                String value = row.getValue(feedItem.getKey());
                valueArrayMap[i][j++] = value;
            }
            i++;
        }
        return new PersistableDataSetForm(keys, valueArrayMap);
    }*/

    public List<IRow> getRows() {
        return rows;
    }

    public String toString() {
        return rows.toString();
    }

    public ListTransform listTransform(List<AnalysisItem> columns, List<AnalysisItem> allItems) {
        Collection<AnalysisDimension> ourDimensions = new ArrayList<AnalysisDimension>();
        for (AnalysisItem column : columns) {
            if (column.hasType(AnalysisItemTypes.DIMENSION)) {
                AnalysisDimension analysisDimension = (AnalysisDimension) column;
                if (analysisDimension.isGroup()) {
                    ourDimensions.add(analysisDimension);
                }
                if (analysisDimension.requiresDataEarly()) {
                    analysisDimension.handleEarlyData(rows);
                }
            }
        }
        Collection<AnalysisItem> paredDownColumns = new LinkedHashSet<AnalysisItem>(columns);
        ListTransform listTransform = new ListTransform(allItems);
        for (IRow row : rows) {
            Map<Key, Value> compositeDimensionKey = new HashMap<Key, Value>();
            for (AnalysisDimension dimension : ourDimensions) {
                Value dimensionValue = row.getValue(dimension.getKey());
                if (dimensionValue == null || dimensionValue.type() == Value.EMPTY) {
                    dimensionValue = row.getValue(dimension.createAggregateKey());
                }
                compositeDimensionKey.put(dimension.getKey(), dimensionValue);
            }
            for (AnalysisItem column : paredDownColumns) {
                if (column.hasType(AnalysisItemTypes.MEASURE)) {
                    AnalysisMeasure measure = (AnalysisMeasure) column;
                    Value value = row.getValue(measure.getKey());
                    if (value == null || value.type() == Value.EMPTY) {
                        value = row.getValue(measure.createAggregateKey());
                    }
                    if (value != null) {
                        listTransform.groupData(compositeDimensionKey, measure, value);
                    }
                } else {
                    AnalysisDimension analysisDimension = (AnalysisDimension) column;
                    Value transformedValue = compositeDimensionKey.get(analysisDimension.getKey());
                    if (transformedValue == null) {
                        transformedValue = row.getValue(analysisDimension.getKey());
                    }
                    if (transformedValue == null || transformedValue.type() == Value.EMPTY) {
                        transformedValue = row.getValue(analysisDimension.createAggregateKey());
                    }
                    if (transformedValue != null) {
                        listTransform.groupData(compositeDimensionKey, (AnalysisDimension) column, transformedValue);
                    }
                }
            }

        }
        return listTransform;
    }

    public Crosstab toCrosstab(WSCrosstabDefinition crosstabDefinition, InsightRequestMetadata insightRequestMetadata) {
        DataSet dataSet = nextStep(crosstabDefinition, crosstabDefinition.getAllAnalysisItems(), insightRequestMetadata);
        return dataSet.crosstab(crosstabDefinition.getRows(),
                crosstabDefinition.getColumns(), crosstabDefinition.getMeasures());
    }

    private Crosstab crosstab(Collection<AnalysisItem> crosstabRows, Collection<AnalysisItem> columns, Collection<AnalysisItem> measures) {
        Crosstab crosstab = new Crosstab();
        // it's only two dimensional, so...
        // for each row, find the value
        //  for each value, add its column
        // starting with a list form...

        // from each row, extract the row value, column value, and measure value fields
        for (IRow row : rows) {
            DataKey dataKey = new DataKey();
            for (AnalysisItem analysisItem : crosstabRows) {
                Value value = row.getValue(analysisItem.getKey());
                dataKey.addRowKeyPair(analysisItem.getKey(), value);
            }
            for (AnalysisItem analysisItem : columns) {
                Value value = row.getValue(analysisItem.getKey());
                dataKey.addColumnKeyPair(analysisItem.getKey(), value);
            }
            for (AnalysisItem measure : measures) {
                crosstab.addData(dataKey, (AnalysisMeasure) measure, row.getValue(measure.getKey()));
            }
        }

        return crosstab;
    }

    public DataSet merge(DataSet dataSet, Key myJoinDimension, Key fromJoinDimension) {
        Map<Value, List<IRow>> index = new HashMap<Value, List<IRow>>();
        Collection<IRow> unjoinedRows = new ArrayList<IRow>();
        for (IRow row : rows) {
            Value joinDimensionValue = row.getValue(myJoinDimension);
            if (joinDimensionValue == null) {
                LogClass.debug("bad bad bad");
            } else {
                List<IRow> rows = index.get(joinDimensionValue);
                if (rows == null){
                    rows = new ArrayList<IRow>();
                    index.put(joinDimensionValue, rows);
                }
                rows.add(row);
            }
        }
        Map<Value, List<IRow>> indexCopy = new HashMap<Value, List<IRow>>(index);
        List<IRow> compositeRows = new LinkedList<IRow>();
        for (IRow row : dataSet.rows) {
            Value joinDimensionValue = row.getValue(fromJoinDimension);
            if (joinDimensionValue == null) {
                LogClass.debug("bad bad bad");
            } else {
                indexCopy.remove(joinDimensionValue);
                List<IRow> sourceRows = index.get(joinDimensionValue);
                if (sourceRows == null) {
                    unjoinedRows.add(row);
                } else {
                    for (IRow sourceRow : sourceRows) {
                        compositeRows.add(sourceRow.merge(row));
                    }
                }
            }
        }
        for (List<IRow> rows : indexCopy.values()) {
            compositeRows.addAll(rows);
        }
        compositeRows.addAll(unjoinedRows);
        DataSet compositeDataSet = new DataSet();
        compositeDataSet.rows = compositeRows;
        return compositeDataSet;
    }

    public void addRow(IRow row) {
        rows.add(row);
    }

    public ListDataResults toList(WSAnalysisDefinition listDefinition, List<AnalysisItem> underlyingFields, InsightRequestMetadata insightRequestMetadata) {
        List<AnalysisItem> allRequestedAnalysisItems = new ArrayList<AnalysisItem>(listDefinition.getAllAnalysisItems());
        Set<AnalysisItem> allNeededAnalysisItems = new LinkedHashSet<AnalysisItem>();
        List<AnalysisItem> derivedItems = new ArrayList<AnalysisItem>();
        for (AnalysisItem item : allRequestedAnalysisItems) {
            allNeededAnalysisItems.addAll(item.getAnalysisItems(underlyingFields, allRequestedAnalysisItems));
            derivedItems.addAll(item.getDerivedItems());
        }
        List<AnalysisItem> neededItemList = new ArrayList<AnalysisItem>(allNeededAnalysisItems);
        DataSet dataSet = nextStep(listDefinition, allNeededAnalysisItems, insightRequestMetadata);
        ListTransform listTransform = dataSet.listTransform(neededItemList, allRequestedAnalysisItems);
        DataSet aggregatedData = listTransform.aggregate(neededItemList, derivedItems, allRequestedAnalysisItems);
        aggregatedData = aggregatedData.filter(listDefinition, insightRequestMetadata);
        LimitsResults limitsResults = listDefinition.applyLimits(aggregatedData);
        ListDataResults listDataResults = listTransform.toListDataResults(allRequestedAnalysisItems, aggregatedData);
        listDataResults.setLimitedResults(limitsResults.isLimitedResults());
        listDataResults.setLimitResults(limitsResults.getLimitResults());
        listDataResults.setMaxResults(limitsResults.getMaxResults());
        return listDataResults;
    }

    public DataSet nextStep(WSAnalysisDefinition analysisDefinition, Set<AnalysisItem> neededItems, InsightRequestMetadata insightRequestMetadata) {

        if (analysisDefinition.getDataScrubs() != null && !analysisDefinition.getDataScrubs().isEmpty()) {
            new DataSetScrubber().scrub(this, analysisDefinition.getDataScrubs());
        }

        Collection<AnalysisItem> analysisItems = new HashSet<AnalysisItem>();
        Map<AnalysisItem, Collection<MaterializedFilterDefinition>> filterMap = new HashMap<AnalysisItem, Collection<MaterializedFilterDefinition>>();
        if (analysisDefinition.getFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : analysisDefinition.getFilterDefinitions()) {
                if (filterDefinition.isApplyBeforeAggregation()) {
                    analysisItems.add(filterDefinition.getField());
                    Collection<MaterializedFilterDefinition> filters = filterMap.get(filterDefinition.getField());
                    if (filters == null) {
                        filters = new ArrayList<MaterializedFilterDefinition>();
                        filterMap.put(filterDefinition.getField(), filters);
                    }
                    MaterializedFilterDefinition materializedFilterDefinition = filterDefinition.materialize(insightRequestMetadata);
                    filters.add(materializedFilterDefinition);
                    if (materializedFilterDefinition.requiresDataEarly()) {
                        materializedFilterDefinition.handleEarlyData(getRows());
                    }
                }
            }
        }
        analysisItems.addAll(analysisDefinition.getLimitFields());
        analysisItems.addAll(neededItems);

        preProcessData(analysisItems);

        return transformDataSet(analysisItems, filterMap);
    }

    private void preProcessData(Collection<AnalysisItem> analysisItems) {
        for (AnalysisItem analysisItem : analysisItems) {
            if (analysisItem.requiresDataEarly()) {
                analysisItem.handleEarlyData(getRows());
            }
        }
    }

    private DataSet transformDataSet(Collection<AnalysisItem> analysisItems, Map<AnalysisItem, Collection<MaterializedFilterDefinition>> filterMap) {
        DataSet resultDataSet = new DataSet();

        // Perform any one to many calculations.

        List<IRow> rows = new ArrayList<IRow>(getRows());
        for (AnalysisItem analysisItem : analysisItems) {
            if (analysisItem.isMultipleTransform()) {
                List<IRow> tempRows = new ArrayList<IRow>();
                for (IRow row : rows) {
                    Value value = row.getValue(analysisItem.getKey());
                    Value[] transformedValues = analysisItem.transformToMultiple(value);
                    Map<Key, Value> existingContents = row.getValues();
                    for (Value multipleVal : transformedValues) {
                        Map<Key, Value> newRowContents = new HashMap<Key, Value>(existingContents);
                        newRowContents.put(analysisItem.getKey(), multipleVal);
                        IRow tempRow = new Row();
                        tempRow.addValues(newRowContents);
                        tempRows.add(tempRow);
                    }
                }
                rows = tempRows;
            }
        }

        // Allow each analysis item to perform its necessary transformValue(), then filter against the resulting value.

        for (IRow row : rows) {
            boolean rowValid = true;
            //Map<Key, Value> valueMap = new HashMap<Key, Value>(row.getValues());
            Map<Key, Value> valueMap;
            if (analysisItems.isEmpty())
                valueMap = new HashMap<Key, Value>(row.getValues());
            else
                valueMap = new HashMap<Key, Value>();
            for (AnalysisItem analysisItem : analysisItems) {
                Value value = row.getValue(analysisItem.getKey());
                if (value == null || value.type() == Value.EMPTY) {
                    value = row.getValue(analysisItem.createAggregateKey());
                }
                Value preFilterValue = analysisItem.renameMeLater(value);
                Value transformedValue = analysisItem.transformValue(value);

                valueMap.put(analysisItem.createAggregateKey(), transformedValue);

                Collection<MaterializedFilterDefinition> filterDefinitions = filterMap.get(analysisItem);
                if (filterDefinitions != null) {
                    for (MaterializedFilterDefinition filter : filterDefinitions) {
                        if (!filter.allows(transformedValue, preFilterValue)) {
                            rowValid = false;
                        }
                    }
                }
            }
            if (rowValid) {
                IRow newRow = resultDataSet.createRow();
                newRow.addValues(valueMap);
            }
        } 
        return resultDataSet;
    }

    public DataSet filter(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata) {
        Map<AnalysisItem, Collection<MaterializedFilterDefinition>> filterMap = new HashMap<AnalysisItem, Collection<MaterializedFilterDefinition>>();
        if (analysisDefinition.getFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : analysisDefinition.getFilterDefinitions()) {
                if (!filterDefinition.isApplyBeforeAggregation()) {
                    Collection<MaterializedFilterDefinition> filters = filterMap.get(filterDefinition.getField());
                    if (filters == null) {
                        filters = new ArrayList<MaterializedFilterDefinition>();
                        filterMap.put(filterDefinition.getField(), filters);
                    }
                    filters.add(filterDefinition.materialize(insightRequestMetadata));
                }
            }
        }
        DataSet resultDataSet = new DataSet();
        for (IRow row : rows) {
            boolean rowValid = true;
            for (AnalysisItem analysisItem : filterMap.keySet()) {
                Value value = row.getValue(analysisItem.createAggregateKey());
                Value preFilterValue = analysisItem.renameMeLater(value);
                Value transformedValue = analysisItem.transformValue(value);

                Collection<MaterializedFilterDefinition> filterDefinitions = filterMap.get(analysisItem);
                if (filterDefinitions != null) {
                    for (MaterializedFilterDefinition filter : filterDefinitions) {
                        if (!filter.allows(transformedValue, preFilterValue)) {
                            rowValid = false;
                        }
                    }
                }
            }
            if (rowValid) {
                IRow newRow = resultDataSet.createRow();
                newRow.addValues(row.getValues());
            }
        }
        return resultDataSet;
    }

    public void applyCalculations(List<AnalysisCalculation> analysisCalculations) {
        for (AnalysisCalculation calculation : analysisCalculations) {
            calculation.preHandleData(this);
        }
        for (IRow row : rows) {
            for (AnalysisCalculation calculation : analysisCalculations) {
                Value value = calculation.createValue(row);
                row.addValue(calculation.getKey(), value);
            }
        }
    }

    public void sort(AnalysisItem analysisItem, boolean descending) {
        Collections.sort(rows, new RowComparator(analysisItem, !descending));
    }


    public void subset(int number) {
        rows = rows.subList(0, Math.min(rows.size(), number));
    }

    public void mergeWheres(List<IWhere> wheres) {
        for (IRow row : rows) {
            for (IWhere where : wheres) {
                if (where.hasConcreteValue()) {
                    row.addValue(where.getKey(), where.getConcreteValue());
                }
            }
        }
    }
}
