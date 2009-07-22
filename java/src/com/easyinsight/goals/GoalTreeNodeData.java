package com.easyinsight.goals;

import com.easyinsight.logging.LogClass;
import com.easyinsight.datafeeds.CredentialFulfillment;

import java.util.Date;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Oct 23, 2008
 * Time: 11:48:19 PM
 */
public class GoalTreeNodeData extends GoalTreeNode {
    private GoalOutcome goalOutcome;
    private GoalValue currentValue;

    public GoalOutcome getGoalOutcome() {
        return goalOutcome;
    }

    public void setGoalOutcome(GoalOutcome goalOutcome) {
        this.goalOutcome = goalOutcome;
    }

    public GoalValue getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(GoalValue currentValue) {
        this.currentValue = currentValue;
    }

    public void populateCurrentValue() {
        try {
            this.currentValue = new GoalEvaluationStorage().getLatestGoalValue(this);
        } catch (SQLException e) {
            LogClass.error(e);
        }
    }

    public void determineOutcome(Date startDate, Date endDate, GoalEvaluationStorage goalEvaluationStorage) throws SQLException {
        if (getCoreFeedID() > 0) {
            if (isGoalDefined() || !getProblemConditions().isEmpty())
                this.goalOutcome = goalEvaluationStorage.getEvaluations(this, startDate, endDate,
                    getGoalValue(), isHighIsGood(), new ArrayList<CredentialFulfillment>());
        }
    }

    public GoalOutcome summarizeOutcomes() {
        if (getChildren() != null) {
            int worstOutcome = GoalOutcome.NO_DATA;
            //List<GoalOutcome> childOutcomes = new ArrayList<GoalOutcome>();
            double outcomeValue = 0;
            if (goalOutcome != null) {
                //childOutcomes.add(goalOutcome);
                worstOutcome = goalOutcome.getOutcomeState();
                outcomeValue = goalOutcome.getOutcomeValue();
            }
            for (GoalTreeNode child : getChildren()) {
                GoalTreeNodeData dataChild = (GoalTreeNodeData) child;
                GoalOutcome childOutcome = dataChild.summarizeOutcomes();
                if (childOutcome.getOutcomeState() > worstOutcome) {
                    worstOutcome = childOutcome.getOutcomeState();
                    //childOutcomes.add(childOutcome);
                }
            }
            goalOutcome = new GoalOutcome(worstOutcome, outcomeValue);
            /*if (childOutcomes.size() > 0) {
                double sumValue = 0;
                for (GoalOutcome goalOutcome : childOutcomes) {
                    sumValue += goalOutcome.getOutcomeValue();
                }
                double resultAverage = sumValue / childOutcomes.size();
                int resultOutcomeState;
                *//*if (resultAverage >= 1) {
                    resultOutcomeState = GoalOutcome.EXCEEDING_GOAL;
                } else*//* if (resultAverage > .2) {
                    resultOutcomeState = GoalOutcome.POSITIVE;
                } else if (resultAverage < -.2) {
                    resultOutcomeState = GoalOutcome.NEGATIVE;
                } else {
                    resultOutcomeState = GoalOutcome.NEUTRAL;
                }
                goalOutcome = new GoalOutcome(resultOutcomeState, resultAverage);
            }*/
        }
        if (goalOutcome == null) {
            goalOutcome = new GoalOutcome(GoalOutcome.NO_DATA, 0);            
        }
        return goalOutcome;
    }
}
