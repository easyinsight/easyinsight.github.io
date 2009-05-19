package com.easyinsight.analysis;

import com.easyinsight.core.*;
import com.easyinsight.conditions.MeasureConditionRange;
import com.easyinsight.analysis.AnalysisItemResultMetadata;
import com.easyinsight.analysis.AnalysisMeasureResultMetadata;

import javax.persistence.*;
import java.util.List;

/**
 * User: James Boe
 * Date: Jan 20, 2008
 * Time: 11:08:13 PM
 */
@Entity
@Table(name="analysis_measure")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class AnalysisMeasure extends AnalysisItem {
    @Column(name="aggregation")
    private int aggregation;

    /*@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="analysis_measure_id")
    private long analysisMeasureID;*/

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="measure_condition_range_id")
    private MeasureConditionRange measureConditionRange;

    /*@OneToMany(cascade = CascadeType.ALL)
    private List<PersistableFilterDefinition> filters;*/

    public AnalysisMeasure() {
    }

    public int getType() {
        return AnalysisItemTypes.MEASURE;
    }

    public AnalysisMeasure(Key key, int aggregation) {
        super(key);
        this.aggregation = aggregation;
    }

    public AnalysisMeasure(String key, int aggregation) {
        super(key);
        this.aggregation = aggregation;
    }

    public Value transformValue(Value value) {
        Value result;
        if (value == null) {
            result = new EmptyValue();
        } else if (value.type() == Value.STRING) {
            StringValue stringValue = (StringValue) value;
            NumericValue numericValue = new NumericValue();
            numericValue.setValue(stringValue.getValue());
            result = numericValue;
        } else  {
            result = value;
        }
        return result;
    }

    /*public long getAnalysisMeasureID() {
        return analysisMeasureID;
    }

    public void setAnalysisMeasureID(long analysisMeasureID) {
        this.analysisMeasureID = analysisMeasureID;
    }*/

    public int getAggregation() {
        return aggregation;
    }

    public void setAggregation(int aggregation) {
        this.aggregation = aggregation;
    }

    public MeasureConditionRange getMeasureConditionRange() {
        return measureConditionRange;
    }

    public void setMeasureConditionRange(MeasureConditionRange measureConditionRange) {
        this.measureConditionRange = measureConditionRange;
    }

    public AnalysisItemResultMetadata createResultMetadata() {
        return new AnalysisMeasureResultMetadata();
    }

    @Override
    public AnalysisItem clone() throws CloneNotSupportedException {
        AnalysisMeasure analysisMeasure = (AnalysisMeasure) super.clone();
        if (analysisMeasure.getMeasureConditionRange() != null) {
            analysisMeasure.setMeasureConditionRange(analysisMeasure.getMeasureConditionRange().clone());
        }
        return analysisMeasure;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnalysisMeasure)) return false;
        if (!super.equals(o)) return false;

        AnalysisMeasure that = (AnalysisMeasure) o;

        return aggregation == that.aggregation;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + aggregation;
        return result;
    }
}
