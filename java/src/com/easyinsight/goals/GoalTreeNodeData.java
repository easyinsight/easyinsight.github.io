package com.easyinsight.goals;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

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

    public void determineOutcome(Date startDate, Date endDate, GoalEvaluationStorage goalEvaluationStorage) {
        if (getCoreFeedID() > 0) {
            this.goalOutcome = goalEvaluationStorage.getEvaluations(getGoalTreeNodeID(), startDate, endDate,
                getGoalValue(), isHighIsGood(), 1);
        }
    }

    public GoalOutcome summarizeOutcomes() {
        if (getChildren() != null) {
            List<GoalOutcome> childOutcomes = new ArrayList<GoalOutcome>();
            if (goalOutcome != null) {
                childOutcomes.add(goalOutcome);
            }
            for (GoalTreeNode child : getChildren()) {
                GoalTreeNodeData dataChild = (GoalTreeNodeData) child;
                GoalOutcome childOutcome = dataChild.summarizeOutcomes();
                if (childOutcome.getOutcomeState() != GoalOutcome.NO_DATA) {
                    childOutcomes.add(childOutcome);
                }
            }
            if (childOutcomes.size() > 0) {
                double sumValue = 0;
                for (GoalOutcome goalOutcome : childOutcomes) {
                    sumValue += goalOutcome.getOutcomeValue();
                }
                double resultAverage = sumValue / childOutcomes.size();
                int resultOutcomeState;
                if (resultAverage >= 1) {
                    resultOutcomeState = GoalOutcome.EXCEEDING_GOAL;
                } else if (resultAverage > .2) {
                    resultOutcomeState = GoalOutcome.POSITIVE;
                } else if (resultAverage < .2) {
                    resultOutcomeState = GoalOutcome.NEGATIVE;
                } else {
                    resultOutcomeState = GoalOutcome.NEUTRAL;
                }
                goalOutcome = new GoalOutcome(resultOutcomeState, resultAverage);
            }
        }
        if (goalOutcome == null) {
            goalOutcome = new GoalOutcome(GoalOutcome.NO_DATA, 0);            
        }
        return goalOutcome;
    }
}
