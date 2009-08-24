package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisItem;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

/**
 * User: James Boe
 * Date: Sep 8, 2008
 * Time: 8:54:12 PM
 */
@Entity
@Table(name="list_limits_metadata")
@PrimaryKeyJoinColumn(name="limits_metadata_id")
public class ListLimitsMetadata extends LimitsMetadata implements Serializable, Cloneable {
    @OneToOne
    @JoinColumn(name="analysis_item_id")
    private AnalysisItem analysisItem;

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public void setAnalysisItem(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    @Override
    public ListLimitsMetadata clone() throws CloneNotSupportedException {
        return (ListLimitsMetadata) super.clone();
    }

    public void updateIDs(Map<Long, AnalysisItem> replacementMap) {
        analysisItem = replacementMap.get(analysisItem.getAnalysisItemID());
    }
}
