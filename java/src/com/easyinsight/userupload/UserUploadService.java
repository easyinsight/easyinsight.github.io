package com.easyinsight.userupload;

import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.storage.StorageLimitException;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.file.FileBasedFeedDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.users.*;
import com.easyinsight.analysis.*;

import java.io.*;
import java.util.*;
import java.sql.*;

import flex.messaging.MessageBroker;
import flex.messaging.messages.AsyncMessage;
import flex.messaging.util.UUIDUtils;

/**
 * User: James Boe
 * Date: Jan 26, 2008
 * Time: 9:17:37 PM
 */
public class UserUploadService implements IUserUploadService {


    private FeedStorage feedStorage = new FeedStorage();
    private Map<Long, RawUploadData> rawDataMap = new WeakHashMap<Long, RawUploadData>();

    public UserUploadService() {
    }

    public List<Object> getFeedAnalysisTree() {
        long userID = SecurityUtil.getUserID();
        try {
            List<Object> objects = new ArrayList<Object>();
            List<FeedDescriptor> descriptors = feedStorage.searchForSubscribedFeeds(userID);
            objects.addAll(descriptors);
            AnalysisStorage analysisStorage = new AnalysisStorage();
            Map<Long, List<InsightDescriptor>> analysisDefinitions = new HashMap<Long, List<InsightDescriptor>>();
            for (InsightDescriptor analysisDefinition : analysisStorage.getInsightDescriptors(userID)) {
                List<InsightDescriptor> defList = analysisDefinitions.get(analysisDefinition.getDataFeedID());
                if (defList == null) {
                    defList = new ArrayList<InsightDescriptor>();
                    analysisDefinitions.put(analysisDefinition.getDataFeedID(), defList);
                }
                defList.add(analysisDefinition);
            }
            for (FeedDescriptor feedDescriptor : descriptors) {
                List<InsightDescriptor> analysisDefList = analysisDefinitions.remove(feedDescriptor.getDataFeedID());
                if (analysisDefList == null) {
                    analysisDefList = new ArrayList<InsightDescriptor>();
                }
                feedDescriptor.setChildren(analysisDefList);
            }
            for (List<InsightDescriptor> defList : analysisDefinitions.values()) {
                objects.addAll(defList);
            }
            return objects;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }    

    public FeedDefinition getDataFeedConfiguration(long dataFeedID) {
        try {
            //SecurityUtil.authorizeFeed(dataFeedID, Roles.OWNER);
            return feedStorage.getFeedDefinitionData(dataFeedID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void updateFeedDefinition(FeedDefinition feedDefinition) {
        try {
            SecurityUtil.authorizeFeed(feedDefinition.getDataFeedID(), Roles.OWNER);
            feedStorage.updateDataFeedConfiguration(feedDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public long addRawUploadData(long userID, String fileName, byte[] rawData) {

        // get size of data on that user
        Connection conn = Database.instance().getConnection();
        try {
            long uploadID;
            PreparedStatement anythingExistingStmt = conn.prepareStatement("SELECT USER_UPLOAD_ID FROM USER_UPLOAD WHERE " +
                    "ACCOUNT_ID = ? AND DATA_NAME = ?");
            anythingExistingStmt.setLong(1, userID);
            anythingExistingStmt.setString(2, fileName);
            ResultSet existingRS = anythingExistingStmt.executeQuery();
            if (existingRS.next()) {
                uploadID = existingRS.getLong(1);
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE USER_UPLOAD SET USER_DATA = ? WHERE " +
                        "USER_UPLOAD_ID = ?");
                ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
                updateStmt.setBinaryStream(1, bais, rawData.length);
                updateStmt.setLong(2, uploadID);
                updateStmt.executeUpdate();
            } else {
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO USER_UPLOAD (ACCOUNT_ID, DATA_NAME, " +
                        "USER_DATA) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                insertStmt.setLong(1, userID);
                insertStmt.setString(2, fileName);
                ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
                insertStmt.setBinaryStream(3, bais, rawData.length);
                insertStmt.execute();
                uploadID = Database.instance().getAutoGenKey(insertStmt);
            }
            rawDataMap.put(uploadID, new RawUploadData(userID, fileName, rawData));
            return uploadID;
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    public long createNewDefaultFeed(String name) {
        Connection conn = Database.instance().getConnection();
        DataStorage tableDef = null;
        try {
            conn.setAutoCommit(false);
            FeedDefinition feedDefinition = new FeedDefinition();
            feedDefinition.setFeedName(name);
            feedDefinition.setOwnerName(new UserService().retrieveUser(conn).getUserName());
            UploadPolicy uploadPolicy = new UploadPolicy(SecurityUtil.getUserID());
            feedDefinition.setUploadPolicy(uploadPolicy);
            feedDefinition.setFields(new ArrayList<AnalysisItem>());
            FeedCreationResult result = new FeedCreation().createFeed(feedDefinition, conn, new DataSet(), SecurityUtil.getUserID());
            tableDef = result.getTableDefinitionMetadata();
            tableDef.commit();
            conn.commit();
            return result.getFeedID();
        } catch (Exception e) {
            LogClass.error(e);
            if (tableDef != null) {
                tableDef.rollback();
            }
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        } finally {
            if (tableDef != null) {
                tableDef.closeConnection();
            }
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
    }

    public UploadResponse create(long uploadID, String name) {
        UploadResponse uploadResponse;
        Connection conn = Database.instance().getConnection();
        DataStorage tableDef = null;
        try {
            conn.setAutoCommit(false);
            RawUploadData rawUploadData = retrieveRawData(uploadID);
            UploadFormat uploadFormat = new UploadFormatTester().determineFormat(rawUploadData.userData);
            if (uploadFormat == null) {
                uploadResponse = new UploadResponse("Sorry, we couldn't figure out what type of file you tried to upload. Supported types are Excel 1997-2003 and delimited text files.");
            } else {
                UserUploadAnalysis userUploadAnalysis = uploadFormat.analyze(uploadID, rawUploadData.userData);
                List<AnalysisItem> fields = userUploadAnalysis.getFields();
                PersistableDataSetForm dataSet = UploadAnalysisCache.instance().getDataSet(uploadID);
                if (dataSet == null) {
                    dataSet = uploadFormat.createDataSet(rawUploadData.userData, fields);
                }
                for (AnalysisItem field : fields) {
                    dataSet.refreshKey(field.getKey());
                }
                
                FileBasedFeedDefinition feedDefinition = new FileBasedFeedDefinition();
                feedDefinition.setUploadFormat(uploadFormat);
                feedDefinition.setFeedName(name);
                feedDefinition.setOwnerName(new UserService().retrieveUser(conn).getUserName());
                UploadPolicy uploadPolicy = new UploadPolicy(rawUploadData.accountID);
                feedDefinition.setUploadPolicy(uploadPolicy);
                feedDefinition.setFields(fields);
                FeedCreationResult result = new FeedCreation().createFeed(feedDefinition, conn, dataSet.toDataSet(), rawUploadData.accountID);
                tableDef = result.getTableDefinitionMetadata();
                uploadResponse = new UploadResponse(result.getFeedID(), feedDefinition.getAnalysisDefinitionID());
                tableDef.commit();                
            }
            tableDef.commit();
            conn.commit();
        } catch (StorageLimitException se) {
            if (tableDef != null) {
                tableDef.rollback();
            }
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            uploadResponse = new UploadResponse("You have reached your account storage limit.");
        } catch (Exception e) {
            LogClass.error(e);
            if (tableDef != null) {
                tableDef.rollback();
            }
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            uploadResponse = new UploadResponse("Something caused an internal error in the processing of the uploaded file.");
        } finally {
            if (tableDef != null) {
                tableDef.closeConnection();
            }
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
        return uploadResponse;
    }

    private FeedDefinition createFeedFromUpload(long uploadID, UploadFormat uploadFormat, String feedName, String genre,
                       List<AnalysisItem> fields, UploadPolicy uploadPolicy, TagCloud tagCloud) {
        RawUploadData rawUploadData = retrieveRawData(uploadID);
        FileBasedFeedDefinition feedDefinition = new FileBasedFeedDefinition();
        feedDefinition.setUploadFormat(uploadFormat);
        feedDefinition.setFeedName(feedName);
        feedDefinition.setGenre(genre);
        feedDefinition.setUploadPolicy(uploadPolicy);
        feedDefinition.setFields(fields);
        feedStorage.addFeedDefinitionData(feedDefinition);
        createUserFeedLink(rawUploadData.accountID, feedDefinition.getDataFeedID(), Roles.OWNER);
        return feedDefinition;
    }
    
    private RawUploadData retrieveRawData(long uploadID) {
        RawUploadData rawUploadData = rawDataMap.get(uploadID);
        if (rawUploadData == null) {
            Connection conn = Database.instance().getConnection();
            try {
                PreparedStatement rawDataStmt = conn.prepareStatement("SELECT ACCOUNT_ID, DATA_NAME, USER_DATA FROM " +
                        "USER_UPLOAD WHERE USER_UPLOAD_ID = ?");
                rawDataStmt.setLong(1, uploadID);
                ResultSet dataRS = rawDataStmt.executeQuery();
                if (dataRS.next()) {
                    long accountID = dataRS.getLong(1);
                    String dataName = dataRS.getString(2);
                    byte[] userData = dataRS.getBytes(3);
                    rawUploadData = new RawUploadData(accountID, dataName, userData);
                } else {
                    throw new RuntimeException("Couldn't find upload info");
                }
            } catch (SQLException e) {
                LogClass.error(e);
                throw new RuntimeException(e);
            } finally {
                Database.instance().closeConnection(conn);
            }
        }
        return rawUploadData;
    }

    private static class RawUploadData {

        private RawUploadData(long accountID, String dataName, byte[] userData) {
            this.accountID = accountID;
            this.dataName = dataName;
            this.userData = userData;
        }

        long accountID;
        String dataName;
        byte[] userData;
    }

    public void deleteUserUpload(long dataFeedID) {
        Connection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            int role = SecurityUtil.getUserRoleToFeed(dataFeedID);
            if (role == Roles.OWNER) {
                DataStorage.delete(dataFeedID, conn);
                PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM DATA_FEED WHERE DATA_FEED_ID = ?");
                deleteStmt.setLong(1, dataFeedID);
                deleteStmt.executeUpdate();
            } else if (role == Roles.SUBSCRIBER) {
                deleteUserFeedLink(SecurityUtil.getUserID(), dataFeedID, conn);
            } else {
                throw new SecurityException();
            }
            conn.commit();
        } catch (SQLException e) {
            LogClass.error(e);
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
            Database.instance().closeConnection(conn);
        }
    }

    public List<FeedDescriptor> getOwnedFeeds() {
        long userID = SecurityUtil.getUserID();
        try {
            return feedStorage.searchForSubscribedFeeds(userID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public long asyncParse(long uploadID, UploadFormat uploadFormat, String feedName, String genre,
                       List<AnalysisItem> fields, UploadPolicy uploadPolicy, TagCloud tagCloud) {
        RawUploadData rawUploadData = retrieveRawData(uploadID);
        FeedDefinition feedDefinition = createFeedFromUpload(uploadID, uploadFormat, feedName, genre, fields, uploadPolicy, tagCloud);
        PersistableDataSetForm dataSet = UploadAnalysisCache.instance().getDataSet(uploadID);
        if (dataSet == null) {
            dataSet = uploadFormat.createDataSet(rawUploadData.userData, fields);
        }
        for (AnalysisItem field : fields) {
            dataSet.refreshKey(field.getKey());
        }
        AsyncPersistanceManager.instance().addAsyncPersistence(new AsyncPersistence(dataSet, feedDefinition.getDataFeedID()));
        return feedDefinition.getDataFeedID();
    }

    public long createAnalysisBasedFeed(AnalysisBasedFeedDefinition definition) {
        long userID = SecurityUtil.getUserID();
        try {
            long feedID = feedStorage.addFeedDefinitionData(definition);
            createUserFeedLink(userID, feedID, Roles.OWNER);
            return feedID;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void subscribe(long dataFeedID) {
        long userID = SecurityUtil.getUserID();
        try {
            createUserFeedLink(userID, dataFeedID, Roles.SUBSCRIBER);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public int getRole(long userID, long feedID) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT USER_ROLE FROM USER_TO_FEED WHERE " +
                    "USER_ID = ? AND DATA_FEED_ID = ?");
            existingLinkQuery.setLong(1, userID);
            existingLinkQuery.setLong(2, feedID);
            ResultSet rs = existingLinkQuery.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new RuntimeException();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    public void createUserFeedLink(long userID, long dataFeedID, int owner, Connection conn) {
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT UPLOAD_POLICY_USERS_ID FROM UPLOAD_POLICY_USERS WHERE " +
                    "USER_ID = ? AND FEED_ID = ?");
            existingLinkQuery.setLong(1, userID);
            existingLinkQuery.setLong(2, dataFeedID);
            ResultSet existingRS = existingLinkQuery.executeQuery();
            if (existingRS.next()) {
                long existingID = existingRS.getLong(1);
                PreparedStatement updateLinkStmt = conn.prepareStatement("UPDATE UPLOAD_POLICY_USERS SET ROLE = ? WHERE " +
                        "UPLOAD_POLICY_USERS_ID = ?");
                updateLinkStmt.setLong(1, owner);
                updateLinkStmt.setLong(2, existingID);
                updateLinkStmt.executeUpdate();
            } else {
                PreparedStatement insertFeedStmt = conn.prepareStatement("INSERT INTO UPLOAD_POLICY_USERS (USER_ID, FEED_ID, ROLE) " +
                        "VALUES (?, ?, ?)");
                insertFeedStmt.setLong(1, userID);
                insertFeedStmt.setLong(2, dataFeedID);
                insertFeedStmt.setLong(3, owner);
                insertFeedStmt.execute();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void createUserFeedLink(long userID, long dataFeedID, int owner) {
        Connection conn = Database.instance().getConnection();
        try {
            createUserFeedLink(userID, dataFeedID, owner, conn);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    private void deleteUserFeedLink(long accountID, long feedID, Connection conn) throws SQLException {
        PreparedStatement existingLinkQuery = conn.prepareStatement("DELETE FROM UPLOAD_POLICY_USERS WHERE " +
                "USER_ID = ? AND FEED_ID = ?");
        existingLinkQuery.setLong(1, accountID);
        existingLinkQuery.setLong(2, feedID);
        existingLinkQuery.executeUpdate();
    }

    public int getCredentialsForFeed(long feedID) {
        SecurityUtil.authorizeFeed(feedID, Roles.OWNER);
        try {
            FeedDefinition feedDefinition = getDataFeedConfiguration(feedID);
            return feedDefinition.getCredentialsDefinition();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public CredentialsResponse refreshData(long feedID, Credentials credentials) {
        SecurityUtil.authorizeFeed(feedID, Roles.OWNER);
        Connection conn = Database.instance().getConnection();
        DataStorage dataStorage = null;
        try {
            conn.setAutoCommit(false);
            FeedDefinition feedDefinition = getDataFeedConfiguration(feedID);
            DataSet dataSet = feedDefinition.getDataSet(credentials, feedDefinition.newDataSourceFields(credentials));
            dataStorage = DataStorage.writeConnection(feedDefinition, conn);
            dataStorage.truncate();
            dataStorage.insertData(dataSet);
            dataStorage.commit();
            conn.commit();
            MessageBroker msgBroker = MessageBroker.getMessageBroker(null);
            String clientID = UUIDUtils.createUUID();
            AsyncMessage msg = new AsyncMessage();
            msg.setDestination("dataUpdates");
            msg.setHeader(AsyncMessage.SUBTOPIC_HEADER_NAME, String.valueOf(feedID));
            msg.setMessageId(clientID);
            msg.setTimestamp(System.currentTimeMillis());
            msgBroker.routeMessageToService(msg, null);
            return new CredentialsResponse(true);
        } catch (Exception e) {
            LogClass.error(e);
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            return new CredentialsResponse(false, e.getMessage());
        } finally {
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
            dataStorage.closeConnection();
            Database.instance().closeConnection(conn);
        }
    }

    public void updateData(long feedID, long rawDataID, boolean update) {
        SecurityUtil.authorizeFeed(feedID, Roles.OWNER);
        Connection conn = Database.instance().getConnection();
        DataStorage metadata = null;
        try {
            conn.setAutoCommit(false);
            RawUploadData rawUploadData = retrieveRawData(rawDataID);
            FileBasedFeedDefinition feedDefinition = (FileBasedFeedDefinition) getDataFeedConfiguration(feedID);
            metadata = DataStorage.writeConnection(feedDefinition, conn);
            PersistableDataSetForm form = feedDefinition.getUploadFormat().createDataSet(rawUploadData.userData, feedDefinition.getFields());
            if (update) {
                //DataRetrievalManager.instance().storeData(feedID, form);
                metadata.truncate();
                metadata.insertData(form.toDataSet());
            } else {
                //DataRetrievalManager.instance().appendData(feedID, form);
                metadata.insertData(form.toDataSet());
            }
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

    public long uploadPNG(byte[] bytes) {
        try {
            long userID = SecurityUtil.getUserID();
            return new PNGExportOperation().write(bytes, userID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public FeedDescriptor getFeedDescriptor(long feedID) {
        SecurityUtil.authorizeFeed(feedID, Roles.OWNER);
        try {
            return feedStorage.getFeedDescriptor(SecurityUtil.getUserID(), feedID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public String validateCredentials(FeedDefinition feedDefinition, Credentials credentials) {
        try {
            return feedDefinition.validateCredentials(credentials);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public long newExternalDataSource(FeedDefinition feedDefinition, Credentials credentials) {
        if (SecurityUtil.getAccountTier() < feedDefinition.getRequiredAccountTier()) {
            throw new RuntimeException("You are not allowed to create data sources of this type with your account.");
        }
        long userID = SecurityUtil.getUserID();
        Connection conn = Database.instance().getConnection();
        DataStorage metadata = null;
        try {
            conn.setAutoCommit(false);
            Map<String, Key> keys = feedDefinition.newDataSourceFields(credentials);
            DataSet dataSet = feedDefinition.getDataSet(credentials, keys);
            feedDefinition.setFields(feedDefinition.createAnalysisItems(keys, dataSet));
            feedDefinition.setOwnerName(new UserService().retrieveUser(conn).getUserName());
            UploadPolicy uploadPolicy = new UploadPolicy(userID);
            feedDefinition.setUploadPolicy(uploadPolicy);
            FeedCreationResult feedCreationResult = new FeedCreation().createFeed(feedDefinition, conn, dataSet, userID);
            metadata = feedCreationResult.getTableDefinitionMetadata();
            metadata.commit();
            conn.commit();
            return feedCreationResult.getFeedID();
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
            Database.instance().closeConnection(conn);
        }
    }
}
