package com.easyinsight.dataset;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.core.EmptyValue;
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
    private List<String> audits = new ArrayList<String>();

    public DataSet() {
        rows = new ArrayList<IRow>();
    }

    public DataSet(int rowSize) {
        rows = new ArrayList<IRow>(rowSize);
    }

    public DataSet(List<IRow> rows) {
        this.rows = rows;
    }

    public List<String> getAudits() {
        return audits;
    }

    public void setAudits(List<String> audits) {
        this.audits = audits;
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

    public ListTransform listTransform(List<AnalysisItem> columns, Set<Integer> skipAggregations) {
        ListTransform listTransform = new ListTransform(skipAggregations);
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
        for (IRow row : rows) {
            Map<Key, Value> compositeDimensionKey = new HashMap<Key, Value>();
            for (AnalysisDimension dimension : ourDimensions) {
                Value dimensionValue = row.getValue(dimension.createAggregateKey());
                compositeDimensionKey.put(dimension.createAggregateKey(), dimensionValue);
            }
            for (AnalysisItem column : paredDownColumns) {
                if (column.hasType(AnalysisItemTypes.MEASURE)) {
                    AnalysisMeasure measure = (AnalysisMeasure) column;
                    Value value = row.getValue(measure.createAggregateKey());
                    if (value != null) {
                        listTransform.groupData(compositeDimensionKey, measure, value, row);
                    }
                } else {
                    AnalysisDimension analysisDimension = (AnalysisDimension) column;
                    Value transformedValue = compositeDimensionKey.get(analysisDimension.getKey());
                    if (transformedValue == null) {
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

    public void addRow(IRow row) {
        rows.add(row);
    }

    public DataResults toListDataResults(List<AnalysisItem> columns) {
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



    public void normalize() {
        Set<Key> keySet = new HashSet<Key>();
        for (IRow row : rows) {
            Collection<Key> keys = row.getKeys();
            for (Key key : keys) {
                keySet.add(key);
            }
        }

        for (IRow row : rows) {
            for (Key key : keySet) {
                Value value = row.getValue(key);
                if (value == null) {
                    row.addValue(key, new EmptyValue());
                } else if (value.type() == Value.STRING) {
                    if ("".equals(value.toString())) {
                        row.addValue(key, new EmptyValue());
                    }
                }
            }
        }
    }
}
