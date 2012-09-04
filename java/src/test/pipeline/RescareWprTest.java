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
import java.util.Arrays;
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
    private DataSourceWrapper participantStatus;

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
        participants = DataSourceWrapper.createDataSource("Participants", conn, "Participant ID", GROUPING, "Original Roster Date", DATE, "Required Monthly Core Hours (IM)", MEASURE, "Deemed Hours (IM)", MEASURE);
        activities = DataSourceWrapper.createDataSource("Activities", conn, "Activities - Record ID#", GROUPING, "Activities - Related Participant", GROUPING, "Activity Type", GROUPING);
        hours = DataSourceWrapper.createDataSource("Hours", conn, "Hours - Record ID#", GROUPING, "Hours - Related Activity", GROUPING, "Time In", MEASURE, "Time Out", MEASURE, "Lunch Time Out", MEASURE, "Lunch Time In", MEASURE, "Status", GROUPING, "Lunch Hour", GROUPING, "Good Cause Approved", GROUPING, "Scheduled Time In", MEASURE, "Scheduled Time Out", MEASURE, "Hours Date", DATE);
        participantStatus = DataSourceWrapper.createDataSource("Participant Status", conn, "Partici zpant Status - Record ID#", GROUPING, "Participant Status - Related Participant", GROUPING, "Participant Status - ResCare Participant Status Modified Date", DATE, "Participant Status - ResCare Participant Status", GROUPING);

        rescare = DataSourceWrapper.createJoinedSource("Franklin", conn, participants, activities, hours, participantStatus);
        rescare.join(participants, activities, "Participant ID", "Activities - Related Participant");
        rescare.join(activities, hours, "Activities - Record ID#", "Hours - Related Activity");
        rescare.join(participants, participantStatus, "Participant ID", "Participant Status - Related Participant");

        report = rescare.createReport();
        report.addDerivedGrouping("Core Activity", "case([Activity Type], \"Training - Related to Employment\", \"false\", \"Training - Secondary School\", \"false\", \"Training - GED\", \"false\", \"Job Skills Workshop\", \"false\", \"ResCare Academy - Non-Core\", \"false\", \"true\")", true);
        report.addCalculation("Calculated Lunch Hours", "equalTo(Lunch Hour, \"true\", (Lunch Time In - Lunch Time Out)/(60 * 60 * 1000), 0)", true);
        report.addCalculation("Calculated Hours", "((Time Out - Time In)/(60*60*1000)) - Calculated Lunch Hours", true);
        report.addCalculation("Calculated Scheduled Hours", "((Scheduled Time Out - Scheduled Time In) / (60 * 60 * 1000)) - equalTo(Lunch Hour, \"true\", 1, 0)", true);
        report.addCalculation("Calculated Absent Hours", "max(0, Calculated Scheduled Hours - Calculated Hours)", true);
        FieldWrapper w = report.addCalculation("Calculated Excused Hours", "Calculated Absent Hours", true);

        FieldWrapper ww = report.addCalculation("Calculated Approved Hours", "Calculated Hours", true);
        ww.addFilter(new FilterValueDefinition(report.getField("Status").getAnalysisItem(), false, Arrays.asList((Object) "Submitted", "")));

        List<Object> values = new ArrayList<Object>();
        values.add("Submitted");
        values.add("");
        FilterDefinition f = new FilterValueDefinition(report.getField("Status").getAnalysisItem(), false, values);
        w.addFilter(f);
        w.addFilter(new FilterValueDefinition(report.getField("Good Cause Approved").getAnalysisItem(), true, Arrays.asList((Object) "true")));

        report.addCalculation("Countable Hours", "Calculated Approved Hours + Calculated Excused Hours");
        FieldWrapper fw = report.addCalculation("Countable Core Hours", "Calculated Approved Hours + Calculated Excused Hours");
        fw.addFilter(new FilterValueDefinition(report.getField("Core Activity").getAnalysisItem(), true, Arrays.asList((Object) "true")));

        report.addDerivedGrouping("Counts Towards WPR", "greaterThan([Required Monthly Core Hours (IM)], 0, \"true\", \"false\")", true);
        report.addDerivedGrouping("Meets WPR", "greaterThan([Required Monthly Core Hours (IM)], Countable Core Hours + [Deemed Hours (IM)], \"false\", \"true\")", false);

        report.getListDefinition().setMarmotScript(
                "createnamedpipeline(\"Middle1\", \"afterJoins\", \"1\")\n" +
                "assignPipeline(\"Countable Hours\", \"Middle1\")\n" +
                "assignPipeline(\"Countable Core Hours\", \"Middle1\")\n" +
                "uniquefield(\"Participant ID\")\n" +
//                        "additionalgrouping(\"Participant ID\")\n" +
                "\n");

        FieldWrapper status = report.addDerivedGrouping("Current Participant Status", "[Participant Status - ResCare Participant Status]", false);

        FilterDefinition dd = new LastValueFilter();
        dd.setFilterName("Last Date Filter");
        dd.setField(report.getField("Participant Status - ResCare Participant Status Modified Date").getAnalysisItem());

        status.addFilter(dd);
//          report.addFilter(dd);

    }

    public void createActivityReport(EIConnection conn) throws Exception {
        createReport(conn);
        report.addField("Activities - Record ID#");
        report.addField("Core Activity");
    }

    public void testWprCounts() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            createReport(conn);
            report.addField("Participant ID");
            report.addField("Counts Towards WPR");
            participants.addRow("123456", "2012-06-06", 20, 10);
            participants.addRow("123459", "2012-06-06", 0, 0);
            Results results = report.runReport(conn);
            results.verifyRow("123456", "true");
            results.verifyRow("123459", "false");
        } finally {
            Database.closeConnection(conn);
        }
    }

//    public void testMeetsWpr() throws Exception {
//        EIConnection conn = Database.instance().getConnection();
//        try {
//            createReport(conn);
//            report.addField("Participant ID");
//            report.addField("Meets WPR");
//            participants.addRow("123456", "2012-06-06", 20, 10);
//            activities.addRow("1", "123456", "Job Search");
//            hours.addRow("1", "1", 8 * HOURS, 17 * HOURS, 12 * HOURS, 13 * HOURS, "Approved", "true", "false", 8 * HOURS, 17 * HOURS, "2012-06-07");
//            hours.addRow("2", "1", 8 * HOURS, 17 * HOURS, 12 * HOURS, 13 * HOURS, "Approved", "true", "false", 8 * HOURS, 17 * HOURS, "2012-06-08");
//
//
//            activities.addRow("2", "123456", "Training - Related to Employment");
//            hours.addRow("3", "2", 8 * HOURS, 17 * HOURS, 12 * HOURS, 13 * HOURS, "Approved", "true", "false", 8 * HOURS, 17 * HOURS, "2012-06-09");
//
//            participants.addRow("123459", "2012-06-06", 20, 0);
//            activities.addRow("3", "123459", "Job Search");
//            hours.addRow("3", "3", 0, 0, 0, 0, "Approved", "true", "false", 8 * HOURS, 17 * HOURS, "2012-06-07");
//            hours.addRow("4", "3", 8 * HOURS, 12 * HOURS, 0, 0, "Approved", "true", "true", 8 * HOURS, 17 * HOURS, "2012-06-08");
//            Results results = report.runReport(conn);
//            results.verifyRow("123456", "true");
//            results.verifyRow("123459", "false");
//        } finally {
//            Database.closeConnection(conn);
//        }
//    }

    public void testCoreActivities() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            createActivityReport(conn);

            participants.addRow("123456", "2012-06-06", 0, 0);
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
            createActivityReport(conn);
            report.addField("Hours - Record ID#");
            report.addField("Calculated Hours");

            participants.addRow("123456", "2012-06-06", 0, 0);
            activities.addRow("1", "123456", "Community Work Experience");
            hours.addRow("1", "1", 6 * HOURS, 15 * HOURS, 12 * HOURS, 13 * HOURS, "Submitted", "true", "false", 6 * HOURS, 15 * HOURS, "2012-06-07");
            hours.addRow("2", "1", 6 * HOURS, 15 * HOURS, 0, 0, "Submitted", "false", "false", 6 * HOURS, 15 * HOURS, "2012-06-08");


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
            createActivityReport(conn);
            report.addField("Hours - Record ID#");
            report.addField("Calculated Absent Hours");
            participants.addRow("123456", "2012-06-06", 0, 0);
            activities.addRow("1", "123456", "Community Work Experience");
            hours.addRow("1", "1", 0, 0, 0, 0, "Submitted", "true", "false", 6 * HOURS, 15 * HOURS, "2012-06-07");
            hours.addRow("2", "1", 6 * HOURS, 12 * HOURS, 0, 0, "Submitted", "true", "false", 6 * HOURS, 15 * HOURS, "2012-06-08");
            hours.addRow("3", "1", 6 * HOURS, 16 * HOURS, 12 * HOURS, 13 * HOURS, "Submitted", "true", "false", 6 * HOURS, 15 * HOURS, "2012-06-09");

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
            createActivityReport(conn);
            report.addField("Calculated Excused Hours");
            participants.addRow("123456", "2012-06-06", 0, 0);
            activities.addRow("1", "123456", "Community Work Experience");
            hours.addRow("1", "1", 0, 0, 0, 0, "Approved", "true", "false", 6 * HOURS, 15 * HOURS, "2012-06-07");
            hours.addRow("2", "1", 6 * HOURS, 12 * HOURS, 0, 0, "Approved", "true", "true", 6 * HOURS, 15 * HOURS, "2012-06-08");
            hours.addRow("3", "1", 6 * HOURS, 16 * HOURS, 12 * HOURS, 13 * HOURS, "Approved", "true", "false", 6 * HOURS, 15 * HOURS, "2012-06-09");
            hours.addRow("4", "1", 6 * HOURS, 12 * HOURS, 0, 0, "Submitted", "true", "true", 6 * HOURS, 15 * HOURS, "2012-06-10");
            hours.addRow("5", "1", 6 * HOURS, 12 * HOURS, 0, 0, "", "true", "true", 6 * HOURS, 15 * HOURS, "2012-06-11");
            Results results = report.runReport(conn);
            results.verifyRow("1", "true", 2);


        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testCountableHours() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            createActivityReport(conn);
            report.addField("Countable Hours");
            participants.addRow("123456", "2012-06-06", 0, 0);
            activities.addRow("1", "123456", "Community Work Experience");
            hours.addRow("1", "1", 0, 0, 0, 0, "Approved", "true", "false", 6 * HOURS, 15 * HOURS, "2012-06-07");
            hours.addRow("2", "1", 6 * HOURS, 12 * HOURS, 0, 0, "Approved", "true", "true", 6 * HOURS, 15 * HOURS, "2012-06-08"); // 6am - 12pm, excused for the rest
            hours.addRow("3", "1", 6 * HOURS, 16 * HOURS, 12 * HOURS, 13 * HOURS, "Approved", "true", "false", 6 * HOURS, 15 * HOURS, "2012-06-09"); // 6am - 4pm, lunch from 12pm to 1pm, scheduled to work from 6am - 3pm
            hours.addRow("4", "1", 6 * HOURS, 12 * HOURS, 0, 0, "Submitted", "true", "true", 6 * HOURS, 15 * HOURS, "2012-06-10");
            hours.addRow("5", "1", 6 * HOURS, 12 * HOURS, 0, 0, "", "true", "true", 6 * HOURS, 15 * HOURS, "2012-06-11");
            Results results = report.runReport(conn);
            results.verifyRow("1", "true", 17);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testTotalCoreHours() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            createActivityReport(conn);

            report.addField("Countable Core Hours");
            participants.addRow("123456", "2012-06-06", 0, 0);
            activities.addRow("1", "123456", "Community Work Experience");
            hours.addRow("1", "1", 0, 0, 0, 0, "Approved", "true", "false", 6 * HOURS, 15 * HOURS, "2012-06-07");
            hours.addRow("2", "1", 6 * HOURS, 12 * HOURS, 0, 0, "Approved", "true", "true", 6 * HOURS, 15 * HOURS, "2012-06-08");
            hours.addRow("3", "1", 6 * HOURS, 16 * HOURS, 12 * HOURS, 13 * HOURS, "Approved", "true", "false", 6 * HOURS, 15 * HOURS, "2012-06-09");
            hours.addRow("4", "1", 6 * HOURS, 12 * HOURS, 0, 0, "Submitted", "true", "true", 6 * HOURS, 15 * HOURS, "2012-06-10");
            hours.addRow("5", "1", 6 * HOURS, 12 * HOURS, 0, 0, "", "true", "true", 6 * HOURS, 15 * HOURS, "2012-06-11");
            activities.addRow("2", "123456", "Training - GED");
            hours.addRow("6", "2", 6 * HOURS, 15 * HOURS, 12 * HOURS, 13 * HOURS, "Approved", "true", "false", 6 * HOURS, 15 * HOURS, "2012-06-12");
            hours.addRow("7", "2", 6 * HOURS, 15 * HOURS, 12 * HOURS, 13 * HOURS, "Approved", "true", "false", 6 * HOURS, 15 * HOURS, "2012-06-13");
            Results results = report.runReport(conn);
            results.verifyRow("1", "true", 17.0);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testParticipantStatus() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            createReport(conn);
            report.addField("Participant ID");
            report.addField("Current Participant Status");
            participants.addRow("123456", "2012-06-06", 0, 0);
            participantStatus.addRow("1", "123456", "2012-01-01", "Active");
            participantStatus.addRow("2", "123456", "2012-02-01", "InActive");
            participantStatus.addRow("3", "123456", "2012-06-06", "Active");

            participants.addRow("123457", "2012-06-06", 0, 0);
            participantStatus.addRow("4", "123457", "2012-06-06", "Active");
            participantStatus.addRow("5", "123457", "2012-06-08", "InActive");

            Results results = report.runReport(conn);

            results.verifyRow("123456", "Active");
            results.verifyRow("123457", "InActive");
            results.verifyRowCount(2);
        } finally {
            Database.closeConnection(conn);
        }
    }

}