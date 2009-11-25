package com.easyinsight.dataset;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.logging.LogClass;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
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
    private Date lastTime;

    public DataSet() {
        rows = new ArrayList<IRow>();
    }

    public DataSet(int rowSize) {
        rows = new ArrayList<IRow>(rowSize);
    }

    public DataSet(List<IRow> rows) {
        this.rows = rows;
    }

    public void replaceKey(Key existingKey, Key newKey) {
        for (IRow row : rows) {
            row.replaceKey(existingKey, newKey);
        }
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

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
        ListTransform listTransform = new ListTransform();
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
                        listTransform.groupData(compositeDimensionKey, measure, value, row);
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

    public ListDataResults toListDataResults(List<AnalysisItem> columns) {
        ListRow[] listRows = new ListRow[getRows().size()];
        int rowCount = 0;
        for (IRow row : getRows()) {
            int columnCount = 0;
            listRows[rowCount] = new ListRow();
            Value[] values = new Value[columns.size()];
            listRows[rowCount].setValues(values);
            for (AnalysisItem analysisItem : columns) {
                Key key = analysisItem.createAggregateKey();
                listRows[rowCount].getValues()[columnCount] = analysisItem.polishValue(row.getValue(key));
                columnCount++;
            }
            rowCount++;
        }
        ListDataResults listDataResults = new ListDataResults();
        List<AnalysisItem> allColumns = new ArrayList<AnalysisItem>(columns);
        AnalysisItem[] headers = new AnalysisItem[allColumns.size()];
        allColumns.toArray(headers);
        listDataResults.setHeaders(headers);
        listDataResults.setRows(listRows);
        return listDataResults;
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
