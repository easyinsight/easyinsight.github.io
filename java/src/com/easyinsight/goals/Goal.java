package com.easyinsight.goals;

import java.util.List;

/**
 * User: James Boe
 * Date: Oct 23, 2008
 * Time: 3:06:43 PM
 */
public class Goal {
    private List<GoalCondition> goalCondition;

    public List<GoalCondition> getGoalCondition() {
        return goalCondition;
    }

    public void setGoalCondition(List<GoalCondition> goalCondition) {
        this.goalCondition = goalCondition;
    }
}
