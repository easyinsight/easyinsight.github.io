package com.easyinsight.analysis;

import com.easyinsight.core.DataSourceDescriptor;

import java.util.List;

/**
 * User: jamesboe
 * Date: 3/1/11
 * Time: 3:06 PM
 */
public class ReportJoins {
    private List<JoinOverride> joinOverrides;
    private List<DataSourceDescriptor> dataSources;

    public List<JoinOverride> getJoinOverrides() {
        return joinOverrides;
    }

    public void setJoinOverrides(List<JoinOverride> joinOverrides) {
        this.joinOverrides = joinOverrides;
    }

    public List<DataSourceDescriptor> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSourceDescriptor> dataSources) {
        this.dataSources = dataSources;
    }
}
