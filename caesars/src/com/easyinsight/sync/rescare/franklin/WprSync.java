package com.easyinsight.sync.rescare.franklin;

import com.easyinsight.helper.*;
import nu.xom.Node;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 10/8/12
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class WprSync extends BaseSyncThread {

    public static final String ACTIVE_DURING_MONTH = "ActiveDuringMonth";
    public static final String EFFECTIVE_MONTH = "EffectiveMonth";
    public static final String PARTICIPANT_ID = "ParticipantID";
    public static final String SCHEDULED_TO_MEET_CORE_WPR = "Scheduled To Meet Core WPR";
    public static final String SCHEDULED_TO_MEET_NON_CORE_WPR = "Scheduled to Meet Non-Core WPR";
    public static final String MEETING_WPR = "Meeting WPR";
    public static final String REQUIRED_MONTHLY_CORE_HOURS = "Required Monthly Core Hours";
    public static final String REQUIRED_MONTHLY_NON_CORE_HOURS = "Required Monthly Non-Core Hours";
    public static final String DEEMED_HOURS = "Deemed Hours";
    public static final String EMPLOYED_AT_ABOVE_WPR = "Employed At/Above WPR";
    public static final String EMPLOYED_BELOW_WPR = "Employed Below WPR";

    public WprSync(String username, String password) {
        super(username, password);
        this.reportId = "EIbkeKVRJFOUrDbTATvF";
        this.sourceName = "WPR Data Mart";
    }


    public static void main(String[] args) {
        new WprSync("alanrc@easy-insight.com", "e@symone$").run();
    }

    protected void setWhereClauses(Update u, Map<String, Object> rec) {
        Calendar c = Calendar.getInstance();
        c.setTime((Date) rec.get(EFFECTIVE_MONTH));
        u.setWhereClauses(new StringWhereClause(PARTICIPANT_ID,(String) rec.get(PARTICIPANT_ID)), new DayWhereClause(EFFECTIVE_MONTH, c.get(Calendar.DAY_OF_YEAR), c.get(Calendar.YEAR)));
    }

    protected DataSourceOperationFactory defineFields(DataSourceFactory dataSourceFactory) {
        dataSourceFactory.addGrouping(PARTICIPANT_ID);
        dataSourceFactory.addDate(EFFECTIVE_MONTH);
        dataSourceFactory.addGrouping(ACTIVE_DURING_MONTH);
        dataSourceFactory.addGrouping(SCHEDULED_TO_MEET_CORE_WPR);
        dataSourceFactory.addGrouping(SCHEDULED_TO_MEET_NON_CORE_WPR);
        dataSourceFactory.addGrouping(MEETING_WPR);
        dataSourceFactory.addMeasure(REQUIRED_MONTHLY_CORE_HOURS);
        dataSourceFactory.addMeasure(REQUIRED_MONTHLY_NON_CORE_HOURS);
        dataSourceFactory.addMeasure(DEEMED_HOURS);
        dataSourceFactory.addGrouping(EMPLOYED_AT_ABOVE_WPR);
        dataSourceFactory.addGrouping(EMPLOYED_BELOW_WPR);
        return dataSourceFactory.defineDataSource();
    }


    public void processIn(Node data, List<Map<String, Object>> records) throws Exception {

        String dateStr = getField(data, "Today Date");

        Date curDate = eiDateFormat.parse(dateStr);
        Calendar currentMonthDate = Calendar.getInstance();
        currentMonthDate.setTime(curDate);
        String participantID = getField(data, "Participant ID");
        Map<String, Object> currentMonth = new HashMap<String, Object>();
        currentMonth.put(PARTICIPANT_ID, participantID);
        currentMonth.put(ACTIVE_DURING_MONTH, getField(data, "Active During Current Month"));
        currentMonth.put(EFFECTIVE_MONTH, currentMonthDate.getTime());
        currentMonth.put(SCHEDULED_TO_MEET_CORE_WPR, getField(data, "Scheduled to Meet Core WPR (CM)"));
        currentMonth.put(SCHEDULED_TO_MEET_NON_CORE_WPR, getField(data, "Scheduled to Meet Non-Core WPR (CM)"));
        currentMonth.put(MEETING_WPR, getField(data, "Meeting WPR (CM)"));
        currentMonth.put(REQUIRED_MONTHLY_CORE_HOURS, getField(data, "Current Required Monthly Core Hours"));
        currentMonth.put(REQUIRED_MONTHLY_NON_CORE_HOURS, getField(data, "Current Required Monthly Non-Core Hours"));
        currentMonth.put(DEEMED_HOURS, getField(data, "Current Deemed Hours"));
        currentMonth.put(EMPLOYED_AT_ABOVE_WPR, getField(data, "Employed At/Above WPR"));
        currentMonth.put(EMPLOYED_BELOW_WPR, getField(data, "Employed Below WPR"));

        records.add(currentMonth);

        Map<String, Object> pastMonth = new HashMap<String, Object>();
        Calendar pastMonthDate = Calendar.getInstance();
        pastMonthDate.setTime(curDate);
        pastMonthDate.add(Calendar.MONTH, -1);
        pastMonth.put(PARTICIPANT_ID, participantID);
        pastMonth.put(ACTIVE_DURING_MONTH, getField(data, "Active During Previous Month"));
        pastMonth.put(EFFECTIVE_MONTH, pastMonthDate.getTime());
        pastMonth.put(SCHEDULED_TO_MEET_CORE_WPR, getField(data, "Scheduled to Meet Core WPR (PM)"));
        pastMonth.put(SCHEDULED_TO_MEET_NON_CORE_WPR, getField(data, "Scheduled to Meet Non-Core WPR (PM)"));
        pastMonth.put(MEETING_WPR, getField(data, "Meeting WPR (PM)"));
        pastMonth.put(REQUIRED_MONTHLY_CORE_HOURS, getField(data, "PM Required Monthly Core Hours"));
        pastMonth.put(REQUIRED_MONTHLY_NON_CORE_HOURS, getField(data, "PM Required Monthly Non-Core Hours"));
        pastMonth.put(DEEMED_HOURS, getField(data, "PM Deemed Hours"));
        pastMonth.put(EMPLOYED_AT_ABOVE_WPR, getField(data, "Employed PM At/Above WPR"));
        pastMonth.put(EMPLOYED_BELOW_WPR, getField(data, "Employed PM Below WPR"));

        records.add(pastMonth);
    }

}