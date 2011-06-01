package com.easyinsight.analysis;

import com.easyinsight.datafeeds.FeedNode;
import com.easyinsight.etl.LookupTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: James Boe
 * Date: Jan 13, 2008
 * Time: 6:08:58 PM
 */
public class FeedMetadata implements Serializable {
    private AnalysisItem[] fields;
    private String urlKey;
    private List<FeedNode> fieldHierarchy;
    private long dataFeedID;
    private String dataSourceName;
    private int version;
    private boolean dataSourceAdmin;
    private boolean exchangeSave;
    private String filterExampleMessage;
    private DataSourceInfo dataSourceInfo;
    private ReportFault reportFault;
    private List<FilterDefinition> intrinsicFilters = new ArrayList<FilterDefinition>();
    private boolean customJoinsAllowed;
    private List<LookupTable> lookupTables;

    public List<LookupTable> getLookupTables() {
        return lookupTables;
    }

    public void setLookupTables(List<LookupTable> lookupTables) {
        this.lookupTables = lookupTables;
    }

    public boolean isExchangeSave() {
        return exchangeSave;
    }

    public void setExchangeSave(boolean exchangeSave) {
        this.exchangeSave = exchangeSave;
    }

    public boolean isCustomJoinsAllowed() {
        return customJoinsAllowed;
    }

    public void setCustomJoinsAllowed(boolean customJoinsAllowed) {
        this.customJoinsAllowed = customJoinsAllowed;
    }

    public ReportFault getReportFault() {
        return reportFault;
    }

    public void setReportFault(ReportFault reportFault) {
        this.reportFault = reportFault;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public DataSourceInfo getDataSourceInfo() {
        return dataSourceInfo;
    }

    public void setDataSourceInfo(DataSourceInfo dataSourceInfo) {
        this.dataSourceInfo = dataSourceInfo;
    }

    public String getFilterExampleMessage() {
        return filterExampleMessage;
    }

    public void setFilterExampleMessage(String filterExampleMessage) {
        this.filterExampleMessage = filterExampleMessage;
    }

    public List<FeedNode> getFieldHierarchy() {
        return fieldHierarchy;
    }

    public void setFieldHierarchy(List<FeedNode> fieldHierarchy) {
        this.fieldHierarchy = fieldHierarchy;
    }

    public List<FilterDefinition> getIntrinsicFilters() {
        return intrinsicFilters;
    }

    public void setIntrinsicFilters(List<FilterDefinition> intrinsicFilters) {
        this.intrinsicFilters = intrinsicFilters;
    }

    public boolean isDataSourceAdmin() {
        return dataSourceAdmin;
    }

    public void setDataSourceAdmin(boolean dataSourceAdmin) {
        this.dataSourceAdmin = dataSourceAdmin;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getDataFeedID() {
        return dataFeedID;
    }

    public void setDataFeedID(long dataFeedID) {
        this.dataFeedID = dataFeedID;
    }

    public AnalysisItem[] getFields() {
        return fields;
    }

    public void setFields(AnalysisItem[] fields) {
        this.fields = fields;
    }
}
