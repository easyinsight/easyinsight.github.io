package test.gnip;

import junit.framework.TestCase;
import com.gnipcentral.client.resource.*;
import com.gnipcentral.client.Config;
import com.gnipcentral.client.GnipConnection;
import com.gnipcentral.client.GnipException;
import com.easyinsight.datafeeds.gnip.GnipHelper;
import org.joda.time.DateTime;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Apr 15, 2009
 * Time: 10:19:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class GnipTest extends TestCase {
    public void testGnipHelpers() {
        GnipHelper gh = new GnipHelper();
        gh.setUsername("abaldwin@easy-insight.com");
        gh.setPassword("testing123");
        DateTime d = new DateTime();
        try {
            Activities act = gh.getActivities(new Publisher(PublisherScope.GNIP, "gnip-sample"), "testfilter", new DateTime(d.getYear(), d.getMonthOfYear(), d.getDayOfMonth(), d.getHourOfDay(), d.getMinuteOfHour() > 0 ? d.getMinuteOfHour() - 1 : d.getMinuteOfHour(), d.getSecondOfMinute(), d.getMillisOfSecond()));
            assertEquals(true, act.getActivities().size() > 0);
        } catch (GnipException e) {
            assertEquals(false, true);
        }
    }
}
