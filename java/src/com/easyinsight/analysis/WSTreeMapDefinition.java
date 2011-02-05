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

    private String colorStrategy = "Linear";
    private int lowColor = 3355528;
    private int highColor = 11184895;

    public String getColorStrategy() {
        return colorStrategy;
    }

    public void setColorStrategy(String colorStrategy) {
        this.colorStrategy = colorStrategy;
    }

    public int getLowColor() {
        return lowColor;
    }

    public void setLowColor(int lowColor) {
        this.lowColor = lowColor;
    }

    public int getHighColor() {
        return highColor;
    }

    public void setHighColor(int highColor) {
        this.highColor = highColor;
    }

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
        if (hierarchy == null || item.getHierarchyLevel() == null) {
            return new HashSet<AnalysisItem>();
        }
        int startIndex = item.getHierarchyLevels().indexOf(item.getHierarchyLevel());
        for (int i = startIndex; i < item.getHierarchyLevels().size() && i < (startIndex + 2); i++) {
            columnList.add(item.getHierarchyLevels().get(i).getAnalysisItem());
        }
        columnList.add(measure1);
        columnList.add(measure2);
        return columnList;
    }
}