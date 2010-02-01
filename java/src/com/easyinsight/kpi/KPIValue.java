package com.easyinsight.kpi;

import java.util.Date;

/**
 * User: jamesboe
 * Date: Jan 18, 2010
 * Time: 3:27:36 PM
 */
public class KPIValue {
    private double value;
    private Date date;
    private long kpiID;

    public long getKpiID() {
        return kpiID;
    }

    public void setKpiID(long kpiID) {
        this.kpiID = kpiID;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
