package test.jira;

import junit.framework.TestCase;
import com.easyinsight.datafeeds.jira.JiraDataSource;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.users.Credentials;
import com.easyinsight.database.Database;
import test.util.TestUtil;

/**
 * User: James Boe
 * Date: Feb 26, 2009
 * Time: 3:01:57 PM
 */
public class JiraTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
    }

    public void testJiraDataSourceCreation() {
        TestUtil.getIndividualTestUser();
        JiraDataSource jiraDataSource = new JiraDataSource();
        jiraDataSource.setUrl("http://5.92.30.62:8686");
        UserUploadService uploadService = new UserUploadService();
        Credentials credentials = new Credentials();
        credentials.setUserName("testuser");
        credentials.setPassword("password");
        //long dataSourceID = uploadService.newExternalDataSource(jiraDataSource, credentials);
        
    }
}
