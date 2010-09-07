package com.easyinsight.datafeeds;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.etl.LookupPair;
import com.easyinsight.etl.LookupTable;
import com.easyinsight.etl.LookupTableDescriptor;
import com.easyinsight.etl.LookupTableUtil;
import com.easyinsight.reportpackage.ReportPackageDescriptor;
import com.easyinsight.scorecard.ScorecardStorage;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.*;
import com.easyinsight.security.SecurityException;
import com.easyinsight.goals.GoalTreeDescriptor;
import com.easyinsight.groups.GroupDescriptor;
import com.easyinsight.email.UserStub;
import com.easyinsight.notifications.UserToDataSourceNotification;
import com.easyinsight.notifications.NotificationBase;
import com.easyinsight.notifications.DataSourceToGroupNotification;
import com.easyinsight.users.User;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import com.easyinsight.util.RandomTextGenerator;
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

    public HomeState determineHomeState() {
        // has a data source been successfully defined?
        // have any reports been successfully created?
        Map<Long, DataSourceDescriptor> dataSourceMap = new HashMap<Long, DataSourceDescriptor>();
        Map<Long, InsightDescriptor> reportMap = new HashMap<Long, InsightDescriptor>();
        EIConnection conn = Database.instance().getConnection();
        try {
            Map<Long, ExtraDataSourceInfo> infos = new HashMap<Long, ExtraDataSourceInfo>();
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED_ID, DATA_FEED.feed_name, DATA_FEED.feed_type, DATA_FEED.create_date" +
                    " FROM DATA_FEED, UPLOAD_POLICY_USERS WHERE VISIBLE = ? AND " +
                    "UPLOAD_POLICY_USERS.user_id = ? AND UPLOAD_POLICY_USERS.feed_id = data_feed.data_feed_id");
            queryStmt.setBoolean(1, true);
            queryStmt.setLong(2, SecurityUtil.getUserID());
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long dataSourceID = rs.getLong(1);
                String name = rs.getString(2);
                int feedType = rs.getInt(3);
                Date creationDate = new Date(rs.getTimestamp(4).getTime());
                DataSourceDescriptor dataSourceDescriptor = new DataSourceDescriptor(name, dataSourceID, feedType);
                infos.put(dataSourceID, new ExtraDataSourceInfo(creationDate, feedType));
                dataSourceMap.put(dataSourceID, dataSourceDescriptor);
            }
            queryStmt.close();
            PreparedStatement queryGroupStmt = conn.prepareStatement("SELECT DATA_FEED_ID, DATA_FEED.feed_name, DATA_FEED.feed_type," +
                    "DATA_FEED.create_date FROM DATA_FEED, UPLOAD_POLICY_GROUPS, group_to_user_join WHERE " +
                    "DATA_FEED.data_feed_id = upload_policy_groups.feed_id and group_to_user_join.group_id = upload_policy_groups.group_id and " +
                    "group_to_user_join.user_id = ? AND DATA_FEED.VISIBLE = ?");
            queryGroupStmt.setLong(1, SecurityUtil.getUserID());
            queryGroupStmt.setBoolean(2, true);
            ResultSet groupRS = queryGroupStmt.executeQuery();
            while (groupRS.next()) {
                long dataSourceID = groupRS.getLong(1);
                String name = groupRS.getString(2);
                int feedType = groupRS.getInt(3);
                Date creationDate = new Date(groupRS.getTimestamp(4).getTime());
                DataSourceDescriptor dataSourceDescriptor = new DataSourceDescriptor(name, dataSourceID, feedType);
                infos.put(dataSourceID, new ExtraDataSourceInfo(creationDate, feedType));
                dataSourceMap.put(dataSourceID, dataSourceDescriptor);
            }
            for (DataSourceDescriptor dataSource : dataSourceMap.values()) {
                describe(dataSource, infos.get(dataSource.getId()), conn);
            }
            queryGroupStmt.close();
            PreparedStatement reportQueryStmt = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID, ANALYSIS.data_feed_id, analysis.title, analysis.report_type," +
                    "analysis.url_key FROM " +
                        "USER_TO_ANALYSIS, ANALYSIS WHERE " +
                        "USER_TO_ANALYSIS.analysis_id = analysis.analysis_id and analysis.temporary_report = ? and user_to_analysis.user_id = ?");
            reportQueryStmt.setBoolean(1, false);
            reportQueryStmt.setLong(2, SecurityUtil.getUserID());
            ResultSet queryRS = reportQueryStmt.executeQuery();
            while (queryRS.next()) {
                long reportID = queryRS.getLong(1);
                long dataSourceID = queryRS.getLong(2);
                String reportName = queryRS.getString(3);
                int reportType = queryRS.getInt(4);
                String urlKey = queryRS.getString(5);
                reportMap.put(reportID, new InsightDescriptor(reportID, reportName, dataSourceID, reportType, urlKey));
            }
            reportQueryStmt.close();
            PreparedStatement groupReportStmt = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID, ANALYSIS.data_feed_id, analysis.title, analysis.report_type," +
                    "analysis.url_key FROM " +
                        "group_to_insight, analysis, group_to_user_join WHERE " +
                            "GROUP_TO_INSIGHT.group_id = group_to_user_join.group_id and group_to_user_join.user_id = ? and " +
                            "group_to_insight.insight_id = analysis.analysis_id and analysis.temporary_report = ?");
            groupReportStmt.setLong(1, SecurityUtil.getUserID());
            groupReportStmt.setBoolean(2, false);
            ResultSet groupReportRS = groupReportStmt.executeQuery();
            while (groupReportRS.next()) {
                long reportID = groupReportRS.getLong(1);
                long dataSourceID = groupReportRS.getLong(2);
                String reportName = groupReportRS.getString(3);
                int reportType = groupReportRS.getInt(4);
                String urlKey = groupReportRS.getString(5);
                reportMap.put(reportID, new InsightDescriptor(reportID, reportName, dataSourceID, reportType, urlKey));
            }
            groupReportStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return new HomeState(new ArrayList<DataSourceDescriptor>(dataSourceMap.values()), new ArrayList<InsightDescriptor>(reportMap.values()));
    }

    private static class ExtraDataSourceInfo {
        private Date creationDate;
        private int feedType;

        private ExtraDataSourceInfo(Date creationDate, int feedType) {
            this.creationDate = creationDate;
            this.feedType = feedType;
        }
    }

    private void describe(DataSourceDescriptor descriptor, ExtraDataSourceInfo extraInfo, EIConnection conn) throws SQLException {
        PreparedStatement queryReportCountStmt = conn.prepareStatement("SELECT COUNT(ANALYSIS_ID) FROM ANALYSIS WHERE " +
                "ANALYSIS.data_feed_id = ? AND ANALYSIS.temporary_report = ?");            
        String description;
        queryReportCountStmt.setLong(1, descriptor.getId());
        queryReportCountStmt.setBoolean(2, false);
        ResultSet rs = queryReportCountStmt.executeQuery();
        int count = 0;
        if (rs.next()) {
            count = rs.getInt(1);
        }
        String dateString = SimpleDateFormat.getDateInstance().format(extraInfo.creationDate);
        if (count == 0) {
            description = "this data source was created on " + dateString + " and has no reports.";
        } else if (count == 1) {
            description = "this data source was created on " + dateString + " and has 1 report.";
        } else {
            description = "this data source was created on " + dateString + " and has " + count + " reports.";
        }
        descriptor.setDescription(description);
        queryReportCountStmt.close();
    }

    public boolean needsConfig(long dataSourceID) {
        SecurityUtil.authorizeFeed(dataSourceID, Roles.SUBSCRIBER);
        try {
            return feedStorage.getFeedDefinitionData(dataSourceID).getCredentialsDefinition() != CredentialsDefinition.NO_CREDENTIALS;
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
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

    public MultiReportInfo getReportsForDataSource(long dataSourceID) {
        List<InsightDescriptor> descriptors = new ArrayList<InsightDescriptor>();
        String apiKey;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement keyStmt = conn.prepareStatement("SELECT DATA_FEED.API_KEY FROM DATA_FEED WHERE DATA_FEED.data_feed_id = ?");
            keyStmt.setLong(1, dataSourceID);
            ResultSet keyRS = keyStmt.executeQuery();
            keyRS.next();
            apiKey = keyRS.getString(1);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID, TITLE, REPORT_TYPE FROM ANALYSIS, USER_TO_ANALYSIS WHERE " +
                    "data_feed_id = ? and root_definition = ? AND " +
                    "USER_TO_ANALYSIS.analysis_id = ANALYSIS.analysis_id AND USER_TO_ANALYSIS.user_id = ? AND TEMPORARY_REPORT = ?");
            queryStmt.setLong(1, dataSourceID);
            queryStmt.setBoolean(2, false);
            queryStmt.setLong(3, SecurityUtil.getUserID());
            queryStmt.setBoolean(4, false);
            ResultSet reportRS = queryStmt.executeQuery();
            while (reportRS.next()) {
                // TODO: Add urlKey
                descriptors.add(new InsightDescriptor(reportRS.getLong(1), reportRS.getString(2), dataSourceID, reportRS.getInt(3),null));
            }
            queryStmt.close();
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
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
        return new MultiReportInfo(apiKey, validDescriptors);
    }

    public List<CredentialRequirement> launchAsyncRefresh(long dataSourceID, List<CredentialFulfillment> credentials) {
        try {
            return new ScorecardStorage().blah(dataSourceID, credentials);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<EIDescriptor> getDescriptors() {
        List<EIDescriptor> descriptorList = new ArrayList<EIDescriptor>();
        Connection conn = Database.instance().getConnection();

        try {
            PreparedStatement queryDataSources = conn.prepareStatement("SELECT FEED_NAME, DATA_FEED.DATA_FEED_ID, DATA_FEED.FEED_TYPE FROM DATA_FEED, UPLOAD_POLICY_USERS WHERE " +
                    "DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND UPLOAD_POLICY_USERS.user_id = ? AND DATA_FEED.visible = ?");
            queryDataSources.setLong(1, SecurityUtil.getUserID());
            queryDataSources.setBoolean(2, true);
            ResultSet queryRS = queryDataSources.executeQuery();
            while (queryRS.next()) {
                descriptorList.add(new DataSourceDescriptor(queryRS.getString(1), queryRS.getLong(2), queryRS.getInt(3)));
            }
            PreparedStatement getInsightsStmt = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID, TITLE, DATA_FEED_ID, REPORT_TYPE, URL_KEY FROM ANALYSIS, user_to_analysis WHERE " +
                    "ANALYSIS.ANALYSIS_ID = USER_TO_ANALYSIS.ANALYSIS_ID AND USER_TO_ANALYSIS.user_id = ? AND ANALYSIS.ROOT_DEFINITION = ? AND ANALYSIS.TEMPORARY_REPORT = ?");
            getInsightsStmt.setLong(1, SecurityUtil.getUserID());
            getInsightsStmt.setBoolean(2, false);
            getInsightsStmt.setBoolean(3, false);
            ResultSet reportRS = getInsightsStmt.executeQuery();
            while (reportRS.next()) {
                descriptorList.add(new InsightDescriptor(reportRS.getLong(1), reportRS.getString(2), reportRS.getLong(3), reportRS.getInt(4), reportRS.getString(5)));
            }
            PreparedStatement getGoalTreeStmt = conn.prepareStatement("SELECT GOAL_TREE.GOAL_TREE_ID, GOAL_TREE.name, USER_TO_GOAL_TREE.user_role, GOAL_TREE.goal_tree_icon FROM GOAL_TREE, USER_TO_GOAL_TREE " +
                    "WHERE GOAL_TREE.goal_tree_id = user_to_goal_tree.goal_tree_id and user_to_goal_tree.user_id = ?");
            getGoalTreeStmt.setLong(1, SecurityUtil.getUserID());
            ResultSet goalTreeRS = getGoalTreeStmt.executeQuery();
            while (goalTreeRS.next()) {
                // TODO: Add urlKey
                descriptorList.add(new GoalTreeDescriptor(goalTreeRS.getLong(1), goalTreeRS.getString(2), goalTreeRS.getInt(3), goalTreeRS.getString(4), null));
            }
            PreparedStatement getPackageStmt = conn.prepareStatement("SELECT REPORT_PACKAGE.package_name, REPORT_PACKAGE.report_package_id, REPORT_PACKAGE.url_key FROM REPORT_PACKAGE, user_to_report_package WHERE " +
                    "user_to_report_package.user_id = ? AND report_package.report_package_id = user_to_report_package.report_package_id AND REPORT_PACKAGE.TEMPORARY_PACKAGE = ?");
            getPackageStmt.setLong(1, SecurityUtil.getUserID());
            getPackageStmt.setBoolean(2, false);
            ResultSet packageRS = getPackageStmt.executeQuery();
            while (packageRS.next()) {
                String packageName = packageRS.getString(1);
                long packageID = packageRS.getLong(2);
                String urlKey = packageRS.getString(3);
                descriptorList.add(new ReportPackageDescriptor(packageName, packageID, urlKey));
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

    public FeedResponse openFeedIfPossible(String urlKey) {
        FeedResponse feedResponse;
        try {
            try {
                long feedID = SecurityUtil.authorizeFeedAccess(urlKey);
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
            FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(feedID, conn);
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

    public JoinAnalysis testJoin(CompositeFeedConnection connection, List<CredentialFulfillment> credentials) {
        try {
            return new JoinTester(connection).generateReport(credentials);
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

    public String updateFeedDefinition(FeedDefinition feedDefinition, String tagString) {
        SecurityUtil.authorizeFeed(feedDefinition.getDataFeedID(), Roles.OWNER);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            if (tagString == null) {
                tagString = "";
            }
            String[] tags = tagString.split(" ");
            List<Tag> tagList = new ArrayList<Tag>();
            for (String tagName : tags) {
                tagList.add(new Tag(tagName));
            }
            feedDefinition.setTags(tagList);
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

    public List<LookupTableDescriptor> getLookupTableDescriptors() {
        List<LookupTableDescriptor> descriptors = new ArrayList<LookupTableDescriptor>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT LOOKUP_TABLE_ID, LOOKUP_TABLE_NAME, DATA_SOURCE_ID FROM LOOKUP_TABLE, UPLOAD_POLICY_USERS " +
                    "WHERE LOOKUP_TABLE.data_source_id = UPLOAD_POLICY_USERS.feed_id  AND UPLOAD_POLICY_USERS.user_id = ?");
            queryStmt.setLong(1, SecurityUtil.getUserID());
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                LookupTableDescriptor lookupTableDescriptor = new LookupTableDescriptor();
                lookupTableDescriptor.setId(rs.getLong(1));
                lookupTableDescriptor.setName(rs.getString(2));
                lookupTableDescriptor.setDataSourceID(rs.getLong(3));
                descriptors.add(lookupTableDescriptor);
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return descriptors;
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
                throw new RuntimeException();
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
                    if (sourceValue.length() > 200) {
                        sourceValue = sourceValue.substring(0, 200);
                    }
                    insertStmt.setString(2, sourceValue);
                    insertStmt.setString(3, lookupPair.getTargetValue().toString());
                    insertStmt.execute();
                } else {
                    String sourceValue = lookupPair.getSourceValue().toString();
                    if (sourceValue.length() > 200) {
                        sourceValue = sourceValue.substring(0, 200);
                    }
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
                    if (sourceValue.length() > 200) {
                        sourceValue = sourceValue.substring(0, 200);
                    }
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
                    if (sourceValue.length() > 200) {
                        sourceValue = sourceValue.substring(0, 200);
                    }
                    updateStmt.setString(1, sourceValue);
                    DateValue dateValue = (DateValue) lookupPair.getTargetValue();
                    if (dateValue.getDate() == null) {
                        updateStmt.setNull(2, Types.TIMESTAMP);
                    } else {
                        updateStmt.setTimestamp(2, new java.sql.Timestamp(dateValue.getDate().getTime()));
                    }
                    updateStmt.setLong(3, lookupPair.getLookupPairID());
                    updateStmt.execute();
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
                    if (sourceValue.length() > 200) {
                        sourceValue = sourceValue.substring(0, 200);
                    }
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
                    if (sourceValue.length() > 200) {
                        sourceValue = sourceValue.substring(0, 200);
                    }
                    updateStmt.setString(1, sourceValue);
                    NumericValue numericValue = (NumericValue) lookupPair.getTargetValue();
                    if (numericValue.getValue() == null) {
                        updateStmt.setNull(2, Types.DOUBLE);
                    } else {
                        updateStmt.setDouble(2, numericValue.getValue());
                    }
                    updateStmt.setLong(3, id);
                    updateStmt.execute();
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
