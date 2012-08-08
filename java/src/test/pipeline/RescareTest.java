package test.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedRegistry;
import junit.framework.TestCase;
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
public class RescareTest extends TestCase implements ITestConstants {
    private DataSourceWrapper rescare;
    private DataSourceWrapper appointments;
    private DataSourceWrapper participants;
    private DataSourceWrapper referrals;

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

        today = new Date();

        afterToday = new Date(today.getTime() + 2 * 60 * 60 * 24 * 1000);
        beforeToday = new Date(today.getTime() - 2 * 60 * 60 * 24 * 1000);
    }

    public void createReport(EIConnection conn) throws Exception {
        participants = DataSourceWrapper.createDataSource("Participants", conn, "Participant ID", GROUPING, "Original Roster Date", DATE);
        appointments = DataSourceWrapper.createDataSource("Appointments", conn, "Appointment - Record ID#", GROUPING, "Appointment - Related Participant", GROUPING, "Appointment Type", GROUPING, "Appointment Date", DATE, "No Show", GROUPING);
        referrals = DataSourceWrapper.createDataSource("Referrals", conn, "Referral - Record ID#", GROUPING, "Referral - Related Participant", GROUPING, "Referral Type", GROUPING, "Referral Date", DATE);

        rescare = DataSourceWrapper.createJoinedSource("Franklin", conn, participants, referrals, appointments);
        rescare.join(participants, appointments, "Participant ID", "Appointment - Related Participant");
        rescare.join(participants, referrals, "Participant ID", "Referral - Related Participant");
        report = rescare.createReport();
        List<Object> values = new ArrayList<Object>();
        values.add("Orientation");
        FilterDefinition appointmentFilter = new FilterValueDefinition(report.getField("Appointment Type").getAnalysisItem(), true, values);
        report.addFilter(appointmentFilter);
        report.addField("Participant ID");
        report.addField("Appointment - Record ID#");
        report.addField("Appointment Date");
        report.addField("Referral - Record ID#");

        report.addCalculation("Time Between Appointment and Original Roster Date", "Appointment Date - Original Roster Date");
//        report.addField("Time Between Appointment and Original Roster Date");
        report.addCalculation("Time Between Referral and Original Roster Date", "Referral Date - Original Roster Date");
//        report.addField("Time Between Referral and Original Roster Date");

        report.addCalculation("Time Between Referral and Orientation", "(Referral Date - Appointment Date) / (60 * 60 * 1000)");
//        report.addField("Time Between Referral and Orientation");
        report.addCalculation("Hours Since Orientation", "(now() - Appointment Date) / (60 * 60 * 1000)");

        report.addDerivedDate("Counted Date", "greaterThan(Referral Date, 0, greaterThan(Time Between Referral and Orientation, 72, Appointment Date + (72 * 60 * 60 * 1000), Referral Date), greaterThan(Hours Since Orientation, 72, Appointment Date + (72 * 60 * 60 * 1000), 0))", false, AnalysisDateDimension.DAY_LEVEL);
        report.addField("Counted Date");

        RollingFilterDefinition rfd = new RollingFilterDefinition();
        rfd.setField(report.getField("Appointment Date").getAnalysisItem());
        rfd.setInterval(MaterializedRollingFilterDefinition.CUSTOM);
        rfd.setCustomBeforeOrAfter(RollingFilterDefinition.BEFORE);
        rfd.setCustomIntervalType(2);
        rfd.setCustomIntervalAmount(1);
        report.addFilter(rfd);
        FilterRangeDefinition ordFilter = new FilterRangeDefinition();
        ordFilter.setLowerOperator(FilterRangeDefinition.LESS_THAN_EQUAL_TO);
        ordFilter.setStartValue(0);
        ordFilter.setStartValueDefined(true);
        ordFilter.setField(report.getField("Time Between Appointment and Original Roster Date").getAnalysisItem());
        report.addFilter(ordFilter);

        FilterRangeDefinition refOrdFilter = new FilterRangeDefinition();
        refOrdFilter.setLowerOperator(FilterRangeDefinition.LESS_THAN_EQUAL_TO);
        refOrdFilter.setStartValue(0);
        refOrdFilter.setStartValueDefined(true);
        refOrdFilter.setField(report.getField("Time Between Referral and Original Roster Date").getAnalysisItem());
        report.addFilter(refOrdFilter);

        values = new ArrayList<Object>();
        values.add("0");
        report.addFilter(new FilterValueDefinition(report.getField("No Show").getAnalysisItem(), true, values));
        values = new ArrayList<Object>();
        values.add("WEP");
        values.add("");
        report.addFilter(new FilterValueDefinition(report.getField("Referral Type").getAnalysisItem(), true, values));
    }

    public void testMultipleAppointments() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {

            createReport(conn);

            participants.addRow("123456", "2012-06-06");
            appointments.addRow("1", "123456", "Orientation", "2012-01-20", "0");
            appointments.addRow("2", "123456", "Assessment", "2012-02-01", "0");
            appointments.addRow("3", "123456", "Orientation", "2012-06-07", "1");
            appointments.addRow("6", "123456", "Orientation", "2012-06-08", "0");
            appointments.addRow("4", "123456", "Assessment", "2012-06-08", "0");
            appointments.addRow("5", "123456", "Orientation", afterToday, "0");
            referrals.addRow("1", "123456", "WEP", "2012-01-30");
            referrals.addRow("2", "123456", "JRED", "2012-02-10");
            referrals.addRow("3", "123456", "WEP", "2012-06-08");
            referrals.addRow("4", "123456", "JRED", "2012-06-10");
            referrals.addRow("5", "123456", "WEP", "2012-06-20");

            Results results = report.runReport(conn);
            results.verifyRow("123456", "6", "2012-06-08", "3");
            results.verifyRowCount(1);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testSingleAppointmentSingleReferral() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            createReport(conn);
            String participant2 = "234657";
            participants.addRow(participant2, "2012-06-08");
            appointments.addRow("6", participant2, "Orientation", "2012-06-08", "0");
            referrals.addRow("6", participant2, "WEP", "2012-06-10");

            Results results = report.runReport(conn);

            results.verifyRow(participant2, "6", "2012-06-08", "6", "2012-06-10");
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testLateReferral() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            createReport(conn);

            String participant3 = "135790";
            participants.addRow(participant3, "2012-07-01");
            appointments.addRow("7", participant3, "Orientation", "2012-07-02", "0");
            referrals.addRow("7", participant3, "WEP", "2012-07-08");

            Results results = report.runReport(conn);
            results.verifyRow(participant3, "7", "2012-07-02", "7", "2012-07-05");
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testNoReferral() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            createReport(conn);

            String participant4 = "124589";
            participants.addRow(participant4, "2012-05-30");
            appointments.addRow("8", participant4, "Orientation", "2012-05-30", "0");

            Results results = report.runReport(conn);
            results.verifyRow(participant4, "8", "2012-05-30", "", "2012-06-01");
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testRecentNoReferral() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            createReport(conn);

            String participant5 = "888888";
            participants.addRow(participant5, beforeToday);
            appointments.addRow("9", participant5, "Orientation", beforeToday, "0");

            Results results = report.runReport(conn);
            results.verifyRow(participant5, "9", beforeToday, "", new Date(0));
        } finally {
            Database.closeConnection(conn);
        }
    }
}
