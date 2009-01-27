package com.easyinsight.dataset;

/**
 * User: James Boe
 * Date: Nov 5, 2008
 * Time: 1:32:09 PM
 */
public class LimitsResults {
    private boolean limitedResults;
    private int limitResults;
    private int maxResults;

    public LimitsResults(boolean limitedResults, int limitResults, int maxResults) {
        this.limitedResults = limitedResults;
        this.limitResults = limitResults;
        this.maxResults = maxResults;
    }

    public boolean isLimitedResults() {
        return limitedResults;
    }

    public void setLimitedResults(boolean limitedResults) {
        this.limitedResults = limitedResults;
    }

    public int getLimitResults() {
        return limitResults;
    }

    public void setLimitResults(int limitResults) {
        this.limitResults = limitResults;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }
}
