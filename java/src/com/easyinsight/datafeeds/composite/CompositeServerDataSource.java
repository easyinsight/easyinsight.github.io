package com.easyinsight.datafeeds.composite;

import com.easyinsight.datafeeds.*;
import com.easyinsight.core.Key;
import com.easyinsight.users.Credentials;
import com.easyinsight.users.User;
import com.easyinsight.users.SubscriptionLicense;
import com.easyinsight.users.Account;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.userupload.CredentialsResponse;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;
import flex.messaging.MessageBroker;
import flex.messaging.messages.AsyncMessage;
import flex.messaging.util.UUIDUtils;

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
        return Account.INDIVIDUAL;
    }

    protected abstract Set<FeedType> getFeedTypes();

    protected abstract Collection<ChildConnection> getChildConnections();

    protected abstract IServerDataSourceDefinition createForFeedType(FeedType feedType);

    public long create(Credentials credentials, Connection conn) throws SQLException, CloneNotSupportedException {
        setOwnerName(retrieveUser(conn, SecurityUtil.getUserID()).getUserName());
        UploadPolicy uploadPolicy = new UploadPolicy(SecurityUtil.getUserID());
        setUploadPolicy(uploadPolicy);
        FeedCreationResult feedCreationResult = new FeedCreation().createFeed(this, conn, null, SecurityUtil.getUserID());
        obtainChildDataSources(conn, credentials);
        new FeedStorage().updateDataFeedConfiguration(this, conn);
        return feedCreationResult.getFeedID();
    }

    protected List<IServerDataSourceDefinition> obtainChildDataSources(Connection conn, Credentials credentials) throws SQLException, CloneNotSupportedException {
        List<IServerDataSourceDefinition> dataSources = new ArrayList<IServerDataSourceDefinition>();
        Map<FeedType, IServerDataSourceDefinition> feedMap = new HashMap<FeedType, IServerDataSourceDefinition>();
        List<CompositeFeedNode> nodes = new ArrayList<CompositeFeedNode>();
        Set<FeedType> feedTypes = getFeedTypes();
        FeedStorage feedStorage = new FeedStorage();
        boolean newSource = getCompositeFeedNodes().size() == 0;
        if (newSource) {
            for (FeedType feedType : feedTypes) {
                IServerDataSourceDefinition definition = createForFeedType(feedType);
                newDefinition(definition, conn, credentials, retrieveUser(conn, SecurityUtil.getUserID()).getUserName(), SecurityUtil.getUserID());
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
                        sourceKey, targetKey);
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

    private void newDefinition(IServerDataSourceDefinition definition, Connection conn, Credentials credentials, String userName, long userID) throws SQLException {
        DataStorage metadata = null;
        try {
            FeedDefinition feedDefinition = (FeedDefinition) definition;
            feedDefinition.setVisible(false);
            Map<String, Key> keys = feedDefinition.newDataSourceFields(credentials);
            DataSet dataSet = feedDefinition.getDataSet(credentials, keys, new Date(), this);
            feedDefinition.setFields(feedDefinition.createAnalysisItems(keys, dataSet, credentials, conn));
            feedDefinition.setOwnerName(userName);
            feedDefinition.setParentSourceID(getDataFeedID());
            UploadPolicy uploadPolicy = new UploadPolicy(userID);
            feedDefinition.setUploadPolicy(uploadPolicy);
            FeedCreationResult feedCreationResult = new FeedCreation().createFeed(feedDefinition, conn, dataSet, userID);
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
                user.setLicenses(new ArrayList<SubscriptionLicense>());
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

    public void setCredentialsDefinition(int i) {

    }

    public CredentialsResponse refreshData(Credentials credentials, long accountID, Date now, FeedDefinition parentDefinition) {
        Connection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            refreshData(credentials, accountID, now, conn, null);
            conn.commit();
            return new CredentialsResponse(true);
        } catch (Exception e) {
            LogClass.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            return new CredentialsResponse(false, e.getMessage());
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    public boolean refreshData(Credentials credentials, long accountID, Date now, Connection conn, FeedDefinition parentDefinition) throws Exception {
        if(credentials == null) {
            if(this.getCredentialsDefinition() == CredentialsDefinition.STANDARD_USERNAME_PW) {
                credentials = new Credentials();
                credentials.setUserName(getUsername());
                credentials.setPassword(retrievePassword());
            }
        }
        boolean changed = false;
        // possibilities...
        // a new data soucrce was added as a child as part of an upgrade
        // a new custom field was added, etc
        List<AnalysisItem> allItems = new ArrayList<AnalysisItem>();
        List<IServerDataSourceDefinition> sources = obtainChildDataSources(conn, credentials);
        for (IServerDataSourceDefinition source : sources) {
            source.refreshData(credentials, accountID, now, conn, this);
            allItems.addAll(source.getFields());
        }
        
        notifyOfDataUpdate();
        return false;
    }

    private void notifyOfDataUpdate() {
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
    }

    public String retrievePassword() {
        return password;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
