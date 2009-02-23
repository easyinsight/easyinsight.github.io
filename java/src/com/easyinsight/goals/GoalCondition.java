package com.easyinsight.goals;

import com.easyinsight.analysis.AnalysisItem;

/**
 * User: James Boe
 * Date: Oct 23, 2008
 * Time: 7:40:27 PM
 */
public abstract class GoalCondition {
    private AnalysisItem analysisItem;
    private long goalConditionID;

    public long getGoalConditionID() {
        return goalConditionID;
    }

    public void setGoalConditionID(long goalConditionID) {
        this.goalConditionID = goalConditionID;
    }

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public void setAnalysisItem(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    public abstract double calculateScore();

    // we know the milestone, we have a time interval configured...
    private void calculate(long milestoneTime, int milestoneInterval, long startTime) {
        
    }

    private void calculateChange() {
        
    }

    private Double getNewValue(int timeID) {
        return null;
    }

    private Double getExistingValue(int timeID) {
        return null;
    }

    // given a data set of results...
    // each row will have a different value that we'll apply through this condition
    // we need the last value for each row as well...
}
