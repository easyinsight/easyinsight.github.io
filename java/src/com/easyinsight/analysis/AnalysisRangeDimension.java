package com.easyinsight.analysis;

import com.easyinsight.core.*;
import com.easyinsight.database.Database;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Feb 29, 2008
 * Time: 10:52:57 AM
 */
@Entity
@Table(name="analysis_range")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class AnalysisRangeDimension extends AnalysisDimension {
    private static final int THRESHOLD = 10;

    private transient ExplicitRange range;

    @Column(name="aggregation_type")
    private int aggregationType;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "analysis_range_to_range_option",
            joinColumns = @JoinColumn(name = "analysis_item_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "range_option_id", nullable = false))
    private List<RangeOption> explicitOptions = new ArrayList<RangeOption>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="lower_range_option_id")
    private RangeOption lowerBound;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="higher_range_option_id")
    private RangeOption upperBound;

    public AnalysisRangeDimension(Key key, boolean group) {
        super(key, group);
    }

    public AnalysisRangeDimension() {
    }

    public int getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(int aggregationType) {
        this.aggregationType = aggregationType;
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        setExplicitOptions(new ArrayList<RangeOption>(getExplicitOptions()));
    }

    /* @Override
    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(this);
        AnalysisMeasure analysisMeasure = new AnalysisMeasure(getKey(), aggregationType);
        items.add(analysisMeasure);
        return items;
    }*/

    public RangeOption getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(RangeOption lowerBound) {
        this.lowerBound = lowerBound;
    }

    public RangeOption getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(RangeOption upperBound) {
        this.upperBound = upperBound;
    }

    public List<RangeOption> getExplicitOptions() {
        return explicitOptions;
    }

    public void setExplicitOptions(List<RangeOption> explicitOptions) {
        this.explicitOptions = explicitOptions;
    }

    public int getType() {
        return super.getType() | AnalysisItemTypes.RANGE_DIMENSION;
    }

    public Value toRange(Value value) {
        if (range == null) {
            range = new ExplicitRange(explicitOptions);
        }
        Value transformedValue;
        try {
            if (value.getOriginalValue() != null && value.type() == Value.STRING) {
                transformedValue = value;
            } else {
                Double doubleValue = value.toDouble();
                if (doubleValue != null) {
                    RangeOption matchedOption = range.getRange(doubleValue);
                    if (matchedOption == null) {
                        transformedValue = new EmptyValue();
                    } else {
                        String string = matchedOption.getRangeMinimum() + " to " + matchedOption.getRangeMaximum();
                        transformedValue = new StringValue(string);
                        transformedValue.setOriginalValue(new NumericValue(matchedOption.getRangeMinimum()));
                    }
                } else {
                    transformedValue = new EmptyValue();
                }
            }
        } catch (NumberFormatException e) {
            transformedValue = new EmptyValue();
        }
        return transformedValue;
    }

    public boolean equals(Object o) {
        return this == o || o instanceof AnalysisRangeDimension && super.equals(o);

    }

    public boolean blocksDBAggregation() {
        return true;
    }
}

