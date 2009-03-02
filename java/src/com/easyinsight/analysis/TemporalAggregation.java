package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.core.Key;

/**
 * User: James Boe
 * Date: Oct 17, 2008
 * Time: 11:34:54 AM
 */
public abstract class TemporalAggregation implements ITemporalAggregation {

    private AnalysisDimension sortDate;
    private AnalysisMeasure wrappedMeasure;
    private int newAggregation;
    private boolean requiresReAggregation;

    protected TemporalAggregation(AnalysisDimension sortDate, AnalysisMeasure wrappedMeasure, int newAggregation, boolean requiresReAggregation) {
        this.sortDate = sortDate;
        this.wrappedMeasure = wrappedMeasure;
        this.newAggregation = newAggregation;
        this.requiresReAggregation = requiresReAggregation;
    }

    public boolean isRequiresReAggregation() {
        return requiresReAggregation;
    }

    public AnalysisItem getSortItem() {
        return sortDate;
    }

    public Key getAggregateKey() {
        return new AggregateKey(wrappedMeasure.getKey(), wrappedMeasure.getAggregation());
    }

    public Key getNewAggregateKey() {
        return new AggregateKey(wrappedMeasure.getKey(), AnalysisItemTypes.TEMPORAL_MEASURE | AnalysisItemTypes.MEASURE);
    }

    public void setSortItem(AnalysisDateDimension analysisDateDimension) {
        this.sortDate = analysisDateDimension;
    }
}
