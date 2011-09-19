package com.easyinsight.datafeeds.composite;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.core.Key;
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
        List<IServerDataSourceDefinition> dataSources = new ArrayList<IServerDataSourceDefinition>();
        Map<FeedType, IServerDataSourceDefinition> feedMap = new HashMap<FeedType, IServerDataSourceDefinition>();
        List<CompositeFeedNode> nodes = new ArrayList<CompositeFeedNode>();
        Set<FeedType> feedTypes = getFeedTypes();
        FeedStorage feedStorage = new FeedStorage();
        boolean newSource = getCompositeFeedNodes().size() == 0;
        if (newSource) {
            for (FeedType feedType : feedTypes) {
                IServerDataSourceDefinition definition = createForFeedType(feedType);
                newDefinition(definition, conn, "", getUploadPolicy());
                dataSources.add(definition);
                CompositeFeedNode node = new CompositeFeedNode();
                node.setDataFeedID(definition.getDataFeedID());
                feedMap.put(definition.getFeedType(), definition);
                nodes.add(node);
            }
            setCompositeFeedNodes(nodes);
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
            for (CompositeFeedNode node : getCompositeFeedNodes()) {
                IServerDataSourceDefinition definition = (IServerDataSourceDefinition) feedStorage.getFeedDefinitionData(node.getDataFeedID(), conn);
                dataSources.add(definition);
            }
        }
        return dataSources;
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
            connections.add(connection);
        }
        return connections;
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

    public boolean refreshData(long accountID, Date now, EIConnection conn, FeedDefinition parentDefinition, String callDataID, Date lastRefreshTime) throws Exception {
        boolean changed = false;
        DataTypeMutex.mutex().lock(getFeedType(), getDataFeedID());
        try {
            List<IServerDataSourceDefinition> sources = obtainChildDataSources(conn);
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
                        this, callDataID, lastRefreshTime, conn));
            }
            conn.setAutoCommit(false);
            for (IServerDataSourceDefinition source : sources) {
                ServerDataSourceDefinition serverDataSourceDefinition = (ServerDataSourceDefinition) source;
                String tempTable = tempTables.get(serverDataSourceDefinition.getDataFeedID());
                serverDataSourceDefinition.applyTempLoad(conn, accountID, this, lastRefreshTime, tempTable);
            }
            refreshDone();
        } finally {
            DataTypeMutex.mutex().unlock(getFeedType());
        }

        return changed;
    }

    protected void refreshDone() {

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
