package com.easyinsight.goals;

import com.easyinsight.analysis.ReportFault;

/**
 * User: jamesboe
 * Date: Apr 5, 2010
 * Time: 11:16:33 AM
 */
public class KPITreeWrapper {
    private GoalTree goalTree;
    private ReportFault reportFault;
    private boolean asyncRefresh;

    public ReportFault getReportFault() {
        return reportFault;
    }

    public void setReportFault(ReportFault reportFault) {
        this.reportFault = reportFault;
    }

    public GoalTree getGoalTree() {
        return goalTree;
    }

    public void setGoalTree(GoalTree goalTree) {
        this.goalTree = goalTree;
    }

    public boolean isAsyncRefresh() {
        return asyncRefresh;
    }

    public void setAsyncRefresh(boolean asyncRefresh) {
        this.asyncRefresh = asyncRefresh;
    }
}
