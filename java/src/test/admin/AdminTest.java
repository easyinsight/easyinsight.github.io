package test.admin;

import junit.framework.TestCase;
import com.easyinsight.admin.AdminService;

/**
 * User: James Boe
 * Date: Feb 26, 2009
 * Time: 4:45:54 PM
 */
public class AdminTest extends TestCase {
    public void testAdmin() {
        AdminService adminService = new AdminService();
        adminService.threadDump();
        adminService.getHealthInfo();
    }
}
