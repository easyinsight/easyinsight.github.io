package test.basecamp;

import com.easyinsight.database.EIConnection;
import junit.framework.TestCase;
import com.easyinsight.datafeeds.basecamp.BaseCampCompositeSource;
import com.easyinsight.datafeeds.basecamp.BaseCampTimeSource;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.database.Database;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.users.Credentials;
import com.easyinsight.dataset.DataSet;
import test.util.TestUtil;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 19, 2009
 * Time: 10:25:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class BaseCampTimeTest extends TestCase {

    private BaseCampTimeSource ds;

    public void setUp() {
        ds = new BaseCampTimeSource();
    }

    public void testTodoSource() {
        Database.initialize();
        FeedRegistry.initialize();
        TestUtil.getIndividualTestUser();
        UserUploadService uploadService = new UserUploadService();
        BaseCampCompositeSource comp = new BaseCampCompositeSource();
        comp.setUrl("easyinsight.basecamphq.com");
        Credentials c = new Credentials();
        c.setUserName("apiuser");
        c.setPassword("@p!user");

        EIConnection conn = Database.instance().getConnection();
        try {
            DataSet dataSet = ds.getDataSet(c, ds.newDataSourceFields(c), new Date(), comp, null, conn);
            dataSet.toString();
        } finally {
            Database.closeConnection(conn);
        }
    }
}