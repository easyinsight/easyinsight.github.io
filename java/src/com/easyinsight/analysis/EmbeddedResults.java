package com.easyinsight.analysis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Dec 1, 2009
 * Time: 9:36:58 AM
 */
public class EmbeddedResults implements Serializable, Cloneable {
    protected WSAnalysisDefinition definition;
    private boolean dataSourceAccessible;
    protected DataSourceInfo dataSourceInfo;
    private ReportFault reportFault;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public ReportFault getReportFault() {
        return reportFault;
    }

    public void setReportFault(ReportFault reportFault) {
        this.reportFault = reportFault;
    }

    @Override
    public EmbeddedResults clone() throws CloneNotSupportedException {
        return (EmbeddedResults) super.clone();
    }

    public DataSourceInfo getDataSourceInfo() {
        return dataSourceInfo;
    }

    public void setDataSourceInfo(DataSourceInfo dataSourceInfo) {
        this.dataSourceInfo = dataSourceInfo;
    }

    public boolean isDataSourceAccessible() {
        return dataSourceAccessible;
    }

    public void setDataSourceAccessible(boolean dataSourceAccessible) {
        this.dataSourceAccessible = dataSourceAccessible;
    }

    public WSAnalysisDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(WSAnalysisDefinition definition) {
        this.definition = definition;
    }
}
