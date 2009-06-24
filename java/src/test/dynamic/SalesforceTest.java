package test.dynamic;

import junit.framework.TestCase;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.salesforce.SalesforceConnection;
import com.easyinsight.users.Credentials;

/**
 * User: James Boe
 * Date: Feb 8, 2008
 * Time: 2:12:27 PM
 */
public class SalesforceTest extends TestCase {
    protected void setUp() throws Exception {
        super.setUp();
        Database.initialize();
    }

    public void testConnectivity() {
        SalesforceConnection salesforceConnection = new SalesforceConnection();
        Credentials credentials = new Credentials("jboe99@gmail.com", "Grav1tyfailsQrBLE3rV3VZPrKmtqZBZk9rm");
        salesforceConnection.testSFConnect(credentials);
    }
}
