package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.core.Key;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Oct 17, 2008
 * Time: 11:58:27 AM
 */
@Entity
@Table(name="time_based_analysis_measure")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class TemporalAnalysisMeasure extends AnalysisMeasure {
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="analysis_item_id")
    private AnalysisDimension analysisDimension;
    @Column(name="wrapped_aggregation")
    private int wrappedAggregation;

    private transient boolean applied;

    public AnalysisDimension getAnalysisDimension() {
        return analysisDimension;
    }

    public void setAnalysisDimension(AnalysisDimension analysisDimension) {
        this.analysisDimension = analysisDimension;
    }

    public int getWrappedAggregation() {
        return wrappedAggregation;
    }

    public void setWrappedAggregation(int wrappedAggregation) {
        this.wrappedAggregation = wrappedAggregation;
    }

    public Key getAggregateKey() {
        return new AggregateKey(getKey(), wrappedAggregation);
    }

    public int getType() {
        return super.getType() | AnalysisItemTypes.TEMPORAL_MEASURE;
    }

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, List<AnalysisItem> insightItems) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisMeasure(this.getKey(), wrappedAggregation));
        boolean foundDateDim = false;
        if (getAnalysisDimension().hasType(AnalysisItemTypes.DATE_DIMENSION)) {
            AnalysisDateDimension sortDim = (AnalysisDateDimension) getAnalysisDimension();
            for (AnalysisItem analysisItem : insightItems) {
                if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                    AnalysisDateDimension dimension = (AnalysisDateDimension) analysisItem;
                    if (analysisItem.getKey().equals(getAnalysisDimension().getKey()) && dimension.getDateLevel() == sortDim.getDateLevel()) {
                        foundDateDim = true;
                        break;
                    }
                }
            }
        } else {
            for (AnalysisItem analysisItem : insightItems) {
                if (analysisItem.getKey().equals(getAnalysisDimension().getKey()) && analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
                    foundDateDim = true;
                    break;
                }
            }
        }
        if (!foundDateDim) {
            items.add(analysisDimension);
        }
        return items;
    }

    public boolean hasBeenApplied() {
        return applied;
    }

    public void triggerApplied(boolean applied) {
        this.applied = applied;
    }

    public boolean requiresReAggregation() {
        if (getAggregation() == AggregationTypes.DELTA) {
            return false;
        } else {
            return true;
        }
    }

    public ITemporalAggregation createAggregation() {
        ITemporalAggregation aggregation;
        switch (getAggregation()) {
            case AggregationTypes.DELTA:
                aggregation = new DeltaTemporalAggregation(analysisDimension, new AnalysisMeasure(this.getKey(), wrappedAggregation), AggregationTypes.DELTA, false);
                break;
            case AggregationTypes.LAST_VALUE:
                aggregation = new LastValueTemporalAggregation(analysisDimension, new AnalysisMeasure(this.getKey(), wrappedAggregation), AggregationTypes.LAST_VALUE, true);
                break;
            default:
                throw new RuntimeException("Unknown temporal aggregation type " + getAggregation());
        }
        return aggregation;
    }
}
