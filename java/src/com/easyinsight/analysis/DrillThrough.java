package com.easyinsight.analysis;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: Aug 28, 2009
 * Time: 10:44:28 AM
 */
@Entity
@Table(name="drill_through")
@PrimaryKeyJoinColumn(name="link_id")
public class DrillThrough extends Link {
    @Column(name="report_id")
    private long reportID;

    public long getReportID() {
        return reportID;
    }

    public void setReportID(long reportID) {
        this.reportID = reportID;
    }
}
