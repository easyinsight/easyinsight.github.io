package com.easyinsight.scorecard;

import com.easyinsight.datafeeds.CredentialRequirement;
import com.easyinsight.kpi.KPI;

import java.util.List;

/**
 * User: jamesboe
 * Date: Feb 24, 2010
 * Time: 8:28:52 AM
 */
public class ScorecardWrapper {
    private Scorecard scorecard;
    private List<CredentialRequirement> credentials;
    private boolean asyncRefresh;
    private List<KPI> asyncRefreshKpis;

    public boolean isAsyncRefresh() {
        return asyncRefresh;
    }

    public void setAsyncRefresh(boolean asyncRefresh) {
        this.asyncRefresh = asyncRefresh;
    }

    public Scorecard getScorecard() {
        return scorecard;
    }

    public void setScorecard(Scorecard scorecard) {
        this.scorecard = scorecard;
    }

    public List<CredentialRequirement> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<CredentialRequirement> credentials) {
        this.credentials = credentials;
    }

    public List<KPI> getAsyncRefreshKpis() {
        return asyncRefreshKpis;
    }

    public void setAsyncRefreshKpis(List<KPI> asyncRefreshKpis) {
        this.asyncRefreshKpis = asyncRefreshKpis;
    }
}
