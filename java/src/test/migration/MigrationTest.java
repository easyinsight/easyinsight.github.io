package test.migration;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import junit.framework.TestCase;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: Mar 1, 2010
 * Time: 11:27:00 AM
 */
public class MigrationTest extends TestCase {
    @Override
    protected void setUp() throws Exception {

    }

    public void testMigration() {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT VERSION, DATA_FEED_ID FROM DATA_FEED WHERE FEED_TYPE = ? AND VERSION < ?");
            
        } catch (SQLException se) {

        } finally {
            conn.rollback();
            Database.closeConnection(conn);
        }
    }
}
