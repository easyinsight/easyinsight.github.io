package com.easyinsight.goals;

import com.easyinsight.datafeeds.CredentialRequirement;

import java.util.List;

/**
 * User: jamesboe
 * Date: Apr 5, 2010
 * Time: 11:16:33 AM
 */
public class KPITreeWrapper {
    private GoalTree goalTree;
    private List<CredentialRequirement> credentials;
    private boolean asyncRefresh;

    public GoalTree getGoalTree() {
        return goalTree;
    }

    public void setGoalTree(GoalTree goalTree) {
        this.goalTree = goalTree;
    }

    public List<CredentialRequirement> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<CredentialRequirement> credentials) {
        this.credentials = credentials;
    }

    public boolean isAsyncRefresh() {
        return asyncRefresh;
    }

    public void setAsyncRefresh(boolean asyncRefresh) {
        this.asyncRefresh = asyncRefresh;
    }
}
