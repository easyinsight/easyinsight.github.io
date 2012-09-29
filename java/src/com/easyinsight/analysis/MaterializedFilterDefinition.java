package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.List;

/**
 * User: James Boe
 * Date: Jan 16, 2008
 * Time: 9:09:23 PM
 */
public abstract class MaterializedFilterDefinition {
    private AnalysisItem key;
    
    public MaterializedFilterDefinition(AnalysisItem key) {
        this.key = key;
    }

    public AnalysisItem getKey() {
        return key;
    }

    public DataSet processDataSet(DataSet dataSet, IFilterProcessor filterProcessor, FilterDefinition filterDefinition) {
        DataSet resultDataSet = new DataSet();
        for (IRow row : dataSet.getRows()) {
            boolean rowValid = true;
            Value value = row.getValue(getKey());
            if (!allows(value)) {
                rowValid = false;
            }
            if (filterDefinition.isNotCondition()) {
                rowValid = !rowValid;
            }
            filterProcessor.createRow(resultDataSet, row, filterDefinition, rowValid);
        }
        return resultDataSet;
    }

    public abstract boolean allows(Value value);

    public boolean requiresDataEarly() {
        return false;
    }

    public void handleEarlyData(List<IRow> rows) {
        throw new UnsupportedOperationException();
    }
}
