package com.easyinsight.userupload;

/**
 * User: jamesboe
 * Date: 9/28/14
 * Time: 9:03 AM
 */
public class SuggestionResult {

    public static final int NAVIGATE_TO_DASHBOARD = 1;
    public static final int NAVIGATE_TO_SCHEDULING = 2;

    private int responseType;
    private long dashboardID;

    public SuggestionResult() {
    }

    public SuggestionResult(int responseType, long dashboardID) {
        this.responseType = responseType;
        this.dashboardID = dashboardID;
    }

    public int getResponseType() {
        return responseType;
    }

    public void setResponseType(int responseType) {
        this.responseType = responseType;
    }

    public long getDashboardID() {
        return dashboardID;
    }

    public void setDashboardID(long dashboardID) {
        this.dashboardID = dashboardID;
    }
}
