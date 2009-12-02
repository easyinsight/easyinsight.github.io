package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.WSChartDefinition;
import com.easyinsight.analysis.AnalysisItem;

import java.util.Map;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:45:09 PM
 */
public abstract class WSTwoAxisDefinition extends WSChartDefinition {
    private AnalysisItem measure;
    private AnalysisItem xaxis;
    private AnalysisItem yaxis;

    public AnalysisItem getXaxis() {
        return xaxis;
    }

    public void setXaxis(AnalysisItem xaxis) {
        this.xaxis = xaxis;
    }

    public AnalysisItem getYaxis() {
        return yaxis;
    }

    public void setYaxis(AnalysisItem yaxis) {
        this.yaxis = yaxis;
    }

    public AnalysisItem getMeasure() {
        return measure;
    }

    public void setMeasure(AnalysisItem measure) {
        this.measure = measure;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("xAxis", Arrays.asList(xaxis), structure);
        addItems("yAxis", Arrays.asList(yaxis), structure);
        addItems("measure", Arrays.asList(measure), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        xaxis = firstItem("xAxis", structure);
        yaxis = firstItem("yAxis", structure);
        measure = firstItem("measure", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        columnList.add(measure);
        columnList.add(xaxis);
        columnList.add(yaxis);
        return columnList;
    }
}
