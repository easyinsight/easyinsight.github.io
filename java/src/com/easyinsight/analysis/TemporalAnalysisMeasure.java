package com.easyinsight.analysis;

import com.easyinsight.AnalysisMeasure;
import com.easyinsight.AnalysisDateDimension;
import com.easyinsight.AnalysisItem;
import com.easyinsight.AnalysisItemTypes;
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
    private AnalysisDateDimension analysisDimension;
    @Column(name="wrapped_aggregation")
    private int wrappedAggregation;

    public AnalysisDateDimension getAnalysisDimension() {
        return analysisDimension;
    }

    public void setAnalysisDimension(AnalysisDateDimension analysisDimension) {
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
        for (AnalysisItem analysisItem : insightItems) {
            if (analysisItem.getKey().equals(analysisDimension.getKey())) {
                foundDateDim = true;
                break;
            }
        }
        if (!foundDateDim) {
            items.add(analysisDimension);
        }
        return items;
    }

    public ITemporalAggregation createAggregation() {
        ITemporalAggregation aggregation;
        switch (getAggregation()) {
            case AggregationTypes.DELTA:
                aggregation = new DeltaTemporalAggregation(analysisDimension, new AnalysisMeasure(this.getKey(), wrappedAggregation), AggregationTypes.DELTA);
                break;
            default:
                throw new RuntimeException("Unknown temporal aggregation type " + getAggregation());
        }
        return aggregation;
    }
}
