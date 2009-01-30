package test.util;

import com.easyinsight.core.Key;
import com.easyinsight.core.KeyStorage;
import com.easyinsight.users.*;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.userupload.UploadResponse;

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

    /**
     * Looks for a user called testuser and creates if it doesn't exist with an individual account type.
     * @return the user ID
     */
    public static long getIndividualTestUser() {
        UserService userService = new UserService();
        User user = new InternalUserService().retrieveUser("testuser");
        long userID;
        if (user == null) {
            UserTransferObject initialUser = new UserTransferObject("testuser", 0, "James Boe", "testuser99@gmail.com", null);
            AccountTransferObject account = new AccountTransferObject();
            account.setAccountType(new IndividualAccount());
            userService.createAccount(initialUser, account, "password");
            userID = userService.getUserStub("testuser").getUserID();
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
     * @param userID user to associate the data source to
     * @return the ID of the data source
     */
    public static long createDefaultTestDataSource(long userID) {
        String csvText = "Customer,Product,Revenue,Units,When\nAcme,WidgetX,500,20,2009-01-25\nAcme,WidgetY,200,5,2009-01-26";
        UserUploadService userUploadService = new UserUploadService();
        long uploadID = userUploadService.addRawUploadData(userID, "blah.csv", csvText.getBytes());
        UploadResponse uploadResponse = userUploadService.create(uploadID, "Default Test Data Source");
        return uploadResponse.getFeedID();
    }
}
