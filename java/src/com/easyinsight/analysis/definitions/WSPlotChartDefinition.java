package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.WSChartDefinition;

import java.util.Map;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:45:48 PM
 */
public class WSPlotChartDefinition extends WSChartDefinition {
    private AnalysisItem dimension;
    private AnalysisItem xaxisMeasure;
    private AnalysisItem yaxisMeasure;

    public AnalysisItem getXaxisMeasure() {
        return xaxisMeasure;
    }

    public void setXaxisMeasure(AnalysisItem xaxisMeasure) {
        this.xaxisMeasure = xaxisMeasure;
    }

    public AnalysisItem getYaxisMeasure() {
        return yaxisMeasure;
    }

    public void setYaxisMeasure(AnalysisItem yaxisMeasure) {
        this.yaxisMeasure = yaxisMeasure;
    }

    public AnalysisItem getDimension() {
        return dimension;
    }

    public void setDimension(AnalysisItem dimension) {
        this.dimension = dimension;
    }

    protected void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("xAxisMeasure", Arrays.asList(xaxisMeasure), structure);
        addItems("yAxisMeasure", Arrays.asList(yaxisMeasure), structure);
        addItems("dimension", Arrays.asList(dimension), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        xaxisMeasure = firstItem("xAxisMeasure", structure);
        yaxisMeasure = firstItem("yAxisMeasure", structure);
        dimension = firstItem("dimension", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        columnList.add(dimension);
        columnList.add(xaxisMeasure);
        columnList.add(yaxisMeasure);
        return columnList;
    }

    public int getChartType() {
        return ChartDefinition.PLOT_TYPE;
    }

    public int getChartFamily() {
        return ChartDefinition.PLOT_FAMILY;
    }
}
