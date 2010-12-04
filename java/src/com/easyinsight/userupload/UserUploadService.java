package com.easyinsight.userupload;

import com.easyinsight.core.EIDescriptor;
import com.easyinsight.dashboard.DashboardService;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.etl.LookupTableDescriptor;
import com.easyinsight.goals.GoalStorage;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.storage.StorageLimitException;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.users.*;
import com.easyinsight.analysis.*;
import com.easyinsight.PasswordStorage;
import com.easyinsight.scheduler.*;
import com.easyinsight.solutions.SolutionInstallInfo;

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


    private static FeedStorage feedStorage = new FeedStorage();
    private static Map<Long, RawUploadData> rawDataMap = new WeakHashMap<Long, RawUploadData>();
    private static final long TEN_MEGABYTES = 10485760;

    public UserUploadService() {
    }

    public List<SolutionInstallInfo> copyDataSource(long dataSourceID, String newName, boolean copyData, boolean includeChildren) {
        SecurityUtil.authorizeFeed(dataSourceID, Roles.OWNER);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            FeedDefinition existingDef = feedStorage.getFeedDefinitionData(dataSourceID, conn);
            List<SolutionInstallInfo> results = DataSourceCopyUtils.installFeed(SecurityUtil.getUserID(), conn, copyData, dataSourceID, existingDef, includeChildren, newName, 0,
                    SecurityUtil.getAccountID(), SecurityUtil.getUserName());
            conn.commit();
            return results;
        } catch (Throwable e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public MyDataTree getFeedAnalysisTree(boolean includeGroups, boolean firstCall) {
        long userID = SecurityUtil.getUserID();
        try {
            List<Object> objects = new ArrayList<Object>();
            List<FeedDescriptor> descriptors = feedStorage.searchForSubscribedFeeds(userID);
            objects.addAll(new GoalStorage().getTreesForUser(userID));
            objects.addAll(new DashboardService().getDashboardsForUser(userID));
            Map<Long, FeedDescriptor> descriptorMap = new HashMap<Long, FeedDescriptor>();
            for (FeedDescriptor descriptor : descriptors) {
                descriptorMap.put(descriptor.getId(), descriptor);
            }
            if (includeGroups) {
                List<FeedDescriptor> groupDataSources = new ArrayList<FeedDescriptor>();
                groupDataSources.addAll(feedStorage.getDataSourcesFromGroups(userID));
                groupDataSources.addAll(feedStorage.getDataSourcesFromAccount(SecurityUtil.getAccountID()));
                for (FeedDescriptor groupDescriptor : groupDataSources) {
                    if (!descriptorMap.containsKey(groupDescriptor.getId())) {
                        descriptorMap.put(groupDescriptor.getId(), groupDescriptor);
                    }
                }
            }
            objects.addAll(descriptorMap.values());
            AnalysisStorage analysisStorage = new AnalysisStorage();
            Map<Long, List<EIDescriptor>> analysisDefinitions = new HashMap<Long, List<EIDescriptor>>();
            Set<InsightDescriptor> groupReports = new HashSet<InsightDescriptor>();
            if (includeGroups) {
                groupReports.addAll(analysisStorage.getReportsForGroups(userID));
                groupReports.addAll(analysisStorage.getReportsForAccount(SecurityUtil.getAccountID()));
            }

            for (InsightDescriptor analysisDefinition : analysisStorage.getInsightDescriptors(userID)) {
                if (includeGroups) {
                    groupReports.remove(analysisDefinition);
                }
                List<EIDescriptor> defList = analysisDefinitions.get(analysisDefinition.getDataFeedID());
                if (defList == null) {
                    defList = new ArrayList<EIDescriptor>();
                    analysisDefinitions.put(analysisDefinition.getDataFeedID(), defList);
                }
                defList.add(analysisDefinition);
            }

            if (includeGroups) {
                for (InsightDescriptor analysisDefinition : groupReports) {
                    List<EIDescriptor> defList = analysisDefinitions.get(analysisDefinition.getDataFeedID());
                    if (defList == null) {
                        defList = new ArrayList<EIDescriptor>();
                        analysisDefinitions.put(analysisDefinition.getDataFeedID(), defList);
                    }
                    defList.add(analysisDefinition);
                }
            }

            for (FeedDescriptor feedDescriptor : descriptorMap.values()) {
                List<EIDescriptor> analysisDefList = analysisDefinitions.remove(feedDescriptor.getId());
                if (analysisDefList == null) {
                    analysisDefList = new ArrayList<EIDescriptor>();
                }
                feedDescriptor.setChildren(new ArrayList<EIDescriptor>(new HashSet<EIDescriptor>(analysisDefList)));
            }
            for (LookupTableDescriptor lookupTableDescriptor : new FeedService().getLookupTableDescriptors()) {
                FeedDescriptor feedDescriptor = descriptorMap.get(lookupTableDescriptor.getDataSourceID());
                feedDescriptor.getChildren().add(lookupTableDescriptor);
            }
            for (List<EIDescriptor> defList : analysisDefinitions.values()) {
                objects.addAll(new ArrayList<EIDescriptor>(new HashSet<EIDescriptor>(defList)));
            }
            if (objects.isEmpty() && firstCall && !includeGroups) {
                objects = getFeedAnalysisTree(true, false).getObjects();
                return new MyDataTree(objects, true);
            }
            return new MyDataTree(objects, includeGroups);
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }    

    public FeedDefinition getDataFeedConfiguration(long dataFeedID) {
        SecurityUtil.authorizeFeed(dataFeedID, Roles.SUBSCRIBER);
        try {
            //SecurityUtil.authorizeFeed(dataFeedID, Roles.OWNER);
            return getFeedDefinition(dataFeedID);
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public static FeedDefinition getFeedDefinition(long dataFeedID) {
        try {
            return feedStorage.getFeedDefinitionData(dataFeedID);
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void updateFeedDefinition(FeedDefinition feedDefinition) {
        try {
            SecurityUtil.authorizeFeed(feedDefinition.getDataFeedID(), Roles.OWNER);
            feedStorage.updateDataFeedConfiguration(feedDefinition);
        } catch (Throwable e) {
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
            Database.closeConnection(conn);
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
            UploadPolicy uploadPolicy = new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID());
            feedDefinition.setUploadPolicy(uploadPolicy);
            feedDefinition.setFields(new ArrayList<AnalysisItem>());
            FeedCreationResult result = new FeedCreation().createFeed(feedDefinition, conn, new DataSet(), uploadPolicy);
            tableDef = result.getTableDefinitionMetadata();
            tableDef.commit();
            conn.commit();
            return result.getFeedID();
        } catch (Throwable e) {
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
            Database.closeConnection(conn);
        }
    }

    // three contexts here
    // excel/csv upload
    // google documents
    // unchecked API

    public UploadResponse analyzeUpload(UploadContext uploadContext) {
        UploadResponse uploadResponse;
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            
            String validation = uploadContext.validateUpload(conn);

            if (validation != null) {
                uploadResponse = new UploadResponse(validation);
            } else {
                List<AnalysisItem> fields = uploadContext.guessFields(conn);
                List<FieldUploadInfo> fieldInfos = new ArrayList<FieldUploadInfo>();
                for (AnalysisItem field : fields) {
                    FieldUploadInfo fieldUploadInfo = new FieldUploadInfo();
                    fieldUploadInfo.setGuessedItem(field);
                    fieldUploadInfo.setSampleValues(uploadContext.getSampleValues(field.getKey()));
                    fieldInfos.add(fieldUploadInfo);
                }
                uploadResponse = new UploadResponse();
                uploadResponse.setSuccessful(true);
                uploadResponse.setInfos(fieldInfos);
            }
            conn.commit();
        } catch (Throwable e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        }
        return uploadResponse;
    }

    public UploadResponse createDataSource(String name, UploadContext uploadContext, List<AnalysisItem> analysisItems) {
        UploadResponse uploadResponse;
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            long dataSourceID = uploadContext.createDataSource(name, analysisItems, conn);
            uploadResponse = new UploadResponse(dataSourceID);
            conn.commit();
            return uploadResponse;
        } catch (StorageLimitException se) {
            conn.rollback();
            uploadResponse = new UploadResponse("You have reached your account storage limit.");
        } catch (Throwable e) {
            LogClass.error(e);
            conn.rollback();
            uploadResponse = new UploadResponse("Something caused an internal error in the processing of the uploaded file.");
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return uploadResponse;
    }

    public static RawUploadData retrieveRawData(long uploadID) {
        Connection conn = Database.instance().getConnection();
        RawUploadData result = null;
        try {
            conn.setAutoCommit(false);
            result = retrieveRawData(uploadID, conn);
        }
        catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        finally {
            Database.closeConnection(conn);
        }
        return result;
    }
    
    public static RawUploadData retrieveRawData(long uploadID, Connection conn) throws SQLException {
        RawUploadData rawUploadData = rawDataMap.get(uploadID);
        if (rawUploadData == null) {
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

        }
        return rawUploadData;
    }

    public static class RawUploadData {

        private RawUploadData(long accountID, String dataName, byte[] userData) {
            this.accountID = accountID;
            this.dataName = dataName;
            this.userData = userData;
        }

        public byte[] getUserData() {
            return userData;
        }

        public void setUserData(byte[] userData) {
            this.userData = userData;
        }

        public String getDataName() {
            return dataName;
        }

        public void setDataName(String dataName) {
            this.dataName = dataName;
        }

        public long getAccountID() {
            return accountID;
        }

        public void setAccountID(long accountID) {
            this.accountID = accountID;
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
                FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(dataFeedID, conn);
                feedDefinition.delete(conn);
            } else if (role == Roles.SUBSCRIBER || role == Roles.SHARER) {
                deleteUserFeedLink(SecurityUtil.getUserID(), dataFeedID, conn);
            } else {
                throw new SecurityException();
            }
            conn.commit();
        } catch (Throwable e) {
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
            Database.closeConnection(conn);
        }
    }

    public List<FeedDescriptor> getOwnedFeeds() {
        long userID = SecurityUtil.getUserID();
        try {
            return feedStorage.searchForSubscribedFeeds(userID);
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public long createAnalysisBasedFeed(AnalysisBasedFeedDefinition definition) {
        long userID = SecurityUtil.getUserID();
        SecurityUtil.authorizeInsight(definition.getReportID());
        try {
            long feedID = feedStorage.addFeedDefinitionData(definition);
            new UserUploadInternalService().createUserFeedLink(userID, feedID, Roles.OWNER);
            return feedID;
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void subscribe(long dataFeedID) {
        SecurityUtil.authorizeFeedAccess(dataFeedID);
        long userID = SecurityUtil.getUserID();
        try {
            new UserUploadInternalService().createUserFeedLink(userID, dataFeedID, Roles.SUBSCRIBER);
        } catch (Throwable e) {
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
            Database.closeConnection(conn);
        }
    }

    private void deleteUserFeedLink(long accountID, long feedID, Connection conn) throws SQLException {
        PreparedStatement existingLinkQuery = conn.prepareStatement("DELETE FROM UPLOAD_POLICY_USERS WHERE " +
                "USER_ID = ? AND FEED_ID = ?");
        existingLinkQuery.setLong(1, accountID);
        existingLinkQuery.setLong(2, feedID);
        existingLinkQuery.executeUpdate();
    }

    public CredentialsResponse refreshData(long feedID) {
        return refreshData(feedID, false);
    }

    public CredentialsResponse refreshData(long feedID, boolean synchronous) {
        SecurityUtil.authorizeFeed(feedID, Roles.OWNER);
        try {
            CredentialsResponse credentialsResponse;
            IServerDataSourceDefinition dataSource = (IServerDataSourceDefinition) feedStorage.getFeedDefinitionData(feedID);
            if (SecurityUtil.getAccountTier() < dataSource.getRequiredAccountTier()) {
                return new CredentialsResponse(false, "Your account level is no longer valid for this data source connection.", feedID);
            }
            FeedDefinition feedDefinition = (FeedDefinition) dataSource;
            if ((feedDefinition.getDataSourceType() != DataSourceInfo.LIVE)) {
                if (DataSourceMutex.mutex().lock(dataSource.getDataFeedID())) {
                    try {
                        credentialsResponse = dataSource.refreshData(SecurityUtil.getAccountID(), new Date(), null);
                        if (credentialsResponse.isSuccessful() && !feedDefinition.isVisible()) {
                            feedDefinition.setVisible(true);
                            feedStorage.updateDataFeedConfiguration(feedDefinition);
                        }
                    } finally {
                        DataSourceMutex.mutex().unlock(dataSource.getDataFeedID());
                    }
                } else {
                    credentialsResponse = new CredentialsResponse(true, feedDefinition.getDataFeedID());
                }
            } else {
                feedDefinition.setVisible(true);
                feedStorage.updateDataFeedConfiguration(feedDefinition);
                credentialsResponse = new CredentialsResponse(true, feedID);
            }
            return credentialsResponse;
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void updateData(long feedID, byte[] bytes, boolean update) {
        SecurityUtil.authorizeFeed(feedID, Roles.OWNER);
        Connection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            FileProcessUpdateScheduledTask task = new FileProcessUpdateScheduledTask();
            task.setFeedID(feedID);
            task.setUpdate(update);
            task.setUserID(SecurityUtil.getUserID());
            task.setAccountID(SecurityUtil.getAccountID());
            task.updateData(feedID, update, conn, bytes);
            conn.commit();
        } catch (Throwable e) {
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
            Database.closeConnection(conn);
        }
    }

    public long uploadPNG(byte[] bytes) {
        try {
            Long userID = SecurityUtil.getUserID(false);
            if (userID == 0) {
                userID = null;    
            }
            return new PNGExportOperation().write(bytes, userID);
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public FeedDescriptor getFeedDescriptor(long feedID) {
        SecurityUtil.authorizeFeed(feedID, Roles.OWNER);
        try {
            return feedStorage.getFeedDescriptor(feedID);
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public CredentialsResponse validateCredentials(FeedDefinition feedDefinition) {
        try {
            String failureMessage = feedDefinition.validateCredentials();
            CredentialsResponse credentialsResponse;
            if (failureMessage == null) {
                credentialsResponse = new CredentialsResponse(true, feedDefinition.getDataFeedID());
            } else {
                credentialsResponse = new CredentialsResponse(false, failureMessage, feedDefinition.getDataFeedID());
            }
            return credentialsResponse;
        } catch (ReportException re) {
            return new CredentialsResponse(false, re.getReportFault());
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }


    public long newExternalDataSource(FeedDefinition feedDefinition) {
        if (SecurityUtil.getAccountTier() < feedDefinition.getRequiredAccountTier()) {
            throw new RuntimeException("You are not allowed to create data sources of this type with your account.");
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            IServerDataSourceDefinition serverDataSourceDefinition = (IServerDataSourceDefinition) feedDefinition;
            long id = serverDataSourceDefinition.create(conn, null);
            conn.commit();
            return id;
        } catch (Throwable e) {
            LogClass.error(e);            
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }
    public static User retrieveUser(Connection conn) {
        long userID = SecurityUtil.getUserID();
        return retrieveUser(conn, userID);
    }

    public static User retrieveUser(Connection conn, long userID) {
        try {
            User user = null;
            Session session = Database.instance().createSession(conn);
            List results;
            try {
                session.beginTransaction();
                results = session.createQuery("from User where userID = ?").setLong(0, userID).list();
                session.getTransaction().commit();
            } catch (Throwable e) {
                session.getTransaction().rollback();
                throw new RuntimeException(e);
            } finally {
                session.close();
            }
            if (results.size() > 0) {
                user = (User) results.get(0);                
            }
            return user;
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public Credentials encryptCredentials(Credentials creds) {
        Credentials c = new Credentials();
        c.setUserName(PasswordStorage.encryptString(creds.getUserName() + ":" + SecurityUtil.getUserName()));
        c.setPassword(PasswordStorage.encryptString(creds.getPassword() + ":" + SecurityUtil.getUserName()));
        c.setEncrypted(true);
        return c;
    }

    private Credentials decryptCredentials(Credentials creds) throws MalformedCredentialsException {
        Credentials c = new Credentials();
        String s = PasswordStorage.decryptString(creds.getUserName());
        int i = s.lastIndexOf(":" + SecurityUtil.getUserName());
        if(i == -1) {
            throw new MalformedCredentialsException();
        }
        c.setUserName(s.substring(0, i));
        s = PasswordStorage.decryptString(creds.getPassword());
        i = s.lastIndexOf(":" + SecurityUtil.getUserName());
        if(i == -1)
            throw new MalformedCredentialsException();
        c.setPassword(s.substring(0, i));
        return c;
    }
}
