package com.easyinsight.analysis;

import com.easyinsight.core.*;

import javax.persistence.*;

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

    public AnalysisMeasure(Key key, String displayName, int aggregation) {
        super(key, displayName);
        this.aggregation = aggregation;
    }

    public AnalysisMeasure(Key key, String displayName, int aggregation, boolean highIsGood) {
        this(key, displayName, aggregation);
        this.aggregation = aggregation;
        setHighIsGood(highIsGood);
    }

    public AnalysisMeasure(Key key, String displayName, int aggregation, boolean highIsGood, int formattingType) {
        this(key, displayName, aggregation, highIsGood);
        this.aggregation = aggregation;
        FormattingConfiguration formattingConfiguration = new FormattingConfiguration();
        formattingConfiguration.setFormattingType(formattingType);
        setFormattingConfiguration(formattingConfiguration);
    }

    @Override
    public AggregateKey createAggregateKey() {
        // in case of filters, how do we do this...        
        return new AggregateMeasureKey(getKey(), getType(), aggregation, toDisplay(), getFilters());
    }

    public AggregateKey createAggregateKey(boolean measure) {
        if (measure) {
            return new AggregateKey(getKey(), getType(), getFilters());
        } else {
            return super.createAggregateKey();
        }
    }

    @Override
    protected String getQualifiedSuffix() {
        return getType() + ":" + aggregation + ":" + toDisplay();
    }

    public Value transformValue(Value value, InsightRequestMetadata insightRequestMetadata) {
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

    public int getAggregation() {
        return aggregation;
    }

    public void setAggregation(int aggregation) {
        this.aggregation = aggregation;
    }

    public AnalysisItemResultMetadata createResultMetadata() {
        return new AnalysisMeasureResultMetadata();
    }

    public int getQueryAggregation() {
        return aggregation;
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
