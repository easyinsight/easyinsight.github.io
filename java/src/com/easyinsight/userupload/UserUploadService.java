package com.easyinsight.userupload;

import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.storage.StorageLimitException;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.file.FileBasedFeedDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.users.*;
import com.easyinsight.analysis.*;
import com.easyinsight.PasswordStorage;

import java.io.*;
import java.util.*;
import java.util.Date;
import java.sql.*;

import org.hibernate.Session;

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
        SecurityUtil.authorizeFeed(dataFeedID, Roles.SUBSCRIBER);
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
            feedDefinition.setOwnerName(retrieveUser(conn).getUserName());
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
                feedDefinition.setOwnerName(retrieveUser(conn).getUserName());
                UploadPolicy uploadPolicy = new UploadPolicy(rawUploadData.accountID);
                feedDefinition.setUploadPolicy(uploadPolicy);
                feedDefinition.setFields(fields);
                FeedCreationResult result = new FeedCreation().createFeed(feedDefinition, conn, dataSet.toDataSet(), rawUploadData.accountID);
                tableDef = result.getTableDefinitionMetadata();
                uploadResponse = new UploadResponse(result.getFeedID(), feedDefinition.getAnalysisDefinitionID());
                tableDef.commit();
                conn.commit();
            }
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
        new UserUploadInternalService().createUserFeedLink(rawUploadData.accountID, feedDefinition.getDataFeedID(), Roles.OWNER);
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
        SecurityUtil.authorizeInsight(definition.getAnalysisDefinitionID());
        try {
            long feedID = feedStorage.addFeedDefinitionData(definition);
            new UserUploadInternalService().createUserFeedLink(userID, feedID, Roles.OWNER);
            return feedID;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void subscribe(long dataFeedID) {
        SecurityUtil.authorizeFeed(dataFeedID, Roles.SUBSCRIBER);
        long userID = SecurityUtil.getUserID();
        try {
            new UserUploadInternalService().createUserFeedLink(userID, dataFeedID, Roles.SUBSCRIBER);
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

    public CredentialsResponse refreshData(long feedID, Credentials credentials, boolean saveCredentials) {
        SecurityUtil.authorizeFeed(feedID, Roles.OWNER);
        try {
            ServerDataSourceDefinition feedDefinition = (ServerDataSourceDefinition) getDataFeedConfiguration(feedID);
            if(saveCredentials) {
                EIConnection conn = Database.instance().getConnection();
                try {
                    PasswordStorage.setPasswordCredentials(credentials.getUserName(), credentials.getPassword(), feedID, conn);
                }
                finally {
                    Database.instance().closeConnection(conn);
                }
            }
            return feedDefinition.refreshData(credentials, SecurityUtil.getAccountID(), new Date());
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
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
            DataSet dataSet = feedDefinition.getDataSet(credentials, keys, new Date());
            feedDefinition.setFields(feedDefinition.createAnalysisItems(keys, dataSet));
            feedDefinition.setOwnerName(retrieveUser(conn).getUserName());
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

    private User retrieveUser(Connection conn) {
        long userID = SecurityUtil.getUserID();
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
}
