package com.easyinsight.analysis;

import com.easyinsight.AnalysisItem;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: James Boe
 * Date: Sep 8, 2008
 * Time: 8:54:12 PM
 */
@Entity
@Table(name="list_limits_metadata")
@PrimaryKeyJoinColumn(name="limits_metadata_id")
public class ListLimitsMetadata extends LimitsMetadata implements Serializable {
    @OneToOne
    @JoinColumn(name="analysis_item_id")
    private AnalysisItem analysisItem;

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public void setAnalysisItem(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }
}
