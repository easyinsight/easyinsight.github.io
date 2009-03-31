package test.basecamp;

import junit.framework.TestCase;
import com.easyinsight.datafeeds.basecamp.BaseCampDataSource;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.users.Credentials;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.database.Database;
import com.easyinsight.userupload.UserUploadService;
import test.util.TestUtil;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Mar 30, 2009
 * Time: 6:23:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseCampTest extends TestCase {
    private BaseCampDataSource ds;

    public void setUp() {
        ds = new BaseCampDataSource();
    }

    public void testBaseCampData() {
        Database.initialize();
        FeedRegistry.initialize();
        TestUtil.getIndividualTestUser();
        UserUploadService uploadService = new UserUploadService();
        ds.setUrl("easyinsight.basecamphq.com");
        Credentials c = new Credentials();
        c.setUserName("apiuser");
        c.setPassword("@p!user");
        long sourceId = uploadService.newExternalDataSource(ds, c);
    }

    public void testUrlByName() {
        ds.setUrl("easyinsight");
        assertEquals("http://easyinsight.basecamphq.com", ds.getUrl());
    }

    public void testFullUrl() {
        ds.setUrl("easyinsight.basecamphq.com");
        assertEquals("http://easyinsight.basecamphq.com", ds.getUrl());
    }

    public void testFullUrlWithSlash() {
        ds.setUrl("easyinsight.basecamphq.com/");
        assertEquals("http://easyinsight.basecamphq.com", ds.getUrl());
    }

    public void testHttpFullUrlWithSlash() {
        ds.setUrl("http://easyinsight.basecamphq.com/");
        assertEquals("http://easyinsight.basecamphq.com", ds.getUrl());
    }

    public void testHttpFullUrl() {
        ds.setUrl("http://easyinsight.basecamphq.com");
        assertEquals("http://easyinsight.basecamphq.com", ds.getUrl());
    }

    public void testBaseCampInvalidLogin() {
        Credentials c = new Credentials();
        c.setUserName("failure");
        c.setPassword("isnotanoption");
        ds.setUrl("easyinsight.basecamphq.com");
        String result = ds.validateCredentials(c);
        assertEquals("Invalid username/password. Please try again.", result);
    }
    public void testValidLogin() {
        Credentials c = new Credentials();
        c.setUserName("apiuser");
        c.setPassword("@p!user");
        ds.setUrl("easyinsight.basecamphq.com");
        String result = ds.validateCredentials(c);
        assertEquals(null, result);
    }
}
