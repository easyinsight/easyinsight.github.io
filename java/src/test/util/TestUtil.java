package test.util;

import com.easyinsight.core.Key;
import com.easyinsight.core.KeyStorage;
import com.easyinsight.users.*;
import com.easyinsight.security.SecurityUtil;

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

    public static long getTestUser() {
        UserService userService = new UserService();
        User user = new InternalUserService().retrieveUser("testuser");
        long userID;
        if (user == null) {
            User initialUser = new User("testuser", "password", "James Boe", "testuser99@gmail.com");
            AccountTransferObject account = new AccountTransferObject();
            account.setAccountType(new IndividualAccount());
            userService.createAccount(initialUser, account);
            userID = initialUser.getUserID();
        } else {
            userID = user.getUserID();
        }
        TestSecurityProvider testSecurityProvider = new TestSecurityProvider();
        testSecurityProvider.setUserPrincipal(userID);
        SecurityUtil.setSecurityProvider(testSecurityProvider);        
        return userID;
    }

    public static long createBasicDataFeed() {
        return 0;
    }
}
