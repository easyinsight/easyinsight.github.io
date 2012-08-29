package test.pipeline;

import com.easyinsight.analysis.AnalysisCalculation;
import com.easyinsight.analysis.FilterRangeDefinition;
import com.easyinsight.analysis.FilterValueDefinition;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedRegistry;
import junit.framework.TestCase;
import test.util.TestUtil;

import java.util.Arrays;

/**
 * User: jamesboe
 * Date: 8/29/12
 * Time: 8:14 AM
 */
public class FilterTest extends TestCase implements ITestConstants {
    protected void setUp() throws Exception {
        if (Database.instance() == null) {
            Database.initialize();
        }
        FeedRegistry.initialize();
        TestUtil.getIndividualTestUser();
    }

    public void testMultiValueFilter() throws Exception {

        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper dataSource = DataSourceWrapper.createDataSource(conn, "Customer", GROUPING, "Amount", MEASURE);
            dataSource.addRow("Acme", 50);
            dataSource.addRow("XYZ", 100);
            dataSource.addRow("ABC", 75);
            ReportWrapper report = dataSource.createReport();
            report.addField("Amount");
            report.addFilter(new FilterValueDefinition(report.getField("Customer").getAnalysisItem(), true, Arrays.asList((Object) "Acme", "XYZ")));
            Results results = report.runReport(conn);
            results.verifyRowCount(1);
            results.verifyRow(150);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testRowLevelRangeFilter() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper dataSource = DataSourceWrapper.createDataSource(conn, "Customer", GROUPING, "Amount", MEASURE, "Industry", GROUPING);
            dataSource.addRow("Acme", 50, "A");
            dataSource.addRow("XYZ", 100, "A");
            dataSource.addRow("ABC", 75, "B");
            ReportWrapper report = dataSource.createReport();
            report.addField("Customer");
            FilterRangeDefinition filter = new FilterRangeDefinition(report.getField("Amount").getAnalysisItem(), 74, 5000);
            filter.setApplyBeforeAggregation(true);
            report.addFilter(filter);
            Results results = report.runReport(conn);
            results.verifyRowCount(2);
            results.verifyRow("XYZ");
            results.verifyRow("ABC");
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testAggregateRangeFilter() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper dataSource = DataSourceWrapper.createDataSource(conn, "Customer", GROUPING, "Amount", MEASURE, "Industry", GROUPING);
            dataSource.addRow("Acme", 50, "A");
            dataSource.addRow("XYZ", 100, "A");
            dataSource.addRow("ABC", 75, "B");
            ReportWrapper report = dataSource.createReport();
            report.addField("Industry");
            FilterRangeDefinition filter = new FilterRangeDefinition(report.getField("Amount").getAnalysisItem(), 149, 5000);
            filter.setApplyBeforeAggregation(false);
            report.addFilter(filter);
            Results results = report.runReport(conn);
            results.verifyRowCount(1);
            results.verifyRow("A");
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testFilterOnCalculation() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper dataSource = DataSourceWrapper.createDataSource(conn, "Customer", GROUPING, "Amount", MEASURE);
            dataSource.addRow("Acme", 50);
            dataSource.addRow("XYZ", 100);
            dataSource.addRow("ABC", 75);
            ReportWrapper report = dataSource.createReport();
            report.addField("Customer");
            FieldWrapper revenueCalculation = report.addCalculation("DoubledAmount", "Amount * 2");
            ((AnalysisCalculation) revenueCalculation.getAnalysisItem()).setApplyBeforeAggregation(true);
            FilterRangeDefinition filter = new FilterRangeDefinition(report.getField("DoubledAmount").getAnalysisItem(), 151, 5000);
            filter.setApplyBeforeAggregation(false);
            report.addFilter(filter);
            Results results = report.runReport(conn);
            results.verifyRowCount(1);
            results.verifyRow("XYZ");
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testFilterOnAggregateCalculation() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper dataSource = DataSourceWrapper.createDataSource(conn, "Customer", GROUPING, "Amount", MEASURE);
            dataSource.addRow("Acme", 50);
            dataSource.addRow("XYZ", 100);
            dataSource.addRow("ABC", 75);
            ReportWrapper report = dataSource.createReport();
            report.addField("Customer");
            FieldWrapper revenueCalculation = report.addCalculation("DoubledAmount", "Amount * 2");
            ((AnalysisCalculation) revenueCalculation.getAnalysisItem()).setApplyBeforeAggregation(false);
            FilterRangeDefinition filter = new FilterRangeDefinition(report.getField("DoubledAmount").getAnalysisItem(), 151, 5000);
            filter.setApplyBeforeAggregation(false);
            report.addFilter(filter);
            Results results = report.runReport(conn);
            results.verifyRowCount(1);
            results.verifyRow("XYZ");
        } finally {
            Database.closeConnection(conn);
        }
    }
}
