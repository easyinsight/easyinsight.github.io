package test.pipeline;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedRegistry;
import junit.framework.TestCase;
import test.util.TestUtil;

/**
 * User: jamesboe
 * Date: 8/21/12
 * Time: 7:49 AM
 */
public class FederatedTest extends TestCase implements ITestConstants {
    protected void setUp() throws Exception {
        if (Database.instance() == null) {
            Database.initialize();
        }
        FeedRegistry.initialize();
        TestUtil.getIndividualTestUser();
    }

    public void testFederatedSource() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper customers1 = DataSourceWrapper.createDataSource("Customers1", conn, "Customer", GROUPING, "Industry", GROUPING);
            DataSourceWrapper customers2 = DataSourceWrapper.createDataSource("Customers2", conn, "Customer", GROUPING, "Industry", GROUPING);
            DataSourceWrapper federated = DataSourceWrapper.createFederatedSource("Federated Customers", conn, customers1, customers2);
            customers1.addRow("A", "Plumbing");
            customers1.addRow("B", "Plumbing");
            customers1.addRow("C", "Other");
            customers2.addRow("D", "Plumbing");
            customers2.addRow("E", "Other");
            ReportWrapper reportWrapper = federated.createReport();
            reportWrapper.addField("Customer");
            Results results = reportWrapper.runReport(conn);
            results.verifyRowCount(5);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testFederatedCombinedSource() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper customers1 = DataSourceWrapper.createDataSource("Customers1", conn, "Customer", GROUPING, "Industry", GROUPING);
            DataSourceWrapper orders1 = DataSourceWrapper.createDataSource("Orders", conn, "Customer", GROUPING, "Amount", MEASURE);
            DataSourceWrapper customers2 = DataSourceWrapper.createDataSource("Customers2", conn, "Customer", GROUPING, "Industry", GROUPING);
            DataSourceWrapper orders2 = DataSourceWrapper.createDataSource("Orders", conn, "Customer", GROUPING, "Amount", MEASURE);
            DataSourceWrapper warehouse1 = DataSourceWrapper.createJoinedSource("Warehouse", conn, customers1, orders1);
            DataSourceWrapper warehouse2 = DataSourceWrapper.createJoinedSource("Warehouse", conn, customers2, orders2);
            DataSourceWrapper federated = DataSourceWrapper.createFederatedSource("Federated Customers", conn, warehouse1, warehouse2);
            customers1.addRow("A", "Plumbing");
            customers1.addRow("B", "Plumbing");
            customers1.addRow("C", "Other");
            orders1.addRow("A", 500);
            orders1.addRow("B", 750);
            orders1.addRow("C", 1000);
            customers2.addRow("D", "Plumbing");
            customers2.addRow("E", "Other");
            orders2.addRow("D", 100);
            orders2.addRow("E", 200);
            ReportWrapper reportWrapper = federated.createReport();
            reportWrapper.addField("Amount");
            Results results = reportWrapper.runReport(conn);
            results.verifyRow(2550);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
