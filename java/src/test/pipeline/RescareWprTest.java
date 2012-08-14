package test.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedRegistry;
import junit.framework.TestCase;
import test.util.TestUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: 7/20/12
 * Time: 12:16 PM
 */
public class RescareWprTest extends TestCase implements ITestConstants {
    private DataSourceWrapper rescare;
    private DataSourceWrapper participants;
    private DataSourceWrapper activities;

    private ReportWrapper report;
    private Date today;
    private Date afterToday;
    private Date beforeToday;

    protected void setUp() throws Exception {
        if (Database.instance() == null) {
            Database.initialize();
        }
        FeedRegistry.initialize();
        TestUtil.getIndividualTestUser();
    }

    public void createReport(EIConnection conn) throws Exception {
        participants = DataSourceWrapper.createDataSource("Participants", conn, "Participant ID", GROUPING, "Original Roster Date", DATE);
        activities = DataSourceWrapper.createDataSource("Activities", conn, "Activities - Record ID#", GROUPING, "Related Participant", GROUPING, "Activity Type", GROUPING);

        rescare = DataSourceWrapper.createJoinedSource("Franklin", conn, participants, activities);
        rescare.join(participants, activities, "Participant ID", "Related Participant");

        report = rescare.createReport();
        report.addDerivedGrouping("Core Activity", "case([Activity Type], \"Training - Related to Employment\", \"false\", \"Training - Secondary School\", \"false\", \"Training - GED\", \"false\", \"Job Skills Workshop\", \"false\", \"ResCare Academy - Non-Core\", \"false\", \"true\")", false);
        report.addField("Activities - Record ID#");
        report.addField("Core Activity");


    }

    public void testCoreActivities() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            createReport(conn);

            participants.addRow("123456", "2012-06-06");
            activities.addRow("1", "123456", "Training - Related to Employment");
            activities.addRow("2", "123456", "Training - Secondary School");
            activities.addRow("3", "123456", "Training - GED");
            activities.addRow("4", "123456", "Job Skills Workshop");
            activities.addRow("5", "123456", "ResCare Academy - Non-Core");
            activities.addRow("6", "123456", "Job Search");
            activities.addRow("7", "123456", "Community Work Experience");
            Results results = report.runReport(conn);
            results.verifyRow("1", "false");
            results.verifyRow("2", "false");
            results.verifyRow("3", "false");
            results.verifyRow("4", "false");
            results.verifyRow("5", "false");
            results.verifyRow("6", "true");
            results.verifyRow("7", "true");
            results.verifyRowCount(1);
        } finally {
            Database.closeConnection(conn);
        }
    }

}
