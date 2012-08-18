package test.pipeline;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.basecamp.BaseCampTimeSource;
import com.easyinsight.datafeeds.basecamp.BaseCampTodoSource;
import junit.framework.TestCase;
import test.util.TestUtil;

/**
 * User: jamesboe
 * Date: 7/30/12
 * Time: 9:57 AM
 */
public class HorribleBasecampTest extends TestCase implements ITestConstants {
    protected void setUp() throws Exception {
        if (Database.instance() == null) {
            Database.initialize();
        }
        FeedRegistry.initialize();
        TestUtil.getIndividualTestUser();
    }

    public void testBasecampHack() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            DataSourceWrapper basecamp = DataSourceWrapper.createDataSource(FeedType.BASECAMP_MASTER, conn);
            DataSourceWrapper todos = basecamp.find(FeedType.BASECAMP, conn);
            DataSourceWrapper timeTracking = basecamp.find(FeedType.BASECAMP_TIME, conn);
            todos.addNamedRow(BaseCampTodoSource.PROJECTID, "1", BaseCampTodoSource.ITEMID, "1", BaseCampTodoSource.COUNT, 1, BaseCampTodoSource.MILESTONENAME, "Milestone 1");
            todos.addNamedRow(BaseCampTodoSource.PROJECTID, "1", BaseCampTodoSource.ITEMID, "2", BaseCampTodoSource.COUNT, 1, BaseCampTodoSource.MILESTONENAME, "Milestone 1");
            todos.addNamedRow(BaseCampTodoSource.PROJECTID, "1", BaseCampTodoSource.ITEMID, "3", BaseCampTodoSource.COUNT, 1, BaseCampTodoSource.MILESTONENAME, "Milestone 2");
            todos.addNamedRow(BaseCampTodoSource.PROJECTID, "2", BaseCampTodoSource.ITEMID, "4", BaseCampTodoSource.COUNT, 1, BaseCampTodoSource.MILESTONENAME, "Milestone 3");
            timeTracking.addNamedRow(BaseCampTimeSource.PROJECTID, "1", BaseCampTimeSource.TODOID, "1", BaseCampTimeSource.COUNT, 1, BaseCampTimeSource.HOURS, 3);
            timeTracking.addNamedRow(BaseCampTimeSource.PROJECTID, "1", BaseCampTimeSource.TODOID, "1", BaseCampTimeSource.COUNT, 1, BaseCampTimeSource.HOURS, 3);
            timeTracking.addNamedRow(BaseCampTimeSource.PROJECTID, "1", BaseCampTimeSource.TODOID, "2", BaseCampTimeSource.COUNT, 1, BaseCampTimeSource.HOURS, 2);
            timeTracking.addNamedRow(BaseCampTimeSource.PROJECTID, "1", BaseCampTimeSource.TODOID, "2", BaseCampTimeSource.COUNT, 1, BaseCampTimeSource.HOURS, 2);
            timeTracking.addNamedRow(BaseCampTimeSource.PROJECTID, "2", BaseCampTimeSource.COUNT, 1, BaseCampTimeSource.HOURS, 5);
            ReportWrapper projectReport = basecamp.createReport();
            projectReport.addField("Todo - Project ID");
            projectReport.addField(BaseCampTimeSource.HOURS);
            Results results = projectReport.runReport(conn);
            results.verifyRow("1", 10);
            results.verifyRow("2", 5);
            ReportWrapper todoReport = basecamp.createReport();
            todoReport.addField("Item ID");
            todoReport.addField(BaseCampTimeSource.HOURS);
            Results itemResults = todoReport.runReport(conn);
            itemResults.verifyRow("1", 6);
            itemResults.verifyRow("2", 4);
            itemResults.verifyRow("3", 0);
            itemResults.verifyRow("4", 0);
            ReportWrapper milestoneReport = basecamp.createReport();
            milestoneReport.addField(BaseCampTodoSource.MILESTONENAME);
            milestoneReport.addField(BaseCampTimeSource.HOURS);
            Results milestoneResults = milestoneReport.runReport(conn);
            milestoneResults.verifyRow("Milestone 1", 10);
            milestoneResults.verifyRow("Milestone 2", 0);
            milestoneResults.verifyRow("Milestone 3", 0);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
