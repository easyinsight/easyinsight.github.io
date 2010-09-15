package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Aug 5, 2009
 * Time: 12:34:56 PM
 */
public class FirstValueComponent implements IComponent {

    private Map<Map<Key, Value>, IRow> aggregationMap = new LinkedHashMap<Map<Key, Value>, IRow>();

    private FirstValueFilter lastValueFilter;

    public FirstValueComponent(FirstValueFilter lastValueFilter) {
        this.lastValueFilter = lastValueFilter;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        AnalysisDateDimension sortDim = (AnalysisDateDimension) lastValueFilter.getField();
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
    }

    public void decorate(DataResults listDataResults) {
    }
}