package com.easyinsight.analysis;

import java.io.Serializable;
import java.util.List;

/**
 * User: jboe
 * Date: Dec 22, 2007
 * Time: 5:04:09 PM
 */
public class TrendDataResults extends DataResults implements Serializable {
    private List<TrendOutcome> trendOutcomes;

    public List<TrendOutcome> getTrendOutcomes() {
        return trendOutcomes;
    }

    public void setTrendOutcomes(List<TrendOutcome> trendOutcomes) {
        this.trendOutcomes = trendOutcomes;
    }

    @Override
    public EmbeddedResults toEmbeddedResults() {
        EmbeddedCrosstabDataResults embeddedCrosstabDataResults = new EmbeddedCrosstabDataResults();
        embeddedCrosstabDataResults.setAdditionalProperties(getAdditionalProperties());
        return embeddedCrosstabDataResults;
    }
}
