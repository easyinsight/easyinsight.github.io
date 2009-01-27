package com.easyinsight.webservice;

/**
 * User: James Boe
 * Date: Aug 13, 2008
 * Time: 11:31:31 AM
 */
public class ShortChartDefinition {
    private String chartType;
    private WSAnalysisItem[] measures;
    private WSAnalysisItem[] dimensions;

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public WSAnalysisItem[] getMeasures() {
        return measures;
    }

    public void setMeasures(WSAnalysisItem[] measures) {
        this.measures = measures;
    }

    public WSAnalysisItem[] getDimensions() {
        return dimensions;
    }

    public void setDimensions(WSAnalysisItem[] dimensions) {
        this.dimensions = dimensions;
    }
}
