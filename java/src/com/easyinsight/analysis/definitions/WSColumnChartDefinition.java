package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;

import java.util.List;

/**
 * User: James Boe
 * Date: Mar 20, 2009
 * Time: 7:23:14 PM
 */
public class WSColumnChartDefinition extends WSXAxisDefinition {

    private int chartColor;
    private int gradientColor;
    private boolean useChartColor;
    private String columnSort;
    private String axisType = "Linear";

    public String getAxisType() {
        return axisType;
    }

    public void setAxisType(String axisType) {
        this.axisType = axisType;
    }

    public int getGradientColor() {
        return gradientColor;
    }

    public void setGradientColor(int gradientColor) {
        this.gradientColor = gradientColor;
    }

    public int getChartType() {
        return ChartDefinitionState.COLUMN_2D;
    }

    public int getChartFamily() {
        return ChartDefinitionState.COLUMN_FAMILY;
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
        gradientColor = (int) findNumberProperty(properties, "gradientColor", 0);
        useChartColor = findBooleanProperty(properties, "useChartColor", false);
        columnSort = findStringProperty(properties, "columnSort", "Unsorted");
        axisType = findStringProperty(properties, "axisType", "Linear");
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("chartColor", chartColor));
        properties.add(new ReportNumericProperty("gradientColor", gradientColor));
        properties.add(new ReportBooleanProperty("useChartColor", useChartColor));
        properties.add(new ReportStringProperty("columnSort", columnSort));
        properties.add(new ReportStringProperty("axisType", axisType));
        return properties;
    }
}
