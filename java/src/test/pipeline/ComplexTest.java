package test.pipeline;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedRegistry;
import junit.framework.TestCase;
import test.util.TestUtil;

/**
 * User: jamesboe
 * Date: 7/20/12
 * Time: 12:16 PM
 */
public class ComplexTest extends TestCase implements ITestConstants {
    protected void setUp() throws Exception {
        if (Database.instance() == null) {
            Database.initialize();
        }
        FeedRegistry.initialize();
        TestUtil.getIndividualTestUser();
    }

    public void testAttentionScenario() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper todos = DataSourceWrapper.createDataSource("Todo", conn, "Todo - Project Name", GROUPING, "Milestone", GROUPING, "Milestone Completed On", DATE);
            DataSourceWrapper basecamp = DataSourceWrapper.createJoinedSource("Basecamp", conn, todos);
            DataSourceWrapper users = DataSourceWrapper.createDataSource("Users", conn, "User Full Name", GROUPING, "User Default Hourly Rate", MEASURE);
            DataSourceWrapper projects = DataSourceWrapper.createDataSource("Projects", conn, "Project Name", GROUPING, "Budget", MEASURE);
            DataSourceWrapper timeTracking = DataSourceWrapper.createDataSource("Time Tracking", conn, "Hours", MEASURE, "Time Tracking User", GROUPING, "Time Tracking Project", GROUPING);
            DataSourceWrapper harvest = DataSourceWrapper.createJoinedSource("Harvest", conn, users, projects, timeTracking);
            DataSourceWrapper warehouse = DataSourceWrapper.createJoinedSource("Warehouse", conn, basecamp, harvest);
            harvest.join(users, timeTracking, "User Full Name", "Time Tracking User");
            harvest.join(timeTracking, projects, "Time Tracking Project", "Project Name");
            warehouse.join(harvest, basecamp, "Project Name", "Todo - Project Name");
            todos.addRow("Shivano Consulting", "Milestone 1 [40]", "2012-06-01");
            todos.addRow("Shivano Consulting", "Milestone 2 [20]", "2012-07-01");
            todos.addRow("Shivano Consulting", "Milestone 3 [20]", 0);
            todos.addRow("Another Project", "", 0);
            users.addRow("James Boe", 100);
            users.addRow("Jim Bob", 75);
            projects.addRow("Shivano Consulting", 1000);
            projects.addRow("Another Project", 500);
            timeTracking.addRow(5, "James Boe", "Shivano Consulting");
            timeTracking.addRow(3, "Jim Bob", "Shivano Consulting");
            timeTracking.addRow(10, "James Boe", "Another Project");
            timeTracking.addRow(7, "Jim Bob", "Another Project");
            ReportWrapper report = warehouse.createReport();
            report.getListDefinition().setOptimized(true);
            report.getListDefinition().setMarmotScript("uniquefield(\"Project Name\")\n" +
                    "assignunique(\"Spent\", \"Projects\")\n" +
                    "assignunique(\"Budget Remaining\", \"Projects\")\n" +
                    "createnamedpipeline(\"Harvest\", \"onJoins\", \"Harvest\")\n" +
                    "assignpipeline(\"Spent\", \"Harvest\")\n" +
                    "createnamedpipeline(\"Basecamp\", \"onJoins\", \"Basecamp\")\n" +
                    "assignpipeline(\"Milestone Time\", \"Basecamp\")\n"+
                    "assignpipeline(\"Milestone Done\", \"Basecamp\")\n"+
                    "assignpipeline(\"Milestone Remaining\", \"Basecamp\")");
            report.addCalculation("Spent", "notnull([User Full Name], User Default Hourly Rate * Hours)");
            report.addCalculation("Milestone Time", "bracketvalue([Milestone])");
            report.addCalculation("Milestone Done", "greaterthan(Milestone Completed On, 0, bracketvalue(Milestone))").getAnalysisItem();
            report.addCalculation("Milestone Remaining", "greaterthan(1, Milestone Completed On, bracketvalue(Milestone), 0)").getAnalysisItem();
            report.addCalculation("Project Progress", "Milestone Done / (Milestone Remaining + Milestone Done) * 100").getAnalysisItem();
            report.addCalculation("Budget Remaining", "Budget - Spent");
            report.addField("Project Name");
            report.addField("Budget");
            report.addField("Spent");
            report.addField("Budget Remaining");
            report.addField("Milestone Done");
            report.addField("Milestone Remaining");
            report.addField("Project Progress");
            Results results = report.runReport(conn);
            results.verifyRow("Shivano Consulting", 1000, 725, 275, 60, 20, 75);
        } finally {
            Database.closeConnection(conn);
        }
    }



    public void testAttentionScenarioWithClient() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper todos = DataSourceWrapper.createDataSource("Todo", conn, "Todo - Project Name", GROUPING, "Milestone", GROUPING);
            DataSourceWrapper basecamp = DataSourceWrapper.createJoinedSource("Basecamp", conn, todos);
            DataSourceWrapper clients = DataSourceWrapper.createDataSource("Clients", conn, "Client Name", GROUPING);
            DataSourceWrapper users = DataSourceWrapper.createDataSource("Users", conn, "User Full Name", GROUPING, "User Default Hourly Rate", MEASURE);
            DataSourceWrapper projects = DataSourceWrapper.createDataSource("Projects", conn, "Project Name", GROUPING, "Budget", MEASURE, "Project Client", GROUPING);
            DataSourceWrapper timeTracking = DataSourceWrapper.createDataSource("Time Tracking", conn, "Hours", MEASURE, "Time Tracking User", GROUPING, "Time Tracking Project", GROUPING);
            DataSourceWrapper harvest = DataSourceWrapper.createJoinedSource("Harvest", conn, users, projects, timeTracking, clients);
            DataSourceWrapper warehouse = DataSourceWrapper.createJoinedSource("Warehouse", conn, basecamp, harvest);
            harvest.join(users, timeTracking, "User Full Name", "Time Tracking User");
            harvest.join(timeTracking, projects, "Time Tracking Project", "Project Name");
            harvest.join(clients, projects, "Client Name", "Project Client");
            warehouse.join(harvest, basecamp, "Project Name", "Todo - Project Name");
            todos.addRow("Shivano Consulting", "Milestone 1 [50]");
            todos.addRow("Shivano Consulting", "Milestone 2 [25]");
            todos.addRow("Another Project", "");
            users.addRow("James Boe", 100);
            users.addRow("Jim Bob", 75);
            clients.addRow("Client A");
            clients.addRow("Client B");
            projects.addRow("Shivano Consulting", 1000, "Client A");
            projects.addRow("Another Project", 500, "Client A");
            projects.addRow("Unrelated Project", 750, "Client B");
            timeTracking.addRow(5, "James Boe", "Shivano Consulting");
            timeTracking.addRow(3, "Jim Bob", "Shivano Consulting");
            timeTracking.addRow(10, "James Boe", "Another Project");
            timeTracking.addRow(7, "Jim Bob", "Another Project");
            timeTracking.addRow(8, "James Boe", "Unrelated Project");
            timeTracking.addRow(8, "Jim Bob", "Unrelated Project");
            ReportWrapper report = warehouse.createReport();
            report.getListDefinition().setMarmotScript("uniquefield(\"Project Name\")\n" +
                    "assignunique(\"Spent\", \"Projects\")\n" +
                    "assignunique(\"Budget Remaining\", \"Projects\")\n" +
                    "createnamedpipeline(\"Harvest\", \"onJoins\", \"Harvest\")\n" +
                    "assignpipeline(\"Spent\", \"Harvest\")\n" +
                    "createnamedpipeline(\"Basecamp\", \"onJoins\", \"Basecamp\")\n" +
                    "assignpipeline(\"Milestone Time\", \"Basecamp\")");
            report.addCalculation("Spent", "notnull([User Full Name], User Default Hourly Rate * Hours)");
            report.addCalculation("Milestone Time", "bracketvalue([Milestone])");
            report.addCalculation("Budget Remaining", "Budget - Spent");
            report.addField("Client Name");
            report.addField("Budget");
            report.addField("Spent");
            report.addField("Budget Remaining");
            report.addField("Milestone Time");
            Results results = report.runReport(conn);
            results.verifyRow("Client A", 1500, 2250, -750, 75);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
