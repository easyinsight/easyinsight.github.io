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
public class HierarchyLevel implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="hierarchy_level_id")
    private long hierarchyLevelID;
    @OneToOne
    @JoinColumn(name="analysis_item_id")
    private AnalysisItem analysisItem;
    @ManyToOne
    @IndexColumn(name="level", base=0)
    @JoinColumn(name="parent_item_id")
    private AnalysisItem parentItem;

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

    public AnalysisItem getParentItem() {
        return parentItem;
    }

    public void setParentItem(AnalysisItem parentItem) {
        this.parentItem = parentItem;
    }
}
