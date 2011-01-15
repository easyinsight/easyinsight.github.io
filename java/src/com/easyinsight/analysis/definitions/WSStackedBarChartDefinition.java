package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: James Boe
 * Date: Mar 20, 2009
 * Time: 7:23:14 PM
 */
public class WSStackedBarChartDefinition extends WSYAxisDefinition {

    private int chartColor;
    private boolean useChartColor;
    private String columnSort;
    private AnalysisItem stackItem;

    public AnalysisItem getStackItem() {
        return stackItem;
    }

    public void setStackItem(AnalysisItem stackItem) {
        this.stackItem = stackItem;
    }

    public int getChartType() {
        return ChartDefinitionState.BAR_2D_STACKED;
    }

    public int getChartFamily() {
        return ChartDefinitionState.BAR_FAMILY;
    }

    public String getColumnSort() {
        return columnSort;
    }

    public void setColumnSort(String columnSort) {
        this.columnSort = columnSort;
    }

    public int getChartColor() {
        return chartColor;
    }

    public void setChartColor(int chartColor) {
        this.chartColor = chartColor;
    }

    public boolean isUseChartColor() {
        return useChartColor;
    }

    public void setUseChartColor(boolean useChartColor) {
        this.useChartColor = useChartColor;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        chartColor = (int) findNumberProperty(properties, "chartColor", 0);
        useChartColor = findBooleanProperty(properties, "useChartColor", false);
        columnSort = findStringProperty(properties, "columnSort", "Unsorted");
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("chartColor", chartColor));
        properties.add(new ReportBooleanProperty("useChartColor", useChartColor));
        properties.add(new ReportStringProperty("columnSort", columnSort));
        return properties;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        super.createReportStructure(structure);
        addItems("stackItem", Arrays.asList(stackItem), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        super.populateFromReportStructure(structure);
        stackItem = firstItem("stackItem", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = super.getAllAnalysisItems();
        columnList.add(stackItem);
        return columnList;
    }
}
