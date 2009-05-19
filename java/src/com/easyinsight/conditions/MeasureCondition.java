package com.easyinsight.conditions;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: James Boe
 * Date: Jul 3, 2008
 * Time: 11:59:50 AM
 */
@Entity
@Table(name="measure_condition")
public class MeasureCondition implements Serializable, Cloneable {
    @Column(name="low_color")
    private int lowColor;
    @Column(name="low_value")
    private int lowValue;
    @Column(name="high_color")
    private int highColor;
    @Column(name="high_value")
    private int highValue;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="measure_condition_id")
    private int measureConditionID;

    @Override
    public MeasureCondition clone() throws CloneNotSupportedException {
        MeasureCondition measureCondition = (MeasureCondition) super.clone();
        measureCondition.setMeasureConditionID(0);
        return measureCondition;
    }

    public int getMeasureConditionID() {
        return measureConditionID;
    }

    public void setMeasureConditionID(int measureConditionID) {
        this.measureConditionID = measureConditionID;
    }

    public int getLowColor() {
        return lowColor;
    }

    public void setLowColor(int lowColor) {
        this.lowColor = lowColor;
    }

    public int getLowValue() {
        return lowValue;
    }

    public void setLowValue(int lowValue) {
        this.lowValue = lowValue;
    }

    public int getHighColor() {
        return highColor;
    }

    public void setHighColor(int highColor) {
        this.highColor = highColor;
    }

    public int getHighValue() {
        return highValue;
    }

    public void setHighValue(int highValue) {
        this.highValue = highValue;
    }
}
