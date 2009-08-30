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
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "drill_through_to_drill_through_pair",
            joinColumns = @JoinColumn(name = "link_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "drill_through_pair_id", nullable = false))
    private List<DrillThroughPair> pairs = new ArrayList<DrillThroughPair>();

    public List<DrillThroughPair> getPairs() {
        return pairs;
    }

    public void setPairs(List<DrillThroughPair> pairs) {
        this.pairs = pairs;
    }

    public long getReportID() {
        return reportID;
    }

    public void setReportID(long reportID) {
        this.reportID = reportID;
    }
}
