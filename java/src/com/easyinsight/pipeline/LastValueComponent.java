package com.easyinsight.pipeline;

import com.easyinsight.core.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;

import java.util.*;

/**
 * User: jamesboe
 * Date: Aug 5, 2009
 * Time: 12:34:56 PM
 */
public class LastValueComponent implements IComponent {

    private Map<Map<Key, Value>, List<IRow>> aggregationMap = new LinkedHashMap<Map<Key, Value>, List<IRow>>();

    private Map<Map<Key, Value>, Integer> countMap = new HashMap<Map<Key, Value>, Integer>();

    private LastValueFilter lastValueFilter;

    private boolean absolute;

    private int threshold = 1;

    public LastValueComponent(LastValueFilter lastValueFilter) {
        this.lastValueFilter = lastValueFilter;
        this.absolute = lastValueFilter.isAbsolute();
        this.threshold = lastValueFilter.getThreshold();
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        final AnalysisDateDimension sortDim = (AnalysisDateDimension) lastValueFilter.getField();

        if (absolute) {
            Key sortKey = sortDim.createAggregateKey();
            Date firstDate = null;
            for (IRow row : dataSet.getRows()) {
                Value value = row.getValue(sortKey);
                if (value.type() == Value.DATE) {
                    DateValue dateValue = (DateValue) value;
                    if (firstDate == null || firstDate.getTime() < dateValue.getDate().getTime()) {
                        firstDate = dateValue.getDate();
                    }
                }
            }
            DataSet newSet = new DataSet();
            for (IRow row : dataSet.getRows()) {
                Value value = row.getValue(sortKey);
                if (value.type() == Value.DATE) {
                    DateValue dateValue = (DateValue) value;
                    if (dateValue.getDate().equals(firstDate)) {
                        newSet.addRow(row);
                    }
                }
            }
            return newSet;
        } else {
            if (!(sortDim.getKey() instanceof DerivedKey)) {
                return dataSet;
            }
            for (IRow row : dataSet.getRows()) {
                Map<Key, Value> map = createKeyMap(row, pipelineData, sortDim);
                List<IRow> rows = aggregationMap.get(map);
                if (rows == null) {
                    rows = new ArrayList<IRow>();
                    aggregationMap.put(map, rows);
                }
                rows.add(row);
            }

            for (List<IRow> rows : aggregationMap.values()) {
                Collections.sort(rows, new Comparator<IRow>() {

                    public int compare(IRow iRow, IRow iRow1) {
                        return getComparison(sortDim, iRow, iRow1, true);
                    }
                });
            }

            DataSet resultSet = new DataSet();

            for (List<IRow> rows : aggregationMap.values()) {
                for (int i = 0; i < threshold; i++) {
                    if (i < rows.size()) {
                        resultSet.createRow().addValues(rows.get(i));
                    }
                }
            }

            return resultSet;
        }
    }

    private Map<Key, Value> createKeyMap(IRow row, PipelineData pipelineData, AnalysisDimension sortDim) {
        DerivedKey derivedKey = (DerivedKey) sortDim.getKey();
        long tableID = derivedKey.getFeedID();
        Map<Key, Value> map = new HashMap<Key, Value>();
        for (AnalysisItem item : pipelineData.getReportItems()) {
            if (item.hasType(AnalysisItemTypes.DIMENSION)) {
                if (item.getKey() instanceof DerivedKey) {
                    DerivedKey childKey = (DerivedKey) item.getKey();
                    if (childKey.getFeedID() == tableID) {
                        continue;
                    }
                }
                map.put(item.createAggregateKey(), row.getValue(item));
            }
        }
        return map;
    }

    private int getComparison(AnalysisItem field, IRow row1, IRow row2, boolean asc) {
        int comparison = 0;
        int ascending = asc ? -1 : 1;
        Value value1 = row1.getValue(field);
        Value value2 = row2.getValue(field);

        if (value1.type() == Value.NUMBER && value2.type() == Value.NUMBER) {
            comparison = value1.toDouble().compareTo(value2.toDouble()) * ascending;
        } else if (value1.type() == Value.DATE && value2.type() == Value.DATE) {
            DateValue date1 = (DateValue) value1;
            DateValue date2 = (DateValue) value2;
            comparison = date1.getDate().compareTo(date2.getDate()) * ascending;
        } else if (value1.type() == Value.DATE && value2.type() == Value.EMPTY) {
            comparison = ascending;
        } else if (value1.type() == Value.EMPTY && value2.type() == Value.DATE) {
            comparison = -ascending;
        } else if (value1.type() == Value.STRING && value2.type() == Value.STRING) {
            StringValue stringValue1 = (StringValue) value1;
            StringValue stringValue2 = (StringValue) value2;
            comparison = stringValue1.getValue().compareTo(stringValue2.getValue()) * ascending;
        } else if (value1.type() == Value.STRING && value2.type() == Value.EMPTY) {
            comparison = -ascending;
        } else if (value1.type() == Value.EMPTY && value2.type() == Value.STRING) {
            comparison = ascending;
        }
        return comparison;
    }

    public void decorate(DataResults listDataResults) {
    }
}
