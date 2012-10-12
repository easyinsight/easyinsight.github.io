package com.easyinsight.datafeeds;

import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.FormattingConfiguration;
import com.easyinsight.analysis.Link;
import com.easyinsight.core.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.etl.LookupTableDescriptor;
import com.easyinsight.userupload.*;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.email.UserStub;
import com.easyinsight.groups.GroupDescriptor;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;

import com.easyinsight.logging.LogClass;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.io.Serializable;

import flex.messaging.FlexContext;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.proxy.HibernateProxy;

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
        } catch (Exception e) {
            LogClass.error(e);
        }
    }

    public List<LookupTableDescriptor> getLookupTableDescriptors(EIConnection conn) throws SQLException {
        List<LookupTableDescriptor> descriptors = new ArrayList<LookupTableDescriptor>();

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
        queryStmt.close();
        return descriptors;
    }

    public long addFeedDefinitionData(FeedDefinition feedDefinition, Connection conn) throws Exception {
        PreparedStatement insertDataFeedStmt;
        insertDataFeedStmt = conn.prepareStatement("INSERT INTO DATA_FEED (FEED_NAME, FEED_TYPE, PUBLICLY_VISIBLE, FEED_SIZE, " +
                "CREATE_DATE, UPDATE_DATE, DESCRIPTION," +
                "ATTRIBUTION, OWNER_NAME, DYNAMIC_SERVICE_DEFINITION_ID, MARKETPLACE_VISIBLE, " +
                "API_KEY, UNCHECKED_API_BASIC_AUTH, UNCHECKED_API_ENABLED, INHERIT_ACCOUNT_API_SETTINGS," +
                "CURRENT_VERSION, VISIBLE, PARENT_SOURCE_ID, VERSION, ACCOUNT_VISIBLE, last_refresh_start, marmotscript, concrete_fields_editable, refresh_marmot_script, refresh_behavior) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
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
        insertDataFeedStmt.setTimestamp(i++, new java.sql.Timestamp(feedDefinition.getDateCreated().getTime()));
        insertDataFeedStmt.setTimestamp(i++, new java.sql.Timestamp(feedDefinition.getDateUpdated().getTime()));
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
        insertDataFeedStmt.setBoolean(i++, feedDefinition.isInheritAccountAPISettings());
        insertDataFeedStmt.setInt(i++, 1);
        insertDataFeedStmt.setBoolean(i++, feedDefinition.isVisible());
        insertDataFeedStmt.setLong(i++, feedDefinition.getParentSourceID());
        insertDataFeedStmt.setInt(i++, feedDefinition.getVersion());
        insertDataFeedStmt.setBoolean(i++, feedDefinition.isAccountVisible());
        if (feedDefinition.getLastRefreshStart() == null) {
            insertDataFeedStmt.setNull(i++, Types.TIMESTAMP);
        } else {
            insertDataFeedStmt.setTimestamp(i++, new Timestamp(feedDefinition.getLastRefreshStart().getTime()));
        }
        insertDataFeedStmt.setString(i++, feedDefinition.getMarmotScript());
        insertDataFeedStmt.setBoolean(i++, feedDefinition.isConcreteFieldsEditable());
        insertDataFeedStmt.setString(i++, feedDefinition.getRefreshMarmotScript());
        insertDataFeedStmt.setInt(i, feedDefinition.getDataSourceType());
        insertDataFeedStmt.execute();
        long feedID = Database.instance().getAutoGenKey(insertDataFeedStmt);
        feedDefinition.setDataFeedID(feedID);
        savePolicy(conn, feedID, feedDefinition.getUploadPolicy());
        feedDefinition.setDataFeedID(feedID);
        saveFields(feedID, conn, feedDefinition.getFields());
        saveFolders(feedID, conn, feedDefinition.getFolders(), feedDefinition.getFields());
        feedDefinition.exchangeTokens((EIConnection) conn, FlexContext.getHttpRequest(), null);
        feedDefinition.customStorage(conn);

        insertDataFeedStmt.close();
        return feedID;
    }

    private List<AnalysisItem> itemsFromFolders(List<FeedFolder> folders) {
        FolderItemVisitor visitor = new FolderItemVisitor();
        visitor.visit(folders);
        return visitor.items;
    }

    private static class FolderItemVisitor {
        private List<AnalysisItem> items = new ArrayList<AnalysisItem>();

        public void visit(List<FeedFolder> folders) {
            for (FeedFolder folder : folders) {
                items.addAll(folder.getChildItems());
                if (folder.getChildFolders() != null) visit(folder.getChildFolders());
            }
        }
    }

    private void saveFolders(long feedID, Connection conn, List<FeedFolder> folders, List<AnalysisItem> fields) throws SQLException {
        Set<Long> ids = new HashSet<Long>();
        for (AnalysisItem field : fields) {
            ids.add(field.getAnalysisItemID());
        }
        validateFolders(folders, ids);
        PreparedStatement wipeStmt = conn.prepareStatement("DELETE FROM FOLDER WHERE DATA_SOURCE_ID = ?");
        wipeStmt.setLong(1, feedID);
        wipeStmt.executeUpdate();
        wipeStmt.close();
        for (FeedFolder folder : folders) {
            saveFolder(folder, feedID, conn, ids);
        }
    }

    private void validateFolders(List<FeedFolder> folders, Set<Long> ids) {
        Iterator<FeedFolder> iter = folders.iterator();
        while (iter.hasNext()) {
            FeedFolder folder = iter.next();
            boolean valid = validateFolder(folder, ids);
            if (!valid) {
                iter.remove();
            }
        }
    }

    private boolean validateFolder(FeedFolder folder, Set<Long> ids) {
        boolean atLeastOneItem = false;
        Iterator<FeedFolder> iter = folder.getChildFolders().iterator();
        while (iter.hasNext()) {
            FeedFolder childFolder = iter.next();
            boolean childValid = validateFolder(childFolder, ids);
            atLeastOneItem = atLeastOneItem || childValid;
            if (!childValid) {
                iter.remove();
            }
        }
        for (AnalysisItem analysisItem : folder.getChildItems()) {
            if (ids.contains(analysisItem.getAnalysisItemID())) {
                atLeastOneItem = true;
            }
        }
        return atLeastOneItem;
    }

    private long saveFolder(FeedFolder folder, long feedID, Connection conn, Set<Long> ids) throws SQLException {
        PreparedStatement insertFolderStmt = conn.prepareStatement("INSERT INTO FOLDER (FOLDER_NAME, DATA_SOURCE_ID) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        insertFolderStmt.setString(1, folder.getName());
        insertFolderStmt.setLong(2, feedID);
        insertFolderStmt.execute();
        folder.setFolderID(Database.instance().getAutoGenKey(insertFolderStmt));
        insertFolderStmt.close();
        saveFields(conn, folder, ids);

        PreparedStatement clearFoldersStmt = conn.prepareStatement("DELETE FROM folder_to_folder WHERE parent_folder_id = ?");
        clearFoldersStmt.setLong(1, folder.getFolderID());
        clearFoldersStmt.executeUpdate();
        clearFoldersStmt.close();
        for (FeedFolder childFolder : folder.getChildFolders()) {
            saveFolder(childFolder, feedID, conn, ids);
            PreparedStatement insertChildFolderStmt = conn.prepareStatement("INSERT INTO folder_to_folder (parent_folder_id, child_folder_id) values (?, ?)");
            insertChildFolderStmt.setLong(1, folder.getFolderID());
            insertChildFolderStmt.setLong(2, childFolder.getFolderID());
            insertChildFolderStmt.execute();
            insertChildFolderStmt.close();
        }
        return folder.getFolderID();
    }

    private void saveFields(Connection conn, FeedFolder folder, Set<Long> fields) throws SQLException {
        PreparedStatement getJoinsStmt = conn.prepareStatement("SELECT ANALYSIS_ITEM_ID FROM FOLDER_TO_ANALYSIS_ITEM WHERE FOLDER_ID = ?");
        getJoinsStmt.setLong(1, folder.getFolderID());
        Set<Long> ids = new HashSet<Long>();
        ResultSet rs = getJoinsStmt.executeQuery();
        while (rs.next()) {
            ids.add(rs.getLong(1));
        }
        getJoinsStmt.close();

        /*PreparedStatement clearJoinsStmt = conn.prepareStatement("DELETE FROM folder_to_analysis_item WHERE folder_id = ?");
        clearJoinsStmt.setLong(1, folder.getFolderID());
        clearJoinsStmt.executeUpdate();
        clearJoinsStmt.close();*/
        PreparedStatement insertFieldStmt = conn.prepareStatement("INSERT INTO folder_to_analysis_item (folder_id, analysis_item_id) values (?, ?)");
        for (AnalysisItem analysisItem : folder.getChildItems()) {
            boolean okay = fields.contains(analysisItem.getAnalysisItemID());
            if (okay) {
                if (ids.contains(analysisItem.getAnalysisItemID())) {
                    ids.remove(analysisItem.getAnalysisItemID());
                } else {
                    insertFieldStmt.setLong(1, folder.getFolderID());
                    insertFieldStmt.setLong(2, analysisItem.getAnalysisItemID());
                    try {
                        insertFieldStmt.execute();
                    } catch (SQLException e) {
                        LogClass.error("Analysis item " + analysisItem.toDisplay() + " was not yet saved in folder " + folder.getName());
                        throw e;
                    }
                }
            }
        }
        PreparedStatement clearJoinsStmt = conn.prepareStatement("DELETE FROM folder_to_analysis_item WHERE analysis_item_id = ?");
        for (Long id : ids) {
            clearJoinsStmt.setLong(1, id);
            clearJoinsStmt.executeUpdate();
        }
        clearJoinsStmt.close();
        insertFieldStmt.close();
    }

    long fieldSaveTime = 0;

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
        List<Long> childIDs = new ArrayList<Long>();
        while (childRS.next()) {
            long childID = childRS.getLong(1);
            childIDs.add(childID);
        }
        childFoldersStmt.close();
        for (Long childID : childIDs) {
            childFolders.add(getFolder(childID, fields, conn));
        }
        feedFolder.setChildFolders(childFolders);
        return feedFolder;
    }

    public long addFeedDefinitionData(FeedDefinition feedDefinition) throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            long feedID = addFeedDefinitionData(feedDefinition, conn);
            conn.commit();
            return feedID;
        } catch (Exception e) {
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

    private void saveFields(long feedID, Connection conn, List<AnalysisItem> analysisItems) throws SQLException {
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM FEED_TO_ANALYSIS_ITEM WHERE FEED_ID = ?");
        deleteStmt.setLong(1, feedID);
        deleteStmt.executeUpdate();
        deleteStmt.close();
        if (analysisItems != null) {
            Session session = Database.instance().createSession(conn);
            try {
                /*for (AnalysisItem analysisItem : analysisItems) {
                    if (analysisItem.getKey().getKeyID() == 0) {
                        session.save(analysisItem.getKey());
                    } else {
                        session.merge(analysisItem.getKey());
                    }
                }*/
                for (AnalysisItem analysisItem : analysisItems) {
                    try {
                        analysisItem.reportSave(session);
                        if (analysisItem.getAnalysisItemID() == 0) {
                            session.save(analysisItem);
                        } else {
                            session.update(analysisItem);
                        }
                    } catch (ConstraintViolationException e) {
                        LogClass.error("On saving " + analysisItem.toDisplay() + " - " + analysisItem.getAnalysisItemID());
                        throw e;
                    }
                }
                session.flush();
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

        List<AnalysisItem> analysisItems;
        try {
            analysisItems = optimizedRetrieval(feedDefinition, conn, feedID, analysisItemIDs);
        } catch (Exception e) {
            analysisItems = slowRetrieval(conn, analysisItemIDs);
        }
        feedDefinition.setFields(analysisItems);
    }

    private List<AnalysisItem> slowRetrieval(Connection conn, Set<Long> analysisItemIDs) throws SQLException {
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
                        AnalysisItem item = (AnalysisItem) Database.deproxy(analysisItem);
                        if (item.getKey().getClass().getName().equals("com.easyinsight.core.Key")) {
                            PreparedStatement fixStmt = conn.prepareStatement("DELETE FROM FEED_TO_ANALYSIS_ITEM WHERE ANALYSIS_ITEM_ID = ?");
                            fixStmt.setLong(1, analysisItemID);
                            fixStmt.executeUpdate();
                        } else {
                            analysisItems.add(item);
                        }
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
        return analysisItems;
    }

    private List<AnalysisItem> optimizedRetrieval(FeedDefinition feedDefinition, Connection conn, long feedID, Set<Long> analysisItemIDs) throws SQLException {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();

        StringBuilder sb = new StringBuilder();
        for (Long id : analysisItemIDs) {
            sb.append(id).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        StringBuilder aIDs = new StringBuilder(sb.toString());
        Session session = Database.instance().createSession(conn);
        Map<Long, AnalysisItem> formattingConfigurationIDs = new HashMap<Long, AnalysisItem>(analysisItemIDs.size());
        Map<Long, AnalysisItem> keyIDs = new HashMap<Long, AnalysisItem>(analysisItemIDs.size());
        Map<Long, AnalysisItem> coreLookup = new HashMap<Long, AnalysisItem>(analysisItemIDs.size());
        try {
            List items = session.createQuery("from AnalysisItem where analysisItemID in (" + sb.toString() + ")").list();
            for (Object obj : items) {

                AnalysisItem analysisItem = (AnalysisItem) obj;

                AnalysisItem item = (AnalysisItem) Database.deproxy(analysisItem);
                coreLookup.put(item.getAnalysisItemID(), item);

                analysisItems.add(item);

                item.afterLoad(true);
                HibernateProxy hibernateProxy = (HibernateProxy) item.getFormattingConfiguration();
                Serializable id = hibernateProxy.getHibernateLazyInitializer().getIdentifier();
                formattingConfigurationIDs.put((Long) id, analysisItem);
                HibernateProxy keyProxy = (HibernateProxy) item.getKey();
                Serializable keyID = keyProxy.getHibernateLazyInitializer().getIdentifier();
                keyIDs.put((Long) keyID, analysisItem);
            }
            sb = new StringBuilder();
            for (Long id : formattingConfigurationIDs.keySet()) {
                sb.append(id).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);

            List formattingConfigs = session.createQuery("from FormattingConfiguration where formattingConfigurationID in (" + sb.toString() + ")").list();
            for (Object obj : formattingConfigs) {
                FormattingConfiguration formattingConfiguration = (FormattingConfiguration) Database.deproxy(obj);
                formattingConfigurationIDs.get(formattingConfiguration.getFormattingConfigurationID()).setFormattingConfiguration(formattingConfiguration);
            }

            sb = new StringBuilder();
            Iterator<Long> iter = keyIDs.keySet().iterator();
            List<DerivedKey> derivedKeys = new ArrayList<DerivedKey>();
            for (int i = 0; i < keyIDs.size(); i++) {

                Long id = iter.next();
                sb.append(id).append(",");
            }

            sb.deleteCharAt(sb.length() - 1);
            List keys = session.createQuery("from Key where keyID in (" + sb.toString() + ")").list();
            for (Object obj : keys) {
                Key key = (Key) Database.deproxy(obj);
                if (key instanceof DerivedKey) {
                    derivedKeys.add((DerivedKey) key);
                }
                keyIDs.get(key.getKeyID()).setKey(key);
            }

            if (derivedKeys.size() > 0) {
                StringBuilder dkBuilder = new StringBuilder();
                Map<Long, List<DerivedKey>> map = new HashMap<Long, List<DerivedKey>>(derivedKeys.size());
                for (DerivedKey derivedKey : derivedKeys) {
                    HibernateProxy hibernateProxy = (HibernateProxy) derivedKey.getParentKey();
                    Long parentKeyID = (Long) hibernateProxy.getHibernateLazyInitializer().getIdentifier();
                    dkBuilder.append(parentKeyID).append(",");
                    List<DerivedKey> keyList = map.get(parentKeyID);
                    if (keyList == null) {
                        keyList = new ArrayList<DerivedKey>();
                        map.put(parentKeyID, keyList);
                    }
                    keyList.add(derivedKey);
                }
                dkBuilder.deleteCharAt(dkBuilder.length() - 1);
                List parentKeys = session.createQuery("from Key where keyID in (" + dkBuilder.toString() + ")").list();
                for (Object obj : parentKeys) {
                    Key key = (Key) obj;
                    List<DerivedKey> keyList = map.get(key.getKeyID());
                    for (DerivedKey derivedKey : keyList) {
                        derivedKey.setParentKey((Key) Database.deproxy(key));
                        derivedKey.afterLoad();
                    }
                }
            }

            {
                PreparedStatement linkStmt = conn.prepareStatement("SELECT LINK_ID, ANALYSIS_ITEM_TO_LINK.ANALYSIS_ITEM_ID FROM ANALYSIS_ITEM_TO_LINK, FEED_TO_ANALYSIS_ITEM WHERE FEED_TO_ANALYSIS_ITEM.ANALYSIS_ITEM_ID = ANALYSIS_ITEM_TO_LINK.ANALYSIS_ITEM_ID AND FEED_TO_ANALYSIS_ITEM.FEED_ID = ?");
                linkStmt.setLong(1, feedID);
                ResultSet links = linkStmt.executeQuery();
                sb = new StringBuilder();
                Map<Long, Long> lookup = new HashMap<Long, Long>();
                while (links.next()) {
                    long linkID = links.getLong(1);
                    long analysisItemID = links.getLong(2);
                    sb.append(linkID).append(",");
                    lookup.put(linkID, analysisItemID);
                }
                linkStmt.close();
                for (AnalysisItem analysisItem : coreLookup.values()) {
                    analysisItem.setLinks(new ArrayList<Link>());
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                    List linkResults = session.createQuery("from Link where linkID in (" + sb.toString() + ")").list();
                    for (Object obj : linkResults) {
                        Link link = (Link) Database.deproxy(obj);
                        long analysisItemID = lookup.get(link.getLinkID());
                        AnalysisItem analysisItem = coreLookup.get(analysisItemID);
                        analysisItem.getLinks().add(link);
                    }
                }
            }

            {
                PreparedStatement filterStmt = conn.prepareStatement("SELECT FILTER_ID, ANALYSIS_ITEM_TO_FILTER.FILTER_ID FROM ANALYSIS_ITEM_TO_FILTER, FEED_TO_ANALYSIS_ITEM WHERE FEED_TO_ANALYSIS_ITEM.ANALYSIS_ITEM_ID = ANALYSIS_ITEM_TO_FILTER.ANALYSIS_ITEM_ID AND FEED_TO_ANALYSIS_ITEM.FEED_ID = ?");
                filterStmt.setLong(1, feedID);
                ResultSet links = filterStmt.executeQuery();
                sb = new StringBuilder();
                Map<Long, Long> lookup = new HashMap<Long, Long>();
                while (links.next()) {
                    long linkID = links.getLong(1);
                    long analysisItemID = links.getLong(2);
                    sb.append(linkID).append(",");
                    lookup.put(linkID, analysisItemID);
                }
                filterStmt.close();
                for (AnalysisItem analysisItem : coreLookup.values()) {
                    analysisItem.setFilters(new ArrayList<FilterDefinition>());
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                    List linkResults = session.createQuery("from FilterDefinition where filterID in (" + sb.toString() + ")").list();
                    for (Object obj : linkResults) {
                        FilterDefinition filterDefinition = (FilterDefinition) Database.deproxy(obj);
                        filterDefinition.afterLoad();
                        long analysisItemID = lookup.get(filterDefinition.getFilterID());
                        AnalysisItem analysisItem = coreLookup.get(analysisItemID);
                        analysisItem.getFilters().add(filterDefinition);
                    }
                }
            }

        } finally {
            session.close();
        }
        return analysisItems;
    }

    static long time = 0;

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

    public void updateDataFeedConfiguration(FeedDefinition feedDefinition, Connection conn) throws Exception {
        try {
            if (feedCache != null) {
                feedCache.remove(feedDefinition.getDataFeedID());
                if (FeedRegistry.instance() != null) {
                    FeedRegistry.instance().flushCache(feedDefinition.getDataFeedID());
                }
                LogClass.debug("Removed " + feedDefinition.getDataFeedID() + " from feed cache.");
            }

        } catch (CacheException e) {
            LogClass.error(e);
        }
        PreparedStatement updateDataFeedStmt = conn.prepareStatement("UPDATE DATA_FEED SET FEED_NAME = ?, FEED_TYPE = ?, PUBLICLY_VISIBLE = ?, " +
                "FEED_SIZE = ?, DESCRIPTION = ?, ATTRIBUTION = ?, OWNER_NAME = ?, DYNAMIC_SERVICE_DEFINITION_ID = ?, MARKETPLACE_VISIBLE = ?," +
                "API_KEY = ?, unchecked_api_enabled = ?, VISIBLE = ?, parent_source_id = ?, VERSION = ?," +
                "CREATE_DATE = ?, UPDATE_DATE = ?, ACCOUNT_VISIBLE = ?, LAST_REFRESH_START = ?, MARMOTSCRIPT = ?, CONCRETE_FIELDS_EDITABLE = ?, REFRESH_MARMOT_SCRIPT = ?," +
                "REFRESH_BEHAVIOR = ? " +
                "WHERE DATA_FEED_ID = ?");
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
        updateDataFeedStmt.setBoolean(i++, feedDefinition.isUncheckedAPIEnabled());
        updateDataFeedStmt.setBoolean(i++, feedDefinition.isVisible());
        updateDataFeedStmt.setLong(i++, feedDefinition.getParentSourceID());
        updateDataFeedStmt.setLong(i++, feedDefinition.getVersion());
        updateDataFeedStmt.setTimestamp(i++, new Timestamp(feedDefinition.getDateCreated().getTime()));
        updateDataFeedStmt.setTimestamp(i++, new Timestamp(feedDefinition.getDateUpdated().getTime()));
        updateDataFeedStmt.setBoolean(i++, feedDefinition.isAccountVisible());
        if (feedDefinition.getLastRefreshStart() == null) {
            updateDataFeedStmt.setNull(i++, Types.TIMESTAMP);
        } else {
            updateDataFeedStmt.setTimestamp(i++, new Timestamp(feedDefinition.getLastRefreshStart().getTime()));
        }
        updateDataFeedStmt.setString(i++, feedDefinition.getMarmotScript());
        updateDataFeedStmt.setBoolean(i++, feedDefinition.isConcreteFieldsEditable());
        updateDataFeedStmt.setString(i++, feedDefinition.getRefreshMarmotScript());
        updateDataFeedStmt.setInt(i++, feedDefinition.getDataSourceType());
        updateDataFeedStmt.setLong(i, feedDefinition.getDataFeedID());
        int rows = updateDataFeedStmt.executeUpdate();
        if (rows != 1) {
            throw new RuntimeException("Could not locate row to update");
        }
        updateDataFeedStmt.close();
        savePolicy(conn, feedDefinition.getDataFeedID(), feedDefinition.getUploadPolicy());
        saveFields(feedDefinition.getDataFeedID(), conn, feedDefinition.getFields());
        saveFolders(feedDefinition.getDataFeedID(), conn, feedDefinition.getFolders(), feedDefinition.getFields());
        clearProblems(feedDefinition.getDataFeedID(), conn);
        feedDefinition.exchangeTokens((EIConnection) conn, FlexContext.getHttpRequest(), null);
        feedDefinition.customStorage(conn);

        updateDataFeedStmt.close();
    }

    private void clearProblems(long dataSourceID, Connection conn) throws Exception {
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM DATA_SOURCE_PROBLEM WHERE data_source_id = ?");
        deleteStmt.setLong(1, dataSourceID);
        deleteStmt.executeUpdate();
        deleteStmt.close();
    }

    public void updateDataFeedConfiguration(FeedDefinition feedDefinition) throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            updateDataFeedConfiguration(feedDefinition, conn);
            conn.commit();
        } catch (Exception e) {
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
                "UPDATE_DATE, FEED_SIZE," +
                "ATTRIBUTION, DESCRIPTION, OWNER_NAME, DYNAMIC_SERVICE_DEFINITION_ID, API_KEY, unchecked_api_enabled, " +
                "VISIBLE, PARENT_SOURCE_ID, ACCOUNT_VISIBLE, LAST_REFRESH_START, MARMOTSCRIPT, CONCRETE_FIELDS_EDITABLE, refresh_marmot_script " +
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
            long feedSize = rs.getLong(i++);
            String attribution = rs.getString(i++);
            String description = rs.getString(i++);
            String ownerName = rs.getString(i++);
            feedDefinition.setSize(feedSize);
            feedDefinition.setDateCreated(createDate);
            feedDefinition.setDateUpdated(updateDate);
            retrieveFields(feedDefinition, conn);
            feedDefinition.setAttribution(attribution);
            feedDefinition.setDescription(description);
            feedDefinition.setOwnerName(ownerName);
            feedDefinition.setDynamicServiceDefinitionID(rs.getLong(i++));
            feedDefinition.setApiKey(rs.getString(i++));
            feedDefinition.setUncheckedAPIEnabled(rs.getBoolean(i++));
            feedDefinition.setVisible(rs.getBoolean(i++));
            long parentSourceID = rs.getLong(i++);
            if (!rs.wasNull()) {
                feedDefinition.setParentSourceID(parentSourceID);
            }
            feedDefinition.setAccountVisible(rs.getBoolean(i++));
            Timestamp lastRefreshTime = rs.getTimestamp(i++);
            if (lastRefreshTime != null) {
                feedDefinition.setLastRefreshStart(new Date(lastRefreshTime.getTime()));
            }
            feedDefinition.setMarmotScript(rs.getString(i++));
            feedDefinition.setConcreteFieldsEditable(rs.getBoolean(i++));
            feedDefinition.setRefreshMarmotScript(rs.getString(i));
            long folderStartTime = System.currentTimeMillis();
            feedDefinition.setFolders(getFolders(feedDefinition.getDataFeedID(), feedDefinition.getFields(), conn));
            System.out.println("folder time = " + (System.currentTimeMillis() - folderStartTime));
            feedDefinition.setDataSourceBehavior(feedDefinition.getDataSourceType());
            feedDefinition.customLoad(conn);
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

    private DataSourceDescriptor createDescriptor(long dataFeedID, String feedName, Integer userRole,
                                                  long size, int feedType, Date lastDataTime, Date creationDate, String owner, boolean accountVisible, String urlKey,
                                                  int dataSourceBehavior) throws SQLException {
        DataSourceDescriptor dataSourceDescriptor = new DataSourceDescriptor(feedName, dataFeedID, feedType, accountVisible, dataSourceBehavior);
        dataSourceDescriptor.setSize(size);
        dataSourceDescriptor.setLastDataTime(lastDataTime);
        dataSourceDescriptor.setRole(userRole);
        dataSourceDescriptor.setCreationDate(creationDate);
        dataSourceDescriptor.setAuthor(owner);
        dataSourceDescriptor.setUrlKey(urlKey);
        return dataSourceDescriptor;
    }

    public DataSourceDescriptor getFeedDescriptor(long feedID) throws SQLException {
        DataSourceDescriptor feedDescriptor = null;
        Connection conn = Database.instance().getConnection();
        long userID = SecurityUtil.getUserID();
        try {
            StringBuilder queryBuilder = new StringBuilder("SELECT FEED_NAME, FEED_TYPE, ROLE, API_KEY, refresh_behavior " +
                    "FROM DATA_FEED LEFT JOIN UPLOAD_POLICY_USERS ON DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND UPLOAD_POLICY_USERS.USER_ID = ?" +
                    " WHERE DATA_FEED.DATA_FEED_ID = ?");
            PreparedStatement queryStmt = conn.prepareStatement(queryBuilder.toString());
            queryStmt.setLong(1, userID);
            queryStmt.setLong(2, feedID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                String feedName = rs.getString(1);
                int feedType = rs.getInt(2);
                int role = rs.getInt(3);
                if (rs.wasNull()) {
                    role = Roles.NONE;
                }
                feedDescriptor = createDescriptor(feedID, feedName, role, 0, feedType, null, null, null, false, rs.getString(4), rs.getInt(5));
            }
            queryStmt.close();
        } finally {
            Database.closeConnection(conn);
        }
        return feedDescriptor;
    }

    public List<DataSourceDescriptor> getExistingHiddenChildren(long userID, long dataSourceID) throws SQLException {
        List<DataSourceDescriptor> descriptorList = new ArrayList<DataSourceDescriptor>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID, DATA_FEED.FEED_NAME, " +
                    "DATA_FEED.FEED_TYPE, ROLE, API_KEY, refresh_behavior " +
                    " FROM UPLOAD_POLICY_USERS, DATA_FEED WHERE " +
                    "UPLOAD_POLICY_USERS.USER_ID = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND DATA_FEED.PARENT_SOURCE_ID = ?");
            queryStmt.setLong(1, userID);
            queryStmt.setLong(2, dataSourceID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long dataFeedID = rs.getLong(1);
                String feedName = rs.getString(2);
                int feedType = rs.getInt(3);
                int userRole = rs.getInt(4);

                DataSourceDescriptor feedDescriptor = createDescriptor(dataFeedID, feedName, userRole, 0, feedType, null, null, null, false, rs.getString(5), rs.getInt(6));
                descriptorList.add(feedDescriptor);
            }
            queryStmt.close();
        } finally {
            Database.closeConnection(conn);
        }
        return descriptorList;
    }

    public List<DataSourceDescriptor> getDataSources(long userID, long accountID) throws SQLException {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            List<DataSourceDescriptor> dataSources = getDataSources(userID, accountID, conn);
            conn.commit();
            return dataSources;
        } catch (SQLException se) {
            conn.rollback();
            throw se;
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }

    }

    public List<DataSourceDescriptor> getDataSourcesForGroup(long userID, long groupID, EIConnection conn) throws SQLException {
        List<DataSourceDescriptor> dataSources = new ArrayList<DataSourceDescriptor>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID, DATA_FEED.FEED_NAME, " +
                "FEED_PERSISTENCE_METADATA.SIZE, DATA_FEED.FEED_TYPE, FEED_PERSISTENCE_METADATA.LAST_DATA_TIME, DATA_FEED.account_visible, DATA_FEED.api_key, refresh_behavior " +
                " FROM (upload_policy_groups, DATA_FEED LEFT JOIN FEED_PERSISTENCE_METADATA ON DATA_FEED.DATA_FEED_ID = FEED_PERSISTENCE_METADATA.FEED_ID) WHERE " +
                "upload_policy_groups.group_id = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_GROUPS.FEED_ID AND DATA_FEED.VISIBLE = ?");
        PreparedStatement childQueryStmt = conn.prepareStatement("SELECT FEED_PERSISTENCE_METADATA.SIZE FROM FEED_PERSISTENCE_METADATA, DATA_FEED WHERE FEED_PERSISTENCE_METADATA.FEED_ID = DATA_FEED.DATA_FEED_ID AND DATA_FEED.PARENT_SOURCE_ID = ?");
        queryStmt.setLong(1, groupID);
        queryStmt.setBoolean(2, true);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            long dataFeedID = rs.getLong(1);
            String feedName = rs.getString(2);
            long feedSize = rs.getLong(3);
            int feedType = rs.getInt(4);
            Timestamp lastTime = rs.getTimestamp(5);
            Date lastDataTime = null;
            if (lastTime != null) {
                lastDataTime = new Date(lastTime.getTime());
            }
            long size = feedSize;
            childQueryStmt.setLong(1, dataFeedID);
            ResultSet childRS = childQueryStmt.executeQuery();
            while (childRS.next()) {
                size += childRS.getLong(1);
            }
            DataSourceDescriptor feedDescriptor = createDescriptor(dataFeedID, feedName, Roles.SUBSCRIBER, size, feedType, lastDataTime, null, null, rs.getBoolean(6), rs.getString(7),
                    rs.getInt(8));
            dataSources.add(feedDescriptor);
        }
        queryStmt.close();
        childQueryStmt.close();
        PreparedStatement myRoleStmt = conn.prepareStatement("SELECT ROLE FROM upload_policy_users where user_id = ? and feed_id = ?");
        for (DataSourceDescriptor dataSource : dataSources) {
            myRoleStmt.setLong(1, userID);
            myRoleStmt.setLong(2, dataSource.getId());
            ResultSet roleRS = myRoleStmt.executeQuery();
            if (roleRS.next()) {
                int role = roleRS.getInt(1);
                dataSource.setRole(role);
            }
        }
        myRoleStmt.close();
        populateChildInformation(conn, dataSources);
        return dataSources;
    }

    public List<DataSourceDescriptor> getDataSources(long userID, long accountID, EIConnection conn) throws SQLException {
        RolePrioritySet<DataSourceDescriptor> descriptorList = new RolePrioritySet<DataSourceDescriptor>();
        getMyDataSources(userID, conn, descriptorList);
        getAccountDataSources(conn, accountID, descriptorList);
        getGroupDataSources(userID, conn, descriptorList);
        List<DataSourceDescriptor> dataSources = new ArrayList<DataSourceDescriptor>();
        for (EIDescriptor dataSource : descriptorList.values()) {
            dataSources.add((DataSourceDescriptor) dataSource);
        }
        populateChildInformation(conn, dataSources);
        // 415-536-7285
        return dataSources;
    }

    private void getMyDataSources(long userID, EIConnection conn, RolePrioritySet<DataSourceDescriptor> descriptorList) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID, DATA_FEED.FEED_NAME, " +
                "FEED_PERSISTENCE_METADATA.SIZE, DATA_FEED.FEED_TYPE, ROLE, FEED_PERSISTENCE_METADATA.LAST_DATA_TIME, DATA_FEED.create_date, DATA_FEED.account_visible, DATA_FEED.api_key, refresh_behavior" +
                " FROM (UPLOAD_POLICY_USERS, DATA_FEED LEFT JOIN FEED_PERSISTENCE_METADATA ON DATA_FEED.DATA_FEED_ID = FEED_PERSISTENCE_METADATA.FEED_ID) WHERE " +
                "UPLOAD_POLICY_USERS.USER_ID = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND DATA_FEED.VISIBLE = ?");
        PreparedStatement findOwnerStmt = conn.prepareStatement("SELECT FIRST_NAME, NAME FROM USER, UPLOAD_POLICY_USERS WHERE UPLOAD_POLICY_USERS.USER_ID = USER.USER_ID AND " +
                "UPLOAD_POLICY_USERS.FEED_ID = ?");
        queryStmt.setLong(1, userID);
        queryStmt.setBoolean(2, true);

        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            long dataFeedID = rs.getLong(1);
            String feedName = rs.getString(2);
            long feedSize = rs.getLong(3);
            int feedType = rs.getInt(4);
            int userRole = rs.getInt(5);
            Timestamp lastTime = rs.getTimestamp(6);
            Timestamp createDate = rs.getTimestamp(7);
            findOwnerStmt.setLong(1, dataFeedID);
            ResultSet ownerRS = findOwnerStmt.executeQuery();
            String name;
            if (ownerRS.next()) {
                String firstName = ownerRS.getString(1);
                String lastName = ownerRS.getString(2);
                name = firstName != null ? firstName + " " + lastName : lastName;
            } else {
                name = "";
            }
            Date lastDataTime = null;
            if (lastTime != null) {
                lastDataTime = new Date(lastTime.getTime());
            }
            Date creationDate = null;
            if (createDate != null) {
                creationDate = new Date(createDate.getTime());
            }

            DataSourceDescriptor feedDescriptor = createDescriptor(dataFeedID, feedName, userRole, feedSize, feedType, lastDataTime, creationDate, name, rs.getBoolean(8), rs.getString(9), rs.getInt(10));
            descriptorList.add(feedDescriptor);
        }
        queryStmt.close();
        findOwnerStmt.close();
    }

    private void getAccountDataSources(EIConnection conn, long accountID, RolePrioritySet<DataSourceDescriptor> descriptorList) throws SQLException {

        PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID, DATA_FEED.FEED_NAME, " +
                "FEED_PERSISTENCE_METADATA.SIZE, DATA_FEED.FEED_TYPE, FEED_PERSISTENCE_METADATA.LAST_DATA_TIME, DATA_FEED.create_date, DATA_FEED.account_visible, DATA_FEED.api_key, refresh_behavior " +
                " FROM (upload_policy_users, USER, DATA_FEED LEFT JOIN FEED_PERSISTENCE_METADATA ON DATA_FEED.DATA_FEED_ID = FEED_PERSISTENCE_METADATA.FEED_ID) WHERE " +
                "upload_policy_users.user_id = user.user_id AND user.account_id = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND DATA_FEED.account_visible = ? AND data_feed.visible = ?");
        PreparedStatement findOwnerStmt = conn.prepareStatement("SELECT FIRST_NAME, NAME FROM USER, UPLOAD_POLICY_USERS WHERE UPLOAD_POLICY_USERS.USER_ID = USER.USER_ID AND " +
                "UPLOAD_POLICY_USERS.FEED_ID = ?");
        queryStmt.setLong(1, accountID);
        queryStmt.setBoolean(2, true);
        queryStmt.setBoolean(3, true);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            long dataFeedID = rs.getLong(1);
            String feedName = rs.getString(2);
            long feedSize = rs.getLong(3);
            int feedType = rs.getInt(4);
            Timestamp lastTime = rs.getTimestamp(5);
            Timestamp createDate = rs.getTimestamp(6);
            findOwnerStmt.setLong(1, dataFeedID);
            ResultSet ownerRS = findOwnerStmt.executeQuery();
            String name;
            if (ownerRS.next()) {
                String firstName = ownerRS.getString(1);
                String lastName = ownerRS.getString(2);
                name = firstName != null ? firstName + " " + lastName : lastName;
            } else {
                name = "";
            }
            Date lastDataTime = null;
            if (lastTime != null) {
                lastDataTime = new Date(lastTime.getTime());
            }
            Date creationDate = null;
            if (createDate != null) {
                creationDate = new Date(createDate.getTime());
            }
            DataSourceDescriptor feedDescriptor = createDescriptor(dataFeedID, feedName, Roles.SHARER, feedSize, feedType, lastDataTime, creationDate, name, rs.getBoolean(7), rs.getString(8), rs.getInt(9));
            descriptorList.add(feedDescriptor);
        }
        queryStmt.close();
        findOwnerStmt.close();
    }

    private void getGroupDataSources(long userID, EIConnection conn, RolePrioritySet<DataSourceDescriptor> descriptorList) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID, DATA_FEED.FEED_NAME, " +
                "FEED_PERSISTENCE_METADATA.SIZE, DATA_FEED.FEED_TYPE, FEED_PERSISTENCE_METADATA.LAST_DATA_TIME, group_to_user_join.binding_type, DATA_FEED.create_date, DATA_FEED.account_visible, DATA_FEED.api_key, refresh_behavior " +
                " FROM (upload_policy_groups, group_to_user_join, DATA_FEED LEFT JOIN FEED_PERSISTENCE_METADATA ON DATA_FEED.DATA_FEED_ID = FEED_PERSISTENCE_METADATA.FEED_ID) LEFT JOIN PASSWORD_STORAGE ON DATA_FEED.DATA_FEED_ID = PASSWORD_STORAGE.DATA_FEED_ID WHERE " +
                "upload_policy_groups.group_id = group_to_user_join.group_id AND GROUP_TO_USER_JOIN.USER_ID = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_GROUPS.FEED_ID AND DATA_FEED.VISIBLE = ?");
        PreparedStatement findOwnerStmt = conn.prepareStatement("SELECT FIRST_NAME, NAME FROM USER, UPLOAD_POLICY_USERS WHERE UPLOAD_POLICY_USERS.USER_ID = USER.USER_ID AND " +
                "UPLOAD_POLICY_USERS.FEED_ID = ?");
        queryStmt.setLong(1, userID);
        queryStmt.setBoolean(2, true);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            long dataFeedID = rs.getLong(1);
            String feedName = rs.getString(2);
            long feedSize = rs.getLong(3);
            int feedType = rs.getInt(4);
            Timestamp lastTime = rs.getTimestamp(5);
            Timestamp createDate = rs.getTimestamp(7);
            findOwnerStmt.setLong(1, dataFeedID);
            ResultSet ownerRS = findOwnerStmt.executeQuery();
            String name;
            if (ownerRS.next()) {
                String firstName = ownerRS.getString(1);
                String lastName = ownerRS.getString(2);
                name = firstName != null ? firstName + " " + lastName : lastName;
            } else {
                name = "";
            }
            Date lastDataTime = null;
            if (lastTime != null) {
                lastDataTime = new Date(lastTime.getTime());
            }
            Date creationDate = null;
            if (createDate != null) {
                creationDate = new Date(createDate.getTime());
            }
            DataSourceDescriptor feedDescriptor = createDescriptor(dataFeedID, feedName, rs.getInt(6), feedSize, feedType, lastDataTime, creationDate, name, rs.getBoolean(8), rs.getString(9), rs.getInt(10));
            descriptorList.add(feedDescriptor);
        }
        queryStmt.close();
        findOwnerStmt.close();
    }

    public long dataSourceIDForDataSource(String urlKey) throws SQLException {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT DATA_FEED_ID FROM DATA_FEED WHERE API_KEY = ?");
            stmt.setString(1, urlKey);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getLong(1);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public DataSourceDescriptor dataSourceURLKeyForDataSource(long dataSourceID) throws SQLException {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT API_KEY, FEED_NAME, refresh_behavior FROM DATA_FEED WHERE DATA_FEED_ID = ?");
            stmt.setLong(1, dataSourceID);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            String urlKey = rs.getString(1);
            String name = rs.getString(2);
            DataSourceDescriptor dsd = new DataSourceDescriptor(name, dataSourceID, 0, true, rs.getInt(3));
            dsd.setUrlKey(urlKey);
            return dsd;
        } finally {
            Database.closeConnection(conn);
        }
    }


    private void populateChildInformation(EIConnection conn, Collection<DataSourceDescriptor> descriptorList) throws SQLException {
        /*PreparedStatement versionStmt = conn.prepareStatement("SELECT MAX(VERSION) FROM FEED_PERSISTENCE_METADATA WHERE " +
                    "FEED_ID = ?");*/
        PreparedStatement childQueryStmt = conn.prepareStatement("SELECT FEED_PERSISTENCE_METADATA.SIZE, feed_persistence_metadata.last_data_time " +
                "FROM FEED_PERSISTENCE_METADATA, DATA_FEED WHERE FEED_PERSISTENCE_METADATA.FEED_ID = DATA_FEED.DATA_FEED_ID AND " +
                "DATA_FEED.PARENT_SOURCE_ID = ?");
        for (DataSourceDescriptor dataSource : descriptorList) {
            /*versionStmt.setLong(1, dataSource.getId());
            ResultSet versionRS = versionStmt.executeQuery();
            if (versionRS.next()) {*/
            //int version = versionRS.getInt(1);
            childQueryStmt.setLong(1, dataSource.getId());
            //childQueryStmt.setInt(2, version);
            Date lastDataTime = dataSource.getLastDataTime();
            long size = dataSource.getSize();
            ResultSet childRS = childQueryStmt.executeQuery();
            while (childRS.next()) {
                size += childRS.getLong(1);
                Timestamp childLastTime = childRS.getTimestamp(2);
                if (childLastTime != null) {
                    if (lastDataTime == null) {
                        lastDataTime = new Date(childLastTime.getTime());
                    } else if (childLastTime.getTime() > lastDataTime.getTime()) {
                        lastDataTime = new Date(childLastTime.getTime());
                    }
                }
            }
            dataSource.setSize(size);
            dataSource.setLastDataTime(lastDataTime);
            //}
        }
        //versionStmt.close();
        childQueryStmt.close();
    }

    private UploadPolicy createUploadPolicy(Connection conn, long feedID, boolean publiclyVisible, boolean marketplaceVisible) throws SQLException {
        UploadPolicy uploadPolicy = new UploadPolicy();
        uploadPolicy.setPubliclyVisible(publiclyVisible);
        uploadPolicy.setMarketplaceVisible(marketplaceVisible);
        List<FeedConsumer> owners = new ArrayList<FeedConsumer>();
        List<FeedConsumer> viewers = new ArrayList<FeedConsumer>();
        PreparedStatement policyUserStmt = conn.prepareStatement("SELECT USER.USER_ID, ROLE, USER.NAME, USER.USERNAME, USER.EMAIL, USER.ACCOUNT_ID, USER.FIRST_NAME FROM UPLOAD_POLICY_USERS, USER WHERE FEED_ID = ? AND " +
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
            String firstName = usersRS.getString(7);
            UserStub userStub = new UserStub(userID, userName, email, name, accountID, firstName);
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
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID" +
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
