package com.easyinsight.analysis;

import java.util.Map;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

/**
 * User: James Boe
 * Date: Jul 17, 2008
 * Time: 7:45:24 PM
 */
public class WSTreeMapDefinition extends WSAnalysisDefinition {

    private long treeMapDefinitionID;
    private int colorScheme;

    public int getColorScheme() {
        return colorScheme;
    }

    public void setColorScheme(int colorScheme) {
        this.colorScheme = colorScheme;
    }

    public long getTreeMapDefinitionID() {
        return treeMapDefinitionID;
    }

    public void setTreeMapDefinitionID(long treeMapDefinitionID) {
        this.treeMapDefinitionID = treeMapDefinitionID;
    }

    public String getDataFeedType() {
        return AnalysisTypes.TREEMAP;
    }

    private AnalysisItem hierarchy;
    private AnalysisItem measure1;
    private AnalysisItem measure2;

    public AnalysisItem getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(AnalysisItem hierarchy) {
        this.hierarchy = hierarchy;
    }

    public AnalysisItem getMeasure1() {
        return measure1;
    }

    public void setMeasure1(AnalysisItem measure1) {
        this.measure1 = measure1;
    }

    public AnalysisItem getMeasure2() {
        return measure2;
    }

    public void setMeasure2(AnalysisItem measure2) {
        this.measure2 = measure2;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("hierarchy", Arrays.asList(hierarchy), structure);
        addItems("areaMeasure", Arrays.asList(measure1), structure);
        addItems("colorMeasure", Arrays.asList(measure2), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        hierarchy = firstItem("hierarchy", structure);
        measure1 = firstItem("areaMeasure", structure);
        measure2 = firstItem("colorMeasure", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        AnalysisHierarchyItem item = (AnalysisHierarchyItem) hierarchy;
        for (HierarchyLevel level : item.getHierarchyLevels()) {
            columnList.add(level.getAnalysisItem());
        }
        columnList.add(measure1);
        columnList.add(measure2);
        return columnList;
    }
}