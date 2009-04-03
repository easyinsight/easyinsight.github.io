package com.easyinsight.goals;

import java.util.Date;

/**
 * User: James Boe
 * Date: Oct 23, 2008
 * Time: 6:57:01 PM
 */
public class GoalTreeMilestone {
    private Date milestoneDate;
    private long milestoneID;
    private String milestoneName;

    public Date getMilestoneDate() {
        return milestoneDate;
    }

    public void setMilestoneDate(Date milestoneDate) {
        this.milestoneDate = milestoneDate;
    }

    public long getMilestoneID() {
        return milestoneID;
    }

    public void setMilestoneID(long milestoneID) {
        this.milestoneID = milestoneID;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }
}
