package com.easyinsight.datafeeds;

import com.easyinsight.analysis.*;
import com.easyinsight.reportpackage.ReportPackageDescriptor;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.userupload.UserUploadInternalService;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.*;
import com.easyinsight.security.SecurityException;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.scheduler.DataSourceTaskGenerator;
import com.easyinsight.goals.GoalTreeDescriptor;
import com.easyinsight.PasswordStorage;
import com.easyinsight.groups.GroupDescriptor;
import com.easyinsight.email.UserStub;
import com.easyinsight.notifications.UserToDataSourceNotification;
import com.easyinsight.notifications.NotificationBase;
import com.easyinsight.notifications.DataSourceToGroupNotification;
import com.easyinsight.users.Credentials;
import com.easyinsight.users.User;
import com.easyinsight.eventing.EventDispatcher;
import com.easyinsight.eventing.TodoCompletedEvent;

import java.util.*;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.hibernate.Session;

/**
 * User: jboe
 * Date: Jan 3, 2008
 * Time: 3:30:22 PM
 */
public class FeedService implements IDataFeedService {

    private FeedStorage feedStorage = new FeedStorage();
    private MarketplaceStorage marketplaceStorage = new MarketplaceStorage();
    private AnalysisStorage analysisStorage = new AnalysisStorage();

    public FeedService() {
        // this goes into a different data provider        
    }

    

    public List<CredentialRequirement> getCredentials(List<Integer> dataSourceIDs, List<CredentialFulfillment> existingCredentials) {
        try {
            List<CredentialRequirement> neededCredentials = new ArrayList<CredentialRequirement>();
            InsightRequestMetadata metadata = new InsightRequestMetadata();
            metadata.setCredentialFulfillmentList(existingCredentials);
            for (Integer dataSourceID : dataSourceIDs) {
                Feed feed = FeedRegistry.instance().getFeed(dataSourceID);
                Set<CredentialRequirement> requirements = feed.getCredentialRequirement(false);
                for (CredentialRequirement credentialRequirement : requirements) {
                    if (metadata.getCredentialForDataSource(credentialRequirement.getDataSourceID()) == null) {
                        neededCredentials.add(credentialRequirement);
                    }
                }
            }
            return neededCredentials;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<InsightDescriptor> getReportsForDataSource(long dataSourceID) {
        List<InsightDescriptor> descriptors = new ArrayList<InsightDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID, TITLE, REPORT_TYPE FROM ANALYSIS WHERE data_feed_id = ? and root_definition = ?");
            queryStmt.setLong(1, dataSourceID);
            queryStmt.setBoolean(2, false);
            ResultSet reportRS = queryStmt.executeQuery();
            while (reportRS.next()) {
                descriptors.add(new InsightDescriptor(reportRS.getLong(1), reportRS.getString(2), dataSourceID, reportRS.getInt(3)));
            }
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
        List<InsightDescriptor> validDescriptors = new ArrayList<InsightDescriptor>();
        for (InsightDescriptor descriptor : descriptors) {
            try {
                SecurityUtil.authorizeInsight(descriptor.getId());
                validDescriptors.add(descriptor);
            } catch (Exception e) {
                // ignore
            }
        }
        return validDescriptors;
    }

    public List<EIDescriptor> getDescriptors() {
        List<EIDescriptor> descriptorList = new ArrayList<EIDescriptor>();
        Connection conn = Database.instance().getConnection();

        try {
            PreparedStatement queryDataSources = conn.prepareStatement("SELECT FEED_NAME, DATA_FEED.DATA_FEED_ID FROM DATA_FEED, UPLOAD_POLICY_USERS WHERE " +
                    "DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND UPLOAD_POLICY_USERS.user_id = ?");
            queryDataSources.setLong(1, SecurityUtil.getUserID());
            ResultSet queryRS = queryDataSources.executeQuery();
            while (queryRS.next()) {
                descriptorList.add(new DataSourceDescriptor(queryRS.getString(1), queryRS.getLong(2)));
            }
            PreparedStatement getInsightsStmt = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID, TITLE, DATA_FEED_ID, REPORT_TYPE FROM ANALYSIS, user_to_analysis WHERE " +
                    "ANALYSIS.ANALYSIS_ID = USER_TO_ANALYSIS.ANALYSIS_ID AND USER_TO_ANALYSIS.user_id = ? AND ANALYSIS.ROOT_DEFINITION = ? AND ANALYSIS.TEMPORARY_REPORT = ?");
            getInsightsStmt.setLong(1, SecurityUtil.getUserID());
            getInsightsStmt.setBoolean(2, false);
            getInsightsStmt.setBoolean(3, false);
            ResultSet reportRS = getInsightsStmt.executeQuery();
            while (reportRS.next()) {
                descriptorList.add(new InsightDescriptor(reportRS.getLong(1), reportRS.getString(2), reportRS.getLong(3), reportRS.getInt(4)));
            }
            PreparedStatement getGoalTreeStmt = conn.prepareStatement("SELECT GOAL_TREE.GOAL_TREE_ID, GOAL_TREE.name, USER_TO_GOAL_TREE.user_role, GOAL_TREE.goal_tree_icon FROM GOAL_TREE, USER_TO_GOAL_TREE " +
                    "WHERE GOAL_TREE.goal_tree_id = user_to_goal_tree.goal_tree_id and user_to_goal_tree.user_id = ?");
            getGoalTreeStmt.setLong(1, SecurityUtil.getUserID());
            ResultSet goalTreeRS = getGoalTreeStmt.executeQuery();
            while (goalTreeRS.next()) {
                descriptorList.add(new GoalTreeDescriptor(goalTreeRS.getLong(1), goalTreeRS.getString(2), goalTreeRS.getInt(3), goalTreeRS.getString(4)));
            }
            PreparedStatement getPackageStmt = conn.prepareStatement("SELECT REPORT_PACKAGE.package_name, REPORT_PACKAGE.report_package_id FROM REPORT_PACKAGE, user_to_report_package WHERE " +
                    "user_to_report_package.user_id = ? AND report_package.report_package_id = user_to_report_package.report_package_id AND REPORT_PACKAGE.TEMPORARY_PACKAGE = ?");
            getPackageStmt.setLong(1, SecurityUtil.getUserID());
            getPackageStmt.setBoolean(2, false);
            ResultSet packageRS = getPackageStmt.executeQuery();
            while (packageRS.next()) {
                String packageName = packageRS.getString(1);
                long packageID = packageRS.getLong(2);
                descriptorList.add(new ReportPackageDescriptor(packageName, packageID));
            }
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
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return descriptorList;
    }

    public FeedResponse openFeedIfPossible(long feedID) {
        FeedResponse feedResponse;
        try {
            try {
                SecurityUtil.authorizeFeedAccess(feedID);
                //long userID = SecurityUtil.getUserID();
                FeedDescriptor feedDescriptor = feedStorage.getFeedDescriptor(feedID);
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
                FeedDescriptor feedDescriptor = feedStorage.getFeedDescriptor(feedID);
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
            FeedDefinition feedDefinition = getFeedDefinition(feedID);
            metadata = DataStorage.writeConnection(feedDefinition, conn);
            metadata.truncate();
            metadata.commit();
            conn.commit();
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

    public void addView(long feedID) {
        try {
            feedStorage.addFeedView(feedID);
        } catch (Exception e) {
            LogClass.error(e);
        }
    }

    public void addRating(long feedID, long userID, int rating) {
        try {
            feedStorage.rateFeed(feedID, userID, rating);
        } catch (Exception e) {
            LogClass.error(e);
        }
    }

    public List<Tag> getAllFeedTags(boolean solution) {
        try {
            return new TagStorage().getTags(20, solution);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<FeedDescriptor> getMostPopularFeeds(String genreKey, int cutoff) {
        long accountID = SecurityUtil.getUserID(false);
        try {
            return marketplaceStorage.getMostPopularFeeds(accountID, genreKey, cutoff);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<FeedDescriptor> getMostRecentFeeds(String genreKey, int cutoff) {
        long accountID = SecurityUtil.getUserID(false);
        try {
            return marketplaceStorage.getRecentFeeds(cutoff, accountID, genreKey);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<FeedDescriptor> getTopRatedFeeds(String genreKey, int cutoff) {
        long accountID = SecurityUtil.getUserID(false);
        try {
            return marketplaceStorage.getBestRatedFeeds(cutoff, accountID, genreKey);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<CompositeFeedConnection> initialDefine(List<CompositeFeedNode> nodes, List<FeedDescriptor> newFeeds, List<CredentialFulfillment> credentials) {
        try {
            Set<Set<Long>> connectionMap = new HashSet<Set<Long>>();
            List<CompositeFeedConnection> allNewEdges = new ArrayList<CompositeFeedConnection>();
            JoinDiscovery joinDiscovery = new JoinDiscovery();
            for (FeedDescriptor newFeed : newFeeds) {
                for (CompositeFeedNode node : nodes) {
                    Set<Long> ids = new HashSet<Long>();
                    ids.add(newFeed.getDataFeedID());
                    ids.add(node.getDataFeedID());
                    if (!connectionMap.contains(ids)) {
                        connectionMap.add(ids);
                        List<CompositeFeedConnection> potentialJoins = joinDiscovery.findPotentialJoins(node.getDataFeedID(), newFeed.getDataFeedID(), credentials);
                        allNewEdges.addAll(potentialJoins);
                    }
                }
                for (FeedDescriptor otherNewFeed : newFeeds) {
                    if (otherNewFeed == newFeed) {
                        continue;
                    }
                    Set<Long> ids = new HashSet<Long>();
                    ids.add(newFeed.getDataFeedID());
                    ids.add(otherNewFeed.getDataFeedID());
                    if (!connectionMap.contains(ids)) {
                        connectionMap.add(ids);
                        List<CompositeFeedConnection> potentialJoins = joinDiscovery.findPotentialJoins(otherNewFeed.getDataFeedID(), newFeed.getDataFeedID(), credentials);
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

    public List<FeedDescriptor> searchForSubscribedFeeds() {
        long userID = SecurityUtil.getUserID();
        try {
            return feedStorage.searchForSubscribedFeeds(userID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<FeedDescriptor> searchForAvailableFeeds(String keyword, String genreKey) {
        long accountID = SecurityUtil.getUserID(false);
        try {
            return marketplaceStorage.searchForAvailableFeeds(accountID, keyword, genreKey);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<InsightDescriptor> getMostPopularAnalyses(String genre,  int cutoff) {
        try {
            return analysisStorage.getMostPopularAnalyses(genre, cutoff);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<InsightDescriptor> getTopRatedAnalyses(String genre, int cutoff) {
        try {
            return analysisStorage.getBestRatedAnalyses(genre, cutoff);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<InsightDescriptor> getMostRecentAnalyses(String genre, int cutoff) {
        try {
            return analysisStorage.getMostRecentAnalyses(genre, cutoff);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<InsightDescriptor> getHeadlineAnalysesForGenre(String genre) {
        try {
            return marketplaceStorage.getAnalysisDefinitionsForGenre(genre);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<InsightDescriptor> getAllAnalysesForGenre(String genre) {
        try {
            return analysisStorage.getAllDefinitions(genre);
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
            feedDef.setUploadPolicy(new UploadPolicy(userID));
            final ContainedInfo containedInfo = new ContainedInfo();
            new CompositeFeedNodeShallowVisitor() {

                protected void accept(CompositeFeedNode compositeFeedNode) throws SQLException {
                    SecurityUtil.authorizeFeed(compositeFeedNode.getDataFeedID(), Roles.SUBSCRIBER);
                    FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(compositeFeedNode.getDataFeedID(), conn);
                    containedInfo.feedItems.addAll(feedDefinition.getFields());
                }
            }.visit(feedDef);
            feedDef.populateFields(conn);
            long feedID = feedStorage.addFeedDefinitionData(feedDef, conn);
            DataStorage.liveDataSource(feedID, conn);
            new UserUploadInternalService().createUserFeedLink(userID, feedID, Roles.OWNER, conn);
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
        try {
            CompositeFeedDefinition compositeFeed = (CompositeFeedDefinition) getFeedDefinition(feedID);
            compositeFeed.setCompositeFeedNodes(compositeFeedNodes);
            compositeFeed.setConnections(edges);
            feedStorage.updateDataFeedConfiguration(compositeFeed);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void updateFeedDefinition(FeedDefinition feedDefinition, String tagString, WSAnalysisDefinition baseDefinition) {
        SecurityUtil.authorizeFeed(feedDefinition.getDataFeedID(), Roles.OWNER);
        Connection conn = Database.instance().getConnection();
        DataStorage metadata = null;
        try {
            conn.setAutoCommit(false);
            if (baseDefinition != null) {
                new AnalysisStorage().saveAnalysis(AnalysisDefinitionFactory.fromWSDefinition(baseDefinition), conn);
            }
            if (tagString == null) {
                tagString = "";
            }
            String[] tags = tagString.split(" ");
            List<Tag> tagList = new ArrayList<Tag>();
            for (String tagName : tags) {
                tagList.add(new Tag(tagName));
            }
            feedDefinition.setTags(tagList);
            final long feedID = feedDefinition.getDataFeedID();
            FeedDefinition existingFeed = feedStorage.getFeedDefinitionData(feedDefinition.getDataFeedID(), conn, false);
            boolean newTaskGen = false;
            if (existingFeed.getRefreshDataInterval() != feedDefinition.getRefreshDataInterval()) {
                newTaskGen = feedDefinition.getRefreshDataInterval() > 0;
                if (existingFeed.getRefreshDataInterval() > 0) {
                    // nuke the existing generator
                    Session session = Database.instance().createSession(conn);
                    try {
                        List results = session.createQuery("from DataSourceTaskGenerator where dataSourceID = ?").setLong(0, existingFeed.getDataFeedID()).list();
                        if (results.size() > 0) {
                            session.delete(results.get(0));
                        }
                    } finally {
                        session.close();
                    }
                }
            }
            if(feedDefinition instanceof IServerDataSourceDefinition) {
                IServerDataSourceDefinition serverSource = (IServerDataSourceDefinition) feedDefinition;
                if(serverSource.getCredentialsDefinition() == CredentialsDefinition.STANDARD_USERNAME_PW){
                    if(serverSource.getUsername() != null && serverSource.retrievePassword() != null && !"".equals(serverSource.retrievePassword())) {
                            PasswordStorage.setPasswordCredentials(serverSource.getUsername(), serverSource.retrievePassword(), serverSource.getDataFeedID(), conn);
                    }
                    else if(serverSource.getUsername() == null && serverSource.getPassword() == null) {
                        PasswordStorage.clearPasswordCredentials(serverSource.getDataFeedID(), conn);
                    }
                }
                else if(serverSource.getCredentialsDefinition() == CredentialsDefinition.SALESFORCE) {
                    PreparedStatement updateStatement = conn.prepareStatement("UPDATE session_id_storage SET session_id = ? WHERE data_feed_id = ?");
                    updateStatement.setString(1, serverSource.getSessionId());
                    updateStatement.setLong(2, serverSource.getDataFeedID());

                    PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO session_id_storage(data_feed_id, session_id) VALUES (?,?)");
                    insertStatement.setLong(1, serverSource.getDataFeedID());
                    insertStatement.setString(2, serverSource.getSessionId());

                    int rows = updateStatement.executeUpdate();
                    if (rows == 0)
                        insertStatement.execute();
                }
            }
            if (feedDefinition.getDataSourceType() == DataSourceInfo.STORED_PUSH || feedDefinition.getDataSourceType() == DataSourceInfo.STORED_PULL) {
                List<AnalysisItem> existingFields = existingFeed.getFields();
                metadata = DataStorage.writeConnection(feedDefinition, conn);
                feedStorage.updateDataFeedConfiguration(feedDefinition, conn);
                int version = metadata.migrate(existingFields, feedDefinition.getFields());
                feedStorage.updateVersion(feedDefinition, version, conn);
                metadata.commit();
            } else {
                feedStorage.updateDataFeedConfiguration(feedDefinition, conn);
            }
            Session session = Database.instance().createSession(conn);
            try {
                if (newTaskGen) {
                    DataSourceTaskGenerator generator = new DataSourceTaskGenerator();
                    generator.setStartTaskDate(new Date());
                    generator.setDataSourceID(feedDefinition.getDataFeedID());
                    generator.setTaskInterval((int) feedDefinition.getRefreshDataInterval());
                    session.save(generator);
                }
                notifyNewOwners(feedDefinition, existingFeed, session);
                notifyNewViewers(feedDefinition, existingFeed, session);
                session.flush();
            } finally {
                session.close();
            }

            FeedRegistry.instance().flushCache(feedID);

            conn.commit();
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
        EventDispatcher.instance().dispatch(new TodoCompletedEvent(feedDefinition));
        /*final User originator = new UserService().retrieveUser();
        new Thread(new Runnable() {
            public void run() {

                InternalUserService internalUserService = new InternalUserService();
                for (UserFeedLink userFeedLink : userMap.values()) {
                    User user = internalUserService.retrieveUser(userFeedLink.getUserID());
                    new NewAccountInvitation().sendFeedInvitation(user.getEmail(), feedName, String.valueOf(feedID), originator.getName());
                }
            }
        }).start();*/
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

    private static class ContainedInfo {
        Set<AnalysisItem> feedItems = new HashSet<AnalysisItem>();
    }    
}
