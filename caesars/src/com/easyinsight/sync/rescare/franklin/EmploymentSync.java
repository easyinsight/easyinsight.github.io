package com.easyinsight.sync.rescare.franklin;

import com.easyinsight.helper.*;
import nu.xom.Node;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 11/8/12
 * Time: 7:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class EmploymentSync extends BaseSyncThread {

    private static final String PARTICIPANT_ID = "Participant ID";
    private static final String EFFECTIVE_MONTH = "Effective Month";
    private static final String EMPLOYED = "Employed";
    private static final String FULL_TIME = "Full Time";
    private static final String PART_TIME = "Part Time";
    private static final String SEASONAL = "Seasonal";
    private static final String SUBSIDIZED = "Subsidized";
    private static final String UNSUBSIDIZED = "Unsubsidized";
    private static final String SELF_EMPLOYED = "Self Employed";
    private static final String OJT = "OJT";
    private static final String THIRTY_DAYS_VERIFIED = "30 Days Verified";
    private static final String SIXTY_DAYS_VERIFIED = "60 Days Verified";
    private static final String NINETY_DAYS_VERIFIED = "90 Days Verified";
    private static final String ONE_HUNDRED_TWENTY_DAYS_VERIFIED = "120 Days Verified";
    private static final String ONE_HUNDRED_FIFTY_DAYS_VERIFIED = "150 Days Verified";
    private static final String ONE_HUNDRED_EIGHTY_DAYS_VERIFIED = "180 Days Verified";
    private static final String THIRTY_DAYS_UNVERIFIED = "30 Days Unverified";
    private static final String SIXTY_DAYS_UNVERIFIED = "60 Days Unverified";
    private static final String NINETY_DAYS_UNVERIFIED = "90 Days Unverified";
    private static final String ONE_HUNDRED_TWENTY_DAYS_UNVERIFIED = "120 Days Unverified";
    private static final String ONE_HUNDRED_FIFTY_DAYS_UNVERIFIED = "150 Days Unverified";
    private static final String ONE_HUNDRED_EIGHTY_DAYS_UNVERIFIED = "180 Days Unverified";

    public static void main(String[] args) {
        new EmploymentSync("e@symone$", "alanrc@easy-insight.com").run();
    }

    public EmploymentSync(String password, String username) {
        super(password, username);
        this.reportId = "EIbkeKVRJFOUrDbTATvF";
        this.sourceName = "Employment Data Mart";
    }

    @Override
    protected void setWhereClauses(Update u, Map<String, Object> rec) {
        Calendar c = Calendar.getInstance();
                c.setTime((Date) rec.get(EFFECTIVE_MONTH));
                u.setWhereClauses(new StringWhereClause(PARTICIPANT_ID,(String) rec.get(PARTICIPANT_ID)), new DayWhereClause(EFFECTIVE_MONTH, c.get(Calendar.DAY_OF_YEAR), c.get(Calendar.YEAR)));
    }


    @Override
    protected DataSourceOperationFactory defineFields(DataSourceFactory dataSourceFactory) {
        dataSourceFactory.addGrouping(PARTICIPANT_ID);
        dataSourceFactory.addDate(EFFECTIVE_MONTH);
        dataSourceFactory.addGrouping(EMPLOYED);
        dataSourceFactory.addGrouping(FULL_TIME);
        dataSourceFactory.addGrouping(PART_TIME);
        dataSourceFactory.addGrouping(SEASONAL);
        dataSourceFactory.addGrouping(SUBSIDIZED);
        dataSourceFactory.addGrouping(UNSUBSIDIZED);
        dataSourceFactory.addGrouping(SELF_EMPLOYED);
        dataSourceFactory.addGrouping(OJT);
        dataSourceFactory.addGrouping(THIRTY_DAYS_VERIFIED);
        dataSourceFactory.addGrouping(SIXTY_DAYS_VERIFIED);
        dataSourceFactory.addGrouping(NINETY_DAYS_VERIFIED);
        dataSourceFactory.addGrouping(ONE_HUNDRED_TWENTY_DAYS_VERIFIED);
        dataSourceFactory.addGrouping(ONE_HUNDRED_FIFTY_DAYS_VERIFIED);
        dataSourceFactory.addGrouping(ONE_HUNDRED_EIGHTY_DAYS_VERIFIED);
        dataSourceFactory.addGrouping(THIRTY_DAYS_UNVERIFIED);
        dataSourceFactory.addGrouping(SIXTY_DAYS_UNVERIFIED);
        dataSourceFactory.addGrouping(NINETY_DAYS_UNVERIFIED);
        dataSourceFactory.addGrouping(ONE_HUNDRED_TWENTY_DAYS_UNVERIFIED);
        dataSourceFactory.addGrouping(ONE_HUNDRED_FIFTY_DAYS_UNVERIFIED);
        dataSourceFactory.addGrouping(ONE_HUNDRED_EIGHTY_DAYS_UNVERIFIED);

        return dataSourceFactory.defineDataSource();
    }

    @Override
    protected void processIn(Node data, List<Map<String, Object>> records) throws Exception {
        String dateStr = getField(data, "Today Date");
        Date curDate = eiDateFormat.parse(dateStr);
        Calendar currentMonthDate = Calendar.getInstance();
        currentMonthDate.setTime(curDate);
        String participantID = getField(data, "Participant ID");
        Map<String, Object> currentMonth = new HashMap<String, Object>();
        currentMonth.put(PARTICIPANT_ID, participantID);
        currentMonth.put(EFFECTIVE_MONTH, currentMonthDate.getTime());
        currentMonth.put(EMPLOYED, getField(data, "Employed"));
        currentMonth.put(FULL_TIME, getField(data, "Employed-Full Time"));
        currentMonth.put(PART_TIME, getField(data, "Employed-Part Time"));
        currentMonth.put(SEASONAL, getField(data, "Employed-Seasonal"));
        currentMonth.put(SUBSIDIZED, getField(data, "Employed-Subsidized"));
        currentMonth.put(UNSUBSIDIZED, getField(data, "Employed-Unsubsidized"));
        currentMonth.put(SELF_EMPLOYED, getField(data, "Employed-Self-Employed"));
        currentMonth.put(OJT, getField(data, "Employed-OJT"));
        currentMonth.put(THIRTY_DAYS_VERIFIED, getField(data, "Employed-30 Days Verified"));
        currentMonth.put(THIRTY_DAYS_UNVERIFIED, getField(data, "Employed-30 Days Unverified"));
        currentMonth.put(SIXTY_DAYS_VERIFIED, getField(data, "Employed-60 Days Verified"));
        currentMonth.put(SIXTY_DAYS_UNVERIFIED, getField(data, "Employed-60 Days Unverified"));
        currentMonth.put(NINETY_DAYS_VERIFIED, getField(data, "Employed-90 Days Verified"));
        currentMonth.put(NINETY_DAYS_UNVERIFIED, getField(data, "Employed-90 Days Unverified"));
        currentMonth.put(ONE_HUNDRED_TWENTY_DAYS_VERIFIED, getField(data, "Employed-120 Days Verified"));
        currentMonth.put(ONE_HUNDRED_TWENTY_DAYS_UNVERIFIED, getField(data, "Employed-120 Days Unverified"));
        currentMonth.put(ONE_HUNDRED_FIFTY_DAYS_VERIFIED, getField(data, "Employed-150 Days Verified"));
        currentMonth.put(ONE_HUNDRED_FIFTY_DAYS_UNVERIFIED, getField(data, "Employed-150 Days Unverified"));
        currentMonth.put(ONE_HUNDRED_EIGHTY_DAYS_VERIFIED, getField(data, "Employed-180 Days Verified"));
        currentMonth.put(ONE_HUNDRED_EIGHTY_DAYS_UNVERIFIED, getField(data, "Employed-180 Days Unverified"));

        records.add(currentMonth);


        Map<String, Object> pastMonth = new HashMap<String, Object>();
        Calendar pastMonthDate = Calendar.getInstance();
        pastMonthDate.setTime(curDate);
        pastMonthDate.add(Calendar.MONTH, -1);
        pastMonth.put(PARTICIPANT_ID, participantID);
        pastMonth.put(EFFECTIVE_MONTH, pastMonthDate.getTime());
        pastMonth.put(EMPLOYED, getField(data, "Employed PM"));
        pastMonth.put(FULL_TIME, getField(data, "Employed-Full Time (PM)"));
        pastMonth.put(PART_TIME, getField(data, "Employed-Part Time (PM)"));
        pastMonth.put(SEASONAL, getField(data, "Employed-Seasonal (PM)"));
        pastMonth.put(SUBSIDIZED, getField(data, "Employed-Subsidized (PM)"));
        pastMonth.put(UNSUBSIDIZED, getField(data, "Employed-Unsubsidized (PM)"));
        pastMonth.put(SELF_EMPLOYED, getField(data, "Employed-Self-Employed (PM)"));
        pastMonth.put(OJT, getField(data, "Employed-OJT (PM)"));
        pastMonth.put(THIRTY_DAYS_VERIFIED, getField(data, "Employed-30 Days Verified (PM)"));
        pastMonth.put(THIRTY_DAYS_UNVERIFIED, getField(data, "Employed-30 Days Unverified (PM)"));
        pastMonth.put(SIXTY_DAYS_VERIFIED, getField(data, "Employed-60 Days Verified (PM)"));
        pastMonth.put(SIXTY_DAYS_UNVERIFIED, getField(data, "Employed-60 Days Unverified (PM)"));
        pastMonth.put(NINETY_DAYS_VERIFIED, getField(data, "Employed-90 Days Verified (PM)"));
        pastMonth.put(NINETY_DAYS_UNVERIFIED, getField(data, "Employed-90 Days Unverified (PM)"));
        pastMonth.put(ONE_HUNDRED_TWENTY_DAYS_VERIFIED, getField(data, "Employed-120 Days Verified (PM)"));
        pastMonth.put(ONE_HUNDRED_TWENTY_DAYS_UNVERIFIED, getField(data, "Employed-120 Days Unverified (PM)"));
        pastMonth.put(ONE_HUNDRED_FIFTY_DAYS_VERIFIED, getField(data, "Employed-150 Days Verified (PM)"));
        pastMonth.put(ONE_HUNDRED_FIFTY_DAYS_UNVERIFIED, getField(data, "Employed-150 Days Unverified (PM)"));
        pastMonth.put(ONE_HUNDRED_EIGHTY_DAYS_VERIFIED, getField(data, "Employed-180 Days Verified (PM)"));
        pastMonth.put(ONE_HUNDRED_EIGHTY_DAYS_UNVERIFIED, getField(data, "Employed-180 Days Unverified (PM)"));

        records.add(pastMonth);

    }
}
