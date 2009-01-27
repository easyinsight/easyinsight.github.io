package com.easyinsight.goals;

import java.util.List;

/**
 * User: James Boe
 * Date: Oct 23, 2008
 * Time: 3:06:43 PM
 */
public class Goal {
    private List<GoalCondition> goalCondition;
    private long analysisID;

    public long getAnalysisID() {
        return analysisID;
    }

    public void setAnalysisID(long analysisID) {
        this.analysisID = analysisID;
    }

    public List<GoalCondition> getGoalCondition() {
        return goalCondition;
    }

    public void setGoalCondition(List<GoalCondition> goalCondition) {
        this.goalCondition = goalCondition;
    }
}
