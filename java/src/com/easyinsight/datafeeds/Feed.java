package com.easyinsight.datafeeds;


import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.kpi.KPI;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.io.Serializable;

/**
 * User: jboe
 * Date: Jan 3, 2008
 * Time: 11:46:38 AM
 */
public abstract class Feed implements Serializable {
    private long feedID;
    private List<AnalysisItem> fields;
    private List<FeedNode> fieldHierarchy;
    private int version;
    private String name;
    private String attribution;
    private boolean visible;
    private String filterExampleMessage;
    private int type;
    private long originSolution;
    private String urlKey;
    private Map<String, String> properties;

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public String getFilterExampleMessage() {
        return filterExampleMessage;
    }

    public void setFilterExampleMessage(String filterExampleMessage) {
        this.filterExampleMessage = filterExampleMessage;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public long getOriginSolution() {
        return originSolution;
    }

    public void setOriginSolution(long originSolution) {
        this.originSolution = originSolution;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DataSourceInfo getDataSourceInfo() {
        DataSourceInfo dataSourceInfo = new DataSourceInfo();
        dataSourceInfo.setType(type);
        dataSourceInfo.setDataSourceID(feedID);
        dataSourceInfo.setDataSourceName(name);
        dataSourceInfo.setLiveDataSource(!getCredentialRequirement(false).isEmpty());
        dataSourceInfo.setOriginSolution(originSolution);
        return dataSourceInfo;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public List<FeedNode> getFieldHierarchy() {
        return fieldHierarchy;
    }

    public void setFieldHierarchy(List<FeedNode> fieldHierarchy) {
        this.fieldHierarchy = fieldHierarchy;
    }

    public List<FilterDefinition> getIntrinsicFilters() {
        return new ArrayList<FilterDefinition>();
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<AnalysisItem> getFields() {
        return fields;
    }

    public void setFields(List<AnalysisItem> fields) {
        this.fields = fields;
    }

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }

    public Set<CredentialRequirement> getCredentialRequirement(boolean allSources) {
        return new HashSet<CredentialRequirement>();
    }

    public List<Long> getDataSourceIDs() {
        List<Long> ids = new ArrayList<Long>();
        ids.add(feedID);
        return ids;
    }

    public abstract AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata);

    public abstract DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode) throws TokenMissingException;

    /*public DataSet getDataSet(List<Key> columns, Integer maxRows, boolean admin, InsightRequestMetadata insightRequestMetadata) {
        // okay, credentials may be necessary here...
        return getUncachedDataSet(columns, maxRows, admin, insightRequestMetadata);
    }*/

    public abstract DataSet getDetails(Collection<FilterDefinition> filters);

    public List<KPI> createKPIs() {
        return new ArrayList<KPI>();
    }

    public DataSourceInfo createSourceInfo(EIConnection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT FEED_PERSISTENCE_METADATA.last_data_time FROM DATA_FEED," +
                "FEED_PERSISTENCE_METADATA WHERE DATA_FEED.DATA_FEED_ID = FEED_PERSISTENCE_METADATA.feed_id AND " +
                "(DATA_FEED.DATA_FEED_ID = ? OR DATA_FEED.PARENT_SOURCE_ID = ?)");
        stmt.setLong(1, getFeedID());
        stmt.setLong(2, getFeedID());
        DataSourceInfo dataSourceInfo = new DataSourceInfo();
        Date date = null;
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Date dataDate = new Date(rs.getTimestamp(1).getTime());
            date = dataDate;
        }
        dataSourceInfo.setLastDataTime(date);
        return dataSourceInfo;
    }
}
