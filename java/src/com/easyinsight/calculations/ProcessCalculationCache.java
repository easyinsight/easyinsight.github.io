package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: 9/11/12
 * Time: 2:16 PM
 */
public class ProcessCalculationCache implements ICalculationCache {
    private AnalysisItem instanceID;
    private AnalysisItem sortField;

    private Map<Value, List<IRow>> rowMap = new HashMap<Value, List<IRow>>();

    public ProcessCalculationCache(AnalysisItem instanceID, AnalysisItem sortField) {
        this.instanceID = instanceID;
        this.sortField = sortField;
    }

    public void fromDataSet(DataSet dataSet) {
        for (IRow row : dataSet.getRows()) {
            Value instanceIDValue = row.getValue(instanceID);
            List<IRow> rows = rowMap.get(instanceIDValue);
            if (rows == null) {
                rows = new ArrayList<IRow>();
                rowMap.put(instanceIDValue, rows);
            }
            rows.add(row);
        }
        for (List<IRow> rows : rowMap.values()) {
            Collections.sort(rows, new Comparator<IRow>() {

                public int compare(IRow iRow, IRow iRow1) {
                    Value val1 = iRow.getValue(sortField);
                    Value val2 = iRow1.getValue(sortField);
                    if (val1.type() == Value.EMPTY) {
                        return -1;
                    } else if (val2.type() == Value.EMPTY) {
                        return 1;
                    } else {
                        DateValue dateValue1 = (DateValue) val1;
                        DateValue dateValue2 = (DateValue) val2;
                        return dateValue1.getDate().compareTo(dateValue2.getDate());
                    }
                }
            });
        }
    }

    public List<IRow> rowsForValue(Value value) {
        return rowMap.get(value);
    }
}
