package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisItem;

import javax.persistence.*;
import java.io.Serializable;

import org.hibernate.annotations.IndexColumn;

/**
 * User: James Boe
 * Date: Jan 25, 2009
 * Time: 2:32:45 PM
 */
@Entity
@Table(name="hierarchy_level")
public class HierarchyLevel implements Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="hierarchy_level_id")
    private long hierarchyLevelID;
    @OneToOne
    @JoinColumn(name="analysis_item_id")
    private AnalysisItem analysisItem;
    @Column(name="position")
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getHierarchyLevelID() {
        return hierarchyLevelID;
    }

    public void setHierarchyLevelID(long hierarchyLevelID) {
        this.hierarchyLevelID = hierarchyLevelID;
    }

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public void setAnalysisItem(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    @Override
    public HierarchyLevel clone() throws CloneNotSupportedException {
        HierarchyLevel hierarchyLevel = (HierarchyLevel) super.clone();
        hierarchyLevel.setHierarchyLevelID(hierarchyLevelID);
        return hierarchyLevel;
    }
}
