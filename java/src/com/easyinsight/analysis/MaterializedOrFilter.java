package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.List;

/**
 * User: jamesboe
 * Date: 2/17/11
 * Time: 9:48 AM
 */
public class MaterializedOrFilter extends MaterializedFilterDefinition {

    private List<MaterializedFilterDefinition> filters;

    public MaterializedOrFilter(AnalysisItem key) {
        super(key);
    }

    public void setFilters(List<MaterializedFilterDefinition> filters) {
        this.filters = filters;
    }

    public DataSet processDataSet(DataSet dataSet, IFilterProcessor filterProcessor, FilterDefinition filterDefinition) {
        DataSet resultDataSet = new DataSet();
        for (IRow row : dataSet.getRows()) {
            boolean rowValid = false;
            for (MaterializedFilterDefinition filter : filters) {
                Value value = row.getValue(filter.getKey());
                if (filter.allows(value)) {
                    rowValid = true;
                }
            }

            filterProcessor.createRow(resultDataSet, row, filterDefinition, rowValid);
        }
        return resultDataSet;
    }

    @Override
    public boolean allows(Value value) {
        boolean valid = false;
        for (MaterializedFilterDefinition filter : filters) {
            if (filter.allows(value)) {
                valid = true;
                break;
            }
        }
        return valid;
    }
}
