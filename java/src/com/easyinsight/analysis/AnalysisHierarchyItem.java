package com.easyinsight.analysis;

import javax.persistence.*;
import java.util.*;

/**
 * User: James Boe
 * Date: Jan 25, 2009
 * Time: 2:32:26 PM
 */
@Entity
@Table(name="analysis_hierarchy_item")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class AnalysisHierarchyItem extends AnalysisDimension {
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="analysis_hierarchy_item_id")
    private long analysisHierarchyItemID;
    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name="hierarchy_level_id")
    private HierarchyLevel hierarchyLevel;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="analysis_hierarchy_item_to_hierarchy_level",
        joinColumns = @JoinColumn(name="analysis_item_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name="hierarchy_level_id", nullable = false))
    private Collection<HierarchyLevel> hierarchyLevels;

    public long getAnalysisHierarchyItemID() {
        return analysisHierarchyItemID;
    }

    public void setAnalysisHierarchyItemID(long analysisHierarchyItemID) {
        this.analysisHierarchyItemID = analysisHierarchyItemID;
    }

    public void beforeSave() {
        int i = 0;
        for (HierarchyLevel hierarchyLevel : hierarchyLevels) {
            hierarchyLevel.setPosition(i++);
        }
    }

    public void afterLoad() {
        super.afterLoad();
        if (hierarchyLevels != null) {
            List<HierarchyLevel> hierarchyLevelList = new ArrayList<HierarchyLevel>(hierarchyLevels);
            Collections.sort(hierarchyLevelList, new Comparator<HierarchyLevel>() {

                public int compare(HierarchyLevel o1, HierarchyLevel o2) {
                    return new Integer(o1.getPosition()).compareTo(o2.getPosition());
                }
            });
            hierarchyLevels = hierarchyLevelList;
        }
    }

    public Collection<HierarchyLevel> getHierarchyLevels() {
        return hierarchyLevels;
    }

    public void setHierarchyLevels(Collection<HierarchyLevel> hierarchyLevels) {
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
