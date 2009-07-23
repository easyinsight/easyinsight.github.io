package com.easyinsight.goals;

import com.easyinsight.logging.LogClass;

import java.sql.SQLException;
import java.util.Date;

/**
 * User: James Boe
 * Date: Oct 23, 2008
 * Time: 11:48:19 PM
 */
public class GoalTreeNodeData extends GoalTreeNode {
    private GoalOutcome goalOutcome;

    public GoalOutcome getGoalOutcome() {
        return goalOutcome;
    }

    public void setGoalOutcome(GoalOutcome goalOutcome) {
        this.goalOutcome = goalOutcome;
    }

    public void populateCurrentValue() {
        try {
            this.goalOutcome = new GoalEvaluationStorage().getLatestGoalValue(this);
        } catch (SQLException e) {
            LogClass.error(e);
        }
    }

    public GoalOutcome summarizeOutcomes() {
        if (getChildren() != null) {
            int worstOutcome = GoalOutcome.NO_DATA;
            boolean problemCondition = false;
            if (goalOutcome != null) {
                worstOutcome = goalOutcome.getOutcomeState();
                problemCondition = goalOutcome.isProblemEvaluated();
            }

            for (GoalTreeNode child : getChildren()) {
                GoalTreeNodeData dataChild = (GoalTreeNodeData) child;
                GoalOutcome childOutcome = dataChild.summarizeOutcomes();
                problemCondition = problemCondition || childOutcome.isProblemEvaluated();
                if (childOutcome.getOutcomeState() > worstOutcome) {
                    worstOutcome = childOutcome.getOutcomeState();
                }
            }
            if (goalOutcome == null) {
                goalOutcome = new GoalOutcome(worstOutcome, GoalOutcome.NO_DIRECTION, null, problemCondition, null, new Date(), getGoalTreeNodeID());
            } else {
                goalOutcome.setProblemEvaluated(problemCondition);
                goalOutcome.setOutcomeState(worstOutcome);
            }
        }
        if (goalOutcome == null) {
            goalOutcome = new GoalOutcome(GoalOutcome.NO_DATA, GoalOutcome.NO_DIRECTION, null, false, null, new Date(), getGoalTreeNodeID());
        }
        return goalOutcome;
    }
}
