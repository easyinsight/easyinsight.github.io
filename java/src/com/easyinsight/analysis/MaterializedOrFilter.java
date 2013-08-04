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
    private List<FilterDefinition> originalFilters;

    public MaterializedOrFilter(AnalysisItem key) {
        super(key);
    }

    public void setFilters(List<MaterializedFilterDefinition> filters) {
        this.filters = filters;
    }

    public void setOriginalFilters(List<FilterDefinition> originalFilters) {
        this.originalFilters = originalFilters;
    }

    public DataSet processDataSet(DataSet dataSet, IFilterProcessor filterProcessor, FilterDefinition filterDefinition) {
        DataSet resultDataSet = new DataSet();
        for (IRow row : dataSet.getRows()) {
            boolean rowValid = false;
            for (int i = 0; i < filters.size(); i++) {
                MaterializedFilterDefinition filter = filters.get(i);
                FilterDefinition original = originalFilters.get(i);
                Value value = row.getValue(filter.getKey());
                boolean allows = filter.allows(value);
                if (!original.isNotCondition() && allows) {
                    rowValid = true;
                } else if (original.isNotCondition() && !allows) {
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
