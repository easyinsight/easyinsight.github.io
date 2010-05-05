package com.easyinsight.analysis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * User: James Boe
 * Date: Oct 17, 2008
 * Time: 11:05:07 AM
 */

@Entity
@Table(name="complex_analysis_measure")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class ComplexAnalysisMeasure extends AnalysisMeasure {
    @Column(name="wrapped_aggregation")
    private int wrappedAggregation;

    public int getType() {
        return super.getType() | AnalysisItemTypes.COMPLEX_MEASURE;
    }

    public int getWrappedAggregation() {
        return wrappedAggregation;
    }

    public int getQueryAggregation() {
        return wrappedAggregation;
    }

    public void setWrappedAggregation(int wrappedAggregation) {
        this.wrappedAggregation = wrappedAggregation;
    }
}
