package test.gnip;

import junit.framework.TestCase;
import com.gnipcentral.client.resource.*;
import com.gnipcentral.client.Config;
import com.gnipcentral.client.GnipConnection;
import com.gnipcentral.client.GnipException;
import com.easyinsight.datafeeds.gnip.GnipHelper;
import com.easyinsight.datafeeds.gnip.GnipDataSource;
import com.easyinsight.datafeeds.gnip.GnipFilter;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.database.Database;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.users.Credentials;
import org.joda.time.DateTime;
import test.util.TestUtil;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Apr 15, 2009
 * Time: 10:19:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class GnipTest extends TestCase {
    private GnipDataSource ds; 

        public void setUp() {
        ds = new GnipDataSource();
    }

    public void testGnipData() {
        Database.initialize();
        FeedRegistry.initialize();
        TestUtil.getIndividualTestUser();
        UserUploadService uploadService = new UserUploadService();
        Credentials c = new Credentials();

        GnipFilter gf = new GnipFilter();
        gf.setPublisherName("deviantart");
        gf.setScope(PublisherScope.GNIP);
        gf.setFilterName("photofilter");

        ds.getFilters().add(gf);

        c.setUserName("abaldwin@easy-insight.com");
        c.setPassword("testing123");
        long sourceId = uploadService.newExternalDataSource(ds, c);
    }
    public void testGnipHelpers() {
        GnipHelper gh = new GnipHelper();
        gh.setUsername("abaldwin@easy-insight.com");
        gh.setPassword("testing123");
        DateTime d = new DateTime();
        try {
            GnipFilter gf = new GnipFilter();
            gf.setPublisherName("gnip-sample");
            gf.setScope(PublisherScope.GNIP);
            gf.setFilterName("testfilter");
            Activities act = gh.getActivities(gf, new DateTime(d.getYear(), d.getMonthOfYear(), d.getDayOfMonth(), d.getHourOfDay(), d.getMinuteOfHour() > 0 ? d.getMinuteOfHour() - 1 : d.getMinuteOfHour(), d.getSecondOfMinute(), d.getMillisOfSecond()));
            assertEquals(true, act.getActivities().size() > 0);
        } catch (GnipException e) {
            e.printStackTrace();
            assertEquals(false, true);
        }
    }
}
