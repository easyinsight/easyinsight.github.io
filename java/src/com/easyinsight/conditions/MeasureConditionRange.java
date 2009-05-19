package com.easyinsight.conditions;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: James Boe
 * Date: Jul 3, 2008
 * Time: 11:59:09 AM
 */
@Entity
@Table(name="measure_condition_range")
public class MeasureConditionRange implements Serializable, Cloneable {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="low_measure_condition_id")
    private MeasureCondition lowCondition;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="high_measure_condition_id")
    private MeasureCondition highCondition;

    @Column(name="value_range_type")
    private int valueRangeType;
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="measure_condition_range_id")
    private long measureConditionRangeID;

    @Override
    public MeasureConditionRange clone() throws CloneNotSupportedException {
        MeasureConditionRange measureConditionRange = (MeasureConditionRange) super.clone();
        measureConditionRange.setMeasureConditionRangeID(0);
        measureConditionRange.setLowCondition(lowCondition.clone());
        measureConditionRange.setHighCondition(highCondition.clone());
        return measureConditionRange;
    }

    public long getMeasureConditionRangeID() {
        return measureConditionRangeID;
    }

    public void setMeasureConditionRangeID(long measureConditionRangeID) {
        this.measureConditionRangeID = measureConditionRangeID;
    }

    public MeasureCondition getLowCondition() {
        return lowCondition;
    }

    public void setLowCondition(MeasureCondition lowCondition) {
        this.lowCondition = lowCondition;
    }

    public MeasureCondition getHighCondition() {
        return highCondition;
    }

    public void setHighCondition(MeasureCondition highCondition) {
        this.highCondition = highCondition;
    }

    public int getValueRangeType() {
        return valueRangeType;
    }

    public void setValueRangeType(int valueRangeType) {
        this.valueRangeType = valueRangeType;
    }
}
