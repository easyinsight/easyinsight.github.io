package test.api;

import com.easyinsight.api.UncheckedPublishService;

/**
 * User: James Boe
 * Date: Feb 26, 2009
 * Time: 1:59:55 PM
 */
public class TestUncheckedPublish extends UncheckedPublishService {

    private long userID;
    private long accountID;

    public TestUncheckedPublish(long userID, long accountID) {
        this.userID = userID;
        this.accountID = accountID;
    }

    protected String getUserName() {
        return "testuser";
    }

    protected long getAccountID() {
        return accountID;
    }

    protected long getUserID() {
        return userID;
    }
}
