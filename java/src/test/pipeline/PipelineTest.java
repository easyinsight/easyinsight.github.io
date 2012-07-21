package test.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedRegistry;
import junit.framework.TestCase;
import test.util.TestUtil;

import java.util.Arrays;

/**
 * User: jamesboe
 * Date: 7/19/12
 * Time: 10:18 AM
 */
public class PipelineTest extends TestCase implements ITestConstants {
    protected void setUp() throws Exception {
        if (Database.instance() == null) {
            Database.initialize();
        }
        FeedRegistry.initialize();
        TestUtil.getIndividualTestUser();
    }

    /*
    Test Order:

    Simple retrieval


     */

    public void testRecursiveBug() throws Exception {

        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper dataSource = DataSourceWrapper.createDataSource(conn, "Customer", GROUPING, "Amount", MEASURE);
            dataSource.addRow("Acme", 50);
            dataSource.addRow("XYZ", 100);
            ReportWrapper report = dataSource.createReport();
            FieldWrapper amount = report.addField("Amount");
            FieldWrapper customer = report.getField("Amount");
            amount.addFilter(new FilterRangeDefinition(customer.getAnalysisItem(), 0, 5));
            Results results = report.runReport(conn);
            results.verifyRow(0);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testMeasureFilter() throws Exception {

        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper dataSource = DataSourceWrapper.createDataSource(conn, "Customer", GROUPING, "Amount", MEASURE);
            dataSource.addRow("Acme", 50);
            dataSource.addRow("XYZ", 100);
            ReportWrapper report = dataSource.createReport();
            FieldWrapper amount = report.addField("Amount");
            FieldWrapper customer = report.getField("Customer");
            amount.addFilter(new FilterValueDefinition(customer.getAnalysisItem(), true, Arrays.asList((Object) "Acme")));
            Results results = report.runReport(conn);
            results.verifyRow(50);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testRowLevelCalculation() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper dataSource = DataSourceWrapper.createDataSource(conn, "Customer", GROUPING, "Amount", MEASURE, "Cost", MEASURE);
            dataSource.addRow("Acme", 100, 5);
            ReportWrapper report = dataSource.createReport();
            FieldWrapper revenueCalculation = report.addCalculation("Revenue", "Amount * Cost");
            ((AnalysisCalculation) revenueCalculation.getAnalysisItem()).setApplyBeforeAggregation(true);
            report.addField("Revenue");
            Results results = report.runReport(conn);
            results.verifyRow(500);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testRowLevelCalculationWithMeasureFilter() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper dataSource = DataSourceWrapper.createDataSource(conn, "Customer", GROUPING, "Amount", MEASURE, "Cost", MEASURE);
            dataSource.addRow("Acme", 100, 5);
            dataSource.addRow("Acme", 100, 5);
            dataSource.addRow("XYZ", 100, 5);
            ReportWrapper report = dataSource.createReport();
            FieldWrapper revenueCalculation = report.addCalculation("Revenue", "Amount * Cost");
            ((AnalysisCalculation) revenueCalculation.getAnalysisItem()).setApplyBeforeAggregation(true);
            FieldWrapper customer = report.getField("Customer");
            revenueCalculation.addFilter(new FilterValueDefinition(customer.getAnalysisItem(), true, Arrays.asList((Object) "Acme")));
            report.addField("Revenue");
            Results results = report.runReport(conn);
            results.verifyRow(1000);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testRowLevelCalculationField() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper dataSource = DataSourceWrapper.createDataSource(conn, "Customer", GROUPING, "Amount", MEASURE, "Cost", MEASURE);
            dataSource.addRow("Acme [50]", 100, 5);
            dataSource.addRow("Acme [25]", 100, 5);
            ReportWrapper report = dataSource.createReport();
            FieldWrapper revenueCalculation = report.addCalculation("Revenue", "bracketvalue([Customer])");
            ((AnalysisCalculation) revenueCalculation.getAnalysisItem()).setApplyBeforeAggregation(true);
            report.addField("Revenue");
            Results results = report.runReport(conn);
            results.verifyRow(75);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testAggregateLevelCalculation() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper dataSource = DataSourceWrapper.createDataSource(conn, "Customer", GROUPING, "Amount", MEASURE, "Cost", MEASURE);
            dataSource.addRow("Acme", 100, 5);
            ReportWrapper report = dataSource.createReport();
            FieldWrapper revenueCalculation = report.addCalculation("Revenue", "Amount * Cost");
            ((AnalysisCalculation) revenueCalculation.getAnalysisItem()).setApplyBeforeAggregation(false);
            report.addField("Revenue");
            Results results = report.runReport(conn);
            results.verifyRow(500);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testAggregateLevelCalculationField() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper dataSource = DataSourceWrapper.createDataSource(conn, "Customer", GROUPING, "Amount", MEASURE, "Cost", MEASURE);
            dataSource.addRow("Acme [50]", 100, 5);
            dataSource.addRow("Acme [25]", 100, 5);
            ReportWrapper report = dataSource.createReport();
            FieldWrapper revenueCalculation = report.addCalculation("Revenue", "bracketvalue([Customer])");
            ((AnalysisCalculation) revenueCalculation.getAnalysisItem()).setApplyBeforeAggregation(false);
            report.addField("Revenue");
            Results results = report.runReport(conn);
            results.verifyRow(75);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testAggregateLevelCalculationWithMeasureFilter() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper dataSource = DataSourceWrapper.createDataSource(conn, "Customer", GROUPING, "Amount", MEASURE, "Cost", MEASURE);
            dataSource.addRow("Acme", 100, 5);
            dataSource.addRow("Acme", 100, 5);
            dataSource.addRow("XYZ", 100, 5);
            ReportWrapper report = dataSource.createReport();
            FieldWrapper revenueCalculation = report.addCalculation("Revenue", "Amount * Cost");
            ((AnalysisCalculation) revenueCalculation.getAnalysisItem()).setApplyBeforeAggregation(false);
            FieldWrapper customer = report.getField("Customer");
            revenueCalculation.addFilter(new FilterValueDefinition(customer.getAnalysisItem(), true, Arrays.asList((Object) "Acme")));
            report.addField("Revenue");
            Results results = report.runReport(conn);
            results.verifyRow(2000);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testUngroupedDimensions() throws Exception {

        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper dataSource = DataSourceWrapper.createDataSource(conn, "Customer", GROUPING, "Amount", MEASURE);
            dataSource.addRow("Acme", 50);
            dataSource.addRow("Acme", 75);
            ReportWrapper report = dataSource.createReport();
            FieldWrapper customer = report.addField("Customer");
            ((AnalysisDimension)customer.getAnalysisItem()).setGroup(false);
            Results results = report.runReport(conn);
            results.verifyRowCount(2);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testSimpleJoin() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper customers = DataSourceWrapper.createDataSource("Customers", conn, "Customer", GROUPING, "Industry", GROUPING);
            DataSourceWrapper orders = DataSourceWrapper.createDataSource("Orders", conn, "Customer", GROUPING, "Amount", MEASURE);
            DataSourceWrapper warehouse = DataSourceWrapper.createJoinedSource("Warehouse", conn, customers, orders);
            warehouse.join(customers, orders, "Customer", "Customer");
            customers.addRow("A", "Plumbing");
            customers.addRow("B", "Plumbing");
            customers.addRow("C", "Other");
            orders.addRow("A", 500);
            orders.addRow("B", 750);
            orders.addRow("C", 1000);
            ReportWrapper report = warehouse.createReport();
            report.addField("Industry");
            report.addField("Amount");
            Results results = report.runReport(conn);
            results.verifyRow("Plumbing", 1250);
            results.verifyRow("Other", 1000);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testHarvestSpent() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper users = DataSourceWrapper.createDataSource("Users", conn, "User Full Name", GROUPING, "User Default Hourly Rate", MEASURE);
            DataSourceWrapper projects = DataSourceWrapper.createDataSource("Projects", conn, "Project Name", GROUPING, "Budget", MEASURE);
            DataSourceWrapper timeTracking = DataSourceWrapper.createDataSource("Time Tracking", conn, "Hours", MEASURE, "Time Tracking User", GROUPING, "Time Tracking Project", GROUPING);
            DataSourceWrapper harvest = DataSourceWrapper.createJoinedSource("Harvest", conn, users, projects, timeTracking);
            harvest.join(users, timeTracking, "User Full Name", "Time Tracking User");
            harvest.join(timeTracking, projects, "Time Tracking Project", "Project Name");
            users.addRow("James Boe", 100);
            users.addRow("Jim Bob", 75);
            projects.addRow("Shivano Consulting", 1000);
            timeTracking.addRow(5, "James Boe", "Shivano Consulting");
            timeTracking.addRow(3, "Jim Bob", "Shivano Consulting");
            ReportWrapper report = harvest.createReport();
            report.getListDefinition().setMarmotScript("uniquefield(\"Project Name\")");
            report.addCalculation("Spent", "notnull([User Full Name], User Default Hourly Rate * Hours)");
            report.addField("Project Name");
            report.addField("Budget");
            report.addField("Spent");
            Results results = report.runReport(conn);
            results.verifyRow("Shivano Consulting", 1000, 725);
        } finally {
            Database.closeConnection(conn);
        }
    }



    public void testBasecampHack() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper todos = DataSourceWrapper.createDataSource("Todo", conn, "Project Name", GROUPING, "Milestone", GROUPING, "Todo List", GROUPING, "Todo ID", GROUPING);
            DataSourceWrapper timeTracking = DataSourceWrapper.createDataSource("Time Tracking", conn, "Project Name", GROUPING, "Todo ID", GROUPING, "Hours", MEASURE);
            DataSourceWrapper basecamp = DataSourceWrapper.createJoinedSource("Basecamp", conn, todos);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
