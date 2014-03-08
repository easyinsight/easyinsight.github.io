package com.easyinsight.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 9/15/11
 * Time: 12:04 AM
 */
public class EmbeddedTrendDataResults extends EmbeddedResults {
    private List<TrendOutcome> trendOutcomes = new ArrayList<TrendOutcome>();
    private String nowString;
    private String previousString;

    public String getNowString() {
        return nowString;
    }

    public void setNowString(String nowString) {
        this.nowString = nowString;
    }

    public String getPreviousString() {
        return previousString;
    }

    public void setPreviousString(String previousString) {
        this.previousString = previousString;
    }

    public List<TrendOutcome> getTrendOutcomes() {
        return trendOutcomes;
    }

    public void setTrendOutcomes(List<TrendOutcome> trendOutcomes) {
        this.trendOutcomes = trendOutcomes;
    }
}
