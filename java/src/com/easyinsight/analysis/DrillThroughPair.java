package com.easyinsight.analysis;

import javax.persistence.*;

/**
 * User: jamesboe
 * Date: Aug 28, 2009
 * Time: 10:48:37 AM
 */
@Entity
@Table(name="drill_through_pair")
public class DrillThroughPair {
    @Column(name="analysis_item_id")
    private long analysisItemID;
    @Column(name="filter_id")
    private long filterID;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="drill_through_pair_id")
    private long drillThroughPairID;

    public long getDrillThroughPairID() {
        return drillThroughPairID;
    }

    public void setDrillThroughPairID(long drillThroughPairID) {
        this.drillThroughPairID = drillThroughPairID;
    }

    public long getAnalysisItemID() {
        return analysisItemID;
    }

    public void setAnalysisItemID(long analysisItemID) {
        this.analysisItemID = analysisItemID;
    }

    public long getFilterID() {
        return filterID;
    }

    public void setFilterID(long filterID) {
        this.filterID = filterID;
    }
}
