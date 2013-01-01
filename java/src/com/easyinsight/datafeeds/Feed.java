package com.easyinsight.datafeeds;


import com.easyinsight.core.Value;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.kpi.KPI;
import com.easyinsight.security.SecurityUtil;
import org.hibernate.Session;

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
    private FeedDefinition dataSource;
    private long feedID;
    private List<AnalysisItem> fields;
    private List<FeedNode> fieldHierarchy;
    private int version;
    private String name;
    private String attribution;
    private boolean visible;
    private String filterExampleMessage;
    private int type;
    private boolean exchangeSave;
    private String urlKey;
    private Map<String, String> properties;
    private String marmotScript;
    private FeedType feedType;

    public void setMarmotScript(String marmotScript) {
        this.marmotScript = marmotScript;
    }

    public String getMarmotScript() {
        return marmotScript;
    }

    protected FeedDefinition getParentSource(EIConnection conn) throws SQLException {
        return new FeedStorage().getFeedDefinitionData(getDataSource().getParentSourceID());
    }

    public FeedDefinition getDataSource() {
        return dataSource;
    }

    public void setDataSource(FeedDefinition dataSource) {
        this.dataSource = dataSource;
    }

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

    public boolean isExchangeSave() {
        return exchangeSave;
    }

    public void setExchangeSave(boolean exchangeSave) {
        this.exchangeSave = exchangeSave;
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
        dataSourceInfo.setLiveDataSource(getDataSource().getDataSourceType() == DataSourceInfo.LIVE);
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

    public List<FilterDefinition> getIntrinsicFilters(EIConnection conn) {
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

    public List<Long> getDataSourceIDs() {
        List<Long> ids = new ArrayList<Long>();
        ids.add(feedID);
        return ids;
    }

    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSAnalysisDefinition report, List<FilterDefinition> otherFilters, FilterDefinition requester) {
        AnalysisItemResultMetadata metadata = analysisItem.createResultMetadata();
        WSListDefinition tempList = new WSListDefinition();
        if (report != null) {
            tempList.setMarmotScript(report.getMarmotScript());
        }
        tempList.setFilterDefinitions(new ArrayList<FilterDefinition>());
        if (report != null && report.getAddedItems() != null && report.getAddedItems().size() > 0) {
            tempList.setAddedItems(report.getAddedItems());
        }
        List<AnalysisItem> columns = new ArrayList<AnalysisItem>();
        // Kerry Skitch, 562-968
        columns.add(analysisItem);
        tempList.setDataFeedID(getFeedID());
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();

        if (requester != null && otherFilters != null && requester.getParentFilters() != null) {
            String[] targetNames = requester.getParentFilters().split(",");
            for (String targetName : targetNames) {
                for (FilterDefinition filterDefinition : otherFilters) {
                    if (targetName.equals(filterDefinition.getFilterName())) {
                        filters.add(filterDefinition);
                        columns.add(filterDefinition.getField());
                    }
                }
            }
        }

        try {
            PreparedStatement dlsStmt = conn.prepareStatement("SELECT user_dls_to_filter.FILTER_ID FROM user_dls_to_filter, user_dls, dls where " +
                        "user_dls_to_filter.user_dls_id = user_dls.user_dls_id and user_dls.dls_id = dls.dls_id and dls.data_source_id = ? and " +
                        "user_dls.user_id = ?");
            dlsStmt.setLong(1, feedID);
            dlsStmt.setLong(2, SecurityUtil.getUserID(false));

            ResultSet dlsRS = dlsStmt.executeQuery();
            while (dlsRS.next()) {
                long filterID = dlsRS.getLong(1);
                Session session = Database.instance().createSession(conn);
                try {
                    List results = session.createQuery("from FilterDefinition where filterID = ?").setLong(0, filterID).list();
                    if (results.size() > 0) {
                        FilterDefinition filter = (FilterDefinition) results.get(0);
                        filter.getField().afterLoad();
                        filter.afterLoad();
                        filters.add(filter);
                        columns.add(filter.getField());
                    }
                } finally {
                    session.close();
                }
            }
            dlsStmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Iterator<FilterDefinition> iter = filters.iterator();
        while (iter.hasNext()) {
            FilterDefinition filter = iter.next();
            if (!filter.isEnabled()) {
                iter.remove();
            } else {
                if (filter instanceof FilterValueDefinition) {
                    FilterValueDefinition filterValueDefinition = (FilterValueDefinition) filter;
                    if (filterValueDefinition.isAllOption() && filterValueDefinition.getFilteredValues().size() == 1 &&
                            "All".equals(filterValueDefinition.getFilteredValues().get(0).toString())) {
                        iter.remove();
                    }
                } else if (filter instanceof RollingFilterDefinition) {
                    RollingFilterDefinition rollingFilterDefinition = (RollingFilterDefinition) filter;
                    if (rollingFilterDefinition.getInterval() == MaterializedRollingFilterDefinition.ALL) {
                        iter.remove();
                    }
                }
            }
        }

        tempList.setColumns(columns);
        tempList.setJoinOverrides(insightRequestMetadata.getJoinOverrides());
        tempList.setFullJoins(insightRequestMetadata.isTraverseAllJoins());
        DataSet dataSet = DataService.listDataSet(tempList, insightRequestMetadata, conn);
        for (FilterDefinition filter : filters) {
            MaterializedFilterDefinition mFilter = filter.materialize(insightRequestMetadata);
            Iterator<IRow> rowIter = dataSet.getRows().iterator();
            while (rowIter.hasNext()) {
                IRow row = rowIter.next();
                Value value = row.getValue(filter.getField());
                if (!mFilter.allows(value)) {
                    rowIter.remove();
                }
            }
        }
        for (IRow row : dataSet.getRows()) {
            metadata.addValue(analysisItem, analysisItem.polishValue(row.getValue(analysisItem.createAggregateKey())), insightRequestMetadata);
        }
        return metadata;
    }

    public abstract DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException;

    public List<KPI> createKPIs() {
        return new ArrayList<KPI>();
    }

    public DataSourceInfo createSourceInfo(EIConnection conn) throws SQLException {
        PreparedStatement versionStmt = conn.prepareStatement("SELECT MAX(FEED_PERSISTENCE_METADATA.VERSION) FROM FEED_PERSISTENCE_METADATA, DATA_FEED WHERE " +
                "DATA_FEED.DATA_FEED_ID = FEED_PERSISTENCE_METADATA.feed_id AND " +
                "(DATA_FEED.DATA_FEED_ID = ? OR DATA_FEED.PARENT_SOURCE_ID = ?)");
        versionStmt.setLong(1, getFeedID());
        versionStmt.setLong(2, getFeedID());
        int version;
        Date date = null;
        ResultSet versionRS = versionStmt.executeQuery();
        if (versionRS.next()) {
            version = versionRS.getInt(1);
            PreparedStatement stmt = conn.prepareStatement("SELECT FEED_PERSISTENCE_METADATA.last_data_time FROM DATA_FEED," +
                "FEED_PERSISTENCE_METADATA WHERE DATA_FEED.DATA_FEED_ID = FEED_PERSISTENCE_METADATA.feed_id AND " +
                "(DATA_FEED.DATA_FEED_ID = ? OR DATA_FEED.PARENT_SOURCE_ID = ?) AND FEED_PERSISTENCE_METADATA.VERSION = ?");
            stmt.setLong(1, getFeedID());
            stmt.setLong(2, getFeedID());
            stmt.setInt(3, version);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Date date1 = new Date(rs.getTimestamp(1).getTime());
                if (date == null || date.getTime() < date1.getTime()) {
                    date = date1;
                }
            }
            stmt.close();
        }
        DataSourceInfo dataSourceInfo = getDataSourceInfo();
        dataSourceInfo.setLastDataTime(date);
        PreparedStatement scheduleStmt = conn.prepareStatement("SELECT SCHEDULED_DATA_SOURCE_REFRESH_ID FROM SCHEDULED_DATA_SOURCE_REFRESH WHERE SCHEDULED_DATA_SOURCE_REFRESH.data_source_id = ?");
        scheduleStmt.setLong(1, getFeedID());
        ResultSet scheduleRS = scheduleStmt.executeQuery();
        dataSourceInfo.setScheduled(scheduleRS.next());
        versionStmt.close();
        scheduleStmt.close();
        return dataSourceInfo;
    }

    public void setFeedType(FeedType feedType) {
        this.feedType = feedType;
    }

    public FeedType getFeedType() {
        return feedType;
    }
}
