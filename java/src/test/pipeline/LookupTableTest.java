package test.pipeline;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedRegistry;
import junit.framework.TestCase;
import test.util.TestUtil;

/**
 * User: jamesboe
 * Date: 8/22/12
 * Time: 3:29 PM
 */
public class LookupTableTest extends TestCase implements ITestConstants {
    protected void setUp() throws Exception {
        if (Database.instance() == null) {
            Database.initialize();
        }
        FeedRegistry.initialize();
        TestUtil.getIndividualTestUser();
    }

    public void testGroupingLookupTable() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper dataSource = DataSourceWrapper.createDataSource(conn, "Customer", GROUPING, "Amount", MEASURE);
            //dataSource.addLookupTable();
            dataSource.addRow("Acme", 50);
            dataSource.addRow("XYZ", 100);
            ReportWrapper report = dataSource.createReport();
            FieldWrapper amount = report.addField("Amount");
            FieldWrapper customer = report.getField("Amount");
            Results results = report.runReport(conn);
            results.verifyRow(0);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
