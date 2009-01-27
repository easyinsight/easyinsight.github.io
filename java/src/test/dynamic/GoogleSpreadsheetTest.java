package test.dynamic;

import junit.framework.TestCase;
import com.easyinsight.database.Database;

/**
 * User: James Boe
 * Date: Feb 8, 2008
 * Time: 1:20:31 PM
 */
public class GoogleSpreadsheetTest extends TestCase {
    protected void setUp() throws Exception {
        super.setUp();
        Database.initialize();
    }

    public void testSpreadsheetRetrieval() {
    }
}
