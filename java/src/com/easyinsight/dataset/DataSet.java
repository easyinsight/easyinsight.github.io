package com.easyinsight.dataset;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.core.*;
import com.easyinsight.pipeline.PipelineData;
import com.easyinsight.storage.IWhere;

import java.util.*;
import java.io.Serializable;

/**
 * User: jboe
* Date: Dec 22, 2007
* Time: 10:35:21 AM
*/
public class DataSet implements Serializable, Cloneable {

    private List<IRow> rows;
    private Date lastTime;
    private List<ReportAuditEvent> audits = new ArrayList<ReportAuditEvent>();
    private DataSetKeys dataSetKeys = new DataSetKeys();
    private String reportLog;
    private transient PipelineData pipelineData;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    private List<DataSet> additionalSets = new ArrayList<DataSet>();

    private long time;

    public DataSet() {
        rows = new ArrayList<IRow>();
        time = System.currentTimeMillis();
    }

    public DataSet(int rowSize) {
        rows = new ArrayList<IRow>(rowSize);
        time = System.currentTimeMillis();
    }

    public DataSet(List<IRow> rows) {
        this.rows = rows;
        time = System.currentTimeMillis();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void addSet(DataSet dataSet) {
        additionalSets.add(dataSet);
    }

    public List<DataSet> getAdditionalSets() {
        return additionalSets;
    }

    public void copyState(DataSet dataSet) {
        additionalSets = dataSet.getAdditionalSets();
        audits = dataSet.getAudits();
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public DataSet clone() throws CloneNotSupportedException {
        DataSet clone = (DataSet) super.clone();
        DataSetKeys cloneKeys = dataSetKeys.clone();
        clone.dataSetKeys = cloneKeys;
        List<IRow> targetRows = new ArrayList<IRow>();
        for (IRow row : rows) {
            IRow cloneRow = row.clone();
            cloneRow.setDataSetKeys(cloneKeys);
            targetRows.add(cloneRow);
        }
        clone.rows = targetRows;
        return clone;
    }

    public PipelineData getPipelineData() {
        return pipelineData;
    }

    public void setPipelineData(PipelineData pipelineData) {
        this.pipelineData = pipelineData;
    }

    public DataSetKeys getDataSetKeys() {
        return dataSetKeys;
    }

    public String getReportLog() {
        return reportLog;
    }

    public void setReportLog(String reportLog) {
        this.reportLog = reportLog;
    }

    public List<ReportAuditEvent> getAudits() {
        return audits;
    }

    public void setAudits(List<ReportAuditEvent> audits) {
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
        IRow row = new Row(dataSetKeys);
        rows.add(row);
        return row;
    }

    public void removeRow(IRow row) {
        rows.remove(row);
    }

    public IRow createRow(int size) {
        IRow row = new Row(size, dataSetKeys);
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

    private UniqueKey toID(Key key) {
        if (key instanceof DerivedKey) {
            DerivedKey derivedKey = (DerivedKey) key;
            Key next = derivedKey.getParentKey();
            if (next instanceof NamedKey) {
                return new UniqueKey(derivedKey.getFeedID(), UniqueKey.DERIVED);
            }
            return toID(next);
        } else if (key instanceof ReportKey) {
            ReportKey reportKey = (ReportKey) key;
            return new UniqueKey(reportKey.getReportID(), UniqueKey.REPORT);
        }
        return null;
    }

    public ListTransform listTransform(List<AnalysisItem> columns, Set<Integer> skipAggregations, Map<UniqueKey, AnalysisItem> uniqueItems, Map<String, UniqueKey> fieldToUniques, int tier) {
        ListTransform listTransform = new ListTransform(skipAggregations);
        List<AnalysisDimension> ourDimensions = new ArrayList<>();
        List<AnalysisDimension> ungroupedDimensions = new ArrayList<>();
        Map<AnalysisItem, AnalysisItem> keyMapping = new HashMap<>();
        for (AnalysisItem column : columns) {
            if (column.hasType(AnalysisItemTypes.DIMENSION)) {
                AnalysisDimension analysisDimension = (AnalysisDimension) column;
                if (analysisDimension.isGroup()) {
                    ourDimensions.add(analysisDimension);
                } else {
                    ungroupedDimensions.add(analysisDimension);
                }
                if (analysisDimension.requiresDataEarly()) {
                    analysisDimension.handleEarlyData(rows);
                }
            } else if (column.hasType(AnalysisItemTypes.MEASURE)) {
                if (uniqueItems != null) {
                    UniqueKey id = toID(column.getKey());
                    if (id != null) {
                        AnalysisItem dim = uniqueItems.get(id);
                        keyMapping.put(column, dim);
                    } else {
                        if (fieldToUniques != null) {
                            UniqueKey idObj = fieldToUniques.get(column.toDisplay());
                            if (idObj != null) {
                                AnalysisItem dim = uniqueItems.get(idObj);
                                keyMapping.put(column, dim);
                            }
                        }
                    }
                }
            }
        }
        List<AnalysisItem> paredDownColumns = new ArrayList<>(new HashSet<>(columns));
        List<String> keys = new ArrayList<String>(rows.size());
        int ungroupedDimCount = ungroupedDimensions.size();
        int ourDimCount = ourDimensions.size();
        int baseLen = ourDimensions.size() + 1;
        int size = baseLen + (ourDimensions.size() * 10);
        for (int i = 0; i < rows.size(); i++) {
            IRow row = rows.get(i);
            StringBuilder keyBuilder = new StringBuilder(size);
            for (int j = 0; j < ourDimCount; j++) {
                AnalysisDimension dimension = ourDimensions.get(j);
                Value dimensionValue = row.getValueNullOnEmpty(dimension.createAggregateKey());
                if (dimensionValue == null) {
                    keyBuilder.append(j).append(":").append("").append(":");
                } else {
                    keyBuilder.append(j).append(":").append(dimensionValue.performantString()).append(":");
                }
            }
            for (int j = 0; j < ungroupedDimCount; j++) {
                if (uniqueItems != null && uniqueItems.size() > 0) {
                    keyBuilder.append(j + ourDimCount);
                } else {
                    keyBuilder.append(j + ourDimCount).append(":").append(i);
                }
            }
            String key = keyBuilder.toString();
            keys.add(key);
        }

        listTransform.addCompositeKeys(keys);
        listTransform.addColumns(paredDownColumns);
        for (int i = 0; i < rows.size(); i++) {
            IRow row = rows.get(i);
            String key = keys.get(i);
            if (row.getPassthroughRow() != null) {
                listTransform.add(key, row.getPassthroughRow());
            }
            for (int k = 0; k < paredDownColumns.size(); k++) {
                AnalysisItem column = paredDownColumns.get(k);
                if (column.hasType(AnalysisItemTypes.MEASURE)) {
                    AnalysisMeasure measure = (AnalysisMeasure) column;
                    Value value = row.getValue(measure.createAggregateKey());
                    if (value != null) {
                        AnalysisItem keyDim = keyMapping.get(measure);
                        Value keyValue = null;
                        if (keyDim != null) {
                            keyValue = row.getValue(keyDim);
                        }
                        listTransform.groupData(key, measure, value, keyValue);
                    }
                } else {
                    AnalysisDimension analysisDimension = (AnalysisDimension) column;
                    Value transformedValue = row.getValue(analysisDimension.createAggregateKey());
                    if (transformedValue != null) {
                        listTransform.groupData(key, (AnalysisDimension) column, transformedValue);
                    }
                }
            }
        }
        return listTransform;
    }

    public void addRow(IRow row) {
        rows.add(row);
    }

    public DataResults toListDataResults(List<AnalysisItem> columns, Map<AnalysisItem, AnalysisItem> aliases, WSAnalysisDefinition report) {
        ListRow[] listRows = new ListRow[getRows().size()];

        int rowCount = 0;
        for (IRow row : getRows()) {
            int columnCount = 0;
            listRows[rowCount] = new ListRow();
            Value[] values = new Value[columns.size()];
            listRows[rowCount].setValues(values);
            for (int i = 0; i < columns.size(); i++) {
                AnalysisItem analysisItem = columns.get(i);
                Key key = analysisItem.createAggregateKey();
                listRows[rowCount].getValues()[columnCount] = analysisItem.polishValue(row.getValue(key));
                columnCount++;
            }
            rowCount++;
        }

        ListDataResults listDataResults = new ListDataResults();

        AnalysisItem[] headers = new AnalysisItem[columns.size()];
        int i = 0;
        for (AnalysisItem analysisItem : columns) {
            AnalysisItem alias = aliases.get(analysisItem);
            if (alias == null) {
                headers[i++] = analysisItem;
            } else {
                headers[i++] = alias;
            }
        }
        listDataResults.setHeaders(headers);
        listDataResults.setRows(listRows);
        return listDataResults;
    }

    public void sort(AnalysisItem analysisItem, boolean descending) {
        Collections.sort(rows, new RowComparator(analysisItem, !descending));
    }

    public void sort(RowComparator rowComparator) {
        Collections.sort(rows, rowComparator);
    }

    public List<IRow> subset(int number) {
        int index = Math.min(rows.size(), number);
        List<IRow> subsetRows = new ArrayList<IRow>(rows.subList(0, index));
        List<IRow> remainder;
        if (index < rows.size()) {
            remainder = new ArrayList<IRow>(rows.subList(index + 1, rows.size()));
            rows = subsetRows;
        } else {
            remainder = new ArrayList<IRow>();
        }
        return remainder;
    }

    public DataSet subset(int start, int end) {


        int index = Math.min(rows.size(), end);
        List<IRow> subsetRows = new ArrayList<IRow>(rows.subList(start, index));
        DataSet copy = new DataSet(subsetRows);

        return copy;
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

        EmptyValue empty = new EmptyValue();
        for (IRow row : rows) {
            for (Key key : keySet) {
                Value value = row.getValueNullOnEmpty(key);
                if (value == null) {
                    row.addValue(key, empty);
                } else if (value.type() == Value.STRING) {
                    if ("".equals(value.toString())) {
                        row.addValue(key, empty);
                    }
                }
            }
        }
    }

    public void normalize(Collection<AnalysisItem> analysisItems) {
        for (IRow row : rows) {
            for (AnalysisItem analysisItem : analysisItems) {
                Value value = row.getValue(analysisItem.createAggregateKey());
                if (value.type() == Value.STRING) {
                    if ("".equals(value.toString())) {
                        row.addValue(analysisItem.createAggregateKey(), new EmptyValue());
                    }
                }
            }
        }
    }
    
    public void rowsWithCriteria(Value value) {
        for (IRow row : rows) {
            for (Value existing : row.getValues().values()) {
                if (existing.equals(value)) {
                    System.out.println("Match: " + row);
                }
            }
        }
    }
}
