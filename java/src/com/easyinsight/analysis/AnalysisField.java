package com.easyinsight.analysis;

import com.easyinsight.AnalysisItem;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Apr 13, 2008
 * Time: 2:51:59 PM
 */

@MappedSuperclass
public class AnalysisField {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="analysis_item_id")
    private AnalysisItem analysisItem;
    @Column (name="position")
    private int position;

    public AnalysisField(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    public AnalysisField() {
    }

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public void setAnalysisItem(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
