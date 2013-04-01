package com.easyinsight.analysis;

import javax.persistence.*;

/**
 * User: jamesboe
 * Date: 2/11/13
 * Time: 9:27 AM
 */
@Entity
@Table(name="report_stub")
public class ReportStub implements Cloneable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="report_stub_id")
    private long reportStubID;

    @Column(name="report_id")
    private Long reportID;

    public long getReportStubID() {
        return reportStubID;
    }

    public void setReportStubID(long reportStubID) {
        this.reportStubID = reportStubID;
    }

    public Long getReportID() {
        return reportID;
    }

    public void setReportID(Long reportID) {
        this.reportID = reportID;
    }

    public ReportStub clone() throws CloneNotSupportedException {
        ReportStub clone = (ReportStub) super.clone();
        clone.setReportStubID(0);
        return clone;
    }
}
