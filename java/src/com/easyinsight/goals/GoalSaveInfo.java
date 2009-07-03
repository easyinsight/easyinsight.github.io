package com.easyinsight.goals;

import com.easyinsight.solutions.SolutionInstallInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * User: James Boe
 * Date: Jul 2, 2009
 * Time: 9:03:29 PM
 */
public class GoalSaveInfo {
    private GoalTree goalTree;
    private List<SolutionInstallInfo> installInfos = new ArrayList<SolutionInstallInfo>();

    public GoalSaveInfo() {
    }

    public GoalSaveInfo(GoalTree goalTree, List<SolutionInstallInfo> installInfos) {
        this.goalTree = goalTree;
        this.installInfos = installInfos;
    }

    public GoalTree getGoalTree() {
        return goalTree;
    }

    public void setGoalTree(GoalTree goalTree) {
        this.goalTree = goalTree;
    }

    public List<SolutionInstallInfo> getInstallInfos() {
        return installInfos;
    }

    public void setInstallInfos(List<SolutionInstallInfo> installInfos) {
        this.installInfos = installInfos;
    }
}
