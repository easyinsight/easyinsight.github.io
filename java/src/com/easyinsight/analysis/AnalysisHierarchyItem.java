package com.easyinsight.analysis;

import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: James Boe
 * Date: Jan 25, 2009
 * Time: 2:32:26 PM
 */
@Entity
@Table(name="analysis_hierarchy_item")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class AnalysisHierarchyItem extends AnalysisDimension {
    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name="hierarchy_level_id")
    private HierarchyLevel hierarchyLevel;
    @OneToMany (mappedBy = "parentItem", cascade = CascadeType.ALL)
    private List<HierarchyLevel> hierarchyLevels;

    public List<HierarchyLevel> getHierarchyLevels() {
        return hierarchyLevels;
    }

    public void setHierarchyLevels(List<HierarchyLevel> hierarchyLevels) {
        this.hierarchyLevels = hierarchyLevels;
    }

    public HierarchyLevel getHierarchyLevel() {
        return hierarchyLevel;
    }

    public void setHierarchyLevel(HierarchyLevel hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
    }

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        //analysisItems.add(this);
        analysisItems.add(hierarchyLevel.getAnalysisItem());
        return analysisItems;
    }

    public int getType() {
        return super.getType() | AnalysisItemTypes.HIERARCHY;
    }

    @Override
    public List<AnalysisItem> getDerivedItems() {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(this);
        return items;
    }

    @Override
    public AnalysisItemResultMetadata createResultMetadata() {
        return hierarchyLevel.getAnalysisItem().createResultMetadata();
    }

    @Override
    public boolean isDerived() {
        return true;
    }
}
