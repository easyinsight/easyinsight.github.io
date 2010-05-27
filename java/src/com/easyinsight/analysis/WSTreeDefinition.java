package com.easyinsight.analysis;

import java.util.*;

/**
 * User: James Boe
 * Date: Jul 17, 2008
 * Time: 7:45:24 PM
 */
public class WSTreeDefinition extends WSAnalysisDefinition {

    private long treeDefinitionID;

    public long getTreeDefinitionID() {
        return treeDefinitionID;
    }

    public void setTreeDefinitionID(long treeDefinitionID) {
        this.treeDefinitionID = treeDefinitionID;
    }

    public String getDataFeedType() {
        return AnalysisTypes.TREE;
    }

    private AnalysisItem hierarchy;
    private List<AnalysisItem> items;

    public AnalysisItem getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(AnalysisItem hierarchy) {
        this.hierarchy = hierarchy;
    }

    public List<AnalysisItem> getItems() {
        return items;
    }

    public void setItems(List<AnalysisItem> items) {
        this.items = items;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("hierarchy", Arrays.asList(hierarchy), structure);
        Collections.sort(items, new Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                return new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition());
            }
        });
        addItems("items", items, structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        hierarchy = firstItem("hierarchy", structure);
        items = items("items", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        AnalysisHierarchyItem item = (AnalysisHierarchyItem) hierarchy;
        for (HierarchyLevel level : item.getHierarchyLevels()) {
            columnList.add(level.getAnalysisItem());
        }
        columnList.addAll(items);
        return columnList;
    }
}