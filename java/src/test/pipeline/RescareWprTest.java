package test.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedRegistry;
import junit.framework.TestCase;
import org.joda.time.Hours;
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
    private DataSourceWrapper hours;

    private ReportWrapper report;

    private static final double HOURS = 60 * 60 * 1000;


    protected void setUp() throws Exception {
        if (Database.instance() == null) {
            Database.initialize();
        }
        FeedRegistry.initialize();
        TestUtil.getIndividualTestUser();
    }

    public void createReport(EIConnection conn) throws Exception {
        participants = DataSourceWrapper.createDataSource("Participants", conn, "Participant ID", GROUPING, "Original Roster Date", DATE);
        activities = DataSourceWrapper.createDataSource("Activities", conn, "Activities - Record ID#", GROUPING, "Activities - Related Participant", GROUPING, "Activity Type", GROUPING);
        hours = DataSourceWrapper.createDataSource("Hours", conn, "Hours - Record ID#", GROUPING, "Hours - Related Activity", GROUPING, "Time In", MEASURE, "Time Out", MEASURE, "Lunch Time Out", MEASURE, "Lunch Time In", MEASURE, "Status", GROUPING, "Lunch Hour", GROUPING, "Good Cause Approved", GROUPING, "Scheduled Time In", MEASURE, "Scheduled Time Out", MEASURE);

        rescare = DataSourceWrapper.createJoinedSource("Franklin", conn, participants, activities, hours);
        rescare.join(participants, activities, "Participant ID", "Activities - Related Participant");
        rescare.join(activities, hours, "Activities - Record ID#", "Hours - Related Activity");

        report = rescare.createReport();
        report.addDerivedGrouping("Core Activity", "case([Activity Type], \"Training - Related to Employment\", \"false\", \"Training - Secondary School\", \"false\", \"Training - GED\", \"false\", \"Job Skills Workshop\", \"false\", \"ResCare Academy - Non-Core\", \"false\", \"true\")", true);
        report.addCalculation("Calculated Lunch Hours", "equalTo(Lunch Hour, \"true\", (Lunch Time In - Lunch Time Out)/(60 * 60 * 1000), 0)");
        report.addCalculation("Calculated Hours", "((Time Out - Time In)/(60*60*1000)) - Calculated Lunch Hours");
        report.addCalculation("Calculated Scheduled Hours", "((Scheduled Time Out - Scheduled Time In) / (60 * 60 * 1000)) - equalTo(Lunch Hour, \"true\", 1, 0)");
        report.addCalculation("Calculated Absent Hours", "max(0, Calculated Scheduled Hours - Calculated Hours)");
        report.addField("Activities - Record ID#");
        report.addField("Core Activity");
        FieldWrapper w = report.addCalculation("Calculated Excused Hours", "Calculated Absent Hours", false);
        List<Object> values = new ArrayList<Object>();
        values.add("Submitted");
        values.add(new EmptyValue());
        FilterDefinition f = new FilterValueDefinition(report.getField("Calculated Excused Hours").getAnalysisItem(), false, values);
        w.addFilter(f);
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
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testCalculatedHours() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            createReport(conn);
            report.addField("Hours - Record ID#");
            report.addField("Calculated Hours");

            participants.addRow("123456", "2012-06-06");
            activities.addRow("1", "123456", "Community Work Experience");
            hours.addRow("1", "1", 6 * HOURS, 15 * HOURS, 12 * HOURS, 13 * HOURS, "Submitted", "true", "false", 6 * HOURS, 15 * HOURS);
            hours.addRow("2", "1", 6 * HOURS, 15 * HOURS, 0, 0, "Submitted", "false", "false", 6 * HOURS, 15 * HOURS);


            Results results = report.runReport(conn);

            results.verifyRow("1", "true", "1", 8);
            results.verifyRow("1", "true", "2", 9);

        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testAbsentHours() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            createReport(conn);
            report.addField("Hours - Record ID#");
            report.addField("Calculated Absent Hours");
            participants.addRow("123456", "2012-06-06");
            activities.addRow("1", "123456", "Community Work Experience");
            hours.addRow("1", "1", 0, 0, 0, 0, "Submitted", "true", "false", 6 * HOURS, 15 * HOURS);
            hours.addRow("2", "1", 6 * HOURS, 12 * HOURS, 0, 0, "Submitted", "true", "false", 6 * HOURS, 15 * HOURS);
            hours.addRow("3", "1", 6 * HOURS, 16 * HOURS, 12 * HOURS, 13 * HOURS, "Submitted", "true", "false", 6 * HOURS, 15 * HOURS);

            Results results = report.runReport(conn);
            results.verifyRow("1", "true", "1", 8);
            results.verifyRow("1", "true", "2", 2);
            results.verifyRow("1", "true", "3", 0);

        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testExcusedHours() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            createReport(conn);
            report.addField("Calculated Excused Hours");
            participants.addRow("123456", "2012-06-06");
            activities.addRow("1", "123456", "Community Work Experience");
            hours.addRow("1", "1", 0, 0, 0, 0, "Approved", "true", "false", 6 * HOURS, 15 * HOURS);
            hours.addRow("2", "1", 6 * HOURS, 12 * HOURS, 0, 0, "Approved", "true", "true", 6 * HOURS, 15 * HOURS);
            hours.addRow("3", "1", 6 * HOURS, 16 * HOURS, 12 * HOURS, 13 * HOURS, "Approved", "true", "false", 6 * HOURS, 15 * HOURS);
            hours.addRow("4", "1", 6 * HOURS, 12 * HOURS, 0, 0, "Approved", "true", "true", 6 * HOURS, 15 * HOURS);
            hours.addRow("5", "1", 6 * HOURS, 12 * HOURS, 0, 0, "Approved", "true", "true", 6 * HOURS, 15 * HOURS);
            hours.addRow("6", "1", 6 * HOURS, 12 * HOURS, 0, 0, "Approved", "true", "true", 6 * HOURS, 15 * HOURS);
            Results results = report.runReport(conn);
            results.verifyRow("1", "true", 2);


        } finally {
            Database.closeConnection(conn);
        }
    }

}
