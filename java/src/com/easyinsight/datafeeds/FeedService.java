package com.easyinsight.datafeeds;

import com.easyinsight.analysis.*;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.database.Database;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.*;
import com.easyinsight.users.SubscriptionLicense;

import java.util.*;
import java.sql.SQLException;
import java.sql.Connection;

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

    public FeedResponse openFeedIfPossible(long feedID) {
        FeedResponse feedResponse;
        try {
            try {
                SecurityUtil.authorizeFeedAccess(feedID);
                long userID = SecurityUtil.getUserID();
                FeedDescriptor feedDescriptor = feedStorage.getFeedDescriptor(userID, feedID);
                feedResponse = new FeedResponse(true, feedDescriptor);
            } catch (com.easyinsight.security.SecurityException e) {
                feedResponse = new FeedResponse(false, null);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return feedResponse;
    }

    public void wipeData(long feedID) {
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
            Database.instance().closeConnection(conn);
        }
    }

    public void addView(long feedID) {
        feedStorage.addFeedView(feedID);
    }

    public void addRating(long feedID, long userID, int rating) {
        feedStorage.rateFeed(feedID, userID, rating);
    }

    public List<Tag> getAllFeedTags() {
        try {
            return new TagStorage().getTags(20);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<BriefFeedInfo> getBriefFeedInfo(List<Integer> feedIDs) {
        try {
            return feedStorage.getBriefFeedInfo(feedIDs);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<FeedDefinition> getFeedDefinitions(List<SubscriptionLicense> licenses) {
        List<FeedDefinition> descriptorList = new ArrayList<FeedDefinition>();
        for (SubscriptionLicense license : licenses) {
            descriptorList.add(feedStorage.getFeedDefinitionData(license.getFeedID()));
        }
        return descriptorList;
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

    public List<CompositeFeedConnection> initialDefine(List<CompositeFeedNode> nodes, List<FeedDescriptor> newFeeds) {
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
                        List<CompositeFeedConnection> potentialJoins = joinDiscovery.findPotentialJoins(node.getDataFeedID(), newFeed.getDataFeedID());
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
                        List<CompositeFeedConnection> potentialJoins = joinDiscovery.findPotentialJoins(otherNewFeed.getDataFeedID(), newFeed.getDataFeedID());
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

    public InitialAnalysis getInitialAnalysisSetup(long dataFeedID) {
        try {
            return feedStorage.getFeedDefinitionData(dataFeedID).initialAnalysisDefinition();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<WSAnalysisDefinition> getMostPopularAnalyses(String genre,  int cutoff) {
        try {
            return analysisStorage.getMostPopularAnalyses(genre, cutoff);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<WSAnalysisDefinition> getTopRatedAnalyses(String genre, int cutoff) {
        try {
            return analysisStorage.getBestRatedAnalyses(genre, cutoff);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<WSAnalysisDefinition> getMostRecentAnalyses(String genre, int cutoff) {
        try {
            return analysisStorage.getMostRecentAnalyses(genre, cutoff);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<WSAnalysisDefinition> getHeadlineAnalysesForGenre(String genre) {
        try {
            return marketplaceStorage.getAnalysisDefinitionsForGenre(genre);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<WSAnalysisDefinition> getAllAnalysesForGenre(String genre) {
        try {
            return analysisStorage.getAllDefinitions(genre);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public long createCompositeFeed(List<CompositeFeedNode> compositeFeedNodes, List<CompositeFeedConnection> edges, String feedName) {
        long userID = SecurityUtil.getUserID();
        try {
            CompositeFeedDefinition feedDef = new CompositeFeedDefinition();
            feedDef.setFeedName(feedName);
            feedDef.setCompositeFeedNodes(compositeFeedNodes);
            feedDef.setConnections(edges);
            feedDef.setUploadPolicy(new UploadPolicy(userID));
            final ContainedInfo containedInfo = new ContainedInfo();
            new CompositeFeedNodeVisitor() {

                protected void accept(CompositeFeedNode compositeFeedNode) throws SQLException {
                    FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(compositeFeedNode.getDataFeedID());
                    containedInfo.feedItems.addAll(feedDefinition.getFields());
                }
            }.visit(feedDef);
            feedDef.populateFields();
            long feedID = feedStorage.addFeedDefinitionData(feedDef);
            ListDefinition baseDefinition = new ListDefinition();
            baseDefinition.setDataFeedID(feedID);
            baseDefinition.setRootDefinition(true);
            baseDefinition.setUserBindings(Arrays.asList(new UserToAnalysisBinding(SecurityUtil.getUserID(), UserPermission.OWNER)));
            new AnalysisStorage().saveAnalysis(baseDefinition);
            feedDef.setAnalysisDefinitionID(baseDefinition.getAnalysisID());
            feedStorage.updateDataFeedConfiguration(feedDef);
            new UserUploadService().createUserFeedLink(userID, feedID, Roles.OWNER);
            return feedID;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void updateCompositeFeed(List<CompositeFeedNode> compositeFeedNodes, List<CompositeFeedConnection> edges, long feedID) {
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

    public void updateFeedDefinition(FeedDefinition feedDefinition, String tagString) {
        /*final Map<Long, UserFeedLink> userMap = new HashMap<Long, UserFeedLink>();
        if (feedDefinition.getUploadPolicy().getUploadPolicy() == UploadPolicy.PRIVATE) {
            PrivateUploadPolicy privatePolicy = (PrivateUploadPolicy) feedDefinition.getUploadPolicy();
            List<UserFeedLink> userFeedLinks = privatePolicy.getUserFeedLinks();
            for (UserFeedLink userFeedLink : userFeedLinks) {
                userMap.put(userFeedLink.getUserID(), userFeedLink);
            }
            if (feedDefinition.getUploadPolicy().getUploadPolicy() == UploadPolicy.PRIVATE) {
                PrivateUploadPolicy existingPolicy = (PrivateUploadPolicy) feedDefinition.getUploadPolicy();
                List<UserFeedLink> existingLinks = existingPolicy.getUserFeedLinks();
                for (UserFeedLink userFeedLink : existingLinks) {
                    userMap.remove(userFeedLink.getUserID());
                }
            }
        }   */
        Connection conn = Database.instance().getConnection();
        DataStorage metadata = null;
        try {
            conn.setAutoCommit(false);
            String[] tags = tagString.split(" ");
            List<Tag> tagList = new ArrayList<Tag>();
            for (String tagName : tags) {
                tagList.add(new Tag(tagName));
            }
            feedDefinition.setTags(tagList);
            final String feedName = feedDefinition.getFeedName();
            final long feedID = feedDefinition.getDataFeedID();
            FeedDefinition existingFeed = feedStorage.getFeedDefinitionData(feedDefinition.getDataFeedID());
            List<AnalysisItem> existingFields = existingFeed.getFields();
            metadata = DataStorage.writeConnection(feedDefinition, conn);
            feedStorage.updateDataFeedConfiguration(feedDefinition, conn);
            metadata.migrate(existingFields, feedDefinition.getFields());
            FeedRegistry.instance().flushCache(feedID);
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
            Database.instance().closeConnection(conn);
        }
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

    public FeedDefinition getFeedDefinition(long dataFeedID) {
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
