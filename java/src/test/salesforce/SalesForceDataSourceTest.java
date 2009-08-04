package test.salesforce;

import junit.framework.TestCase;
import com.easyinsight.datafeeds.salesforce.SalesforceBaseDataSource;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.users.Credentials;
import com.easyinsight.core.Key;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.database.Database;

import java.util.Map;
import java.sql.SQLException;

import test.util.TestUtil;

/**
 * User: abaldwin
 * Date: Jul 13, 2009
 * Time: 10:45:09 AM
 */

public class SalesForceDataSourceTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
    }

    public void testDataSource() throws SQLException {
        TestUtil.getProUser();
        SalesforceBaseDataSource sbds = new SalesforceBaseDataSource();
        Credentials credentials = new Credentials("jboe99@gmail.com", "e@symone$rKxLSrt0eol9SbnHAr8UbZOR");

        long dataSourceID = new UserUploadService().newExternalDataSource(sbds, credentials);
        FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID);
        System.out.println("blah");
    }
}
