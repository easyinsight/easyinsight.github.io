package com.easyinsight.datafeeds;

import com.easyinsight.database.Database;
import com.easyinsight.userupload.*;
import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.google.GoogleFeedDefinition;
import com.easyinsight.datafeeds.salesforce.SalesforceFeedDefinition;
import com.easyinsight.datafeeds.salesforce.SalesforceSubFeedDefinition;
import com.easyinsight.datafeeds.file.FileBasedFeedDefinition;
import com.easyinsight.AnalysisItem;
import com.easyinsight.AnalysisItemTypes;
import com.easyinsight.email.UserStub;
import com.easyinsight.groups.GroupDescriptor;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.Roles;

import java.sql.*;
import java.util.*;
import java.util.Date;

import org.hibernate.Session;

/**
 * User: jboe
 * Date: Jan 3, 2008
 * Time: 10:46:24 PM
 */
public class FeedStorage {

    public long addFeedDefinitionData(FeedDefinition feedDefinition, Connection conn) throws SQLException {
        PreparedStatement insertDataFeedStmt;
        insertDataFeedStmt = conn.prepareStatement("INSERT INTO DATA_FEED (FEED_NAME, FEED_TYPE, PUBLICLY_VISIBLE, FEED_SIZE, " +
                    "CREATE_DATE, UPDATE_DATE, FEED_VIEWS, FEED_RATING_COUNT, FEED_RATING_AVERAGE, DESCRIPTION," +
                    "ATTRIBUTION, OWNER_NAME, DYNAMIC_SERVICE_DEFINITION_ID, ANALYSIS_ID, MARKETPLACE_VISIBLE) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
        insertDataFeedStmt.setString(1, feedDefinition.getFeedName());
        insertDataFeedStmt.setInt(2, feedDefinition.getFeedType().getType());
        insertDataFeedStmt.setBoolean(3, feedDefinition.getUploadPolicy().isPubliclyVisible());
        insertDataFeedStmt.setLong(4, feedDefinition.getSize());
        if (feedDefinition.getDateCreated() == null) {
            feedDefinition.setDateCreated(new Date());
        }
        if (feedDefinition.getDateUpdated() == null) {
            feedDefinition.setDateUpdated(new Date());
        }
        insertDataFeedStmt.setDate(5, new java.sql.Date(feedDefinition.getDateCreated().getTime()));
        insertDataFeedStmt.setDate(6, new java.sql.Date(feedDefinition.getDateUpdated().getTime()));
        insertDataFeedStmt.setInt(7, feedDefinition.getViewCount());
        insertDataFeedStmt.setInt(8, feedDefinition.getRatingCount());
        insertDataFeedStmt.setDouble(9, feedDefinition.getRatingAverage());
        insertDataFeedStmt.setString(10, feedDefinition.getDescription());
        insertDataFeedStmt.setString(11, feedDefinition.getAttribution());
        insertDataFeedStmt.setString(12, feedDefinition.getOwnerName());
        if (feedDefinition.getDynamicServiceDefinitionID() > 0)
            insertDataFeedStmt.setLong(13, feedDefinition.getDynamicServiceDefinitionID());
        else
            insertDataFeedStmt.setNull(13, Types.BIGINT);
        if (feedDefinition.getAnalysisDefinitionID() > 0) {
            insertDataFeedStmt.setLong(14, feedDefinition.getAnalysisDefinitionID());
        } else {
            insertDataFeedStmt.setNull(14, Types.BIGINT);
        }
        insertDataFeedStmt.setBoolean(15, feedDefinition.getUploadPolicy().isMarketplaceVisible());
        insertDataFeedStmt.execute();
        long feedID = Database.instance().getAutoGenKey(insertDataFeedStmt);
        feedDefinition.setDataFeedID(feedID);
        savePolicy(conn, feedID, feedDefinition.getUploadPolicy());
        feedDefinition.setDataFeedID(feedID);
        saveFields(feedID, conn, feedDefinition.getFields());
        saveTags(feedID, conn, feedDefinition.getTags());
        feedDefinition.customStorage(conn);
        return feedID;
    }

    public long addFeedDefinitionData(FeedDefinition feedDefinition) {
        Connection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            long feedID = addFeedDefinitionData(feedDefinition, conn);
            conn.commit();
            return feedID;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
    }

    private void savePolicy(Connection conn, long feedID, UploadPolicy uploadPolicy) throws SQLException {
        PreparedStatement clearExistingStmt = conn.prepareStatement("DELETE FROM UPLOAD_POLICY_USERS WHERE FEED_ID = ?");
        clearExistingStmt.setLong(1, feedID);
        clearExistingStmt.executeUpdate();
        clearExistingStmt.close();
        PreparedStatement clearGroupStmt = conn.prepareStatement("DELETE FROM UPLOAD_POLICY_GROUPS WHERE FEED_ID = ?");
        clearGroupStmt.setLong(1, feedID);
        clearGroupStmt.executeUpdate();
        clearGroupStmt.close();
        PreparedStatement addUserStmt = conn.prepareStatement("INSERT INTO UPLOAD_POLICY_USERS (USER_ID, FEED_ID, ROLE) VALUES (?, ?, ?)");
        PreparedStatement addGroupStmt = conn.prepareStatement("INSERT INTO UPLOAD_POLICY_GROUPS (GROUP_ID, FEED_ID, ROLE) VALUES (?, ?, ?)");
        for (FeedConsumer feedConsumer : uploadPolicy.getOwners()) {
            if (feedConsumer instanceof UserStub) {
                UserStub userStub = (UserStub) feedConsumer;
                addUserStmt.setLong(1, userStub.getUserID());
                addUserStmt.setLong(2, feedID);
                addUserStmt.setInt(3, Roles.OWNER);
                addUserStmt.execute();
            } else if (feedConsumer instanceof GroupDescriptor) {
                GroupDescriptor groupDescriptor = (GroupDescriptor) feedConsumer;
                addGroupStmt.setLong(1, groupDescriptor.getGroupID());
                addGroupStmt.setLong(2, feedID);
                addGroupStmt.setInt(3, Roles.OWNER);
                addGroupStmt.execute();
            }
        }
        for (FeedConsumer feedConsumer : uploadPolicy.getViewers()) {
            if (feedConsumer instanceof UserStub) {
                UserStub userStub = (UserStub) feedConsumer;
                addUserStmt.setLong(1, userStub.getUserID());
                addUserStmt.setLong(2, feedID);
                addUserStmt.setInt(3, Roles.SHARER);
                addUserStmt.execute();
            } else if (feedConsumer instanceof GroupDescriptor) {
                GroupDescriptor groupDescriptor = (GroupDescriptor) feedConsumer;
                addGroupStmt.setLong(1, groupDescriptor.getGroupID());
                addGroupStmt.setLong(2, feedID);
                addGroupStmt.setInt(3, Roles.SHARER);
                addGroupStmt.execute();
            }
        }
    }

    private void saveTags(long feedID, Connection conn, Collection<Tag> tags) throws SQLException {
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM feed_to_tag WHERE FEED_ID = ?");
        deleteStmt.setLong(1, feedID);
        deleteStmt.executeUpdate();
        deleteStmt.close();
        if (tags != null) {
            Session session = Database.instance().createSession(conn);
            try {
                for (Tag tag : tags) {
                    if (tag.getTagID() != null && tag.getTagID() == 0) {
                        tag.setTagID(null);
                    }
                    session.saveOrUpdate(tag);
                }
                session.flush();
            } catch (Exception e) {
                LogClass.error(e);
                throw new RuntimeException(e);
            } finally {
                session.close();
            }
            PreparedStatement insertLinkStmt = conn.prepareStatement("INSERT INTO FEED_TO_TAG (FEED_ID, ANALYSIS_TAGS_ID) " +
                    "VALUES (?, ?)");
            for (Tag tag : tags) {
                insertLinkStmt.setLong(1, feedID);
                insertLinkStmt.setLong(2, tag.getTagID());
                insertLinkStmt.execute();
            }
            insertLinkStmt.close();
        }
    }

    private void saveFields(long feedID, Connection conn, List<AnalysisItem> analysisItems) throws SQLException {
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM FEED_TO_ANALYSIS_ITEM WHERE FEED_ID = ?");
        deleteStmt.setLong(1, feedID);
        deleteStmt.executeUpdate();
        deleteStmt.close();
        if (analysisItems != null) {
            Session session = Database.instance().createSession(conn);
            try {
                for (AnalysisItem analysisItem : analysisItems) {
                    if (analysisItem.getKey().getKeyID() != null && analysisItem.getKey().getKeyID() == 0) {
                        analysisItem.getKey().setKeyID(null);
                    }
                    session.saveOrUpdate(analysisItem.getKey());
                }
                for (AnalysisItem analysisItem : analysisItems) {
                    analysisItem.resetIDs();
                    session.save(analysisItem);
                }
                session.flush();
            } catch (Exception e) {
                LogClass.error(e);
                throw new RuntimeException(e);
            } finally {
                session.close();
            }
            PreparedStatement insertLinkStmt = conn.prepareStatement("INSERT INTO FEED_TO_ANALYSIS_ITEM (FEED_ID, ANALYSIS_ITEM_ID) " +
                    "VALUES (?, ?)");
            for (AnalysisItem analysisItem : analysisItems) {
                insertLinkStmt.setLong(1, feedID);
                insertLinkStmt.setLong(2, analysisItem.getAnalysisItemID());
                insertLinkStmt.execute();
            }
            insertLinkStmt.close();
        }
    }

    public Set<Tag> getTags(long feedID, Connection conn) throws SQLException {
        PreparedStatement queryTagsStmt = conn.prepareStatement("SELECT ANALYSIS_TAGS_ID FROM FEED_TO_TAG WHERE FEED_ID = ?");
        queryTagsStmt.setLong(1, feedID);
        Set<Long> tagIDs = new HashSet<Long>();
        ResultSet rs = queryTagsStmt.executeQuery();
        while (rs.next()) {
            tagIDs.add(rs.getLong(1));
        }
        queryTagsStmt.close();
        Set<Tag> tags = new HashSet<Tag>();
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            for (Long tagID : tagIDs) {
                List items = session.createQuery("from Tag where tagID = ?").setLong(0, tagID).list();
                if (items.size() > 0) {
                    Tag tag = (Tag) items.get(0);
                    tags.add(tag);
                }
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return tags;
    }

    public List<AnalysisItem> retrieveFields(long feedID, Connection conn) throws SQLException {
        PreparedStatement queryFieldsStmt = conn.prepareStatement("SELECT ANALYSIS_ITEM_ID FROM FEED_TO_ANALYSIS_ITEM WHERE FEED_ID = ?");
        queryFieldsStmt.setLong(1, feedID);
        Set<Long> analysisItemIDs = new HashSet<Long>();
        ResultSet rs = queryFieldsStmt.executeQuery();
        while (rs.next()) {
            analysisItemIDs.add(rs.getLong(1));
        }
        queryFieldsStmt.close();
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            for (Long analysisItemID : analysisItemIDs) {
                List items = session.createQuery("from AnalysisItem where analysisItemID = ?").setLong(0, analysisItemID).list();
                if (items.size() > 0) {
                    AnalysisItem analysisItem = (AnalysisItem) items.get(0);
                    if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
                        AnalysisHierarchyItem analysisHierarchyItem = (AnalysisHierarchyItem) analysisItem;
                        analysisHierarchyItem.setHierarchyLevels(new ArrayList<HierarchyLevel>(analysisHierarchyItem.getHierarchyLevels()));
                    }
                    analysisItems.add(analysisItem);
                }
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return analysisItems;
    }

    public void addFeedView(long feedID) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement getViewCountStmt = conn.prepareStatement("SELECT FEED_VIEWS FROM DATA_FEED WHERE " +
                    "DATA_FEED_ID = ?");
            PreparedStatement updateViewsStmt = conn.prepareStatement("UPDATE DATA_FEED SET FEED_VIEWS = ? WHERE " +
                    "DATA_FEED_ID = ?");
            getViewCountStmt.setLong(1, feedID);
            ResultSet rs = getViewCountStmt.executeQuery();
            if (rs.next()) {
                int feedViews = rs.getInt(1);
                updateViewsStmt.setInt(1, feedViews + 1);
                updateViewsStmt.setLong(2, feedID);
                updateViewsStmt.executeUpdate();
            }
            getViewCountStmt.close();
            updateViewsStmt.close();
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    public void rateFeed(long feedID, long accountID, int rating) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement getExistingRatingStmt = conn.prepareStatement("SELECT FEED_RATING_COUNT, FEED_RATING_AVERAGE FROM " +
                    "DATA_FEED WHERE DATA_FEED_ID = ?");
            PreparedStatement updateRatingStmt = conn.prepareStatement("UPDATE DATA_FEED SET FEED_RATING_COUNT = ?, " +
                    "FEED_RATING_AVERAGE = ? WHERE DATA_FEED_ID = ?");
            getExistingRatingStmt.setLong(1, feedID);
            ResultSet rs = getExistingRatingStmt.executeQuery();
            while (rs.next()) {
                int count = rs.getInt(1);
                double average = rs.getDouble(2);
                int newCount = count + 1;
                double newAverage = ((average + rating) * newCount) / newCount;
                updateRatingStmt.setInt(1, count);
                updateRatingStmt.setDouble(2, newAverage);
                updateRatingStmt.setLong(3, feedID);
                updateRatingStmt.executeUpdate();
            }
            getExistingRatingStmt.close();
            updateRatingStmt.close();
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    public void updateDataFeedConfiguration(FeedDefinition feedDefinition, Connection conn) throws SQLException {
        PreparedStatement updateDataFeedStmt = conn.prepareStatement("UPDATE DATA_FEED SET FEED_NAME = ?, FEED_TYPE = ?, PUBLICLY_VISIBLE = ?, GENRE = ?, " +
                        "FEED_SIZE = ?, ANALYSIS_ID = ?, DESCRIPTION = ?, ATTRIBUTION = ?, OWNER_NAME = ?, DYNAMIC_SERVICE_DEFINITION_ID = ?, MARKETPLACE_VISIBLE = ? WHERE DATA_FEED_ID = ?");
        feedDefinition.setDateUpdated(new Date());
        updateDataFeedStmt.setString(1, feedDefinition.getFeedName());
        updateDataFeedStmt.setInt(2, feedDefinition.getFeedType().getType());
        updateDataFeedStmt.setBoolean(3, feedDefinition.getUploadPolicy().isPubliclyVisible());
        updateDataFeedStmt.setString(4, feedDefinition.getGenre());
        updateDataFeedStmt.setLong(5, feedDefinition.getSize());
        updateDataFeedStmt.setLong(6, feedDefinition.getAnalysisDefinitionID());
        updateDataFeedStmt.setString(7, feedDefinition.getDescription());
        updateDataFeedStmt.setString(8, feedDefinition.getAttribution());
        updateDataFeedStmt.setString(9, feedDefinition.getOwnerName());
        if (feedDefinition.getDynamicServiceDefinitionID() > 0)
            updateDataFeedStmt.setLong(10, feedDefinition.getDynamicServiceDefinitionID());
        else
            updateDataFeedStmt.setNull(10, Types.BIGINT);
        updateDataFeedStmt.setBoolean(11, feedDefinition.getUploadPolicy().isMarketplaceVisible());
        updateDataFeedStmt.setLong(12, feedDefinition.getDataFeedID());
        int rows = updateDataFeedStmt.executeUpdate();
        if (rows != 1) {
            throw new RuntimeException("Could not locate row to update");
        }
        updateDataFeedStmt.close();
        savePolicy(conn, feedDefinition.getDataFeedID(), feedDefinition.getUploadPolicy());
        saveFields(feedDefinition.getDataFeedID(), conn, feedDefinition.getFields());
        saveTags(feedDefinition.getDataFeedID(), conn, feedDefinition.getTags());
    }

    public void updateDataFeedConfiguration(FeedDefinition feedDefinition) {
        Connection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            updateDataFeedConfiguration(feedDefinition, conn);
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
    }

    public FeedDefinition getFeedDefinitionData(long identifier, Connection conn) {
        FeedDefinition feedDefinition;
        try {
            PreparedStatement queryFeedStmt = conn.prepareStatement("SELECT FEED_NAME, FEED_TYPE, PUBLICLY_VISIBLE, GENRE, CREATE_DATE," +
                    "UPDATE_DATE, FEED_VIEWS, FEED_RATING_COUNT, FEED_RATING_AVERAGE, FEED_SIZE, ANALYSIS_ID," +
                    "ATTRIBUTION, DESCRIPTION, OWNER_NAME, DYNAMIC_SERVICE_DEFINITION_ID, MARKETPLACE_VISIBLE FROM DATA_FEED WHERE " +
                    "DATA_FEED_ID = ?");
            queryFeedStmt.setLong(1, identifier);
            ResultSet rs = queryFeedStmt.executeQuery();
            if (rs.next()) {
                String feedName = rs.getString(1);
                FeedType feedType = FeedType.valueOf(rs.getInt(2));
                if (feedType.equals(FeedType.STATIC)) {
                    feedDefinition = new FileBasedFeedDefinition();
                } else if (feedType.equals(FeedType.ANALYSIS_BASED)) {
                    feedDefinition = new AnalysisBasedFeedDefinition();
                } else if (feedType.equals(FeedType.GOOGLE)) {
                    feedDefinition = new GoogleFeedDefinition();
                } else if (feedType.equals(FeedType.COMPOSITE)) {
                    feedDefinition = new CompositeFeedDefinition();
                } else if (feedType.equals(FeedType.SALESFORCE)) {
                    feedDefinition = new SalesforceFeedDefinition();
                } else if (feedType.equals(FeedType.SALESFORCE_SUB)) {
                    feedDefinition = new SalesforceSubFeedDefinition();
                } else if (feedType.equals(FeedType.DEFAULT)) {
                    feedDefinition = new FeedDefinition();
                } else {
                    throw new RuntimeException("Couldn't identify type");
                }
                String genre = rs.getString(4);
                feedDefinition.setFeedName(feedName);
                feedDefinition.setDataFeedID(identifier);
                boolean publiclyVisible = rs.getBoolean(3);
                boolean marketplaceVisible = rs.getBoolean(15);
                feedDefinition.setUploadPolicy(createUploadPolicy(conn, identifier, publiclyVisible, marketplaceVisible));
                Date createDate = new Date(rs.getDate(5).getTime());
                Date updateDate = new Date(rs.getDate(6).getTime());
                int views = rs.getInt(7);
                int ratingCount = rs.getInt(8);
                double ratingAverage = rs.getDouble(9);
                long feedSize = rs.getLong(10);
                long analysisDefinitionID = rs.getLong(11);
                String attribution = rs.getString(12);
                String description = rs.getString(13);
                String ownerName = rs.getString(14);
                feedDefinition.setSize(feedSize);
                feedDefinition.setDateCreated(createDate);
                feedDefinition.setDateUpdated(updateDate);
                feedDefinition.setViewCount(views);
                feedDefinition.setRatingCount(ratingCount);
                feedDefinition.setRatingAverage(ratingAverage);
                feedDefinition.setGenre(genre);
                feedDefinition.setFields(retrieveFields(feedDefinition.getDataFeedID(), conn));
                feedDefinition.setAnalysisDefinitionID(analysisDefinitionID);
                feedDefinition.setAttribution(attribution);
                feedDefinition.setDescription(description);
                feedDefinition.setOwnerName(ownerName);
                feedDefinition.setDynamicServiceDefinitionID(rs.getLong(15));
                feedDefinition.setTags(getTags(feedDefinition.getDataFeedID(), conn));
                feedDefinition.customLoad(conn);
            } else {
                throw new RuntimeException("hmm");
            }
            queryFeedStmt.close();
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return feedDefinition;
    }

    public FeedDefinition getFeedDefinitionData(long identifier) {
        FeedDefinition feedDefinition;
        Connection conn = Database.instance().getConnection();
        try {
            feedDefinition = getFeedDefinitionData(identifier, conn);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return feedDefinition;

    }

    private FeedDescriptor createDescriptor(long dataFeedID, String feedName, boolean publiclyVisible, boolean marketplaceVisible, Integer userRole,
                                            long size, int feedType, String ownerName, String description, String attribution, Connection conn) throws SQLException {
        UploadPolicy uploadPolicy = createUploadPolicy(conn, dataFeedID, publiclyVisible, marketplaceVisible);
        return new FeedDescriptor(feedName, dataFeedID, uploadPolicy, size, feedType, userRole != null ? userRole : 0, ownerName, description, attribution);
    }

    public List<BriefFeedInfo> getBriefFeedInfo(List<Integer> feedIDs) {
        Set<Integer> feedIDSet = new HashSet<Integer>(feedIDs);
        List<BriefFeedInfo> briefFeedInfos = new ArrayList<BriefFeedInfo>();
        if (feedIDs.isEmpty()) return briefFeedInfos;
        Connection conn = Database.instance().getConnection();
        PreparedStatement feedStmt = null;
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT DATA_FEED_ID, FEED_NAME FROM DATA_FEED WHERE DATA_FEED_ID IN (");
            Iterator<Integer> iter = feedIDSet.iterator();
            while (iter.hasNext()) {
                Integer feedID = iter.next();
                queryBuilder.append(feedID);
                if (iter.hasNext()) {
                    queryBuilder.append(",");
                }
            }
            queryBuilder.append(")");
            feedStmt = conn.prepareStatement(queryBuilder.toString());
            ResultSet rs = feedStmt.executeQuery();
            while (rs.next()) {
                long feedID = rs.getLong(1);
                String feedName = rs.getString(2);
                briefFeedInfos.add(new BriefFeedInfo(feedName, feedID));
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            if (feedStmt != null) {
                try {
                    feedStmt.close();
                } catch (SQLException e) {
                    LogClass.error(e);
                }
            }
            Database.instance().closeConnection(conn);
        }
        return briefFeedInfos;
    }

    public FeedDescriptor getFeedDescriptor(long accountID, long feedID) {
        FeedDescriptor feedDescriptor = null;
        Connection conn = Database.instance().getConnection();
        try {
            StringBuilder queryBuilder = new StringBuilder("SELECT FEED_NAME, FEED_TYPE, OWNER_NAME, DESCRIPTION, ATTRIBUTION, ROLE, PUBLICLY_VISIBLE, MARKETPLACE_VISIBLE, ANALYSIS_ID " +
                    "FROM DATA_FEED, UPLOAD_POLICY_USERS WHERE " +
                    "DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND UPLOAD_POLICY_USERS.USER_ID = ? AND DATA_FEED_ID = ?");
            PreparedStatement queryStmt = conn.prepareStatement(queryBuilder.toString());
            queryStmt.setLong(1, accountID);
            queryStmt.setLong(2, feedID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                String feedName = rs.getString(1);
                int feedType = rs.getInt(2);
                String ownerName = rs.getString(3);
                String description = rs.getString(4);
                String attribution = rs.getString(5);
                int role = rs.getInt(6);
                boolean publiclyVisible = rs.getBoolean(7);
                boolean marketplaceVisible = rs.getBoolean(8);
                long analysisID = rs.getLong(9);
                AnalysisDefinition def = new AnalysisStorage().getAnalysisDefinition(analysisID, conn);
                if (def == null) {
                    LogClass.error("Could not find core definition for feed " + feedID);
                }
                WSAnalysisDefinition analysisDefinition = def.createBlazeDefinition();
                feedDescriptor = createDescriptor(feedID, feedName, publiclyVisible, marketplaceVisible, role, 0, feedType, ownerName, description, attribution, conn);
                Collection<Tag> tags = getTags(feedID, conn);
                StringBuilder tagStringBuilder = new StringBuilder();
                Iterator<Tag> tagIter = tags.iterator();
                while (tagIter.hasNext()) {
                    Tag tag = tagIter.next();
                    tagStringBuilder.append(tag.getTagName());
                    if (tagIter.hasNext()){
                        tagStringBuilder.append(" ");
                    }
                }
                feedDescriptor.setTagString(tagStringBuilder.toString());
                if (feedDescriptor.getRole() == Roles.OWNER) {
                    analysisDefinition.setCanSaveDirectly(true);
                }
                feedDescriptor.setDefinition(analysisDefinition);
            }
            queryStmt.close();
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return feedDescriptor;
    }    

    public List<FeedDescriptor> searchForSubscribedFeeds(long userID) {
        List<FeedDescriptor> descriptorList = new ArrayList<FeedDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DISTINCT DATA_FEED.DATA_FEED_ID, DATA_FEED.FEED_NAME, " +
                    "FEED_PERSISTENCE_METADATA.SIZE, DATA_FEED.FEED_TYPE, DATA_FEED.ANALYSIS_ID, OWNER_NAME, DESCRIPTION, ATTRIBUTION, ROLE, PUBLICLY_VISIBLE, MARKETPLACE_VISIBLE" +
                    " FROM UPLOAD_POLICY_USERS, DATA_FEED LEFT JOIN FEED_PERSISTENCE_METADATA ON DATA_FEED.DATA_FEED_ID = FEED_PERSISTENCE_METADATA.FEED_ID WHERE " +
                    "USER_ID = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID");
            queryStmt.setLong(1, userID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long dataFeedID = rs.getLong(1);
                String feedName = rs.getString(2);
                long feedSize = rs.getLong(3);
                int feedType = rs.getInt(4);
                long analysisID = rs.getLong(5);
                String ownerName = rs.getString(6);
                String description = rs.getString(7);
                String attribution = rs.getString(8);
                int userRole = rs.getInt(9);
                boolean publiclyVisible = rs.getBoolean(10);
                boolean marketplaceVisible = rs.getBoolean(11);
                AnalysisDefinition def = new AnalysisStorage().getAnalysisDefinition(analysisID, conn);
                if (def == null) {
                    LogClass.error("Could not find core definition for feed " + dataFeedID);
                    continue;
                }
                WSAnalysisDefinition analysisDefinition = def.createBlazeDefinition();
                FeedDescriptor feedDescriptor = createDescriptor(dataFeedID, feedName, publiclyVisible, marketplaceVisible, userRole, feedSize, feedType, ownerName, description, attribution, conn);
                Collection<Tag> tags = getTags(dataFeedID, conn);
                StringBuilder tagStringBuilder = new StringBuilder();
                Iterator<Tag> tagIter = tags.iterator();
                while (tagIter.hasNext()) {
                    Tag tag = tagIter.next();
                    tagStringBuilder.append(tag.getTagName());
                    if (tagIter.hasNext()){
                        tagStringBuilder.append(" ");
                    }
                }
                feedDescriptor.setTagString(tagStringBuilder.toString());
                if (feedDescriptor.getRole() == Roles.OWNER) {
                    analysisDefinition.setCanSaveDirectly(true);
                }
                feedDescriptor.setDefinition(analysisDefinition);
                descriptorList.add(feedDescriptor);
            }
            queryStmt.close();
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return descriptorList;
    }

    private UploadPolicy createUploadPolicy(Connection conn, long feedID, boolean publiclyVisible, boolean marketplaceVisible) throws SQLException {
        UploadPolicy uploadPolicy = new UploadPolicy();
        uploadPolicy.setPubliclyVisible(publiclyVisible);
        uploadPolicy.setMarketplaceVisible(marketplaceVisible);
        List<FeedConsumer> owners = new ArrayList<FeedConsumer>();
        List<FeedConsumer> viewers = new ArrayList<FeedConsumer>();
        PreparedStatement policyUserStmt = conn.prepareStatement("SELECT USER.USER_ID, ROLE, USER.NAME, USER.USERNAME, USER.EMAIL FROM UPLOAD_POLICY_USERS, USER WHERE FEED_ID = ? AND " +
                "UPLOAD_POLICY_USERS.USER_ID = USER.USER_ID");
        policyUserStmt.setLong(1, feedID);
        ResultSet usersRS = policyUserStmt.executeQuery();
        while (usersRS.next()) {
            long userID = usersRS.getLong(1);
            int role = usersRS.getInt(2);
            String name = usersRS.getString(3);
            String userName = usersRS.getString(4);
            String email = usersRS.getString(5);
            UserStub userStub = new UserStub(userID, userName, email, name);
            if (role == Roles.OWNER) {
                owners.add(userStub);
            } else {
                viewers.add(userStub);
            }
        }
        PreparedStatement policyGroupsStmt = conn.prepareStatement("SELECT COMMUNITY_GROUP.NAME, COMMUNITY_GROUP.COMMUNITY_GROUP_ID, ROLE FROM UPLOAD_POLICY_GROUPS, COMMUNITY_GROUP WHERE FEED_ID = ? AND " +
                "UPLOAD_POLICY_GROUPS.GROUP_ID = COMMUNITY_GROUP.COMMUNITY_GROUP_ID");
        policyGroupsStmt.setLong(1, feedID);
        ResultSet groupsRS = policyGroupsStmt.executeQuery();
        while (groupsRS.next()) {
            String groupName = groupsRS.getString(1);
            long groupID = groupsRS.getLong(2);
            int role = groupsRS.getInt(3);
            GroupDescriptor groupDescriptor = new GroupDescriptor(groupName, groupID, 0, null);
            if (role == Roles.OWNER) {
                owners.add(groupDescriptor);
            } else {
                viewers.add(groupDescriptor);
            }
        }
        uploadPolicy.setOwners(owners);
        uploadPolicy.setViewers(viewers);
        return uploadPolicy;
    }
}
