package com.easyinsight.analysis;

import javax.persistence.*;

/**
 * User: jamesboe
 * Date: Feb 3, 2010
 * Time: 12:56:47 PM
 */
@Entity
@Table(name="range_option")
public class RangeOption implements Cloneable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="range_option_id")
    private long rangeOptionID;
    @Column(name="low_value")
    private double rangeMinimum;
    @Column(name="high_value")
    private double rangeMaximum;
    @Column(name="display_name")
    private String displayName;

    public RangeOption clone() throws CloneNotSupportedException {
        RangeOption rangeOption = (RangeOption) super.clone();
        rangeOption.setRangeOptionID(0);
        return rangeOption;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getRangeOptionID() {
        return rangeOptionID;
    }

    public void setRangeOptionID(long rangeOptionID) {
        this.rangeOptionID = rangeOptionID;
    }

    public double getRangeMinimum() {
        return rangeMinimum;
    }

    public void setRangeMinimum(double rangeMinimum) {
        this.rangeMinimum = rangeMinimum;
    }

    public double getRangeMaximum() {
        return rangeMaximum;
    }

    public void setRangeMaximum(double rangeMaximum) {
        this.rangeMaximum = rangeMaximum;
    }
}
