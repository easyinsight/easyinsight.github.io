package test.pipeline;

import com.easyinsight.analysis.AnalysisCalculation;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.FilterValueDefinition;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedRegistry;
import junit.framework.TestCase;
import test.util.TestUtil;

import java.util.Arrays;

/**
 * User: jamesboe
 * Date: 8/26/12
 * Time: 1:33 PM
 */
public class PipelinePhaseTest extends TestCase implements ITestConstants {
    protected void setUp() throws Exception {
        if (Database.instance() == null) {
            Database.initialize();
        }
        FeedRegistry.initialize();
        TestUtil.getIndividualTestUser();
    }

    public void testMultiPipeline() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper todos = DataSourceWrapper.createDataSource("Todo", conn, "Todo - Project Name", GROUPING, "Milestone", GROUPING);
            DataSourceWrapper basecamp = DataSourceWrapper.createJoinedSource("Basecamp", conn, todos);
            DataSourceWrapper users = DataSourceWrapper.createDataSource("Users", conn, "User Full Name", GROUPING, "User Default Hourly Rate", MEASURE);
            DataSourceWrapper projects = DataSourceWrapper.createDataSource("Projects", conn, "Project Name", GROUPING, "Budget", MEASURE, "BigCat", GROUPING);
            DataSourceWrapper timeTracking = DataSourceWrapper.createDataSource("Time Tracking", conn, "Hours", MEASURE, "Time Tracking User", GROUPING, "Time Tracking Project", GROUPING);
            DataSourceWrapper harvest = DataSourceWrapper.createJoinedSource("Harvest", conn, users, projects, timeTracking);
            DataSourceWrapper warehouse = DataSourceWrapper.createJoinedSource("Warehouse", conn, basecamp, harvest);
            harvest.join(users, timeTracking, "User Full Name", "Time Tracking User");
            harvest.join(timeTracking, projects, "Time Tracking Project", "Project Name");
            warehouse.join(harvest, basecamp, "Project Name", "Todo - Project Name");
            todos.addRow("Shivano Consulting", "Milestone 1 [50]");
            todos.addRow("Shivano Consulting", "Milestone 2 [25]");
            todos.addRow("Another Project", "");
            users.addRow("James Boe", 100);
            users.addRow("Jim Bob", 75);
            projects.addRow("Shivano Consulting", 900);
            projects.addRow("Oregano Consulting", 100);
            projects.addRow("XYZ Consulting", 500);
            projects.addRow("Another Project", 500);
            timeTracking.addRow(5, "James Boe", "Shivano Consulting");
            timeTracking.addRow(3, "Jim Bob", "Shivano Consulting");
            timeTracking.addRow(10, "James Boe", "Another Project");
            timeTracking.addRow(7, "Jim Bob", "Another Project");

            ReportWrapper report = warehouse.createReport();
            FilterValueDefinition filter = new FilterValueDefinition(warehouse.getField("Project Name").getAnalysisItem(), true, Arrays.asList((Object) "Shivano Consulting", "Oregano Consulting"));
            filter.setFilterName("FilterX");
            report.addFilter(filter);
            report.getListDefinition().setMarmotScript("createnamedpipeline(\"MidProc1\", \"afterJoins\", \"1\")\n" +
                    "assignpipeline(\"Val2\", \"MidProc1\")\n"+
                    "assignfilterpipeline(\"FilterX\", \"MidProc1\")");
            report.addCalculation("Val1", "Budget");
            report.addCalculation("Val2", "Val1 * 2");
            FieldWrapper wrapper = report.addCalculation("Val3", "Val2 * 2");
            ((AnalysisCalculation) wrapper.getAnalysisItem()).setApplyBeforeAggregation(false);
            report.addField("Val1");
            report.addField("Val2");
            report.addField("Val3");
            Results results = report.runReport(conn);
            results.verifyRow(1000, 2000, 4000);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
