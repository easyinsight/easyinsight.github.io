package com.easyinsight.datafeeds.composite;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.core.Key;
import com.easyinsight.intention.Intention;
import com.easyinsight.intention.IntentionSuggestion;
import com.easyinsight.scorecard.DataSourceRefreshEvent;
import com.easyinsight.users.User;
import com.easyinsight.users.Account;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.sql.*;
import java.util.*;
import java.util.Date;

import com.easyinsight.util.ServiceUtil;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;

/**
 * User: James Boe
 * Date: Jun 16, 2009
 * Time: 9:45:02 AM
 */
public abstract class CompositeServerDataSource extends CompositeFeedDefinition implements IServerDataSourceDefinition {

    private String username;
    private String password;
    private String sessionId;

    private List<CompositeFeedConnection> addonConnections;

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement dStmt = conn.prepareStatement("DELETE FROM data_source_additional_connection WHERE data_source_id = ?");
        dStmt.setLong(1, getDataFeedID());
        dStmt.executeUpdate();
        dStmt.close();
        if (addonConnections != null) {
            PreparedStatement iStmt = conn.prepareStatement("INSERT INTO data_source_additional_connection (data_source_id, connection_id) values (?, ?)");
            for (CompositeFeedConnection connection : addonConnections) {
                iStmt.setLong(1, getDataFeedID());
                iStmt.setLong(2, connection.store(conn, 0));
                iStmt.execute();
            }
            iStmt.close();
        }
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement qStmt = conn.prepareStatement("SELECT connection_id FROM data_source_additional_connection WHERE data_source_id = ?");
        qStmt.setLong(1, getDataFeedID());
        ResultSet rs = qStmt.executeQuery();
        List<CompositeFeedConnection> edges = new ArrayList<CompositeFeedConnection>();
        PreparedStatement queryConnStmt = conn.prepareStatement("SELECT SOURCE_FEED_NODE_ID, TARGET_FEED_NODE_ID," +
                "SOURCE_JOIN, TARGET_JOIN, SOURCE_ITEM_ID, TARGET_ITEM_ID, left_join, right_join, left_join_on_original, right_join_on_original, marmot_script, SOURCE_REPORT_ID, TARGET_REPORT_ID " +
                " FROM COMPOSITE_CONNECTION WHERE COMPOSITE_CONNECTION_ID = ?");
        PreparedStatement nameStmt = conn.prepareStatement("SELECT FEED_NAME FROM DATA_FEED WHERE DATA_FEED_ID = ?");
        PreparedStatement reportNameStmt = conn.prepareStatement("SELECT TITLE FROM ANALYSIS WHERE ANALYSIS_ID = ?");
        while (rs.next()) {
            long connectionID = rs.getLong(1);

            queryConnStmt.setLong(1, connectionID);

            ResultSet connectionRS = queryConnStmt.executeQuery();
            while (connectionRS.next()) {
                long sourceID = connectionRS.getLong(1);
                long targetID = connectionRS.getLong(2);
                long sourceReportID = connectionRS.getLong(12);
                long targetReportID = connectionRS.getLong(13);
                String sourceName;
                String targetName;
                if (sourceID > 0) {
                    nameStmt.setLong(1, sourceID);
                    ResultSet nameRS = nameStmt.executeQuery();
                    nameRS.next();

                    sourceName = nameRS.getString(1);
                } else {
                    reportNameStmt.setLong(1, sourceReportID);
                    ResultSet nameRS = reportNameStmt.executeQuery();
                    nameRS.next();
                    sourceName = nameRS.getString(1);
                }

                if (targetID > 0) {
                    nameStmt.setLong(1, targetID);
                    ResultSet nameRS = nameStmt.executeQuery();
                    nameRS.next();

                    targetName = nameRS.getString(1);
                } else {
                    reportNameStmt.setLong(1, targetReportID);
                    ResultSet nameRS = reportNameStmt.executeQuery();
                    nameRS.next();
                    targetName = nameRS.getString(1);
                }

                long sourceKeyID = connectionRS.getLong(3);
                if (connectionRS.wasNull()) {
                    AnalysisItem sourceItem = getItem(conn, connectionRS.getLong(5));
                    AnalysisItem targetItem = getItem(conn, connectionRS.getLong(6));
                    boolean sourceJoin = connectionRS.getBoolean(7);
                    boolean targetJoin = connectionRS.getBoolean(8);
                    boolean sourceJoinOnOriginal = connectionRS.getBoolean(9);
                    boolean targetJoinOnOriginal = connectionRS.getBoolean(10);
                    String marmotScript = connectionRS.getString(11);
                    CompositeFeedConnection compositeFeedConnection = new CompositeFeedConnection(sourceID, targetID, sourceItem, targetItem, sourceName, targetName,
                            sourceJoin, targetJoin, sourceJoinOnOriginal, targetJoinOnOriginal, marmotScript);
                    compositeFeedConnection.setSourceReportID(sourceReportID);
                    compositeFeedConnection.setTargetReportID(targetReportID);
                    edges.add(compositeFeedConnection);
                } else {
                    Key sourceKey = getKey(conn, sourceKeyID);
                    Key targetKey = getKey(conn, connectionRS.getLong(4));
                    boolean sourceJoin = connectionRS.getBoolean(7);
                    boolean targetJoin = connectionRS.getBoolean(8);
                    boolean sourceJoinOnOriginal = connectionRS.getBoolean(9);
                    boolean targetJoinOnOriginal = connectionRS.getBoolean(10);
                    String marmotScript = connectionRS.getString(11);
                    CompositeFeedConnection compositeFeedConnection = new CompositeFeedConnection(sourceID, targetID, sourceKey, targetKey, sourceName, targetName,
                            sourceJoin, targetJoin, sourceJoinOnOriginal, targetJoinOnOriginal, marmotScript);
                    compositeFeedConnection.setSourceReportID(sourceReportID);
                    compositeFeedConnection.setTargetReportID(targetReportID);
                    edges.add(compositeFeedConnection);
                }
            }
        }
        qStmt.close();
        queryConnStmt.close();
        nameStmt.close();
        reportNameStmt.close();
        addonConnections = edges;
    }

    public List<CompositeFeedConnection> getAddonConnections() {
        return addonConnections;
    }

    public void setAddonConnections(List<CompositeFeedConnection> addonConnections) {
        this.addonConnections = addonConnections;
    }

    public boolean hasNewData(Date lastRefreshDate, FeedDefinition parent, EIConnection conn) throws Exception {
        List<IServerDataSourceDefinition> sources = obtainChildDataSources(conn);
        boolean hasNew = false;
        for (IServerDataSourceDefinition source : sources) {
            hasNew = hasNew || source.hasNewData(lastRefreshDate, this, conn);
        }
        return hasNew;
    }

    public void reauthorize() {

    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    protected IServerDataSourceDefinition createForFeedType(FeedType feedType) {
        return (IServerDataSourceDefinition) new DataSourceTypeRegistry().createDataSource(feedType);
    }

    public void exchangeTokens(EIConnection conn, HttpServletRequest request, String externalPin) throws Exception {
    }

    protected abstract Set<FeedType> getFeedTypes();

    protected abstract Collection<ChildConnection> getChildConnections();

    protected Collection<ChildConnection> getLiveChildConnections() {
        return getChildConnections();
    }

    public long create(EIConnection conn, List<AnalysisItem> externalAnalysisItems, FeedDefinition parentDefinition) throws Exception {
        setOwnerName(retrieveUser(conn, SecurityUtil.getUserID()).getUserName());
        UploadPolicy uploadPolicy = new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID());
        setUploadPolicy(uploadPolicy);
        FeedCreationResult feedCreationResult = new FeedCreation().createFeed(this, conn, null, uploadPolicy);
        obtainChildDataSources(conn);
        new FeedStorage().updateDataFeedConfiguration(this, conn);
        return feedCreationResult.getFeedID();
    }

    protected List<IServerDataSourceDefinition> obtainChildDataSources(EIConnection conn) throws Exception {
        List<IServerDataSourceDefinition> dataSources;
        Map<FeedType, IServerDataSourceDefinition> feedMap = new HashMap<FeedType, IServerDataSourceDefinition>();
        //List<CompositeFeedNode> nodes = new ArrayList<CompositeFeedNode>();
        boolean newSource = getCompositeFeedNodes().size() == 0;
        if (newSource) {
            dataSources = childDataSources(conn);
            for (IServerDataSourceDefinition definition : dataSources) {
                /*CompositeFeedNode node = new CompositeFeedNode();
                node.setDataFeedID(definition.getDataFeedID());
                node.setDataSourceType(definition.getFeedType().getType());*/
                feedMap.put(definition.getFeedType(), definition);
                //nodes.add(node);
            }

            //setCompositeFeedNodes(nodes);
            List<CompositeFeedConnection> connections = new ArrayList<CompositeFeedConnection>();
            for (ChildConnection childConnection : getChildConnections()) {
                IServerDataSourceDefinition sourceDef = feedMap.get(childConnection.getSourceFeedType());
                IServerDataSourceDefinition targetDef = feedMap.get(childConnection.getTargetFeedType());
                Key sourceKey = sourceDef.getField(childConnection.getSourceKey());
                Key targetKey = targetDef.getField(childConnection.getTargetKey());
                CompositeFeedConnection connection = new CompositeFeedConnection(sourceDef.getDataFeedID(), targetDef.getDataFeedID(),
                        sourceKey, targetKey, sourceDef.getFeedName(), targetDef.getFeedName(), false, false, false, false);
                connections.add(connection);
            }
            setConnections(connections);
            populateFields(conn);
        } else {
            int nodeSize = getCompositeFeedNodes().size();
            dataSources = childDataSources(conn);
            int newNodeSize = getCompositeFeedNodes().size();
            if (newNodeSize > nodeSize) {
                populateFields(conn);
            }
        }
        List<IServerDataSourceDefinition> s = sortSources(dataSources);
        if (s == null) {
            s = dataSources;
        }
        return s;
    }

    protected List<IServerDataSourceDefinition> childDataSources(EIConnection conn) throws Exception {
        FeedStorage feedStorage = new FeedStorage();
        List<IServerDataSourceDefinition> dataSources = new ArrayList<IServerDataSourceDefinition>();
        Set<FeedType> feedTypes = getFeedTypes();
        Map<FeedType, FeedDefinition> feedTypeMap = new HashMap<FeedType, FeedDefinition>();
        boolean newSource = getCompositeFeedNodes().size() == 0;
        for (CompositeFeedNode node : getCompositeFeedNodes()) {
            FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(node.getDataFeedID(), conn);
            if (feedDefinition instanceof IServerDataSourceDefinition) {
                dataSources.add((IServerDataSourceDefinition) feedDefinition);
            }
            feedTypeMap.put(feedDefinition.getFeedType(), feedDefinition);
        }

        newSource = newSource || getFeedType().getType() == FeedType.ZENDESK_COMPOSITE.getType() ||
                getFeedType().getType() == FeedType.CONSTANT_CONTACT.getType() ||
                getFeedType().getType() == FeedType.HARVEST_COMPOSITE.getType() ||
                getFeedType().getType() == FeedType.INSIGHTLY_COMPOSITE.getType() ||
                getFeedType().getType() == FeedType.INFUSIONSOFT_COMPOSITE.getType();

        for (FeedType feedType : feedTypes) {
            FeedDefinition existing = feedTypeMap.get(feedType);
            if (existing == null && newSource) {
                IServerDataSourceDefinition definition = createForFeedType(feedType);
                newDefinition(definition, conn, "", getUploadPolicy());
                CompositeFeedNode node = new CompositeFeedNode();
                node.setDataFeedID(definition.getDataFeedID());
                node.setDataSourceType(feedType.getType());
                getCompositeFeedNodes().add(node);
                dataSources.add(definition);
            }
        }
        return dataSources;
    }

    protected List<IServerDataSourceDefinition> sortSources(List<IServerDataSourceDefinition> children) {
        return null;
    }

    public List<CompositeFeedConnection> obtainChildConnections() throws SQLException {
        Map<FeedType, CompositeFeedNode> feedMap = new HashMap<FeedType, CompositeFeedNode>();
        for (CompositeFeedNode child : getCompositeFeedNodes()) {
            //FeedDefinition childDef = new FeedStorage().getFeedDefinitionData(child.getDataFeedID());
            feedMap.put(new FeedType(child.getDataSourceType()), child);
        }
        List<CompositeFeedConnection> connections = new ArrayList<CompositeFeedConnection>();
        for (ChildConnection childConnection : getLiveChildConnections()) {
            CompositeFeedNode sourceDef = feedMap.get(childConnection.getSourceFeedType());
            CompositeFeedNode targetDef = feedMap.get(childConnection.getTargetFeedType());

            if (sourceDef != null && targetDef != null) {
                CompositeFeedConnection connection = childConnection.createConnection(sourceDef.getDataFeedID(), sourceDef.getDataSourceName(),
                        targetDef.getDataFeedID(), targetDef.getDataSourceName(), this);
                if (connection != null) {
                    connections.add(connection);
                }
            } else if (sourceDef != null) {
                LogClass.error("Could not find child of type " + childConnection.getSourceFeedType().getType());
            } else if (targetDef != null) {
                LogClass.error("Could not find child of type " + childConnection.getTargetFeedType().getType());
            }
        }
        connections.addAll(getAdditionalConnections());
        if (addonConnections != null) {
            connections.addAll(addonConnections);
        }

        return connections;
    }

    protected List<CompositeFeedConnection> getAdditionalConnections() throws SQLException {
        return new ArrayList<CompositeFeedConnection>();
    }

    public void newDefinition(IServerDataSourceDefinition definition, EIConnection conn, String userName, UploadPolicy uploadPolicy) throws Exception {
        DataStorage metadata = null;
        try {
            FeedDefinition feedDefinition = (FeedDefinition) definition;
            feedDefinition.setVisible(false);
            Map<String, Key> keys = feedDefinition.newDataSourceFields(this);
            DataSet dataSet = new DataSet();
            List<AnalysisItem> fields = feedDefinition.createAnalysisItems(keys, conn, this);
            feedDefinition.setFields(fields);
            for (AnalysisItem field : fields) {
                if (field.getFolder() != null) {
                    FeedFolder folder = feedDefinition.defineFolder(field.getFolder());
                    folder.addAnalysisItem(field);
                }
            }
            feedDefinition.setOwnerName(userName);
            feedDefinition.setParentSourceID(getDataFeedID());
            feedDefinition.setUploadPolicy(uploadPolicy);
            FeedCreationResult feedCreationResult = new FeedCreation().createFeed(feedDefinition, conn, dataSet, uploadPolicy);
            metadata = feedCreationResult.getTableDefinitionMetadata();
            if (metadata != null) {
                metadata.commit();
            }
        } catch (SQLException e) {
            if (metadata != null) {
                metadata.rollback();
            }
            throw e;
        } finally {
            if (metadata != null) {
                metadata.closeConnection();
            }
        }
    }

    public static User retrieveUser(Connection conn, long userID) {
        try {
            User user = null;
            Session session = Database.instance().createSession(conn);
            List results;
            try {
                session.beginTransaction();
                results = session.createQuery("from User where userID = ?").setLong(0, userID).list();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new RuntimeException(e);
            } finally {
                session.close();
            }
            if (results.size() > 0) {
                user = (User) results.get(0);
            }
            return user;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return null;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionId() {
        return sessionId;
    }

    protected void beforeRefresh(Date lastRefreshTime) {

    }

    public void validateTableSetup(EIConnection conn) throws SQLException {
    }

    public boolean refreshData(long accountID, Date now, EIConnection conn, FeedDefinition parentDefinition, String callDataID, Date lastRefreshTime, boolean fullRefresh, List<ReportFault> warnings,
                               Map<String, Object> refreshProperties) throws Exception {
        boolean changed = false;
        DataTypeMutex.mutex().lock(getFeedType(), getDataFeedID());
        try {
            long startTime = System.currentTimeMillis();
            // record start time
            if (getFeedType().getType() == FeedType.BATCHBOOK_COMPOSITE.getType() ||
                    getFeedType().getType() == FeedType.SALESFORCE.getType()) {
                conn.commit();
                conn.setAutoCommit(true);
                beforeRefresh(lastRefreshTime);
                conn.setAutoCommit(false);
            } else {
                beforeRefresh(lastRefreshTime);
            }

            int nodeSize = getCompositeFeedNodes().size();
            List<IServerDataSourceDefinition> sources = obtainChildDataSources(conn);
            int afterNodeSize = getCompositeFeedNodes().size();
            changed = nodeSize != afterNodeSize;
            Map<Long, Map<String, Key>> keyMap = new HashMap<Long, Map<String, Key>>();
            for (IServerDataSourceDefinition source : sources) {
                source.validateTableSetup(conn);
                if (callDataID != null) {
                    DataSourceRefreshEvent info = new DataSourceRefreshEvent();
                    info.setDataSourceName("Looking for any changed fields on " + source.getFeedName());
                    ServiceUtil.instance().updateStatus(callDataID, ServiceUtil.RUNNING, info);
                }
                ServerDataSourceDefinition serverDataSourceDefinition = (ServerDataSourceDefinition) source;
                MigrationResult migrationResult = serverDataSourceDefinition.migrations(conn, this);
                changed = migrationResult.isChanged() || changed;
                keyMap.put(serverDataSourceDefinition.getDataFeedID(), migrationResult.getKeyMap());
            }
            conn.commit();
            conn.setAutoCommit(true);
            Map<Long, String> tempTables = new HashMap<Long, String>();
            for (IServerDataSourceDefinition source : sources) {
                ServerDataSourceDefinition serverDataSourceDefinition = (ServerDataSourceDefinition) source;
                if (serverDataSourceDefinition.getDataSourceType() == DataSourceInfo.LIVE) {
                    continue;
                }
                if (callDataID != null) {
                    DataSourceRefreshEvent info = new DataSourceRefreshEvent();
                    info.setDataSourceName("Retrieving data from " + source.getFeedName());
                    ServiceUtil.instance().updateStatus(callDataID, ServiceUtil.RUNNING, info);
                }
                tempTables.put(serverDataSourceDefinition.getDataFeedID(),
                        serverDataSourceDefinition.tempLoad(keyMap.get(serverDataSourceDefinition.getDataFeedID()), now,
                                this, callDataID, lastRefreshTime, conn, fullRefresh, refreshProperties));
            }
            conn.setAutoCommit(false);
            for (IServerDataSourceDefinition source : sources) {
                ServerDataSourceDefinition serverDataSourceDefinition = (ServerDataSourceDefinition) source;
                if (serverDataSourceDefinition.getDataSourceType() == DataSourceInfo.LIVE) {
                    continue;
                }
                if (callDataID != null) {
                    DataSourceRefreshEvent info = new DataSourceRefreshEvent();
                    info.setDataSourceName("Persisting data from " + source.getFeedName());
                    ServiceUtil.instance().updateStatus(callDataID, ServiceUtil.RUNNING, info);
                }
                String tempTable = tempTables.get(serverDataSourceDefinition.getDataFeedID());
                serverDataSourceDefinition.applyTempLoad(conn, accountID, this, lastRefreshTime, tempTable, fullRefresh, warnings, null);
            }
            refreshDone();
            // record end time
            long elapsed = System.currentTimeMillis() - startTime;
            PreparedStatement timeStmt = conn.prepareStatement("INSERT INTO DATA_SOURCE_REFRESH_AUDIT (DATA_SOURCE_ID, ACCOUNT_ID, REFRESH_DATE, ELAPSED, SERVER_ID) VALUES (?, ?, ?, ?, ?)");
            timeStmt.setLong(1, getDataFeedID());
            timeStmt.setLong(2, accountID);
            timeStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            timeStmt.setLong(4, elapsed);
            timeStmt.setString(5, "");
            timeStmt.execute();
            timeStmt.close();
            CachedAddonDataSource.triggerUpdates(getDataFeedID());
        } finally {
            DataTypeMutex.mutex().unlock(getFeedType());
        }

        return changed;
    }

    protected void refreshDone() {
        if (getRefreshMarmotScript() != null) {
            StringTokenizer toker = new StringTokenizer(getRefreshMarmotScript(), "\r\n");
            while (toker.hasMoreTokens()) {
                String line = toker.nextToken();
                try {
                    new ReportCalculation(line).apply(this);
                } catch (ReportException re) {
                    throw re;
                } catch (Exception e) {
                    LogClass.error(e);
                    throw new ReportException(new AnalysisItemFault(e.getMessage() + " in the calculation of data source code " + line + ".", null));
                }
            }
        }
    }

    public String retrievePassword() {
        return password;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    protected FilterDefinition excludeFilter(String fieldName, WSAnalysisDefinition report, List<AnalysisItem> fields) {
        AnalysisItem target = null;
        for (AnalysisItem field : fields) {
            if (field.toDisplay().equals(fieldName)) {
                target = field;
            }
        }
        FilterValueDefinition excludeFilter = new FilterValueDefinition(target, false, Arrays.asList((Object) EmptyValue.EMPTY_VALUE));
        excludeFilter.setShowOnReportView(false);
        return excludeFilter;
    }

    protected AnalysisItem findFieldFromList(String fieldName, List<AnalysisItem> fields) {
        AnalysisItem target = null;
        for (AnalysisItem field : fields) {
            if (field.toDisplay().equals(fieldName)) {
                target = field;
            }
        }
        return target;
    }

    public List<IntentionSuggestion> suggestIntentions(WSAnalysisDefinition report, DataSourceInfo dataSourceInfo) {
        List<IntentionSuggestion> suggestions = super.suggestIntentions(report, dataSourceInfo);
        /*Date lastDataTime = dataSourceInfo.getLastDataTime();
        if (lastDataTime != null) {
            long time = System.currentTimeMillis() - lastDataTime.getTime();
            long days = time / (1000 * 60 * 60 * 24);
            if (days > 2) {
                suggestions.add(new IntentionSuggestion("Refresh Data",
                    "Your " + getFeedName() + " data source hasn't been refreshed with new data in " + days + " days.",
                    IntentionSuggestion.SCOPE_DATA_SOURCE, IntentionSuggestion.OLD_DATA, IntentionSuggestion.WARNING));
            }
        }*/
        return suggestions;
    }

    public List<Intention> createIntentions(WSAnalysisDefinition report, List<AnalysisItem> fields, int type) throws SQLException {
        List<Intention> intentions = new ArrayList<Intention>();
        /*if (type == IntentionSuggestion.OLD_DATA) {
            DataSourceIntention dataSourceIntention = new DataSourceIntention();
            dataSourceIntention.setRefreshData(true);
            intentions.add(dataSourceIntention);
        }*/
        return intentions;
    }

    protected List<JoinOverride> fromChildConnections(List<ChildConnection> childConnections, List<AnalysisItem> fields) throws SQLException {
        List<JoinOverride> joinOverrides = new ArrayList<JoinOverride>();
        Map<FeedType, CompositeFeedNode> feedMap = new HashMap<FeedType, CompositeFeedNode>();
        for (CompositeFeedNode child : getCompositeFeedNodes()) {
            //FeedDefinition childDef = new FeedStorage().getFeedDefinitionData(child.getDataFeedID());
            feedMap.put(new FeedType(child.getDataSourceType()), child);
        }
        for (ChildConnection childConnection : childConnections) {
            CompositeFeedNode sourceDef = feedMap.get(childConnection.getSourceFeedType());
            CompositeFeedNode targetDef = feedMap.get(childConnection.getTargetFeedType());
            if (sourceDef == null || targetDef == null) {
                continue;
            }
            CompositeFeedConnection connection = childConnection.createConnection(sourceDef.getDataFeedID(), sourceDef.getDataSourceName(),
                    targetDef.getDataFeedID(), targetDef.getDataSourceName(), this);
            JoinOverride joinOverride = new JoinOverride();
            joinOverride.setSourceName(sourceDef.getDataSourceName());
            joinOverride.setTargetName(targetDef.getDataSourceName());
            joinOverride.setDataSourceID(getDataFeedID());
            joinOverride.setSourceItem(findSourceItem(connection, fields));
            joinOverride.setTargetItem(findTargetItem(connection, fields));
            joinOverride.setSourceJoinOriginal(connection.isSourceJoinOnOriginal());
            joinOverride.setTargetJoinOriginal(connection.isTargetJoinOnOriginal());
            joinOverride.setSourceOuterJoin(connection.isSourceOuterJoin());
            joinOverride.setTargetOuterJoin(connection.isTargetOuterJoin());
            joinOverrides.add(joinOverride);
        }
        return joinOverrides;
    }

    private AnalysisItem findSourceItem(CompositeFeedConnection connection, List<AnalysisItem> items) {
        AnalysisItem analysisItem = null;
        for (AnalysisItem item : items) {
            Key key = item.getKey();
            if (key instanceof DerivedKey) {
                DerivedKey derivedKey = (DerivedKey) key;
                if (derivedKey.getFeedID() == connection.getSourceFeedID()) {
                    if (connection.getSourceJoin() != null) {
                        if (item.hasType(AnalysisItemTypes.DIMENSION) && item.getKey().toKeyString().equals(connection.getSourceJoin().toKeyString())) {
                            analysisItem = item;
                            break;
                        }
                    } else {

                        if (connection.getSourceItem().getKey().toKeyString().equals(item.getKey().toKeyString())) {
                            analysisItem = item;
                            break;
                        }
                    }
                }
            }

        }
        return analysisItem;
    }

    private AnalysisItem findTargetItem(CompositeFeedConnection connection, List<AnalysisItem> items) {
        AnalysisItem analysisItem = null;
        for (AnalysisItem item : items) {
            Key key = item.getKey();
            if (key instanceof DerivedKey) {
                DerivedKey derivedKey = (DerivedKey) key;
                if (derivedKey.getFeedID() == connection.getTargetFeedID()) {
                    if (connection.getTargetJoin() != null) {
                        if (item.hasType(AnalysisItemTypes.DIMENSION) && item.getKey().toKeyString().equals(connection.getTargetJoin().toKeyString())) {
                            analysisItem = item;
                            break;
                        }
                    } else {

                        if (connection.getTargetItem().getKey().toKeyString().equals(item.getKey().toKeyString())) {
                            analysisItem = item;
                            break;
                        }
                    }
                }
            }

        }
        return analysisItem;
    }
}
