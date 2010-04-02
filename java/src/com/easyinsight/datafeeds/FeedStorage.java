package com.easyinsight.datafeeds;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.userupload.*;
import com.easyinsight.analysis.*;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.email.UserStub;
import com.easyinsight.groups.GroupDescriptor;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.PasswordStorage;
import com.easyinsight.logging.LogClass;
import com.easyinsight.eventing.EventDispatcher;
import com.easyinsight.eventing.TodoCompletedEvent;
import com.easyinsight.users.Credentials;
import com.easyinsight.users.Account;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

/**
 * User: jboe
 * Date: Jan 3, 2008
 * Time: 10:46:24 PM
 */
public class FeedStorage {

    private JCS feedCache = getCache("feedDefinitions");
    private JCS apiKeyCache = getCache("apiKeys");

    private DataSourceTypeRegistry registry = new DataSourceTypeRegistry();

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
        catch (Exception e) {
            LogClass.error(e);
        }
    }

    public long addFeedDefinitionData(FeedDefinition feedDefinition, Connection conn) throws SQLException {
        PreparedStatement insertDataFeedStmt;
        insertDataFeedStmt = conn.prepareStatement("INSERT INTO DATA_FEED (FEED_NAME, FEED_TYPE, PUBLICLY_VISIBLE, FEED_SIZE, " +
                "CREATE_DATE, UPDATE_DATE, FEED_VIEWS, FEED_RATING_COUNT, FEED_RATING_AVERAGE, DESCRIPTION," +
                "ATTRIBUTION, OWNER_NAME, DYNAMIC_SERVICE_DEFINITION_ID, MARKETPLACE_VISIBLE, " +
                "API_KEY, UNCHECKED_API_BASIC_AUTH, UNCHECKED_API_ENABLED, validated_api_basic_auth, validated_api_enabled, INHERIT_ACCOUNT_API_SETTINGS," +
                "REFRESH_INTERVAL, CURRENT_VERSION, VISIBLE, PARENT_SOURCE_ID, VERSION) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        int i = 1;
        insertDataFeedStmt.setString(i++, feedDefinition.getFeedName());
        insertDataFeedStmt.setInt(i++, feedDefinition.getFeedType().getType());
        insertDataFeedStmt.setBoolean(i++, feedDefinition.getUploadPolicy().isPubliclyVisible());
        insertDataFeedStmt.setLong(i++, feedDefinition.getSize());
        if (feedDefinition.getDateCreated() == null) {
            feedDefinition.setDateCreated(new Date());
        }
        if (feedDefinition.getDateUpdated() == null) {
            feedDefinition.setDateUpdated(new Date());
        }
        insertDataFeedStmt.setDate(i++, new java.sql.Date(feedDefinition.getDateCreated().getTime()));
        insertDataFeedStmt.setDate(i++, new java.sql.Date(feedDefinition.getDateUpdated().getTime()));
        insertDataFeedStmt.setInt(i++, feedDefinition.getViewCount());
        insertDataFeedStmt.setInt(i++, feedDefinition.getRatingCount());
        insertDataFeedStmt.setDouble(i++, feedDefinition.getRatingAverage());
        insertDataFeedStmt.setString(i++, feedDefinition.getDescription());
        insertDataFeedStmt.setString(i++, feedDefinition.getAttribution());
        insertDataFeedStmt.setString(i++, feedDefinition.getOwnerName());
        if (feedDefinition.getDynamicServiceDefinitionID() > 0)
            insertDataFeedStmt.setLong(i++, feedDefinition.getDynamicServiceDefinitionID());
        else
            insertDataFeedStmt.setNull(i++, Types.BIGINT);
        insertDataFeedStmt.setBoolean(i++, feedDefinition.getUploadPolicy().isMarketplaceVisible());
        insertDataFeedStmt.setString(i++, feedDefinition.getApiKey());
        insertDataFeedStmt.setBoolean(i++, feedDefinition.isUncheckedAPIUsingBasicAuth());
        insertDataFeedStmt.setBoolean(i++, feedDefinition.isUncheckedAPIEnabled());
        insertDataFeedStmt.setBoolean(i++, feedDefinition.isValidatedAPIUsingBasicAuth());
        insertDataFeedStmt.setBoolean(i++, feedDefinition.isValidatedAPIEnabled());
        insertDataFeedStmt.setBoolean(i++, feedDefinition.isInheritAccountAPISettings());
        insertDataFeedStmt.setLong(i++, feedDefinition.getRefreshDataInterval());
        insertDataFeedStmt.setInt(i++, 1);
        insertDataFeedStmt.setBoolean(i++, feedDefinition.isVisible());
        insertDataFeedStmt.setLong(i++, feedDefinition.getParentSourceID());
        insertDataFeedStmt.setInt(i, feedDefinition.getVersion());
        insertDataFeedStmt.execute();
        long feedID = Database.instance().getAutoGenKey(insertDataFeedStmt);
        feedDefinition.setDataFeedID(feedID);
        savePolicy(conn, feedID, feedDefinition.getUploadPolicy());
        feedDefinition.setDataFeedID(feedID);
        saveFields(feedID, conn, feedDefinition.getFields(), feedDefinition.getVirtualDimensions());
        saveFolders(feedID, conn, feedDefinition.getFolders());
        saveTags(feedID, conn, feedDefinition.getTags());
        feedDefinition.customStorage(conn);
        insertDataFeedStmt.close();
        return feedID;
    }

    private void saveFolders(long feedID, Connection conn, List<FeedFolder> folders) throws SQLException {
        PreparedStatement wipeStmt = conn.prepareStatement("DELETE FROM FOLDER WHERE DATA_SOURCE_ID = ?");
        wipeStmt.setLong(1, feedID);
        wipeStmt.executeUpdate();
        wipeStmt.close();
        for (FeedFolder folder : folders) {
            saveFolder(folder, feedID, conn);
        }
    }

    private long saveFolder(FeedFolder folder, long feedID, Connection conn) throws SQLException {
        PreparedStatement insertFolderStmt = conn.prepareStatement("INSERT INTO FOLDER (FOLDER_NAME, DATA_SOURCE_ID) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        insertFolderStmt.setString(1, folder.getName());
        insertFolderStmt.setLong(2, feedID);
        insertFolderStmt.execute();
        folder.setFolderID(Database.instance().getAutoGenKey(insertFolderStmt));
        insertFolderStmt.close();
        saveFields(conn, folder);
        PreparedStatement insertChildFolderStmt = conn.prepareStatement("INSERT INTO folder_to_folder (parent_folder_id, child_folder_id) values (?, ?)");
        PreparedStatement clearFoldersStmt = conn.prepareStatement("DELETE FROM folder_to_folder WHERE parent_folder_id = ?");
        clearFoldersStmt.setLong(1, folder.getFolderID());
        clearFoldersStmt.executeUpdate();
        clearFoldersStmt.close();
        for (FeedFolder childFolder : folder.getChildFolders()) {
            saveFolder(childFolder, feedID, conn);
            insertChildFolderStmt.setLong(1, folder.getFolderID());
            insertChildFolderStmt.setLong(2, childFolder.getFolderID());
            insertChildFolderStmt.execute();
        }
        insertChildFolderStmt.close();
        return folder.getFolderID();
    }

    private void saveFields(Connection conn, FeedFolder folder) throws SQLException {
        PreparedStatement clearJoinsStmt = conn.prepareStatement("DELETE FROM folder_to_analysis_item WHERE folder_id = ?");
        clearJoinsStmt.setLong(1, folder.getFolderID());
        clearJoinsStmt.executeUpdate();
        clearJoinsStmt.close();
        PreparedStatement insertFieldStmt = conn.prepareStatement("INSERT INTO folder_to_analysis_item (folder_id, analysis_item_id) values (?, ?)");
        for (AnalysisItem analysisItem : folder.getChildItems()) {
            insertFieldStmt.setLong(1, folder.getFolderID());
            insertFieldStmt.setLong(2, analysisItem.getAnalysisItemID());
            try {
                insertFieldStmt.execute();
            } catch (SQLException e) {
                LogClass.error("Analysis item " + analysisItem.toDisplay() + " was not yet saved in folder " + folder.getName());
                throw e;
            }
        }
        insertFieldStmt.close();
    }

    public List<FeedFolder> getFolders(long dataSourceID, List<AnalysisItem> fields, Connection conn) throws SQLException {
        List<FeedFolder> folders = new ArrayList<FeedFolder>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT FOLDER_ID FROM FOLDER WHERE DATA_SOURCE_ID = ? AND " +
                "FOLDER_ID NOT IN (SELECT CHILD_FOLDER_ID FROM FOLDER_TO_FOLDER)");
        queryStmt.setLong(1, dataSourceID);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            long folderID = rs.getLong(1);
            FeedFolder feedFolder = getFolder(folderID, fields, conn);
            folders.add(feedFolder);
        }
        queryStmt.close();
        return folders;
    }

    private FeedFolder getFolder(long folderID, List<AnalysisItem> fields, Connection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT FOLDER_NAME FROM FOLDER WHERE FOLDER_ID = ?");
        queryStmt.setLong(1, folderID);
        ResultSet rs = queryStmt.executeQuery();
        rs.next();

        FeedFolder feedFolder = new FeedFolder();
        feedFolder.setFolderID(folderID);
        feedFolder.setName(rs.getString(1));
        queryStmt.close();
        PreparedStatement analysisItemStmt = conn.prepareStatement("SELECT ANALYSIS_ITEM_ID FROM FOLDER_TO_ANALYSIS_ITEM WHERE FOLDER_ID = ?");
        analysisItemStmt.setLong(1, folderID);
        ResultSet fieldRS = analysisItemStmt.executeQuery();
        while (fieldRS.next()) {
            long analysisItemID = fieldRS.getLong(1);
            for (AnalysisItem analysisItem : fields) {
                if (analysisItem.getAnalysisItemID() == analysisItemID) {
                    feedFolder.addAnalysisItem(analysisItem);
                }
            }
        }
        analysisItemStmt.close();
        PreparedStatement childFoldersStmt = conn.prepareStatement("SELECT CHILD_FOLDER_ID FROM FOLDER_TO_FOLDER WHERE parent_folder_id = ?");
        childFoldersStmt.setLong(1, folderID);
        ResultSet childRS = childFoldersStmt.executeQuery();
        List<FeedFolder> childFolders = new ArrayList<FeedFolder>();
        while (childRS.next()) {
            long childID = childRS.getLong(1);
            childFolders.add(getFolder(childID, fields, conn));
        }
        childFoldersStmt.close();
        feedFolder.setChildFolders(childFolders);
        return feedFolder;
    }

    public long addFeedDefinitionData(FeedDefinition feedDefinition) throws SQLException {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            long feedID = addFeedDefinitionData(feedDefinition, conn);
            conn.commit();
            EventDispatcher.instance().dispatch(new TodoCompletedEvent(feedDefinition));
            return feedID;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    private void savePolicy(Connection conn, long feedID, UploadPolicy uploadPolicy) throws SQLException {
        // tODO: restore, but as it stands, breaks a lot of stuff
        /*Session s = Database.instance().createSession(conn);
        try {
            Account a = (Account) s.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);
            s.flush();
            if(!a.isActivated())
                return;
        }
        finally {
            s.close();
        }*/

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
        addUserStmt.close();
        addGroupStmt.close();
    }

    public void updateVersion(FeedDefinition feedDefinition, int version, Connection conn) throws SQLException {
        PreparedStatement updateVersionStmt = conn.prepareStatement("UPDATE DATA_FEED SET CURRENT_VERSION = ? WHERE DATA_FEED_ID = ?");
        updateVersionStmt.setInt(1, version);
        updateVersionStmt.setLong(2, feedDefinition.getDataFeedID());
        updateVersionStmt.executeUpdate();
        updateVersionStmt.close();
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

                    session.saveOrUpdate(tag);
                }
                session.flush();
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
        //PreparedStatement virtualStmt = conn.prepareStatement("DELETE FROM DATA_SOURCE_TO_VIRTUAL_DIMENSION WHERE DATA_SOURCE_ID = ?");
        deleteStmt.setLong(1, feedID);
        deleteStmt.executeUpdate();
        deleteStmt.close();
        /*virtualStmt.setLong(1, feedID);
        virtualStmt.executeUpdate();
        virtualStmt.close();*/
        if (analysisItems != null) {
            Session session = Database.instance().createSession(conn);
            try {
                //session.getTransaction().begin();
                /*if (virtualDimensions != null) {
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
                }*/
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
                /*if (virtualDimensions != null) {
                    for (VirtualDimension remoteDimension : virtualDimensions) {
                        remoteDimension.fromRemote();
                        session.saveOrUpdate(remoteDimension);
                    }
                }*/
                /*for (AnalysisItem analysisItem : analysisItems) {
                    analysisItem.resetIDs();
                    session.save(analysisItem);
                }*/
                session.flush();
                //session.getTransaction().commit();
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
        if (c != null) {
            ds.setUsername(c.getUserName());
            ds.setPassword(c.getPassword());
        }
    }

    public void setSessionIdCredentials(Connection conn, IServerDataSourceDefinition ds) throws SQLException {
        PreparedStatement selectStmt = conn.prepareStatement("SELECT session_id from session_id_storage WHERE data_feed_id = ?");
        selectStmt.setLong(1, ds.getDataFeedID());
        ResultSet rs = selectStmt.executeQuery();
        if (rs.next()) {
            ds.setSessionId(rs.getString(1));
        }
        selectStmt.close();
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
        Session session = Database.instance().createSession(conn);
        try {
            for (Long tagID : tagIDs) {
                List items = session.createQuery("from Tag where tagID = ?").setLong(0, tagID).list();
                if (items.size() > 0) {
                    Tag tag = (Tag) items.get(0);
                    tags.add(tag);
                }
            }
        } finally {
            session.close();
        }
        return tags;
    }

    public void retrieveFields(FeedDefinition feedDefinition, Connection conn) throws SQLException {
        long feedID = feedDefinition.getDataFeedID();
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
                try {
                    List items = session.createQuery("from AnalysisItem where analysisItemID = ?").setLong(0, analysisItemID).list();
                    if (items.size() > 0) {
                        AnalysisItem analysisItem = (AnalysisItem) items.get(0);
                        /*if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
                            AnalysisHierarchyItem analysisHierarchyItem = (AnalysisHierarchyItem) analysisItem;
                            analysisHierarchyItem.setHierarchyLevels(new ArrayList<HierarchyLevel>(analysisHierarchyItem.getHierarchyLevels()));
                        }*/
                        analysisItems.add((AnalysisItem) Database.deproxy(analysisItem));
                    }
                } catch (HibernateException e) {
                    PreparedStatement fixStmt = conn.prepareStatement("DELETE FROM FEED_TO_ANALYSIS_ITEM WHERE ANALYSIS_ITEM_ID = ?");
                    fixStmt.setLong(1, analysisItemID);
                    fixStmt.executeUpdate();
                }
            }

            for (AnalysisItem item : analysisItems) {
                item.afterLoad();
            }
        } finally {
            session.close();
        }
        feedDefinition.setFields(analysisItems);
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
        } finally {
            session.close();
        }
        return analysisItems;
    }

    public void addFeedView(long feedID) throws CacheException, SQLException {
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
                if (feedCache != null)
                    f = (FeedDefinition) feedCache.get(feedID);
                if (f != null) {
                    f.setViewCount(newFeedViews);
                    feedCache.remove(feedID);
                    feedCache.put(feedID, feedCache);
                }
            }
            getViewCountStmt.close();
            updateViewsStmt.close();
        } finally {
            Database.closeConnection(conn);
        }

    }

    public void rateFeed(long feedID, long userID, int rating) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement getExistingRatingStmt = conn.prepareStatement("SELECT user_data_source_rating_id FROM " +
                    "user_data_source_rating WHERE user_id = ? AND data_source_id = ?");
            getExistingRatingStmt.setLong(1, userID);
            getExistingRatingStmt.setLong(2, feedID);
            ResultSet rs = getExistingRatingStmt.executeQuery();
            if (rs.next()) {
                PreparedStatement updateRatingStmt = conn.prepareStatement("UPDATE USER_DATA_SOURCE_RATING " +
                        "SET RATING = ? WHERE USER_DATA_SOURCE_RATING_ID = ?");
                updateRatingStmt.setInt(1, rating);
                updateRatingStmt.setLong(2, rs.getLong(1));
                updateRatingStmt.executeQuery();
                updateRatingStmt.close();
            } else {
                PreparedStatement insertRatingStmt = conn.prepareStatement("INSERT INTO USER_DATA_SOURCE_RATING " +
                        "(USER_ID, data_source_id, rating) values (?, ?, ?)");
                insertRatingStmt.setLong(1, userID);
                insertRatingStmt.setLong(2, feedID);
                insertRatingStmt.setInt(3, rating);
                insertRatingStmt.execute();
                insertRatingStmt.close();
            }
            getExistingRatingStmt.close();
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void updateDataFeedConfiguration(FeedDefinition feedDefinition, Connection conn) throws SQLException {
        try {
            if (feedCache != null)
                feedCache.remove(feedDefinition.getDataFeedID());
            LogClass.debug("Removed " + feedDefinition.getDataFeedID() + " from feed cache.");
        } catch (CacheException e) {
            LogClass.error(e);
        }
        PreparedStatement updateDataFeedStmt = conn.prepareStatement("UPDATE DATA_FEED SET FEED_NAME = ?, FEED_TYPE = ?, PUBLICLY_VISIBLE = ?, " +
                "FEED_SIZE = ?, DESCRIPTION = ?, ATTRIBUTION = ?, OWNER_NAME = ?, DYNAMIC_SERVICE_DEFINITION_ID = ?, MARKETPLACE_VISIBLE = ?," +
                "API_KEY = ?, validated_api_enabled = ?, unchecked_api_enabled = ?, REFRESH_INTERVAL = ?, VISIBLE = ?, parent_source_id = ?, VERSION = ? WHERE DATA_FEED_ID = ?");
        feedDefinition.setDateUpdated(new Date());
        int i = 1;
        updateDataFeedStmt.setString(i++, feedDefinition.getFeedName());
        updateDataFeedStmt.setInt(i++, feedDefinition.getFeedType().getType());
        updateDataFeedStmt.setBoolean(i++, feedDefinition.getUploadPolicy().isPubliclyVisible());
        updateDataFeedStmt.setLong(i++, feedDefinition.getSize());
        updateDataFeedStmt.setString(i++, feedDefinition.getDescription());
        updateDataFeedStmt.setString(i++, feedDefinition.getAttribution());
        updateDataFeedStmt.setString(i++, feedDefinition.getOwnerName());
        if (feedDefinition.getDynamicServiceDefinitionID() > 0)
            updateDataFeedStmt.setLong(i++, feedDefinition.getDynamicServiceDefinitionID());
        else
            updateDataFeedStmt.setNull(i++, Types.BIGINT);
        updateDataFeedStmt.setBoolean(i++, feedDefinition.getUploadPolicy().isMarketplaceVisible());
        updateDataFeedStmt.setString(i++, feedDefinition.getApiKey());
        updateDataFeedStmt.setBoolean(i++, feedDefinition.isValidatedAPIEnabled());
        updateDataFeedStmt.setBoolean(i++, feedDefinition.isUncheckedAPIEnabled());
        updateDataFeedStmt.setLong(i++, feedDefinition.getRefreshDataInterval());
        updateDataFeedStmt.setBoolean(i++, feedDefinition.isVisible());
        updateDataFeedStmt.setLong(i++, feedDefinition.getParentSourceID());
        updateDataFeedStmt.setLong(i++, feedDefinition.getVersion());
        updateDataFeedStmt.setLong(i, feedDefinition.getDataFeedID());
        int rows = updateDataFeedStmt.executeUpdate();
        if (rows != 1) {
            throw new RuntimeException("Could not locate row to update");
        }
        updateDataFeedStmt.close();
        savePolicy(conn, feedDefinition.getDataFeedID(), feedDefinition.getUploadPolicy());
        saveFields(feedDefinition.getDataFeedID(), conn, feedDefinition.getFields(), feedDefinition.getVirtualDimensions());
        saveFolders(feedDefinition.getDataFeedID(), conn, feedDefinition.getFolders());
        saveTags(feedDefinition.getDataFeedID(), conn, feedDefinition.getTags());
        feedDefinition.customStorage(conn);

        updateDataFeedStmt.close();
    }

    public void updateDataFeedConfiguration(FeedDefinition feedDefinition) throws SQLException {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            updateDataFeedConfiguration(feedDefinition, conn);
            conn.commit();
            EventDispatcher.instance().dispatch(new TodoCompletedEvent(feedDefinition));
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(false);
            Database.closeConnection(conn);
        }
    }

    public FeedDefinition getFeedDefinitionData(long identifier, Connection conn) throws SQLException {
        return getFeedDefinitionData(identifier, conn, true);
    }

    public FeedDefinition getFeedDefinitionData(long identifier, Connection conn, boolean cache) throws SQLException {
        FeedDefinition feedDefinition = null;
        if (feedCache != null && cache)
            feedDefinition = (FeedDefinition) feedCache.get(identifier);
        if (feedDefinition != null) {
            LogClass.debug("Cache hit for data source definition id: " + identifier);
            return feedDefinition;
        }

        LogClass.debug("Cache miss for data source definition id: " + identifier);
        PreparedStatement queryFeedStmt = conn.prepareStatement("SELECT FEED_NAME, FEED_TYPE, PUBLICLY_VISIBLE, MARKETPLACE_VISIBLE, CREATE_DATE," +
                "UPDATE_DATE, FEED_VIEWS, FEED_RATING_COUNT, FEED_RATING_AVERAGE, FEED_SIZE," +
                "ATTRIBUTION, DESCRIPTION, OWNER_NAME, DYNAMIC_SERVICE_DEFINITION_ID, API_KEY, unchecked_api_enabled, validated_api_enabled," +
                "REFRESH_INTERVAL, VISIBLE, PARENT_SOURCE_ID " +
                "FROM DATA_FEED WHERE " +
                "DATA_FEED_ID = ?");
        queryFeedStmt.setLong(1, identifier);
        ResultSet rs = queryFeedStmt.executeQuery();
        if (rs.next()) {
            int i = 1;
            String feedName = rs.getString(i++);
            FeedType feedType = FeedType.valueOf(rs.getInt(i++));
            feedDefinition = registry.createDataSource(feedType);
            feedDefinition.setFeedName(feedName);
            feedDefinition.setDataFeedID(identifier);
            boolean publiclyVisible = rs.getBoolean(i++);
            boolean marketplaceVisible = rs.getBoolean(i++);
            feedDefinition.setUploadPolicy(createUploadPolicy(conn, identifier, publiclyVisible, marketplaceVisible));
            Date createDate = new Date(rs.getDate(i++).getTime());
            Date updateDate = new Date(rs.getDate(i++).getTime());
            int views = rs.getInt(i++);
            int ratingCount = rs.getInt(i++);
            double ratingAverage = rs.getDouble(i++);
            long feedSize = rs.getLong(i++);
            String attribution = rs.getString(i++);
            String description = rs.getString(i++);
            String ownerName = rs.getString(i++);
            feedDefinition.setSize(feedSize);
            feedDefinition.setDateCreated(createDate);
            feedDefinition.setDateUpdated(updateDate);
            feedDefinition.setViewCount(views);
            feedDefinition.setRatingCount(ratingCount);
            feedDefinition.setRatingAverage(ratingAverage);            
            if (!feedType.equals(FeedType.ANALYSIS_BASED)) {
                retrieveFields(feedDefinition, conn);
            }
            feedDefinition.setAttribution(attribution);
            feedDefinition.setDescription(description);
            feedDefinition.setOwnerName(ownerName);
            feedDefinition.setDynamicServiceDefinitionID(rs.getLong(i++));
            feedDefinition.setApiKey(rs.getString(i++));
            feedDefinition.setUncheckedAPIEnabled(rs.getBoolean(i++));
            feedDefinition.setValidatedAPIEnabled(rs.getBoolean(i++));
            feedDefinition.setRefreshDataInterval(rs.getLong(i++));
            feedDefinition.setVisible(rs.getBoolean(i++));
            long parentSourceID = rs.getLong(i);
            if (!rs.wasNull()) {
                feedDefinition.setParentSourceID(parentSourceID);
            }
            feedDefinition.setFolders(getFolders(feedDefinition.getDataFeedID(), feedDefinition.getFields(), conn));
            feedDefinition.setTags(getTags(feedDefinition.getDataFeedID(), conn));
            feedDefinition.customLoad(conn);
            /*if (feedDefinition instanceof IServerDataSourceDefinition) {
                IServerDataSourceDefinition ds = (IServerDataSourceDefinition) feedDefinition;
                if (ds.getCredentialsDefinition() == CredentialsDefinition.SALESFORCE)
                    setSessionIdCredentials(conn, ds);
                else if (ds.getCredentialsDefinition() == CredentialsDefinition.STANDARD_USERNAME_PW) {
                    setPasswordCredentials(conn, ds);
                }
            }*/
        } else {
            throw new RuntimeException("Could not find data source " + identifier);
        }
        queryFeedStmt.close();


        try {
            if (feedCache != null)
                feedCache.put(identifier, feedDefinition);
        } catch (CacheException e) {
            LogClass.error(e);
        }

        return feedDefinition;
    }

    public FeedDefinition getFeedDefinitionData(long identifier) throws SQLException {
        FeedDefinition feedDefinition;
        Connection conn = Database.instance().getConnection();
        try {
            feedDefinition = getFeedDefinitionData(identifier, conn);
        } finally {
            Database.closeConnection(conn);
        }
        return feedDefinition;

    }

    private FeedDescriptor createDescriptor(long dataFeedID, String feedName, Integer userRole,
                                            long size, int feedType, String ownerName, String description, String attribution, Date lastDataTime) throws SQLException {
        return new FeedDescriptor(feedName, dataFeedID, size, feedType, userRole != null ? userRole : 0, ownerName, description, attribution, lastDataTime);
    }

    public FeedDescriptor getFeedDescriptor(long feedID) throws SQLException {
        FeedDescriptor feedDescriptor = null;
        Connection conn = Database.instance().getConnection();
        long userID = SecurityUtil.getUserID(false);
        try {
            if (userID == 0) {
                StringBuilder queryBuilder = new StringBuilder("SELECT FEED_NAME, FEED_TYPE, OWNER_NAME, DESCRIPTION, ATTRIBUTION, PUBLICLY_VISIBLE, MARKETPLACE_VISIBLE " +
                        "FROM DATA_FEED WHERE " +
                        "DATA_FEED_ID = ?");
                PreparedStatement queryStmt = conn.prepareStatement(queryBuilder.toString());
                // queryStmt.setLong(1, accountID);
                queryStmt.setLong(1, feedID);
                ResultSet rs = queryStmt.executeQuery();
                if (rs.next()) {
                    String feedName = rs.getString(1);
                    int feedType = rs.getInt(2);
                    String ownerName = rs.getString(3);
                    String description = rs.getString(4);
                    String attribution = rs.getString(5);
                    int role = Roles.NONE;
                    feedDescriptor = createDescriptor(feedID, feedName, role, 0, feedType, ownerName, description, attribution, null);
                    Collection<Tag> tags = getTags(feedID, conn);
                    StringBuilder tagStringBuilder = new StringBuilder();
                    Iterator<Tag> tagIter = tags.iterator();
                    while (tagIter.hasNext()) {
                        Tag tag = tagIter.next();
                        tagStringBuilder.append(tag.getTagName());
                        if (tagIter.hasNext()) {
                            tagStringBuilder.append(" ");
                        }
                    }
                    feedDescriptor.setTagString(tagStringBuilder.toString());
                }
                queryStmt.close();
            } else {
                StringBuilder queryBuilder = new StringBuilder("SELECT FEED_NAME, FEED_TYPE, OWNER_NAME, DESCRIPTION, ATTRIBUTION, ROLE, PUBLICLY_VISIBLE, MARKETPLACE_VISIBLE " +
                        "FROM DATA_FEED LEFT JOIN UPLOAD_POLICY_USERS ON DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND UPLOAD_POLICY_USERS.USER_ID = ?" +
                        " WHERE DATA_FEED.DATA_FEED_ID = ?");
                PreparedStatement queryStmt = conn.prepareStatement(queryBuilder.toString());
                queryStmt.setLong(1, userID);
                queryStmt.setLong(2, feedID);
                ResultSet rs = queryStmt.executeQuery();
                if (rs.next()) {
                    String feedName = rs.getString(1);
                    int feedType = rs.getInt(2);
                    String ownerName = rs.getString(3);
                    String description = rs.getString(4);
                    String attribution = rs.getString(5);
                    int role = rs.getInt(6);
                    if (rs.wasNull()) {
                        role = Roles.NONE;
                    }
                    feedDescriptor = createDescriptor(feedID, feedName, role, 0, feedType, ownerName, description, attribution, null);
                    Collection<Tag> tags = getTags(feedID, conn);
                    StringBuilder tagStringBuilder = new StringBuilder();
                    Iterator<Tag> tagIter = tags.iterator();
                    while (tagIter.hasNext()) {
                        Tag tag = tagIter.next();
                        tagStringBuilder.append(tag.getTagName());
                        if (tagIter.hasNext()) {
                            tagStringBuilder.append(" ");
                        }
                    }
                    feedDescriptor.setTagString(tagStringBuilder.toString());
                }
                queryStmt.close();
            }

        } finally {
            Database.closeConnection(conn);
        }
        return feedDescriptor;
    }

    public List<FeedDescriptor> getDataSourcesFromGroups(long userID) throws SQLException {
        List<FeedDescriptor> descriptorList = new ArrayList<FeedDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DISTINCT DATA_FEED.DATA_FEED_ID, DATA_FEED.FEED_NAME, " +
                    "FEED_PERSISTENCE_METADATA.SIZE, DATA_FEED.FEED_TYPE, OWNER_NAME, DESCRIPTION, ATTRIBUTION, ROLE, PUBLICLY_VISIBLE, MARKETPLACE_VISIBLE, FEED_PERSISTENCE_METADATA.LAST_DATA_TIME, PASSWORD_STORAGE.USERNAME," +
                    "DATA_FEED.PARENT_SOURCE_ID, UPLOAD_POLICY_GROUPS.GROUP_ID " +
                    " FROM (upload_policy_groups, group_to_user_join, DATA_FEED LEFT JOIN FEED_PERSISTENCE_METADATA ON DATA_FEED.DATA_FEED_ID = FEED_PERSISTENCE_METADATA.FEED_ID) LEFT JOIN PASSWORD_STORAGE ON DATA_FEED.DATA_FEED_ID = PASSWORD_STORAGE.DATA_FEED_ID WHERE " +
                    "upload_policy_groups.group_id = group_to_user_join.group_id AND GROUP_TO_USER_JOIN.USER_ID = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_GROUPS.FEED_ID");
            queryStmt.setLong(1, userID);
            Map<Long, Long> sizeMap = new HashMap<Long, Long>();
            Map<Long, FeedDescriptor> feedMap = new HashMap<Long, FeedDescriptor>();
            Map<Long, Date> lastDateMap = new HashMap<Long, Date>();
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long dataFeedID = rs.getLong(1);
                String feedName = rs.getString(2);
                long feedSize = rs.getLong(3);
                int feedType = rs.getInt(4);
                String ownerName = rs.getString(5);
                String description = rs.getString(6);
                String attribution = rs.getString(7);
                int userRole = rs.getInt(8);
                Timestamp lastTime = rs.getTimestamp(11);
                Date lastDataTime = null;
                boolean hasSavedCredentials = rs.getString(12) != null;
                if (lastTime != null) {
                    lastDataTime = new Date(lastTime.getTime());
                }
                Long parentSourceID = rs.getLong(13);
                if (!rs.wasNull() && parentSourceID > 0) {
                    Long size = sizeMap.get(parentSourceID);
                    if (size == null) {
                        sizeMap.put(parentSourceID, feedSize);
                    } else {
                        sizeMap.put(parentSourceID, feedSize + size);
                    }
                    lastDateMap.put(parentSourceID, lastDataTime);
                } else {
                    long groupID = rs.getLong(14);
                    FeedDescriptor feedDescriptor = createDescriptor(dataFeedID, feedName, userRole, feedSize, feedType, ownerName, description, attribution, lastDataTime);
                    feedDescriptor.setGroupSourceID(groupID);
                    feedDescriptor.setHasSavedCredentials(hasSavedCredentials);
                    descriptorList.add(feedDescriptor);
                    feedMap.put(dataFeedID, feedDescriptor);
                }
            }
            for (Map.Entry<Long, Long> sizeEntry : sizeMap.entrySet()) {
                FeedDescriptor feedDescriptor = feedMap.get(sizeEntry.getKey());
                if (feedDescriptor != null) {
                    feedDescriptor.setSize(sizeEntry.getValue());
                    feedDescriptor.setLastDataTime(lastDateMap.get(sizeEntry.getKey()));
                }
            }
            descriptorList = new ArrayList<FeedDescriptor>(feedMap.values());
            queryStmt.close();
        } finally {
            Database.closeConnection(conn);
        }
        return descriptorList;
    }

    public List<FeedDescriptor> getDataSourcesForGroup(long groupID) throws SQLException {
        List<FeedDescriptor> descriptorList = new ArrayList<FeedDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DISTINCT DATA_FEED.DATA_FEED_ID, DATA_FEED.FEED_NAME, " +
                    "FEED_PERSISTENCE_METADATA.SIZE, DATA_FEED.FEED_TYPE, OWNER_NAME, DESCRIPTION, ATTRIBUTION, ROLE, PUBLICLY_VISIBLE, MARKETPLACE_VISIBLE, FEED_PERSISTENCE_METADATA.LAST_DATA_TIME, PASSWORD_STORAGE.USERNAME," +
                    "DATA_FEED.PARENT_SOURCE_ID " +
                    " FROM (upload_policy_groups, DATA_FEED LEFT JOIN FEED_PERSISTENCE_METADATA ON DATA_FEED.DATA_FEED_ID = FEED_PERSISTENCE_METADATA.FEED_ID) LEFT JOIN PASSWORD_STORAGE ON DATA_FEED.DATA_FEED_ID = PASSWORD_STORAGE.DATA_FEED_ID WHERE " +
                    "upload_policy_groups.group_id = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_GROUPS.FEED_ID");
            queryStmt.setLong(1, groupID);
            Map<Long, Long> sizeMap = new HashMap<Long, Long>();
            Map<Long, FeedDescriptor> feedMap = new HashMap<Long, FeedDescriptor>();
            Map<Long, Date> lastDateMap = new HashMap<Long, Date>();
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long dataFeedID = rs.getLong(1);
                String feedName = rs.getString(2);
                long feedSize = rs.getLong(3);
                int feedType = rs.getInt(4);
                String ownerName = rs.getString(5);
                String description = rs.getString(6);
                String attribution = rs.getString(7);
                int userRole = rs.getInt(8);
                Timestamp lastTime = rs.getTimestamp(9);
                Date lastDataTime = null;
                boolean hasSavedCredentials = rs.getString(12) != null;
                if (lastTime != null) {
                    lastDataTime = new Date(lastTime.getTime());
                }
                Long parentSourceID = rs.getLong(13);
                if (!rs.wasNull() && parentSourceID > 0) {
                    Long size = sizeMap.get(parentSourceID);
                    if (size == null) {
                        sizeMap.put(parentSourceID, feedSize);
                    } else {
                        sizeMap.put(parentSourceID, feedSize + size);
                    }
                    lastDateMap.put(parentSourceID, lastDataTime);
                } else {
                    FeedDescriptor feedDescriptor = createDescriptor(dataFeedID, feedName, userRole, feedSize, feedType, ownerName, description, attribution, lastDataTime);
                    feedDescriptor.setGroupSourceID(groupID);
                    feedDescriptor.setHasSavedCredentials(hasSavedCredentials);
                    descriptorList.add(feedDescriptor);
                    feedMap.put(dataFeedID, feedDescriptor);
                }
            }
            for (Map.Entry<Long, Long> sizeEntry : sizeMap.entrySet()) {
                FeedDescriptor feedDescriptor = feedMap.get(sizeEntry.getKey());
                if (feedDescriptor != null) {
                    feedDescriptor.setSize(sizeEntry.getValue());
                    feedDescriptor.setLastDataTime(lastDateMap.get(sizeEntry.getKey()));
                }
            }
            descriptorList = new ArrayList<FeedDescriptor>(feedMap.values());
            queryStmt.close();
        } finally {
            Database.closeConnection(conn);
        }
        return descriptorList;
    }

    public List<FeedDescriptor> searchForSubscribedFeeds(long userID) throws SQLException {
        List<FeedDescriptor> descriptorList = new ArrayList<FeedDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DISTINCT DATA_FEED.DATA_FEED_ID, DATA_FEED.FEED_NAME, " +
                    "FEED_PERSISTENCE_METADATA.SIZE, DATA_FEED.FEED_TYPE, OWNER_NAME, DESCRIPTION, ATTRIBUTION, ROLE, PUBLICLY_VISIBLE, MARKETPLACE_VISIBLE, FEED_PERSISTENCE_METADATA.LAST_DATA_TIME, PASSWORD_STORAGE.USERNAME," +
                    "DATA_FEED.PARENT_SOURCE_ID, VISIBLE " +
                    " FROM (UPLOAD_POLICY_USERS, DATA_FEED LEFT JOIN FEED_PERSISTENCE_METADATA ON DATA_FEED.DATA_FEED_ID = FEED_PERSISTENCE_METADATA.FEED_ID) LEFT JOIN PASSWORD_STORAGE ON DATA_FEED.DATA_FEED_ID = PASSWORD_STORAGE.DATA_FEED_ID WHERE " +
                    "UPLOAD_POLICY_USERS.USER_ID = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID");
            queryStmt.setLong(1, userID);
            Map<Long, Long> sizeMap = new HashMap<Long, Long>();
            Map<Long, FeedDescriptor> feedMap = new HashMap<Long, FeedDescriptor>();
            Map<Long, Date> lastDateMap = new HashMap<Long, Date>();
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long dataFeedID = rs.getLong(1);
                String feedName = rs.getString(2);
                long feedSize = rs.getLong(3);
                int feedType = rs.getInt(4);
                String ownerName = rs.getString(5);
                String description = rs.getString(6);
                String attribution = rs.getString(7);
                int userRole = rs.getInt(8);
                Timestamp lastTime = rs.getTimestamp(11);
                Date lastDataTime = null;
                boolean hasSavedCredentials = rs.getString(12) != null;
                if (lastTime != null) {
                    lastDataTime = new Date(lastTime.getTime());
                }
                Long parentSourceID = rs.getLong(13);
                boolean visible = rs.getBoolean(14);
                if (!rs.wasNull() && parentSourceID > 0) {
                    Long size = sizeMap.get(parentSourceID);
                    if (size == null) {
                        sizeMap.put(parentSourceID, feedSize);
                    } else {
                        sizeMap.put(parentSourceID, feedSize + size);
                    }
                    lastDateMap.put(parentSourceID, lastDataTime);
                } else {
                    if (visible) {
                        FeedDescriptor feedDescriptor = createDescriptor(dataFeedID, feedName, userRole, feedSize, feedType, ownerName, description, attribution, lastDataTime);
                        feedDescriptor.setHasSavedCredentials(hasSavedCredentials);
                        descriptorList.add(feedDescriptor);
                        feedMap.put(dataFeedID, feedDescriptor);
                    }
                }
            }
            for (Map.Entry<Long, Long> sizeEntry : sizeMap.entrySet()) {
                FeedDescriptor feedDescriptor = feedMap.get(sizeEntry.getKey());
                if (feedDescriptor != null) {
                    feedDescriptor.setSize(sizeEntry.getValue());
                    feedDescriptor.setLastDataTime(lastDateMap.get(sizeEntry.getKey()));
                }
            }
            if (SecurityUtil.getAccountTier() == Account.ADMINISTRATOR && feedMap.size() > 0) {
                StringBuilder sqlBuilder = new StringBuilder("SELECT SOLUTION_ID, FEED_ID FROM SOLUTION_TO_FEED " +
                        "WHERE SOLUTION_TO_FEED.FEED_ID IN (");
                for (FeedDescriptor feedDescriptor : feedMap.values()) {
                    sqlBuilder.append("?,");
                }
                PreparedStatement solutionStmt = conn.prepareStatement(sqlBuilder.substring(0, sqlBuilder.length() - 1) + ")");
                int i = 0;
                for (FeedDescriptor feedDescriptor : feedMap.values()) {
                    solutionStmt.setLong(++i, feedDescriptor.getDataFeedID());
                }
                ResultSet solutionRS = solutionStmt.executeQuery();
                while (solutionRS.next()) {
                    long feedID = solutionRS.getLong(2);
                    //long solutionID = solutionRS.getLong(1);
                    feedMap.get(feedID).setSolutionTemplate(true);
                }

            }
            descriptorList = new ArrayList<FeedDescriptor>(feedMap.values());
            queryStmt.close();
        } finally {
            Database.closeConnection(conn);
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
            UserStub userStub = new UserStub(userID, userName, email, name, accountID);
            if (role == Roles.OWNER) {
                owners.add(userStub);
            } else {
                viewers.add(userStub);
            }
        }
        policyUserStmt.close();
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
        policyGroupsStmt.close();
        uploadPolicy.setOwners(owners);
        uploadPolicy.setViewers(viewers);
        return uploadPolicy;
    }

    public long getFeedForAPIKey(long userID, String apiKey) throws CacheException, SQLException {
        Connection conn = Database.instance().getConnection();
        Long feedID = null;
        if (apiKeyCache != null)
            feedID = (Long) apiKeyCache.get(new FeedApiKey(apiKey, userID));
        if (feedID != null) {
            LogClass.debug("Cache hit for API key: " + apiKey + " & User id: " + userID);
            return feedID;
        }
        try {
            LogClass.debug("Cache miss for API key: " + apiKey + " & User id: " + userID);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DISTINCT DATA_FEED.DATA_FEED_ID" +
                    " FROM UPLOAD_POLICY_USERS, DATA_FEED WHERE " +
                    "UPLOAD_POLICY_USERS.user_id = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND DATA_FEED.API_KEY = ?");
            queryStmt.setLong(1, userID);
            queryStmt.setString(2, apiKey);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                feedID = rs.getLong(1);
                if (apiKeyCache != null)
                    apiKeyCache.put(new FeedApiKey(apiKey, userID), feedID);
                return feedID;
            }
        } finally {
            Database.closeConnection(conn);
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
