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
public class SimpleCalculationCache implements ICalculationCache {
    private AnalysisItem instanceID;

    private Map<Value, List<IRow>> rowMap = new HashMap<Value, List<IRow>>();

    public SimpleCalculationCache(AnalysisItem instanceID) {
        this.instanceID = instanceID;
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
    }

    public List<IRow> rowsForValue(Value value) {
        return rowMap.get(value);
    }
}
