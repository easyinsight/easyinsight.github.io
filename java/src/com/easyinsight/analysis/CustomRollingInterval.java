package com.easyinsight.analysis;

import javax.persistence.*;

/**
 * User: jamesboe
 * Date: 10/18/12
 * Time: 8:32 AM
 */
@Entity
@Table(name="custom_rolling_interval")
public class CustomRollingInterval implements Cloneable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="custom_rolling_interval_id")
    private long customRollingIntervalID;

    @Column(name="interval_number")
    private int intervalNumber;
    @Column(name="filter_label")
    private String filterLabel;
    @Column(name="start_date_defined")
    private boolean startDefined;
    @Column(name="end_date_defined")
    private boolean endDefined;
    @Column(name="start_date_script")
    private String startScript;
    @Column(name="end_date_script")
    private String endScript;

    public CustomRollingInterval clone() {
        try {
            CustomRollingInterval interval = (CustomRollingInterval) super.clone();
            interval.setCustomRollingIntervalID(0);
            return interval;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public int getIntervalNumber() {
        return intervalNumber;
    }

    public void setIntervalNumber(int intervalNumber) {
        this.intervalNumber = intervalNumber;
    }

    public String getFilterLabel() {
        return filterLabel;
    }

    public void setFilterLabel(String filterLabel) {
        this.filterLabel = filterLabel;
    }

    public long getCustomRollingIntervalID() {
        return customRollingIntervalID;
    }

    public void setCustomRollingIntervalID(long customRollingIntervalID) {
        this.customRollingIntervalID = customRollingIntervalID;
    }

    public boolean isStartDefined() {
        return startDefined;
    }

    public void setStartDefined(boolean startDefined) {
        this.startDefined = startDefined;
    }

    public boolean isEndDefined() {
        return endDefined;
    }

    public void setEndDefined(boolean endDefined) {
        this.endDefined = endDefined;
    }

    public String getStartScript() {
        return startScript;
    }

    public void setStartScript(String startScript) {
        this.startScript = startScript;
    }

    public String getEndScript() {
        return endScript;
    }

    public void setEndScript(String endScript) {
        this.endScript = endScript;
    }
}
