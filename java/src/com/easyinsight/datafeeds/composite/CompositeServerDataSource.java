package com.easyinsight.datafeeds.composite;

import com.easyinsight.analysis.ReportCache;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.core.Key;
import com.easyinsight.users.User;
import com.easyinsight.users.Account;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.userupload.CredentialsResponse;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
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
                        sourceKey, targetKey, sourceDef.getFeedName(), targetDef.getFeedName());
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
            metadata.commit();
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

    public CredentialsResponse refreshData(long accountID, Date now, FeedDefinition parentDefinition, EIConnection conn, String callDataID, Date lastRefreshTime) {
        try {
            refreshData(accountID, now, conn, null, callDataID, lastRefreshTime);
            return new CredentialsResponse(true, getDataFeedID());
        } catch (ReportException re) {
            return new CredentialsResponse(false, re.getReportFault());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CredentialsResponse refreshData(long accountID, Date now, FeedDefinition parentDefinition, String callDataID, Date lastRefreshTime) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            refreshData(accountID, now, conn, null, callDataID, lastRefreshTime);
            conn.commit();
            ReportCache.instance().flushResults(getDataFeedID());
            return new CredentialsResponse(true, getDataFeedID());
        } catch (ReportException re) {
            return new CredentialsResponse(false, re.getReportFault());
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            return new CredentialsResponse(false, e.getMessage(), getDataFeedID());
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public boolean refreshData(long accountID, Date now, EIConnection conn, FeedDefinition parentDefinition, String callDataID, Date lastRefreshTime) throws Exception {
        DataTypeMutex.mutex().lock(getFeedType(), getDataFeedID());
        try {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO data_source_refresh_audit (account_id, data_source_id, " +
                    "start_time, server_id) values (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            insertStmt.setLong(1, accountID);
            insertStmt.setLong(2, getDataFeedID());
            insertStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            insertStmt.setString(4, InetAddress.getLocalHost().getHostAddress());
            insertStmt.execute();
            long auditID = Database.instance().getAutoGenKey(insertStmt);
            boolean changed = false;
            // possibilities...
            // a new data soucrce was added as a child as part of an upgrade
            // a new custom field was added, etc

            List<AnalysisItem> allItems = new ArrayList<AnalysisItem>();
            List<IServerDataSourceDefinition> sources = obtainChildDataSources(conn);
            for (IServerDataSourceDefinition source : sources) {
                source.refreshData(accountID, now, conn, this, callDataID, lastRefreshTime);
                allItems.addAll(source.getFields());
            }
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE data_source_refresh_audit set end_time = ? where data_source_refresh_audit_id = ?");
            updateStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            updateStmt.setLong(2, auditID);
            updateStmt.executeUpdate();
        } finally {
            DataTypeMutex.mutex().unlock(getFeedType());
        }
        
        //notifyOfDataUpdate();
        return false;
    }

    /*private void notifyOfDataUpdate() {
        MessageBroker msgBroker = MessageBroker.getMessageBroker(null);
        String clientID = UUIDUtils.createUUID();
        AsyncMessage msg = new AsyncMessage();
        msg.setDestination("dataUpdates");
        msg.setHeader(AsyncMessage.SUBTOPIC_HEADER_NAME, String.valueOf(getDataFeedID()));
        msg.setMessageId(clientID);
        msg.setTimestamp(System.currentTimeMillis());
        if (msgBroker != null) {
            msgBroker.routeMessageToService(msg, null);
        }
    }*/

    public String retrievePassword() {
        return password;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
