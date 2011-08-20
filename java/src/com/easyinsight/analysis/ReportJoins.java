package com.easyinsight.analysis;

import com.easyinsight.core.DataSourceDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 3/1/11
 * Time: 3:06 PM
 */
public class ReportJoins {
    private Map<String, List<JoinOverride>> joinOverrideMap = new HashMap<String, List<JoinOverride>>();

    private Map<String, List<DataSourceDescriptor>> dataSourceMap = new HashMap<String, List<DataSourceDescriptor>>();

    private List<DataSourceDescriptor> configurableDataSources = new ArrayList<DataSourceDescriptor>();

    public List<DataSourceDescriptor> getConfigurableDataSources() {
        return configurableDataSources;
    }

    public void setConfigurableDataSources(List<DataSourceDescriptor> configurableDataSources) {
        this.configurableDataSources = configurableDataSources;
    }

    public Map<String, List<JoinOverride>> getJoinOverrideMap() {
        return joinOverrideMap;
    }

    public void setJoinOverrideMap(Map<String, List<JoinOverride>> joinOverrideMap) {
        this.joinOverrideMap = joinOverrideMap;
    }

    public Map<String, List<DataSourceDescriptor>> getDataSourceMap() {
        return dataSourceMap;
    }

    public void setDataSourceMap(Map<String, List<DataSourceDescriptor>> dataSourceMap) {
        this.dataSourceMap = dataSourceMap;
    }
}
