package com.easyinsight.export;

import java.util.List;

/**
 * User: James Boe
 * Date: Feb 27, 2009
 * Time: 10:04:07 AM
 */
public class ReportSchedule {
    private int interval;
    private long reportScheduleID;
    private List<Integer> users;

    public long getReportScheduleID() {
        return reportScheduleID;
    }

    public void setReportScheduleID(long reportScheduleID) {
        this.reportScheduleID = reportScheduleID;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public List<Integer> getUsers() {
        return users;
    }

    public void setUsers(List<Integer> users) {
        this.users = users;
    }
}
