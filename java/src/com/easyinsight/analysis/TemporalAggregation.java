package com.easyinsight.analysis;

import com.easyinsight.AnalysisItem;
import com.easyinsight.AnalysisDateDimension;
import com.easyinsight.AnalysisMeasure;
import com.easyinsight.AnalysisItemTypes;
import com.easyinsight.core.Key;

/**
 * User: James Boe
 * Date: Oct 17, 2008
 * Time: 11:34:54 AM
 */
public abstract class TemporalAggregation implements ITemporalAggregation {

    private AnalysisDateDimension sortDate;
    private AnalysisMeasure wrappedMeasure;
    private int newAggregation;

    protected TemporalAggregation(AnalysisDateDimension sortDate, AnalysisMeasure wrappedMeasure, int newAggregation) {
        this.sortDate = sortDate;
        this.wrappedMeasure = wrappedMeasure;
        this.newAggregation = newAggregation;
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
