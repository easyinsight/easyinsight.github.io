package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.dataset.DataSet;

import java.util.Collection;

/**
 * User: jamesboe
 * Date: 9/17/11
 * Time: 1:03 PM
 */
public class CalculationMetadata {
    private WSAnalysisDefinition report;
    private Collection<AnalysisItem> dataSourceFields;
    private DataSet dataSet;
    private FilterDefinition filterDefinition;

    public FilterDefinition getFilterDefinition() {
        return filterDefinition;
    }

    public void setFilterDefinition(FilterDefinition filterDefinition) {
        this.filterDefinition = filterDefinition;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public Collection<AnalysisItem> getDataSourceFields() {
        return dataSourceFields;
    }

    public void setDataSourceFields(Collection<AnalysisItem> dataSourceFields) {
        this.dataSourceFields = dataSourceFields;
    }

    public WSAnalysisDefinition getReport() {
        return report;
    }

    public void setReport(WSAnalysisDefinition report) {
        this.report = report;
    }
}
