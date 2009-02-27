package test.admin;

import junit.framework.TestCase;
import com.easyinsight.admin.AdminService;
import com.easyinsight.database.Database;

/**
 * User: James Boe
 * Date: Feb 26, 2009
 * Time: 4:45:54 PM
 */
public class AdminTest extends TestCase {

    protected void setUp() throws Exception {
        Database.initialize();
    }

    public void testAdmin() {
        AdminService adminService = new AdminService();
        adminService.threadDump();
        adminService.getHealthInfo();
    }
}
