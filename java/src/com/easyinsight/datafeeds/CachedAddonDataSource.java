package com.easyinsight.datafeeds;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.email.UserStub;
import com.easyinsight.logging.LogClass;
import com.easyinsight.scheduler.DataSourceScheduledTask;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.storage.IWhere;
import com.easyinsight.storage.StringWhere;
import com.easyinsight.users.Account;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 10/15/13
 * Time: 11:53 AM
 */
public class CachedAddonDataSource extends ServerDataSourceDefinition {

    private long reportID;

    @Override
    public FeedType getFeedType() {
        return FeedType.CACHED_ADDON;
    }

    public long getReportID() {
        return reportID;
    }

    public void setReportID(long reportID) {
        this.reportID = reportID;
    }

    public static void triggerUpdates(long dataSourceID, EIConnection conn) {
        System.out.println("Triggering updates for " + dataSourceID);
        //EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement delayStmt = conn.prepareStatement("SELECT recache_time FROM account WHERE account_id = ?");
            delayStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet delayRS = delayStmt.executeQuery();
            delayRS.next();
            int recacheTime = delayRS.getInt(1);
            delayStmt.close();

            PreparedStatement queryStmt = conn.prepareStatement("SELECT data_source_id from cached_addon_report_source, analysis where analysis.data_feed_id = ? and " +
                    "analysis.analysis_id = cached_addon_report_source.report_id");
            PreparedStatement nodeStmt = conn.prepareStatement("SELECT composite_feed.data_feed_id from composite_node, composite_feed where " +
                    "composite_node.data_feed_id = ? and composite_node.composite_feed_id = composite_feed.composite_feed_id");
            PreparedStatement fedStmt = conn.prepareStatement("SELECT FEDERATED_DATA_SOURCE.DATA_SOURCE_ID FROM FEDERATED_DATA_SOURCE_TO_DATA_SOURCE, FEDERATED_DATA_SOURCE " +
                    "WHERE FEDERATED_DATA_SOURCE_TO_DATA_SOURCE.DATA_SOURCE_ID = ? AND " +
                    "FEDERATED_DATA_SOURCE_TO_DATA_SOURCE.FEDERATED_DATA_SOURCE_ID = FEDERATED_DATA_SOURCE.FEDERATED_DATA_SOURCE_ID");

            queryStmt.setLong(1, dataSourceID);
            ResultSet rs = queryStmt.executeQuery();
            Set<Long> ids = new HashSet<Long>();

            while (rs.next()) {
                ids.add(rs.getLong(1));
            }
            nodeStmt.setLong(1, dataSourceID);
            Set<Long> nextIDs = new HashSet<Long>();
            ResultSet nodeRS = nodeStmt.executeQuery();
            while (nodeRS.next()) {
                long nodeID = nodeRS.getLong(1);
                queryStmt.setLong(1, nodeID);
                ResultSet detailRS = queryStmt.executeQuery();
                while (detailRS.next()) {
                    ids.add(detailRS.getLong(1));
                }
            }
            fedStmt.setLong(1, dataSourceID);
            ResultSet fedRS = fedStmt.executeQuery();
            while (fedRS.next()) {
                long fedID = fedRS.getLong(1);
                nextIDs.add(fedID);
            }
            for (Long id : nextIDs) {
                nodeStmt.setLong(1, id);
                nodeRS = nodeStmt.executeQuery();
                while (nodeRS.next()) {
                    long nodeID = nodeRS.getLong(1);
                    queryStmt.setLong(1, nodeID);
                    ResultSet detailRS = queryStmt.executeQuery();
                    while (detailRS.next()) {
                        ids.add(detailRS.getLong(1));
                    }
                }
            }
            fedStmt.close();
            queryStmt.close();
            nodeStmt.close();
            if (recacheTime == 0) {
                for (Long id : ids) {
                    try {
                        System.out.println("Running report " + id);
                        runReport(conn, id);
                    } catch (Exception e) {
                        LogClass.error(e);
                    }
                }
            } else {
                long interval = System.currentTimeMillis() + (1000 * 60 * recacheTime);
                for (Long id : ids) {
                    PreparedStatement saveLoadStmt = conn.prepareStatement("INSERT INTO cache_to_rebuild (cache_time, data_source_id) VALUES (?, ?)");
                    saveLoadStmt.setTimestamp(1, new Timestamp(interval));
                    saveLoadStmt.setLong(2, id);
                    saveLoadStmt.execute();
                    saveLoadStmt.close();
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            //Database.closeConnection(conn);
        }
    }

    public static void runUpdateFor(String urlKey) throws Exception {
        FeedResponse feedResponse = new FeedService().openFeedIfPossible(urlKey);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            long dataSourceID = feedResponse.getFeedDescriptor().getId();
            runReport(conn, dataSourceID);
            conn.commit();
        } catch (Exception e) {
            if (!conn.getAutoCommit()) {
                conn.rollback();
            }
            LogClass.error(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public static void runUpdates() throws Exception {
        Set<Long> sources = new HashSet<Long>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM cache_to_rebuild WHERE data_source_id = ?");

            PreparedStatement queryStmt = conn.prepareStatement("SELECT DISTINCT data_source_id FROM cache_to_rebuild WHERE cache_time < ?");
            long time = System.currentTimeMillis();
            queryStmt.setTimestamp(1, new Timestamp(time));
            ResultSet rs = queryStmt.executeQuery();

            while (rs.next()) {
                long dataSourceID = rs.getLong(1);
                sources.add(dataSourceID);
                clearStmt.setLong(1, dataSourceID);
                clearStmt.executeUpdate();
            }
            queryStmt.close();
            clearStmt.close();

        } finally {
            Database.closeConnection(conn);
        }


        for (Long id : sources) {
            conn = Database.instance().getConnection();
            try {
                System.out.println("Running report " + id);
                runReport(conn, id);
            } catch (Exception e) {
                if (!conn.getAutoCommit()) {
                    conn.rollback();
                }
                LogClass.error(e);
            } finally {
                conn.setAutoCommit(true);
                Database.closeConnection(conn);
            }
        }

    }

    public static boolean runReport(EIConnection conn, Long id) throws Exception {
        conn.setAutoCommit(false);
        FeedDefinition base = new FeedStorage().getFeedDefinitionData(id, conn);
        if (!(base instanceof IServerDataSourceDefinition)) {
            return true;
        }
        IServerDataSourceDefinition dataSource = (IServerDataSourceDefinition) base;
        UserStub dataSourceUser = null;
        List<FeedConsumer> owners = dataSource.getUploadPolicy().getOwners();
        for (FeedConsumer owner : owners) {
            if (owner.type() == FeedConsumer.USER) {
                dataSourceUser = (UserStub) owner;
            }
        }
        if (dataSourceUser == null) {
            LogClass.info("No user for data data source refresh.");
        } else {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT USERNAME, USER_ID, USER.ACCOUNT_ID, ACCOUNT.ACCOUNT_TYPE, USER.account_admin, USER.guest_user," +
                    "ACCOUNT.FIRST_DAY_OF_WEEK, USER.ANALYST, USER.TEST_ACCOUNT_VISIBLE FROM USER, ACCOUNT " +
                    "WHERE USER.ACCOUNT_ID = ACCOUNT.ACCOUNT_ID AND (ACCOUNT.account_state = ? OR ACCOUNT.ACCOUNT_STATE = ?) AND USER.USER_ID = ?");
            queryStmt.setInt(1, Account.ACTIVE);
            queryStmt.setInt(2, Account.TRIAL);
            queryStmt.setLong(3, dataSourceUser.getUserID());
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                String userName = rs.getString(1);
                long userID = rs.getLong(2);
                long accountID = rs.getLong(3);
                int accountType = rs.getInt(4);
                boolean accountAdmin = rs.getBoolean(5);
                int firstDayOfWeek = rs.getInt(7);
                PreparedStatement stmt = conn.prepareStatement("SELECT PERSONA.persona_name FROM USER, PERSONA WHERE USER.PERSONA_ID = PERSONA.PERSONA_ID AND USER.USER_ID = ?");
                stmt.setLong(1, userID);
                ResultSet personaRS = stmt.executeQuery();

                String personaName = null;
                if (personaRS.next()) {
                    personaName = personaRS.getString(1);
                }
                stmt.close();
                SecurityUtil.populateThreadLocal(userName, userID, accountID, accountType, accountAdmin, firstDayOfWeek, personaName);
                try {

                    if (DataSourceMutex.mutex().lock(dataSource.getDataFeedID())) {
                        try {
                            new DataSourceScheduledTask.DataSourceFactory().createSource(conn, new ArrayList<ReportFault>(), new Date(), base, dataSource, null).invoke();
                            conn.commit();
                        } finally {
                            DataSourceMutex.mutex().unlock(dataSource.getDataFeedID());
                        }
                    }

                } finally {
                    SecurityUtil.clearThreadLocal();
                }
            }


        }
        conn.commit();
        return false;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM cached_addon_report_source WHERE data_source_id = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO cached_addon_report_source (data_source_id, report_id) values (?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setLong(2, reportID);
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement getStmt = conn.prepareStatement("SELECT report_id FROM cached_addon_report_source WHERE data_source_id = ?");
        getStmt.setLong(1, getDataFeedID());
        ResultSet rs = getStmt.executeQuery();
        if (rs.next()) {
            reportID = rs.getLong(1);
        }
        getStmt.close();
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        Map<String, AnalysisItem> structure = report.createStructure();
        for (AnalysisItem item : structure.values()) {
            AnalysisItem clone;
            if (item.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                AnalysisDateDimension baseDate = (AnalysisDateDimension) item;
                AnalysisDateDimension date = new AnalysisDateDimension();
                date.setDateLevel(baseDate.getDateLevel());
                date.setOutputDateFormat(baseDate.getOutputDateFormat());
                date.setDateOnlyField(baseDate.isDateOnlyField() || baseDate.hasType(AnalysisItemTypes.DERIVED_DATE));
                clone = date;
            } else if (item.hasType(AnalysisItemTypes.MEASURE)) {
                AnalysisMeasure baseMeasure = (AnalysisMeasure) item;
                AnalysisMeasure measure = new AnalysisMeasure();
                measure.setFormattingType(item.getFormattingType());
                measure.setAggregation(baseMeasure.getAggregation());
                measure.setPrecision(baseMeasure.getPrecision());
                measure.setMinPrecision(baseMeasure.getMinPrecision());
                measure.setDefaultDate(baseMeasure.getDefaultDate());
                clone = measure;
            } else {
                clone = new AnalysisDimension();
            }
            clone.setOriginalDisplayName(item.toDisplay());
            clone.setDisplayName(report.getName() + " - " + item.toDisplay());
            clone.setUnqualifiedDisplayName(item.toUnqualifiedDisplay());
            clone.setBasedOnReportField(item.getAnalysisItemID());
            Key key = keys.get(clone.toDisplay());
            if (key == null) {
                key = new NamedKey(clone.toDisplay());
            }
            clone.setKey(key);
            items.add(clone);
        }
        if (report.getCachePartitionFilter() != null && !"".equals(report.getCachePartitionFilter())) {

            for (FilterDefinition filter : report.getFilterDefinitions()) {
                if (report.getCachePartitionFilter().equals(filter.getFilterName())) {
                    String name = filter.getFilterName() + "cache";
                    AnalysisItem clone = new AnalysisDimension();
                    clone.setDisplayName(name);
                    clone.setUnqualifiedDisplayName(name);
                    Key key = new NamedKey(clone.toDisplay());
                    clone.setKey(key);
                    items.add(clone);
                }
            }
        }
        return items;
    }

    protected boolean clearsData(FeedDefinition parentSource) {
        WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportID);
        return !(report.getCachePartitionFilter() != null && !"".equals(report.getCachePartitionFilter()));
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
            FilterDefinition partitionFilter = findPartitionFilter(report);
            if (partitionFilter == null) {
                cacheForPartition(IDataStorage, conn, report, null, null, false);
            } else if (partitionFilter instanceof FlatDateFilter) {
                FlatDateFilter flatDateFilter = (FlatDateFilter) partitionFilter;
                int startYear;
                if (flatDateFilter.getStartYear() != 0) {
                    startYear = flatDateFilter.getStartYear();
                    int endYear = 2014;
                    Calendar cal = Calendar.getInstance();
                    if (lastRefreshDate != null && lastRefreshDate.getTime() > 10000) {
                        cal.add(Calendar.YEAR, -1);
                    } else {
                        cal.set(Calendar.YEAR, startYear);
                    }


                    boolean keepGoing = true;
                    do {
                        int year = cal.get(Calendar.YEAR);
                        if (year > endYear) {
                            keepGoing = false;
                        } else {
                            System.out.println("Generating report for " + year);
                            flatDateFilter.setValue(year);
                            Key key = new NamedKey(flatDateFilter.getFilterName() + "cache");
                            cacheForPartition(IDataStorage, conn, report, key, String.valueOf(year), lastRefreshDate != null && lastRefreshDate.after(new Date(1000)));
                            cal.add(Calendar.YEAR, 1);
                        }
                    } while (keepGoing);
                }
            } else if (partitionFilter instanceof FilterValueDefinition) {

            }
        } catch (Exception e) {
            LogClass.error(e);
        }
        return null;
    }

    private FilterDefinition findPartitionFilter(WSAnalysisDefinition report) {
        String name = report.getCachePartitionFilter();
        if (name == null || "".equals(name)) {
            return null;
        }
        List<FilterDefinition> filters = report.getFilterDefinitions();
        for (FilterDefinition filter : filters) {
            if (name.equals(filter.getFilterName())) {
                return filter;
            }
        }
        return null;
    }

    private void cacheForPartition(IDataStorage IDataStorage, EIConnection conn, WSAnalysisDefinition report, @Nullable Key partitionKey, @Nullable String partitionValue, boolean update) throws Exception {
        Map<AnalysisItem, AnalysisItem> map = new HashMap<AnalysisItem, AnalysisItem>();
        Map<String, AnalysisItem> structure = report.createStructure();
        for (AnalysisItem reportItem : structure.values()) {
            for (AnalysisItem field : getFields()) {
                if (field.getBasedOnReportField() != null && field.getBasedOnReportField() == reportItem.getAnalysisItemID()) {
                    map.put(reportItem, field);
                    System.out.println("mapping " + reportItem.toDisplay() + " into " + field.toDisplay() + " with key " + field.getKey().toKeyString());
                    break;
                }
            }
        }
        InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
        DataSet reportSet = DataService.listDataSet(report, insightRequestMetadata, conn);
        DataSet dataSet = new DataSet();
        for (IRow reportRow : reportSet.getRows()) {
            IRow row = dataSet.createRow();
            for (AnalysisItem item : structure.values()) {
                AnalysisItem dataSourceItem = map.get(item);
                row.addValue(dataSourceItem.getKey(), reportRow.getValue(item.createAggregateKey()));
            }
            if (partitionKey != null) {
                row.addValue(partitionKey, partitionValue);
            }
        }
        if (partitionKey != null && partitionValue != null && update) {
            IDataStorage.updateData(dataSet, Arrays.asList((IWhere) new StringWhere(partitionKey, partitionValue)));
        } else {
            IDataStorage.insertData(dataSet);
        }
    }

    @Override
    protected String getUpdateKeyName() {
        WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportID);
        if (report.getCachePartitionFilter() != null && !"".equals(report.getCachePartitionFilter())) {
            return report.getCachePartitionFilter() + "cache";
        }
        return null;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return new ArrayList<String>();
    }
}
