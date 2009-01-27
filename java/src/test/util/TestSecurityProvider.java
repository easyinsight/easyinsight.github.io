package test.util;

import com.easyinsight.security.ISecurityProvider;
import com.easyinsight.security.UserPrincipal;
import com.easyinsight.users.User;
import com.easyinsight.users.InternalUserService;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 11:25:11 AM
 */
public class TestSecurityProvider implements ISecurityProvider {

    private UserPrincipal userPrincipal;

    public void setUserPrincipal(long userID) {
        User user = new InternalUserService().retrieveUser(userID);
        userPrincipal = new UserPrincipal(user.getUserName(), user.getAccountID().getAccountID(), user.getUserID());
    }

    public UserPrincipal getUserPrincipal() {
        return userPrincipal;
    }
}
