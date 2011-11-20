package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.database.EIConnection;
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
    private Dashboard dashboard;
    private EIConnection connection;

    public EIConnection getConnection() {
        return connection;
    }

    public void setConnection(EIConnection connection) {
        this.connection = connection;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

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
