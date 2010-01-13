package test.core;

import junit.framework.TestCase;
import com.easyinsight.users.*;
import com.easyinsight.database.Database;
import com.easyinsight.security.SecurityUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import test.util.TestSecurityProvider;

/**
 * User: James Boe
 * Date: Apr 27, 2009
 * Time: 11:09:16 PM
 */
public class AccountTest extends TestCase {

    protected void setUp() throws Exception {
        Database.initialize();
    }

    public void testAccountScheduling() throws Exception {
        // create trial account
        UserService userService = new UserService();
        Session session = Database.instance().createSession();
        session.beginTransaction();
        List<Account> results = session.createQuery("from Account where name = ?").setString(0, "Test Pro Account").list();
        if (results.size() == 1) {
            Account account = results.get(0);
            session.delete(account);
        }
        session.getTransaction().commit();
        session.close();
        UserTransferObject initialUser = new UserTransferObject("testprouser", 0, "testprouser99@blah.com", "James Boe", null);
        AccountTransferObject accountTransferObject = new AccountTransferObject();
        accountTransferObject.setName("Test Pro Account");
        accountTransferObject.setAccountType(Account.PREMIUM);
        long accountID = userService.createAccount(initialUser, accountTransferObject, "password");
        long userID = userService.getUserStub("testprouser").getUserID();
        TestSecurityProvider testSecurityProvider = new TestSecurityProvider();
        testSecurityProvider.setUserPrincipal(userID);
        SecurityUtil.setSecurityProvider(testSecurityProvider);
        accountTransferObject = userService.retrieveAccount();
        assertEquals(Account.TRIAL, accountTransferObject.getAccountState());
        String activationKey;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT ACTIVATION_KEY FROM ACCOUNT_ACTIVATION WHERE ACCOUNT_ID = ?");
            queryStmt.setLong(1, accountID);
            ResultSet rs = queryStmt.executeQuery();
            rs.next();
            activationKey = rs.getString(1);
        } finally {
            Database.instance().closeConnection(conn);
        }
        new UserAccountAdminService().activateAccount(activationKey);
        Account account = getAccount();
        assertEquals(Account.TRIAL, account.getAccountState());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 31);
        conn = Database.instance().getConnection();
        try {
            new AccountActivityStorage().updateAccountTimes(cal.getTime(), conn);            
        } finally {
            Database.instance().closeConnection(conn);
        }
        account = getAccount();
        assertTrue(account.isActivated());
    }

    private Account getAccount() {
        Session session = Database.instance().createSession();
        session.beginTransaction();
        List<Account> results = session.createQuery("from Account where name = ?").setString(0, "Test Pro Account").list();
        Account account = results.get(0);
        session.getTransaction().commit();
        session.close();
        return account;
    }
}
