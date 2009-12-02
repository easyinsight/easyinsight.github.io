package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.WSChartDefinition;
import com.easyinsight.analysis.AnalysisItem;

import java.util.Set;
import java.util.Map;
import java.util.Arrays;
import java.util.HashSet;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 4:49:42 PM
 */
public abstract class WSXAxisDefinition extends WSChartDefinition {

    private AnalysisItem measure;
    private AnalysisItem xaxis;

    private double yAxisMin;
    private double yAxisMax;

    public double getYAxisMax() {
        return yAxisMax;
    }

    public void setYAxisMax(double yAxisMax) {
        this.yAxisMax = yAxisMax;
    }

    public double getYAxisMin() {
        return yAxisMin;
    }

    public void setYAxisMin(double yAxisMin) {
        this.yAxisMin = yAxisMin;
    }

    public AnalysisItem getXaxis() {
        return xaxis;
    }

    public void setXaxis(AnalysisItem xaxis) {
        this.xaxis = xaxis;
    }

    public AnalysisItem getMeasure() {
        return measure;
    }

    public void setMeasure(AnalysisItem measure) {
        this.measure = measure;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("xAxis", Arrays.asList(xaxis), structure);
        addItems("measure", Arrays.asList(measure), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        xaxis = firstItem("xAxis", structure);
        measure = firstItem("measure", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        columnList.add(measure);
        columnList.add(xaxis);
        return columnList;
    }
}
