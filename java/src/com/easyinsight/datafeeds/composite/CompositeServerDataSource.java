package com.easyinsight.datafeeds.composite;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.core.Key;
import com.easyinsight.intention.DataSourceIntention;
import com.easyinsight.intention.Intention;
import com.easyinsight.intention.IntentionSuggestion;
import com.easyinsight.users.User;
import com.easyinsight.users.Account;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;

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

    public boolean hasNewData(Date lastRefreshDate, FeedDefinition parent, EIConnection conn) throws Exception {
        List<IServerDataSourceDefinition> sources = obtainChildDataSources(conn);
        boolean hasNew = false;
        for (IServerDataSourceDefinition source : sources) {
            hasNew = hasNew || source.hasNewData(lastRefreshDate, this, conn);
        }
        return hasNew;
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
        sortSources(dataSources);
        return dataSources;
    }

    protected List<IServerDataSourceDefinition> childDataSources(EIConnection conn) throws Exception {
        FeedStorage feedStorage = new FeedStorage();
        List<IServerDataSourceDefinition> dataSources = new ArrayList<IServerDataSourceDefinition>();
        Set<FeedType> feedTypes = getFeedTypes();
        Map<FeedType, FeedDefinition> feedTypeMap = new HashMap<FeedType, FeedDefinition>();
        for (CompositeFeedNode node : getCompositeFeedNodes()) {
            FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(node.getDataFeedID(), conn);
            feedTypeMap.put(feedDefinition.getFeedType(), feedDefinition);
        }
        boolean newSource = getCompositeFeedNodes().size() == 0;
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
            } else if (existing instanceof IServerDataSourceDefinition) {
                dataSources.add((IServerDataSourceDefinition) existing);
            }
        }
        return dataSources;
    }

    protected void sortSources(List<IServerDataSourceDefinition> children) {

    }

    public List<CompositeFeedConnection> obtainChildConnections() throws SQLException {
        Map<FeedType, IServerDataSourceDefinition> feedMap = new HashMap<FeedType, IServerDataSourceDefinition>();
        for (CompositeFeedNode child : getCompositeFeedNodes()) {
            FeedDefinition childDef = new FeedStorage().getFeedDefinitionData(child.getDataFeedID());
            feedMap.put(childDef.getFeedType(), (IServerDataSourceDefinition) childDef);
        }
        List<CompositeFeedConnection> connections = new ArrayList<CompositeFeedConnection>();
            for (ChildConnection childConnection : getLiveChildConnections()) {
            IServerDataSourceDefinition sourceDef = feedMap.get(childConnection.getSourceFeedType());
            IServerDataSourceDefinition targetDef = feedMap.get(childConnection.getTargetFeedType());
            
            CompositeFeedConnection connection = childConnection.createConnection(sourceDef, targetDef);
            if (connection != null) {
                connections.add(connection);
            }
        }
        connections.addAll(getAdditionalConnections());
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

    public boolean refreshData(long accountID, Date now, EIConnection conn, FeedDefinition parentDefinition, String callDataID, Date lastRefreshTime, boolean fullRefresh) throws Exception {
        boolean changed = false;
        DataTypeMutex.mutex().lock(getFeedType(), getDataFeedID());
        try {
            beforeRefresh(lastRefreshTime);
            int nodeSize = getCompositeFeedNodes().size();
            List<IServerDataSourceDefinition> sources = obtainChildDataSources(conn);
            int afterNodeSize = getCompositeFeedNodes().size();
            changed = nodeSize != afterNodeSize;
            Map<Long, Map<String, Key>> keyMap = new HashMap<Long, Map<String, Key>>();
            for (IServerDataSourceDefinition source : sources) {
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
                tempTables.put(serverDataSourceDefinition.getDataFeedID(),
                        serverDataSourceDefinition.tempLoad(keyMap.get(serverDataSourceDefinition.getDataFeedID()), now,
                        this, callDataID, lastRefreshTime, conn, fullRefresh));
            }
            conn.setAutoCommit(false);
            for (IServerDataSourceDefinition source : sources) {
                ServerDataSourceDefinition serverDataSourceDefinition = (ServerDataSourceDefinition) source;
                String tempTable = tempTables.get(serverDataSourceDefinition.getDataFeedID());
                serverDataSourceDefinition.applyTempLoad(conn, accountID, this, lastRefreshTime, tempTable, fullRefresh);
            }
            refreshDone();
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
        Date lastDataTime = dataSourceInfo.getLastDataTime();
        if (lastDataTime != null) {
            long time = System.currentTimeMillis() - lastDataTime.getTime();
            long days = time / (1000 * 60 * 60 * 24);
            if (days > 2) {
                suggestions.add(new IntentionSuggestion("Refresh Data",
                    "Your " + getFeedName() + " data source hasn't been refreshed with new data in " + days + " days.",
                    IntentionSuggestion.SCOPE_DATA_SOURCE, IntentionSuggestion.OLD_DATA, IntentionSuggestion.WARNING));
            }
        }
        return suggestions;
    }

    public List<Intention> createIntentions(WSAnalysisDefinition report, List<AnalysisItem> fields, int type) throws SQLException {
        List<Intention> intentions = new ArrayList<Intention>();
        if (type == IntentionSuggestion.OLD_DATA) {
            DataSourceIntention dataSourceIntention = new DataSourceIntention();
            dataSourceIntention.setRefreshData(true);
            intentions.add(dataSourceIntention);
        }
        return intentions;
    }

    protected List<JoinOverride> fromChildConnections(List<ChildConnection> childConnections, List<AnalysisItem> fields) throws SQLException {
        List<JoinOverride> joinOverrides = new ArrayList<JoinOverride>();
        Map<FeedType, FeedDefinition> feedMap = new HashMap<FeedType, FeedDefinition>();
        for (CompositeFeedNode child : getCompositeFeedNodes()) {
            FeedDefinition childDef = new FeedStorage().getFeedDefinitionData(child.getDataFeedID());
            feedMap.put(childDef.getFeedType(), childDef);
        }
        for (ChildConnection childConnection : childConnections) {
            FeedDefinition sourceDef = feedMap.get(childConnection.getSourceFeedType());
            FeedDefinition targetDef = feedMap.get(childConnection.getTargetFeedType());
            if (sourceDef == null || targetDef == null) {
                continue;
            }
            CompositeFeedConnection connection = childConnection.createConnection((IServerDataSourceDefinition) sourceDef, (IServerDataSourceDefinition) targetDef);
            JoinOverride joinOverride = new JoinOverride();
            joinOverride.setSourceName(sourceDef.getFeedName());
            joinOverride.setTargetName(targetDef.getFeedName());
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
