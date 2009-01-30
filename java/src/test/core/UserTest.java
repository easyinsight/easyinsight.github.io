package test.core;

import junit.framework.TestCase;
import com.easyinsight.database.Database;
import com.easyinsight.users.*;

/**
 * User: James Boe
 * Date: Apr 12, 2008
 * Time: 1:08:12 PM
 */
public class UserTest extends TestCase {

    public void setUp() {
        Database.initialize();
    }

    /*public void testUserCreation() {
        IUserService userService = new UserService();
        User user = userService.retrieveUser("testuser");
        if (user != null) {
            userService.deleteUser(user);
        }
        UserServiceResponse badUserName = userService.createUser(new User("", "", "James Boe", "testuser99@gmail.com"));
        assertFalse(badUserName.isSuccessful());
        assertNotNull(badUserName.getFailureMessage());
        UserServiceResponse badPassword = userService.createUser(new User("testuser", "", "James Boe", "testuser99@gmail.com"));
        assertFalse(badPassword.isSuccessful());
        assertNotNull(badPassword.getFailureMessage());
        User testUser = new User("testuser", "password", "James Boe", "testuser99@gmail.com");
        //testUser.setCredentialsList(Arrays.asList(new Credentials("jboe", "blah", "com.easyinsight.google")));
        UserServiceResponse goodCreate = userService.createUser(testUser);
        assertTrue(goodCreate.getFailureMessage(), goodCreate.isSuccessful());
        UserServiceResponse userNotExist = userService.authenticate("testuser99", "");
        assertFalse(userNotExist.isSuccessful());
        UserServiceResponse badPasswordAuthentice = userService.authenticate("testuser", "");
        assertFalse(badPasswordAuthentice.isSuccessful());
        UserServiceResponse goodLogin = userService.authenticate("testuser", "password");
        assertTrue(goodLogin.isSuccessful());
        assertEquals(goodLogin.getName(), "James Boe");
    }    */

    public void testAccountCreation() {
        UserService userService = new UserService();
        // okay, first step is, I click Create User...in creating a user, I need to fill in...
        // user name, password, email, name
        // also need to select the type of account in this page...
        // need quick popup to set that up
        // 
        User testUser = new User("testuser", "password", "James Boe", "testuser99@gmail.com");
        // create an account
        AccountTransferObject accountTransferObject = new AccountTransferObject();
        accountTransferObject.setAccountType(new FreeAccount());
        //long accountID = userService.createAccount(testUser, accountTransferObject);
        //AccountTransferObject retrievedTransferObject = userService.retrieveAccount();
        //retrievedTransferObject.setAccountType(new IndividualAccount());
        // need transactionality on this...
        // so...
        //retrievedTransferObject.setBillingParty(new BillingParty());
        // 
        //userService.updateAccount(retrievedTransferObject);
    }      
}
