package test.util;

import com.easyinsight.core.Key;
import com.easyinsight.core.KeyStorage;
import com.easyinsight.users.*;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.userupload.UploadResponse;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.database.Database;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.datafeeds.*;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FeedMetadata;
import com.easyinsight.analysis.DataService;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.Session;

/**
 * User: James Boe
 * Date: Jun 12, 2008
 * Time: 12:08:29 PM
 */
public class TestUtil {

    private static KeyStorage keyStorage = new KeyStorage();

    public static Key createKey(String name, long feedID) {
        return keyStorage.retrieveKey(feedID, name);
    }

    public static List<Object> objectList(Object... objects) {
        List<Object> objectList = new ArrayList<Object>();
        objectList.addAll(Arrays.asList(objects));
        return objectList;
    }

    public static AnalysisItem getItem(long feedID, String name) {
        FeedMetadata feedMetadata = new DataService().getFeedMetadata(feedID);
        for (AnalysisItem field : feedMetadata.getFields()) {
            if (name.equals(field.getKey().toKeyString())) {
                return field;
            }
        }
        throw new NoSuchElementException();
    }

    public static AnalysisItem getActualItem(long feedID, String name) {
        FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(feedID);
        for (AnalysisItem field : feedDefinition.getFields()) {
            if (name.equals(field.getKey().toKeyString())) {
                return field;
            }
        }
        throw new NoSuchElementException();
    }

    public static long createTestDataSource(DataSet dataSet, List<AnalysisItem> analysisItems) throws SQLException {
        Connection conn = Database.instance().getConnection();
        conn.setAutoCommit(false);
        FeedDefinition feedDefinition = new FeedDefinition();
        feedDefinition.setFeedName("Test");
        feedDefinition.setOwnerName("Test User");
        UploadPolicy uploadPolicy = new UploadPolicy(SecurityUtil.getUserID());
        feedDefinition.setUploadPolicy(uploadPolicy);
        feedDefinition.setFields(analysisItems);
        FeedCreationResult result = new FeedCreation().createFeed(feedDefinition, conn, new DataSet(), SecurityUtil.getUserID());
        DataStorage dataStorage = result.getTableDefinitionMetadata();
        dataStorage.insertData(dataSet);
        dataStorage.commit();
        conn.commit();
        dataStorage.closeConnection();
        conn.setAutoCommit(true);
        Database.instance().closeConnection(conn);
        return feedDefinition.getDataFeedID();
    }

    /**
     * Looks for a user called testuser and creates if it doesn't exist with an individual account type.
     *
     * @return the user ID
     */
    public static long getIndividualTestUser() {
        UserService userService = new UserService();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE ACCOUNT FROM USER, ACCOUNT WHERE USERNAME = ? AND USER.account_id = ACCOUNT.ACCOUNT_ID");
            deleteStmt.setString(1, "testuser");
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        User user = new InternalUserService().retrieveUser("testuser");
        long userID;
        if (user == null) {
            UserTransferObject initialUser = new UserTransferObject("testuser", 0, "testuser99@gmail.com", "James Boe", null);
            AccountTransferObject account = new AccountTransferObject();
            account.setAccountType(Account.INDIVIDUAL);
            long accountID = userService.createAccount(initialUser, account, "password");
            Session session = Database.instance().createSession();
            session.beginTransaction();
            Account accountObj = (Account) session.createQuery("from Account where accountID = ?").setLong(0, accountID).list().get(0);
            accountObj.setMaxSize(50000000000L);
            session.update(accountObj);
            session.getTransaction().commit();
            session.close();
            userID = userService.getUserStub("testuser").getUserID();
        } else {
            userID = user.getUserID();
        }
        TestSecurityProvider testSecurityProvider = new TestSecurityProvider();
        testSecurityProvider.setUserPrincipal(userID);
        SecurityUtil.setSecurityProvider(testSecurityProvider);
        return userID;
    }

    public static long getProUser() {
        UserService userService = new UserService();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE ACCOUNT FROM USER, ACCOUNT WHERE USERNAME = ? AND USER.account_id = ACCOUNT.ACCOUNT_ID");
            deleteStmt.setString(1, "prouser");
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        User user = new InternalUserService().retrieveUser("prouser");
        long userID;
        if (user == null) {
            UserTransferObject initialUser = new UserTransferObject("prouser", 0, "prouser@gmail.com", "James Boe", null);
            AccountTransferObject account = new AccountTransferObject();
            account.setAccountType(Account.PROFESSIONAL);
            long accountID = userService.createAccount(initialUser, account, "password");
            Session session = Database.instance().createSession();
            session.beginTransaction();
            Account accountObj = (Account) session.createQuery("from Account where accountID = ?").setLong(0, accountID).list().get(0);
            accountObj.setMaxSize(50000000000L);
            session.update(accountObj);
            session.getTransaction().commit();
            session.close();
            userID = userService.getUserStub("prouser").getUserID();
        } else {
            userID = user.getUserID();
        }
        TestSecurityProvider testSecurityProvider = new TestSecurityProvider();
        testSecurityProvider.setUserPrincipal(userID);
        SecurityUtil.setSecurityProvider(testSecurityProvider);
        return userID;
    }

    public static long getIndividualAdminUser() {
        UserService userService = new UserService();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE ACCOUNT FROM USER, ACCOUNT WHERE USERNAME = ? AND USER.account_id = ACCOUNT.ACCOUNT_ID");
            deleteStmt.setString(1, "adminuser");
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        User user = new InternalUserService().retrieveUser("adminuser");
        long userID;
        if (user == null) {
            UserTransferObject initialUser = new UserTransferObject("adminuser", 0, "adminuser99@gmail.com", "James Boe", null);
            AccountTransferObject account = new AccountTransferObject();
            account.setAccountType(Account.ADMINISTRATOR);
            long accountID = userService.createAccount(initialUser, account, "password");
            Session session = Database.instance().createSession();
            session.beginTransaction();
            Account accountObj = (Account) session.createQuery("from Account where accountID = ?").setLong(0, accountID).list().get(0);
            accountObj.setMaxSize(50000000000L);
            session.update(accountObj);
            session.getTransaction().commit();
            session.close();
            userID = userService.getUserStub("adminuser").getUserID();
        } else {
            userID = user.getUserID();
        }
        TestSecurityProvider testSecurityProvider = new TestSecurityProvider();
        testSecurityProvider.setUserPrincipal(userID);
        SecurityUtil.setSecurityProvider(testSecurityProvider);
        return userID;
    }

    /**
     * Creates a default data source with five fields--Customer, Product, Revenue, Units, and When
     *
     * @param userID user to associate the data source to
     * @return the ID of the data source
     */
    public static long createDefaultTestDataSource(long userID) {
        String csvText = "Customer,Product,Revenue,Units,When\nAcme,WidgetX,500,20,2009-01-25\nAcme,WidgetY,200,5,2009-01-26";
        UserUploadService userUploadService = new UserUploadService();
        long uploadID = userUploadService.addRawUploadData(userID, "blah.csv", csvText.getBytes());
        UploadResponse uploadResponse = userUploadService.create(uploadID, "Default Test Data Source");
        if (!uploadResponse.isSuccessful()) {
            throw new RuntimeException(uploadResponse.getFailureMessage());
        }
        return uploadResponse.getFeedID();
    }
}
