package com.easyinsight.datafeeds;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.userupload.*;
import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.google.GoogleFeedDefinition;
import com.easyinsight.datafeeds.salesforce.SalesforceFeedDefinition;
import com.easyinsight.datafeeds.salesforce.SalesforceSubFeedDefinition;
import com.easyinsight.datafeeds.file.FileBasedFeedDefinition;
import com.easyinsight.datafeeds.jira.JiraDataSource;
import com.easyinsight.datafeeds.basecamp.BaseCampCompositeSource;
import com.easyinsight.datafeeds.basecamp.BaseCampTimeSource;
import com.easyinsight.datafeeds.basecamp.BaseCampTodoSource;
import com.easyinsight.datafeeds.admin.AdminStatsDataSource;
import com.easyinsight.datafeeds.gnip.GnipDataSource;
import com.easyinsight.datafeeds.ganalytics.GoogleAnalyticsDataSource;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.email.UserStub;
import com.easyinsight.groups.GroupDescriptor;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.Roles;
import com.easyinsight.PasswordStorage;
import com.easyinsight.eventing.EventDispatcher;
import com.easyinsight.eventing.TodoCompletedEvent;
import com.easyinsight.users.Credentials;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.io.Serializable;

import org.hibernate.Session;
import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import com.easyinsight.datafeeds.test.TestAlphaDataSource;
import com.easyinsight.datafeeds.test.TestBetaDataSource;
import com.easyinsight.datafeeds.test.TestGammaDataSource;

/**
 * User: jboe
 * Date: Jan 3, 2008
 * Time: 10:46:24 PM
 */
public class FeedStorage {

    private JCS feedCache = getCache("feedDefinitions");
    private JCS apiKeyCache = getCache("apiKeys");

    private JCS getCache(String cacheName) {

        try {
            return JCS.getInstance(cacheName);
        } catch (Exception e) {
            LogClass.error(e);
        }
        return null;
    }

    public void removeFeed(long feedId) {
        try {
            feedCache.remove(feedId);
        }
        catch(Exception e) {
            LogClass.error(e);
        }
    }

    public long addFeedDefinitionData(FeedDefinition feedDefinition, Connection conn) throws SQLException {
        PreparedStatement insertDataFeedStmt;
        insertDataFeedStmt = conn.prepareStatement("INSERT INTO DATA_FEED (FEED_NAME, FEED_TYPE, PUBLICLY_VISIBLE, FEED_SIZE, " +
                    "CREATE_DATE, UPDATE_DATE, FEED_VIEWS, FEED_RATING_COUNT, FEED_RATING_AVERAGE, DESCRIPTION," +
                    "ATTRIBUTION, OWNER_NAME, DYNAMIC_SERVICE_DEFINITION_ID, ANALYSIS_ID, MARKETPLACE_VISIBLE, " +
                "API_KEY, UNCHECKED_API_BASIC_AUTH, UNCHECKED_API_ENABLED, validated_api_basic_auth, validated_api_enabled, INHERIT_ACCOUNT_API_SETTINGS," +
                "REFRESH_INTERVAL, CURRENT_VERSION) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
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
        insertDataFeedStmt.setString(16, feedDefinition.getApiKey());
        insertDataFeedStmt.setBoolean(17, feedDefinition.isUncheckedAPIUsingBasicAuth());
        insertDataFeedStmt.setBoolean(18, feedDefinition.isUncheckedAPIEnabled());
        insertDataFeedStmt.setBoolean(19, feedDefinition.isValidatedAPIUsingBasicAuth());
        insertDataFeedStmt.setBoolean(20, feedDefinition.isValidatedAPIEnabled());
        insertDataFeedStmt.setBoolean(21, feedDefinition.isInheritAccountAPISettings());
        insertDataFeedStmt.setLong(22, feedDefinition.getRefreshDataInterval());
        insertDataFeedStmt.setInt(23, 1);
        insertDataFeedStmt.execute();
        long feedID = Database.instance().getAutoGenKey(insertDataFeedStmt);
        feedDefinition.setDataFeedID(feedID);
        savePolicy(conn, feedID, feedDefinition.getUploadPolicy());
        feedDefinition.setDataFeedID(feedID);
        saveFields(feedID, conn, feedDefinition.getFields(), feedDefinition.getVirtualDimensions());
        saveTags(feedID, conn, feedDefinition.getTags());
        feedDefinition.customStorage(conn);
        return feedID;
    }

    public long addFeedDefinitionData(FeedDefinition feedDefinition) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            long feedID = addFeedDefinitionData(feedDefinition, conn);
            conn.commit();
            EventDispatcher.instance().dispatch(new TodoCompletedEvent(feedDefinition));
            return feedID;
        } catch (SQLException e) {
            conn.rollback();
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
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

    public void updateVersion(FeedDefinition feedDefinition, int version, Connection conn) throws SQLException {
        PreparedStatement updateVersionStmt = conn.prepareStatement("UPDATE DATA_FEED SET CURRENT_VERSION = ? WHERE DATA_FEED_ID = ?");
        updateVersionStmt.setInt(1, version);
        updateVersionStmt.setLong(2, feedDefinition.getDataFeedID());
        updateVersionStmt.executeUpdate();
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

    private void saveFields(long feedID, Connection conn, List<AnalysisItem> analysisItems, List<VirtualDimension> virtualDimensions) throws SQLException {
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM FEED_TO_ANALYSIS_ITEM WHERE FEED_ID = ?");
        PreparedStatement virtualStmt = conn.prepareStatement("DELETE FROM DATA_SOURCE_TO_VIRTUAL_DIMENSION WHERE DATA_SOURCE_ID = ?");
        deleteStmt.setLong(1, feedID);
        deleteStmt.executeUpdate();
        deleteStmt.close();
        virtualStmt.setLong(1, feedID);
        virtualStmt.executeUpdate();
        virtualStmt.close();
        if (analysisItems != null) {
            Session session = Database.instance().createSession(conn);
            try {
                //session.getTransaction().begin();
                if (virtualDimensions != null) {
                    for (VirtualDimension virtualDimension : virtualDimensions) {
                        AnalysisDimension dim = virtualDimension.getDefaultTransform().getTransformDimension();
                        if (dim.getAnalysisItemID() == 0) {
                            for (AnalysisItem analysisItem : analysisItems) {
                                if (analysisItem.getKey().equals(dim.getKey())) {
                                    virtualDimension.getDefaultTransform().setTransformDimension((AnalysisDimension) analysisItem);
                                }
                            }
                        }
                        for (VirtualTransform transform : virtualDimension.getVirtualTransforms()) {
                            AnalysisDimension transformDim = transform.getTransformDimension();
                            if (transformDim.getAnalysisItemID() == 0) {
                                for (AnalysisItem analysisItem : analysisItems) {
                                    if (analysisItem.getKey().equals(transformDim.getKey())) {
                                        transform.setTransformDimension((AnalysisDimension) analysisItem);
                                    }
                                }
                            }
                        }
                    }
                }
                for (AnalysisItem analysisItem : analysisItems) {
                    if (analysisItem.getKey().getKeyID() == 0) {
                        session.save(analysisItem.getKey());
                    } else {
                        session.merge(analysisItem.getKey());
                    }
                }
                for (AnalysisItem analysisItem : analysisItems) {
                    analysisItem.beforeSave();
                    analysisItem.reportSave(session);
                    if (analysisItem.getAnalysisItemID() == 0) {
                        session.save(analysisItem);
                    } else {
                        session.merge(analysisItem);
                    }
                    //session.saveOrUpdate(analysisItem);
                }
                if (virtualDimensions != null) {
                    for (VirtualDimension remoteDimension : virtualDimensions) {
                        remoteDimension.fromRemote();
                        session.saveOrUpdate(remoteDimension);
                    }
                }
                /*for (AnalysisItem analysisItem : analysisItems) {
                    analysisItem.resetIDs();
                    session.save(analysisItem);
                }*/
                session.flush();
                //session.getTransaction().commit();
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
            PreparedStatement virtualLinkStmt = conn.prepareStatement("INSERT INTO DATA_SOURCE_TO_VIRTUAL_DIMENSION (DATA_SOURCE_ID, VIRTUAL_DIMENSION_ID) " +
                    "VALUES (?, ?)");
            if (virtualDimensions != null) {
                for (VirtualDimension virtualDimension : virtualDimensions) {
                    virtualLinkStmt.setLong(1, feedID);
                    virtualLinkStmt.setLong(2, virtualDimension.getVirtualDimensionID());
                    virtualLinkStmt.execute();
                }
            }
            virtualLinkStmt.close();
        }
    }

    public void setPasswordCredentials(Connection conn, IServerDataSourceDefinition ds) throws SQLException {
        Credentials c = PasswordStorage.getPasswordCredentials(ds.getDataFeedID(), conn);
        if(c != null) {
            ds.setUsername(c.getUserName());
            ds.setPassword(c.getPassword());
        }
    }

    public void setSessionIdCredentials(Connection conn, IServerDataSourceDefinition ds) throws SQLException {
        PreparedStatement selectStmt = conn.prepareStatement("SELECT session_id from session_id_storage WHERE data_feed_id = ?");
        selectStmt.setLong(1, ds.getDataFeedID());
        ResultSet rs = selectStmt.executeQuery();
        if(rs.next()) {
            ds.setSessionId(rs.getString(1));
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

    public void retrieveFields(FeedDefinition feedDefinition, Connection conn) throws SQLException {
        long feedID = feedDefinition.getDataFeedID();
        PreparedStatement queryFieldsStmt = conn.prepareStatement("SELECT ANALYSIS_ITEM_ID FROM FEED_TO_ANALYSIS_ITEM WHERE FEED_ID = ?");
        PreparedStatement queryVirtualDimStmt = conn.prepareStatement("SELECT VIRTUAL_DIMENSION_ID FROM data_source_to_virtual_dimension WHERE " +
                "DATA_SOURCE_ID = ?");
        queryFieldsStmt.setLong(1, feedID);
        Set<Long> analysisItemIDs = new HashSet<Long>();
        ResultSet rs = queryFieldsStmt.executeQuery();
        while (rs.next()) {
            analysisItemIDs.add(rs.getLong(1));
        }
        queryFieldsStmt.close();
        queryVirtualDimStmt.setLong(1, feedID);
        Set<Long> virtualIDSet = new HashSet<Long>();
        ResultSet virtualRS = queryVirtualDimStmt.executeQuery();
        while (virtualRS.next()) {
            virtualIDSet.add(virtualRS.getLong(1));
        }
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        List<VirtualDimension> virtualDimensions = new ArrayList<VirtualDimension>();
        Session session = Database.instance().createSession(conn);
        try {
            for (Long analysisItemID : analysisItemIDs) {
                List items = session.createQuery("from AnalysisItem where analysisItemID = ?").setLong(0, analysisItemID).list();
                if (items.size() > 0) {
                    AnalysisItem analysisItem = (AnalysisItem) items.get(0);
                    /*if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
                        AnalysisHierarchyItem analysisHierarchyItem = (AnalysisHierarchyItem) analysisItem;
                        analysisHierarchyItem.setHierarchyLevels(new ArrayList<HierarchyLevel>(analysisHierarchyItem.getHierarchyLevels()));
                    }*/
                    analysisItems.add((AnalysisItem) Database.deproxy(analysisItem));
                }
            }
            for (Long virtualID : virtualIDSet) {
                List items = session.createQuery("from VirtualDimension where virtualDimensionID = ?").setLong(0, virtualID).list();
                if (items.size() > 0) {
                    VirtualDimension dim = (VirtualDimension) items.get(0);
                    dim.toRemote();
                    virtualDimensions.add(dim);
                }
            }
            for (AnalysisItem item : analysisItems) {
                item.afterLoad();                
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        feedDefinition.setFields(analysisItems);
        feedDefinition.setVirtualDimensions(virtualDimensions);
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
        Session session = Database.instance().createSession(conn);
        try {
            for (Long analysisItemID : analysisItemIDs) {
                List items = session.createQuery("from AnalysisItem where analysisItemID = ?").setLong(0, analysisItemID).list();
                if (items.size() > 0) {
                    AnalysisItem analysisItem = (AnalysisItem) items.get(0);
                    /*if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
                        AnalysisHierarchyItem analysisHierarchyItem = (AnalysisHierarchyItem) analysisItem;
                        analysisHierarchyItem.setHierarchyLevels(new ArrayList<HierarchyLevel>(analysisHierarchyItem.getHierarchyLevels()));
                    }*/
                    analysisItem.afterLoad();
                    analysisItems.add(analysisItem);
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
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
                int newFeedViews = feedViews + 1;
                updateViewsStmt.setInt(1, newFeedViews);
                updateViewsStmt.setLong(2, feedID);
                updateViewsStmt.executeUpdate();

                // update views in feedCache
                FeedDefinition f = null;
                if(feedCache != null)
                    f = (FeedDefinition) feedCache.get(feedID);
                if(f != null) {
                    f.setViewCount(newFeedViews);
                    feedCache.remove(feedID);
                    feedCache.put(feedID, feedCache);
                }
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } catch (CacheException e) {
            LogClass.error(e);
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
        try {
            if(feedCache != null)
                feedCache.remove(feedDefinition.getDataFeedID());
            LogClass.info("Removed " + feedDefinition.getDataFeedID() + " from feed cache.");
        } catch (CacheException e) {
            LogClass.error(e);
        }
        PreparedStatement updateDataFeedStmt = conn.prepareStatement("UPDATE DATA_FEED SET FEED_NAME = ?, FEED_TYPE = ?, PUBLICLY_VISIBLE = ?, GENRE = ?, " +
                        "FEED_SIZE = ?, ANALYSIS_ID = ?, DESCRIPTION = ?, ATTRIBUTION = ?, OWNER_NAME = ?, DYNAMIC_SERVICE_DEFINITION_ID = ?, MARKETPLACE_VISIBLE = ?," +
                "API_KEY = ?, validated_api_enabled = ?, unchecked_api_enabled = ?, REFRESH_INTERVAL = ? WHERE DATA_FEED_ID = ?");
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
        updateDataFeedStmt.setString(12, feedDefinition.getApiKey());
        updateDataFeedStmt.setBoolean(13, feedDefinition.isValidatedAPIEnabled());
        updateDataFeedStmt.setBoolean(14, feedDefinition.isUncheckedAPIEnabled());
        updateDataFeedStmt.setLong(15, feedDefinition.getRefreshDataInterval());
        updateDataFeedStmt.setLong(16, feedDefinition.getDataFeedID());
        int rows = updateDataFeedStmt.executeUpdate();
        if (rows != 1) {
            throw new RuntimeException("Could not locate row to update");
        }
        updateDataFeedStmt.close();
        savePolicy(conn, feedDefinition.getDataFeedID(), feedDefinition.getUploadPolicy());
        saveFields(feedDefinition.getDataFeedID(), conn, feedDefinition.getFields(), feedDefinition.getVirtualDimensions());
        saveTags(feedDefinition.getDataFeedID(), conn, feedDefinition.getTags());
        feedDefinition.customStorage(conn);
        if (feedDefinition.getRefreshDataInterval() > 0) {

        }
    }

    public void updateDataFeedConfiguration(FeedDefinition feedDefinition) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            updateDataFeedConfiguration(feedDefinition, conn);
            conn.commit();
            EventDispatcher.instance().dispatch(new TodoCompletedEvent(feedDefinition));
        } catch (SQLException e) {
            conn.rollback();
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(false);
            Database.instance().closeConnection(conn);
        }
    }

    public FeedDefinition getFeedDefinitionData(long identifier, Connection conn) {
        return getFeedDefinitionData(identifier, conn, true);
    }

    public FeedDefinition getFeedDefinitionData(long identifier, Connection conn, boolean cache) {
        FeedDefinition feedDefinition = null;
        if(feedCache != null && cache)
            feedDefinition = (FeedDefinition) feedCache.get(identifier);
        if(feedDefinition != null) {
            LogClass.info("Cache hit for feed id: " + identifier);
            return feedDefinition;
        }
        try {
            LogClass.info("Cache miss for feed id: " + identifier);
            PreparedStatement queryFeedStmt = conn.prepareStatement("SELECT FEED_NAME, FEED_TYPE, PUBLICLY_VISIBLE, GENRE, CREATE_DATE," +
                    "UPDATE_DATE, FEED_VIEWS, FEED_RATING_COUNT, FEED_RATING_AVERAGE, FEED_SIZE, ANALYSIS_ID," +
                    "ATTRIBUTION, DESCRIPTION, OWNER_NAME, DYNAMIC_SERVICE_DEFINITION_ID, MARKETPLACE_VISIBLE, API_KEY, unchecked_api_enabled, validated_api_enabled," +
                    "REFRESH_INTERVAL " +
                    "FROM DATA_FEED WHERE " +
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
                } else if (feedType.equals(FeedType.JIRA)) {
                    feedDefinition = new JiraDataSource();
                } else if (feedType.equals(FeedType.BASECAMP_MASTER)) {
                    feedDefinition = new BaseCampCompositeSource();
                } else if (feedType.equals(FeedType.ADMIN_STATS)) {
                    feedDefinition = new AdminStatsDataSource();
                } else if (feedType.equals(FeedType.GNIP)) {
                    feedDefinition = new GnipDataSource();
                } else if (feedType.equals(FeedType.GOOGLE_ANALYTICS)) {
                    feedDefinition = new GoogleAnalyticsDataSource();
                } else if (feedType.equals(FeedType.TEST_ALPHA)) {
                    feedDefinition = new TestAlphaDataSource();
                } else if (feedType.equals(FeedType.TEST_BETA)) {
                    feedDefinition = new TestBetaDataSource();
                } else if (feedType.equals(FeedType.TEST_GAMMA)) {
                    feedDefinition = new TestGammaDataSource();
                } else if (feedType.equals(FeedType.BASECAMP)) {
                    feedDefinition = new BaseCampTodoSource();
                } else if (feedType.equals(FeedType.BASECAMP_TIME)) {
                    feedDefinition = new BaseCampTimeSource();
                }
                else {
                    throw new RuntimeException("Couldn't identify type");
                }
                String genre = rs.getString(4);
                feedDefinition.setFeedName(feedName);
                feedDefinition.setDataFeedID(identifier);
                boolean publiclyVisible = rs.getBoolean(3);
                boolean marketplaceVisible = rs.getBoolean(16);
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
                if (!feedType.equals(FeedType.ANALYSIS_BASED)) {
                    retrieveFields(feedDefinition, conn);
                }
                feedDefinition.setAnalysisDefinitionID(analysisDefinitionID);
                feedDefinition.setAttribution(attribution);
                feedDefinition.setDescription(description);
                feedDefinition.setOwnerName(ownerName);
                feedDefinition.setDynamicServiceDefinitionID(rs.getLong(15));
                feedDefinition.setApiKey(rs.getString(17));
                feedDefinition.setUncheckedAPIEnabled(rs.getBoolean(18));
                feedDefinition.setValidatedAPIEnabled(rs.getBoolean(19));
                feedDefinition.setRefreshDataInterval(rs.getLong(20));
                feedDefinition.setTags(getTags(feedDefinition.getDataFeedID(), conn));
                feedDefinition.customLoad(conn);
                if(feedDefinition instanceof IServerDataSourceDefinition) {
                    IServerDataSourceDefinition ds = (IServerDataSourceDefinition) feedDefinition;
                    if(ds.getCredentialsDefinition() == CredentialsDefinition.SALESFORCE)
                        setSessionIdCredentials(conn, ds);
                    else if(ds.getCredentialsDefinition() == CredentialsDefinition.STANDARD_USERNAME_PW) {
                        setPasswordCredentials(conn, ds);
                    }
                }
            } else {
                throw new RuntimeException("Could not find data source " + identifier);
            }
            queryFeedStmt.close();
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }

        try {
            if(feedCache != null)
                feedCache.put(identifier, feedDefinition);
        } catch (CacheException e) {
            LogClass.error(e);
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

    private FeedDescriptor createDescriptor(long dataFeedID, String feedName, Integer userRole,
                                            long size, int feedType, String ownerName, String description, String attribution, Date lastDataTime) throws SQLException {
        return new FeedDescriptor(feedName, dataFeedID, size, feedType, userRole != null ? userRole : 0, ownerName, description, attribution, lastDataTime);
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
                long analysisID = rs.getLong(9);
                WSAnalysisDefinition analysisDefinition = new AnalysisStorage().getAnalysisDefinition(analysisID, conn);
                feedDescriptor = createDescriptor(feedID, feedName, role, 0, feedType, ownerName, description, attribution, null);
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
                //feedDescriptor.setDefinition(analysisDefinition);
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
                    "FEED_PERSISTENCE_METADATA.SIZE, DATA_FEED.FEED_TYPE, DATA_FEED.ANALYSIS_ID, OWNER_NAME, DESCRIPTION, ATTRIBUTION, ROLE, PUBLICLY_VISIBLE, MARKETPLACE_VISIBLE, FEED_PERSISTENCE_METADATA.LAST_DATA_TIME, PASSWORD_STORAGE.USERNAME " +
                    " FROM (UPLOAD_POLICY_USERS, DATA_FEED LEFT JOIN FEED_PERSISTENCE_METADATA ON DATA_FEED.DATA_FEED_ID = FEED_PERSISTENCE_METADATA.FEED_ID) LEFT JOIN PASSWORD_STORAGE ON DATA_FEED.DATA_FEED_ID = PASSWORD_STORAGE.DATA_FEED_ID WHERE " +
                    "UPLOAD_POLICY_USERS.USER_ID = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID");
            queryStmt.setLong(1, userID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long dataFeedID = rs.getLong(1);
                String feedName = rs.getString(2);
                long feedSize = rs.getLong(3);
                int feedType = rs.getInt(4);
                String ownerName = rs.getString(6);
                String description = rs.getString(7);
                String attribution = rs.getString(8);
                int userRole = rs.getInt(9);
                Timestamp lastTime = rs.getTimestamp(12);
                Date lastDataTime = null;
                boolean hasSavedCredentials = rs.getString(13) != null;
                if (lastTime != null) {
                    lastDataTime = new Date(lastTime.getTime());
                }
                FeedDescriptor feedDescriptor = createDescriptor(dataFeedID, feedName, userRole, feedSize, feedType, ownerName, description, attribution, lastDataTime);
                feedDescriptor.setHasSavedCredentials(hasSavedCredentials);
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
        PreparedStatement policyUserStmt = conn.prepareStatement("SELECT USER.USER_ID, ROLE, USER.NAME, USER.USERNAME, USER.EMAIL, USER.ACCOUNT_ID FROM UPLOAD_POLICY_USERS, USER WHERE FEED_ID = ? AND " +
                "UPLOAD_POLICY_USERS.USER_ID = USER.USER_ID");
        policyUserStmt.setLong(1, feedID);
        ResultSet usersRS = policyUserStmt.executeQuery();
        while (usersRS.next()) {
            long userID = usersRS.getLong(1);
            int role = usersRS.getInt(2);
            String name = usersRS.getString(3);
            String userName = usersRS.getString(4);
            String email = usersRS.getString(5);
            long accountID = usersRS.getLong(6);
            UserStub userStub = new UserStub(userID, userName, email, name);
            userStub.setAccountID(accountID);
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

    public long getFeedForAPIKey(long userID, String apiKey) {
        Connection conn = Database.instance().getConnection();
        Long feedID = null;
        if(apiKeyCache != null)
            feedID = (Long) apiKeyCache.get(new FeedApiKey(apiKey, userID));
        if(feedID != null) {
            LogClass.info("Cache hit for API key: " + apiKey + " & User id: " + userID);
            return feedID;
        }
        try {
            LogClass.info("Cache miss for API key: " + apiKey + " & User id: " + userID);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DISTINCT DATA_FEED.DATA_FEED_ID" +
                        " FROM UPLOAD_POLICY_USERS, DATA_FEED WHERE " +
                        "UPLOAD_POLICY_USERS.user_id = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND DATA_FEED.API_KEY = ?");
            queryStmt.setLong(1, userID);
            queryStmt.setString(2, apiKey);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                feedID = rs.getLong(1);
                if(apiKeyCache != null)
                    apiKeyCache.put(new FeedApiKey(apiKey, userID), feedID);
                return feedID;
            }
        } catch (SQLException se) {
            LogClass.error(se);
            throw new RuntimeException(se);
        } catch (CacheException e) {
            LogClass.error(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        throw new RuntimeException("No data source found for API key " + apiKey);
    }

    private class FeedApiKey implements Serializable {
        private String APIKey;
        private long userID;

        public FeedApiKey(String APIKey, long userID) {
            this.APIKey = APIKey;
            this.userID = userID;
        }

        public long getUserID() {
            return userID;
        }

        public void setUserID(long userID) {
            this.userID = userID;
        }

        public String getAPIKey() {
            return APIKey;
        }

        public void setAPIKey(String APIKey) {
            this.APIKey = APIKey;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FeedApiKey that = (FeedApiKey) o;

            return userID == that.userID && APIKey.equals(that.APIKey);

        }

        @Override
        public int hashCode() {
            int result = APIKey.hashCode();
            result = 31 * result + (int) (userID ^ (userID >>> 32));
            return result;
        }
    }
}
