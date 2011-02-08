package com.easyinsight.datafeeds;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.dashboard.DashboardStorage;
import com.easyinsight.etl.LookupPair;
import com.easyinsight.etl.LookupTable;
import com.easyinsight.etl.LookupTableUtil;
import com.easyinsight.goals.GoalStorage;
import com.easyinsight.scorecard.ScorecardInternalService;
import com.easyinsight.storage.DatabaseShardException;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.*;
import com.easyinsight.security.SecurityException;
import com.easyinsight.groups.GroupDescriptor;
import com.easyinsight.email.UserStub;
import com.easyinsight.notifications.UserToDataSourceNotification;
import com.easyinsight.notifications.NotificationBase;
import com.easyinsight.notifications.DataSourceToGroupNotification;
import com.easyinsight.users.User;

import java.sql.*;
import java.util.*;
import java.util.Date;

import com.easyinsight.util.RandomTextGenerator;
import org.hibernate.Session;

/**
 * User: jboe
 * Date: Jan 3, 2008
 * Time: 3:30:22 PM
 */
public class FeedService {

    private FeedStorage feedStorage = new FeedStorage();
    private AnalysisStorage analysisStorage = new AnalysisStorage();

    public FeedService() {
        // this goes into a different data provider        
    }

    public HomeState determineHomeState() {

        EIConnection conn = Database.instance().getConnection();
        try {
            List<DataSourceDescriptor> dataSources = feedStorage.getDataSources(SecurityUtil.getUserID(), SecurityUtil.getAccountID(), conn);
            List<InsightDescriptor> reports = analysisStorage.getReports(SecurityUtil.getUserID(), SecurityUtil.getAccountID(), conn).values();
            return new HomeState(dataSources, reports);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }

    }
    
    public ReportFault getCredentials(List<Integer> dataSourceIDs) {
        EIConnection conn = Database.instance().getConnection();
        try {
            for (Integer dataSourceID : dataSourceIDs) {
                Feed feed = FeedRegistry.instance().getFeed(dataSourceID, conn);
                ReportFault reportFault = feed.getDataSource().validateDataConnectivity();
                if (reportFault != null) {
                    return reportFault;
                }
            }
            return null;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<EIDescriptor> getDescriptors() {
        List<EIDescriptor> descriptorList = new ArrayList<EIDescriptor>();
        long userID = SecurityUtil.getUserID();
        long accountID = SecurityUtil.getAccountID();
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            descriptorList.addAll(feedStorage.getDataSources(userID, accountID, conn));
            descriptorList.addAll(analysisStorage.getReports(userID, accountID, conn).values());
            descriptorList.addAll(new DashboardStorage().getDashboards(userID, accountID, conn).values());
            descriptorList.addAll(new GoalStorage().getTrees(userID, accountID, conn).values());
            descriptorList.addAll(new ScorecardInternalService().getScorecards(userID, accountID, conn).values());
            Map<String, Integer> countMap = new HashMap<String, Integer>();
            Set<String> dupeNames = new HashSet<String>();
            Set<String> allNames = new HashSet<String>();
            for (EIDescriptor descriptor : descriptorList) {
                if (!allNames.add(descriptor.getName())) {
                    dupeNames.add(descriptor.getName());
                }
            }
            for (EIDescriptor descriptor : descriptorList) {
                if (dupeNames.contains(descriptor.getName())) {
                    Integer count = countMap.get(descriptor.getName());
                    if (count == null) {
                        count = 1;
                    } else {
                        count = count + 1;
                    }
                    countMap.put(descriptor.getName(), count);
                    descriptor.setName(descriptor.getName() + " (" + count + ")");
                }    
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return descriptorList;
    }

    public FeedResponse openFeedIfPossible(String urlKey) {
        FeedResponse feedResponse;
        try {
            try {
                long feedID = SecurityUtil.authorizeFeedAccess(urlKey);
                //long userID = SecurityUtil.getUserID();
                DataSourceDescriptor feedDescriptor = feedStorage.getFeedDescriptor(feedID);
                feedResponse = new FeedResponse(FeedResponse.SUCCESS, feedDescriptor);
            } catch (SecurityException e) {
                if (e.getReason() == SecurityException.LOGIN_REQUIRED)
                    feedResponse = new FeedResponse(FeedResponse.NEED_LOGIN, null);
                else
                    feedResponse = new FeedResponse(FeedResponse.REJECTED, null);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return feedResponse;
    }

    public FeedResponse openFeedByAPIKey(String apiKey) {
        FeedResponse feedResponse;
        try {
            try {
                long feedID = feedStorage.getFeedForAPIKey(SecurityUtil.getUserID(), apiKey);
                DataSourceDescriptor feedDescriptor = feedStorage.getFeedDescriptor(feedID);
                feedResponse = new FeedResponse(FeedResponse.SUCCESS, feedDescriptor);
            } catch (SecurityException e) {
                if (e.getReason() == SecurityException.LOGIN_REQUIRED)
                    feedResponse = new FeedResponse(FeedResponse.NEED_LOGIN, null);
                else
                    feedResponse = new FeedResponse(FeedResponse.REJECTED, null);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return feedResponse;
    }

    public void wipeData(long feedID) {
        SecurityUtil.authorizeFeed(feedID, Roles.OWNER);
        Connection conn = Database.instance().getConnection();
        DataStorage metadata = null;
        try {
            conn.setAutoCommit(false);
            FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(feedID, conn);
            metadata = DataStorage.writeConnection(feedDefinition, conn);
            metadata.truncate();
            metadata.commit();
            conn.commit();
        } catch (DatabaseShardException dse) {
            // all fine
        } catch (Exception e) {
            LogClass.error(e);
            if (metadata != null) {
                metadata.rollback();
            }
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            if (metadata != null) {
                metadata.closeConnection();
            }
            Database.closeConnection(conn);
        }
    }

    public JoinAnalysis testJoin(CompositeFeedConnection connection) {
        try {
            return new JoinTester(connection).generateReport();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<CompositeFeedConnection> initialDefine(List<CompositeFeedNode> nodes, List<DataSourceDescriptor> newFeeds) {
        try {
            Set<Set<Long>> connectionMap = new HashSet<Set<Long>>();
            List<CompositeFeedConnection> allNewEdges = new ArrayList<CompositeFeedConnection>();
            JoinDiscovery joinDiscovery = new JoinDiscovery();
            for (DataSourceDescriptor newFeed : newFeeds) {
                for (CompositeFeedNode node : nodes) {
                    Set<Long> ids = new HashSet<Long>();
                    ids.add(newFeed.getId());
                    ids.add(node.getDataFeedID());
                    if (!connectionMap.contains(ids)) {
                        connectionMap.add(ids);
                        List<CompositeFeedConnection> potentialJoins = joinDiscovery.findPotentialJoins(node.getDataFeedID(), newFeed.getId());
                        allNewEdges.addAll(potentialJoins);
                    }
                }
                for (DataSourceDescriptor otherNewFeed : newFeeds) {
                    if (otherNewFeed == newFeed) {
                        continue;
                    }
                    Set<Long> ids = new HashSet<Long>();
                    ids.add(newFeed.getId());
                    ids.add(otherNewFeed.getId());
                    if (!connectionMap.contains(ids)) {
                        connectionMap.add(ids);
                        List<CompositeFeedConnection> potentialJoins = joinDiscovery.findPotentialJoins(otherNewFeed.getId(), newFeed.getId());
                        allNewEdges.addAll(potentialJoins);
                    }
                }
            }
            return allNewEdges;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<FeedDefinition> getMultipleFeeds(long firstID, long secondID) {
        List<FeedDefinition> feeds = new ArrayList<FeedDefinition>();
        try {
            feeds.add(getFeedDefinition(firstID));
            feeds.add(getFeedDefinition(secondID));
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return feeds;
    }

    public List<DataSourceDescriptor> searchForSubscribedFeeds() {
        long userID = SecurityUtil.getUserID();
        try {
            List<DataSourceDescriptor> dataSources = feedStorage.getDataSources(userID, SecurityUtil.getAccountID());
            Collections.sort(dataSources, new Comparator<DataSourceDescriptor>() {

                public int compare(DataSourceDescriptor dataSourceDescriptor, DataSourceDescriptor dataSourceDescriptor1) {
                    return dataSourceDescriptor.getName().compareTo(dataSourceDescriptor1.getName());
                }
            });
            return dataSources;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<DataSourceDescriptor> searchForHiddenChildren(long dataSourceID) {
        long userID = SecurityUtil.getUserID();
        try {
            return feedStorage.getExistingHiddenChildren(userID, dataSourceID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public CompositeFeedDefinition createCompositeFeed(List<CompositeFeedNode> compositeFeedNodes, List<CompositeFeedConnection> edges, String feedName) {
        long userID = SecurityUtil.getUserID();
        final EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            CompositeFeedDefinition feedDef = new CompositeFeedDefinition();
            feedDef.setFeedName(feedName);
            feedDef.setCompositeFeedNodes(compositeFeedNodes);
            feedDef.setConnections(edges);
            feedDef.setUploadPolicy(new UploadPolicy(userID, SecurityUtil.getAccountID()));
            /*final ContainedInfo containedInfo = new ContainedInfo();
            new CompositeFeedNodeShallowVisitor() {

                protected void accept(CompositeFeedNode compositeFeedNode) throws SQLException {
                    SecurityUtil.authorizeFeed(compositeFeedNode.getDataFeedID(), Roles.SUBSCRIBER);
                    FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(compositeFeedNode.getDataFeedID(), conn);
                    containedInfo.feedItems.addAll(feedDefinition.getFields());
                }
            }.visit(feedDef);*/
            feedDef.populateFields(conn);
            feedDef.setApiKey(RandomTextGenerator.generateText(12));
            long feedID = feedStorage.addFeedDefinitionData(feedDef, conn);
            DataStorage.liveDataSource(feedID, conn);
            //new UserUploadInternalService().createUserFeedLink(userID, feedID, Roles.OWNER, conn);
            conn.commit();
            return feedDef;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void updateCompositeFeed(List<CompositeFeedNode> compositeFeedNodes, List<CompositeFeedConnection> edges, long feedID) {
        SecurityUtil.authorizeFeed(feedID, Roles.OWNER);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            CompositeFeedDefinition compositeFeed = (CompositeFeedDefinition) getFeedDefinition(feedID);
            compositeFeed.setCompositeFeedNodes(compositeFeedNodes);
            compositeFeed.setConnections(edges);
            compositeFeed.populateFields(conn);
            feedStorage.updateDataFeedConfiguration(compositeFeed, conn);
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public String updateFeedDefinition(FeedDefinition feedDefinition) {
        SecurityUtil.authorizeFeed(feedDefinition.getDataFeedID(), Roles.OWNER);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            new DataSourceInternalService().updateFeedDefinition(feedDefinition, conn);
            FeedRegistry.instance().flushCache(feedDefinition.getDataFeedID());
            conn.commit();
        } catch (UserMessageException ue) {
            LogClass.error(ue);
            return ue.getUserMessage();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {            
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return null;
    }



    private void notifyNewViewers(FeedDefinition feedDefinition, FeedDefinition existingFeed, Session session) throws SQLException {
        List<FeedConsumer> viewers = new ArrayList<FeedConsumer>(feedDefinition.getUploadPolicy().getViewers());
        List<FeedConsumer> oldViewers = new ArrayList<FeedConsumer>(existingFeed.getUploadPolicy().getViewers());
        viewers.removeAll(existingFeed.getUploadPolicy().getViewers());
        oldViewers.removeAll(feedDefinition.getUploadPolicy().getViewers());
        processViewers(feedDefinition, session, viewers, NotificationBase.ADD, NotificationBase.VIEWER);
        processViewers(feedDefinition, session, oldViewers, NotificationBase.REMOVE, NotificationBase.VIEWER);
    }

    private void processViewers(FeedDefinition feedDefinition, Session session, List<FeedConsumer> viewers, int action, int role) {
        for(FeedConsumer viewer : viewers) {
            switch(viewer.type()) {
                case FeedConsumer.USER:
                    UserToDataSourceNotification userToDataSourceNotification = new UserToDataSourceNotification();
                    userToDataSourceNotification.setActingUser((User) session.get(User.class, SecurityUtil.getUserID()));
                    userToDataSourceNotification.setUser((User) session.get(User.class, ((UserStub) viewer).getUserID()));
                    userToDataSourceNotification.setFeedAction(action);
                    userToDataSourceNotification.setFeedRole(role);
                    userToDataSourceNotification.setNotificationDate(new Date());
                    userToDataSourceNotification.setFeedID(feedDefinition.getDataFeedID());
                    userToDataSourceNotification.setNotificationType(NotificationBase.USER_TO_DATA_SOURCE);
                    session.save(userToDataSourceNotification);
                    break;
                case FeedConsumer.GROUP:
                    DataSourceToGroupNotification dataSourceToGroupNotification = new DataSourceToGroupNotification();
                    dataSourceToGroupNotification.setActingUser((User) session.get(User.class, SecurityUtil.getUserID()));
                    dataSourceToGroupNotification.setGroupID(((GroupDescriptor) viewer).getGroupID());
                    dataSourceToGroupNotification.setFeedAction(action);
                    dataSourceToGroupNotification.setFeedRole(role);
                    dataSourceToGroupNotification.setNotificationDate(new Date());
                    dataSourceToGroupNotification.setFeedID(feedDefinition.getDataFeedID());
                    dataSourceToGroupNotification.setNotificationType(NotificationBase.DATA_SOURCE_TO_GROUP);
                    session.save(dataSourceToGroupNotification);
                    break;
                default:
                    break;
            }
        }
    }

    private void notifyNewOwners(FeedDefinition feedDefinition, FeedDefinition existingFeed, Session session) {
        List<FeedConsumer> owners= new ArrayList<FeedConsumer>(feedDefinition.getUploadPolicy().getOwners());
        List<FeedConsumer> oldOwners = new ArrayList<FeedConsumer>(existingFeed.getUploadPolicy().getOwners());
        owners.removeAll(existingFeed.getUploadPolicy().getOwners());
        oldOwners.removeAll(feedDefinition.getUploadPolicy().getOwners());
        processViewers(feedDefinition, session, owners, NotificationBase.ADD,  NotificationBase.OWNER);
        processViewers(feedDefinition, session, oldOwners, NotificationBase.REMOVE,  NotificationBase.OWNER);
    }

    public FeedDefinition getFeedDefinition(long dataFeedID) {
        SecurityUtil.authorizeFeed(dataFeedID, Roles.SHARER);
        try {
            //SecurityUtil.authorizeFeed(dataFeedID, Roles.OWNER);
            return feedStorage.getFeedDefinitionData(dataFeedID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void deleteLookupTable(long lookupTableID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_SOURCE_ID FROM LOOKUP_TABLE WHERE LOOKUP_TABLE_ID = ?");
            queryStmt.setLong(1, lookupTableID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long dataSourceID = rs.getLong(1);
                SecurityUtil.authorizeFeed(dataSourceID, Roles.OWNER);
                PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM LOOKUP_TABLE WHERE LOOKUP_TABLE_ID = ?");
                deleteStmt.setLong(1, lookupTableID);
                deleteStmt.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }



    public LookupTable getLookupTable(long lookupTableID) {
        LookupTable lookupTable;
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_SOURCE_ID, LOOKUP_TABLE_NAME, SOURCE_ITEM_ID," +
                    "TARGET_ITEM_ID, URL_KEY FROM LOOKUP_TABLE WHERE LOOKUP_TABLE_ID = ?");
            queryStmt.setLong(1, lookupTableID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long dataSourceID = rs.getLong(1);
                String name = rs.getString(2);
                long sourceItemID = rs.getLong(3);
                long targetItemID = rs.getLong(4);
                String urlKey = rs.getString(5);
                if (urlKey == null) {
                    urlKey = RandomTextGenerator.generateText(20);
                    PreparedStatement updateURLStmt = conn.prepareStatement("UPDATE LOOKUP_TABLE SET URL_KEY = ? WHERE " +
                            "LOOKUP_TABLE_ID = ?");
                    updateURLStmt.setString(1, urlKey);
                    updateURLStmt.setLong(2, lookupTableID);
                    updateURLStmt.execute();
                }
                AnalysisItem sourceItem = (AnalysisItem) session.createQuery("from AnalysisItem where analysisItemID = ?").setLong(0, sourceItemID).list().get(0);
                sourceItem.afterLoad();
                AnalysisItem targetItem = (AnalysisItem) session.createQuery("from AnalysisItem where analysisItemID = ?").setLong(0, targetItemID).list().get(0);
                targetItem.afterLoad();
                lookupTable = new LookupTable();
                lookupTable.setName(name);
                lookupTable.setDataSourceID(dataSourceID);
                lookupTable.setSourceField(sourceItem);
                lookupTable.setTargetField(targetItem);
                lookupTable.setUrlKey(urlKey);
                lookupTable.setLookupTableID(lookupTableID);
                PreparedStatement getPairsStmt = conn.prepareStatement("SELECT SOURCE_VALUE, TARGET_VALUE, target_date_value, target_measure, lookup_pair_id FROM " +
                        "LOOKUP_PAIR WHERE LOOKUP_TABLE_ID = ?");
                getPairsStmt.setLong(1, lookupTableID);
                ResultSet pairsRS = getPairsStmt.executeQuery();
                List<LookupPair> pairs = new ArrayList<LookupPair>();
                while (pairsRS.next()) {
                    String sourceValue = pairsRS.getString(1);
                    Value value;
                    if (targetItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                        Timestamp targetDate = pairsRS.getTimestamp(3);
                        if (pairsRS.wasNull()) {
                            value = new EmptyValue();
                        } else {
                            value = new DateValue(new java.sql.Date(targetDate.getTime()));
                        }
                    } else if (targetItem.hasType(AnalysisItemTypes.MEASURE)) {
                        double number = pairsRS.getDouble(4);
                        if (pairsRS.wasNull()) {
                            value = new EmptyValue();
                        } else {
                            value = new NumericValue(number);
                        }
                    } else {
                        String targetValue = pairsRS.getString(2);
                        value = new StringValue(targetValue);
                    }
                    LookupPair lookupPair = new LookupPair();
                    lookupPair.setSourceValue(new StringValue(sourceValue));
                    lookupPair.setTargetValue(value);
                    lookupPair.setLookupPairID(pairsRS.getLong(5));
                    pairs.add(lookupPair);
                }
                lookupTable.setLookupPairs(pairs);
            } else {
                return null;
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return lookupTable;
    }

    public long openLookupTableIfPossible(String urlKey) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT LOOKUP_TABLE_ID, DATA_SOURCE_ID FROM LOOKUP_TABLE WHERE " +
                    "URL_KEY = ?");
            queryStmt.setString(1, urlKey);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long lookupTableID = rs.getLong(1);
                long dataSourceID = rs.getLong(2);
                SecurityUtil.authorizeFeedAccess(dataSourceID);
                return lookupTableID;
            }
        } catch (SecurityException se) {
            return 0;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return 0;
    }

    public long saveNewLookupTable(LookupTable lookupTable) {
        long id;
        SecurityUtil.authorizeFeed(lookupTable.getDataSourceID(), Roles.OWNER);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(lookupTable.getDataSourceID(), conn);
            dataSource.getFields().add(lookupTable.getTargetField());
            new FeedStorage().updateDataFeedConfiguration(dataSource, conn);
            PreparedStatement insertTableStmt = conn.prepareStatement("INSERT INTO LOOKUP_TABLE (DATA_SOURCE_ID," +
                    "LOOKUP_TABLE_NAME, SOURCE_ITEM_ID, TARGET_ITEM_ID, URL_KEY) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertTableStmt.setLong(1, lookupTable.getDataSourceID());
            insertTableStmt.setString(2, lookupTable.getName());
            insertTableStmt.setLong(3, lookupTable.getSourceField().getAnalysisItemID());
            insertTableStmt.setLong(4, lookupTable.getTargetField().getAnalysisItemID());
            insertTableStmt.setString(5, RandomTextGenerator.generateText(20));
            insertTableStmt.execute();
            id = Database.instance().getAutoGenKey(insertTableStmt);
            lookupTable.getTargetField().setLookupTableID(id);
            new FeedStorage().updateDataFeedConfiguration(dataSource, conn);
            FeedRegistry.instance().flushCache(dataSource.getDataFeedID());
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return id;
    }

    private void savePairs(long id, AnalysisItem analysisItem, List<LookupPair> pairs, EIConnection conn) throws SQLException {
        /*PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM LOOKUP_PAIR WHERE LOOKUP_TABLE_ID = ?");
        clearStmt.setLong(1, id);
        clearStmt.executeUpdate();*/
        if (analysisItem.getType() == AnalysisItemTypes.DIMENSION) {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO LOOKUP_PAIR (LOOKUP_TABLE_ID, " +
                    "SOURCE_VALUE, TARGET_VALUE) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE LOOKUP_PAIR SET SOURCE_VALUE = ?," +
                    "TARGET_VALUE = ? WHERE LOOKUP_PAIR_ID = ?");
            for (LookupPair lookupPair : pairs) {
                if (lookupPair.getLookupPairID() == 0) {
                    insertStmt.setLong(1, id);
                    String sourceValue = lookupPair.getSourceValue().toString();
                    /*if (sourceValue.length() > 200) {
                        sourceValue = sourceValue.substring(0, 200);
                    }*/
                    insertStmt.setString(2, sourceValue);
                    insertStmt.setString(3, lookupPair.getTargetValue().toString());
                    insertStmt.execute();
                } else {
                    String sourceValue = lookupPair.getSourceValue().toString();
                    /*if (sourceValue.length() > 200) {
                        sourceValue = sourceValue.substring(0, 200);
                    }*/
                    updateStmt.setString(1, sourceValue);
                    updateStmt.setString(2, lookupPair.getTargetValue().toString());
                    updateStmt.setLong(3, lookupPair.getLookupPairID());
                    updateStmt.executeUpdate();
                }
            }
        } else if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO LOOKUP_PAIR (LOOKUP_TABLE_ID, " +
                    "SOURCE_VALUE, target_date_value) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE LOOKUP_PAIR SET SOURCE_VALUE = ?," +
                    "TARGET_DATE_VALUE = ? WHERE LOOKUP_PAIR_ID = ?");
            for (LookupPair lookupPair : pairs) {
                if (lookupPair.getLookupPairID() == 0) {
                    insertStmt.setLong(1, id);
                    String sourceValue = lookupPair.getSourceValue().toString();
                    /*if (sourceValue.length() > 200) {
                        sourceValue = sourceValue.substring(0, 200);
                    }*/
                    insertStmt.setString(2, sourceValue);
                    DateValue dateValue = (DateValue) lookupPair.getTargetValue();
                    if (dateValue.getDate() == null) {
                        insertStmt.setNull(3, Types.TIMESTAMP);
                    } else {
                        insertStmt.setTimestamp(3, new java.sql.Timestamp(dateValue.getDate().getTime()));
                    }
                    insertStmt.execute();
                } else {
                    String sourceValue = lookupPair.getSourceValue().toString();
                    /*if (sourceValue.length() > 200) {
                        sourceValue = sourceValue.substring(0, 200);
                    }*/
                    updateStmt.setString(1, sourceValue);
                    DateValue dateValue = (DateValue) lookupPair.getTargetValue();
                    if (dateValue.getDate() == null) {
                        updateStmt.setNull(2, Types.TIMESTAMP);
                    } else {
                        updateStmt.setTimestamp(2, new java.sql.Timestamp(dateValue.getDate().getTime()));
                    }
                    updateStmt.setLong(3, lookupPair.getLookupPairID());
                    updateStmt.executeUpdate();
                }
            }
        } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO LOOKUP_PAIR (LOOKUP_TABLE_ID," +
                    "SOURCE_VALUE, TARGET_MEASURE) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE LOOKUP_PAIR SET SOURCE_VALUE = ?," +
                    "TARGET_MEASURE = ? WHERE LOOKUP_PAIR_ID = ?");
            for (LookupPair lookupPair : pairs) {
                if (lookupPair.getLookupPairID() == 0) {
                    insertStmt.setLong(1, id);
                    String sourceValue = lookupPair.getSourceValue().toString();
                    /*if (sourceValue.length() > 200) {
                        sourceValue = sourceValue.substring(0, 200);
                    }*/
                    insertStmt.setString(2, sourceValue);
                    NumericValue numericValue = (NumericValue) lookupPair.getTargetValue();
                    if (numericValue.getValue() == null) {
                        insertStmt.setNull(3, Types.DOUBLE);
                    } else {
                        insertStmt.setDouble(3, numericValue.getValue());
                    }
                    insertStmt.execute();
                } else {
                    String sourceValue = lookupPair.getSourceValue().toString();
                    /*if (sourceValue.length() > 200) {
                        sourceValue = sourceValue.substring(0, 200);
                    }*/
                    updateStmt.setString(1, sourceValue);
                    NumericValue numericValue = (NumericValue) lookupPair.getTargetValue();
                    if (numericValue.getValue() == null) {
                        updateStmt.setNull(2, Types.DOUBLE);
                    } else {
                        updateStmt.setDouble(2, numericValue.getValue());
                    }
                    updateStmt.setLong(3, lookupPair.getLookupPairID());
                    updateStmt.executeUpdate();
                }
            }
        }
    }

    public void updateLookupTable(LookupTable lookupTable) {
        SecurityUtil.authorizeFeed(lookupTable.getDataSourceID(), Roles.OWNER);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE LOOKUP_TABLE SET LOOKUP_TABLE_NAME = ? WHERE " +
                    "LOOKUP_TABLE_ID = ?");
            updateStmt.setString(1, lookupTable.getName());
            updateStmt.setLong(2, lookupTable.getLookupTableID());
            updateStmt.executeUpdate();
            savePairs(lookupTable.getLookupTableID(), lookupTable.getTargetField(), lookupTable.getLookupPairs(), conn);
            FeedRegistry.instance().flushCache(lookupTable.getDataSourceID());
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public List<Value> getLookupTablePairs(long lookupTableID, List<FilterDefinition> filters) {
        try {
            LookupTable lookupTable = getLookupTable(lookupTableID);
            return LookupTableUtil.getValues(lookupTable, filters);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
