package com.easyinsight.goals;

import com.easyinsight.logging.LogClass;

import java.util.TimerTask;

/**
 * User: James Boe
 * Date: Feb 13, 2009
 * Time: 11:08:38 AM
 */
public class GoalUpdateTask extends TimerTask {

    private GoalEvaluationStorage goalEvaluationStorage = new GoalEvaluationStorage();

    public void run() {
        try {
            goalEvaluationStorage.evaluateGoalTrees();
        } catch (Exception e) {
            LogClass.error(e);
        }
    }
}
