package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Aug 5, 2009
 * Time: 12:34:56 PM
 */
public class FirstValueComponent implements IComponent {

    private FirstValueFilter lastValueFilter;
    private AnalysisItem baseItem;

    public FirstValueComponent(FirstValueFilter lastValueFilter) {
        this.lastValueFilter = lastValueFilter;
    }

    public FirstValueComponent(FirstValueFilter lastValueFilter, AnalysisItem baseItem) {
        this.lastValueFilter = lastValueFilter;
        this.baseItem = baseItem;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        AnalysisDateDimension sortDim = (AnalysisDateDimension) lastValueFilter.getField();
        DataSet newSet = new DataSet();
        if (baseItem == null) {
            acrossAllData(dataSet, sortDim, newSet);
        } else {
            basedOnField(dataSet, sortDim, newSet);
        }
        return newSet;
    }

    private void basedOnField(DataSet dataSet, AnalysisDateDimension sortDim, DataSet newSet) {
        Key sortKey = sortDim.createAggregateKey();
        Key baseKey = baseItem.createAggregateKey();
        Map<Value, Date> firstDateMap = new HashMap<Value, Date>();
        for (IRow row : dataSet.getRows()) {
            Value sortValue = row.getValue(sortKey);
            Value baseValue = row.getValue(baseKey);
            if (sortValue.type() == Value.DATE) {
                DateValue dateValue = (DateValue) sortValue;
                Date firstDate = firstDateMap.get(baseValue);
                if (firstDate == null || firstDate.getTime() > dateValue.getDate().getTime()) {
                    firstDate = dateValue.getDate();
                    firstDateMap.put(baseValue, firstDate);
                }
            }
        }

        for (IRow row : dataSet.getRows()) {
            Value baseValue = row.getValue(baseKey);
            Value value = row.getValue(sortKey);
            if (value.type() == Value.DATE) {
                DateValue dateValue = (DateValue) value;
                Date firstDate = firstDateMap.get(baseValue);
                if (dateValue.getDate().equals(firstDate)) {
                    newSet.addRow(row);
                }
            }
        }
    }

    private void acrossAllData(DataSet dataSet, AnalysisDateDimension sortDim, DataSet newSet) {
        Key sortKey = sortDim.createAggregateKey();
        Date firstDate = null;
        for (IRow row : dataSet.getRows()) {
            Value value = row.getValue(sortKey);
            if (value.type() == Value.DATE) {
                DateValue dateValue = (DateValue) value;
                if (firstDate == null || firstDate.getTime() > dateValue.getDate().getTime()) {
                    firstDate = dateValue.getDate();
                }
            }
        }

        for (IRow row : dataSet.getRows()) {
            Value value = row.getValue(sortKey);
            if (value.type() == Value.DATE) {
                DateValue dateValue = (DateValue) value;
                if (dateValue.getDate().equals(firstDate)) {
                    newSet.addRow(row);
                }
            }
        }
    }

    public void decorate(DataResults listDataResults) {
    }
}