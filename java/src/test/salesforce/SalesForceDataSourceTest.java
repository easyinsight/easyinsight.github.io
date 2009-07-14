package test.salesforce;

import junit.framework.TestCase;
import com.easyinsight.datafeeds.salesforce.SalesforceBaseDataSource;
import com.easyinsight.users.Credentials;
import com.easyinsight.core.Key;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jul 13, 2009
 * Time: 10:45:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class SalesForceDataSourceTest extends TestCase {

    SalesforceBaseDataSource dataSource;

    @Override
    protected void setUp() throws Exception {
        dataSource = new SalesforceBaseDataSource();
        super.setUp();
    }

    public void testDataSourceKeys() {
        Map<String, Key> keys = dataSource.newDataSourceFields(new Credentials("jboe99@gmail.com", "e@symone$rKxLSrt0eol9SbnHAr8UbZOR"));
        assertTrue(keys.size() > 0);
    }
}
