package com.easyinsight.analysis;

import java.util.List;

/**
 * User: jamesboe
 * Date: 9/15/11
 * Time: 12:04 AM
 */
public class EmbeddedTrendDataResults extends EmbeddedResults {
    private List<TrendOutcome> trendOutcomes;

    public List<TrendOutcome> getTrendOutcomes() {
        return trendOutcomes;
    }

    public void setTrendOutcomes(List<TrendOutcome> trendOutcomes) {
        this.trendOutcomes = trendOutcomes;
    }
}
