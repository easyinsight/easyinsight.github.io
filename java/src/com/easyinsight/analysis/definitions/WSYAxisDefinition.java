package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.WSChartDefinition;

import java.util.Map;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:43:40 PM
 */
public abstract class WSYAxisDefinition extends WSChartDefinition {
    private AnalysisItem measure;
    private AnalysisItem yaxis;

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
        addItems("yAxis", Arrays.asList(yaxis), structure);
        addItems("measure", Arrays.asList(measure), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        yaxis = firstItem("yAxis", structure);
        measure = firstItem("measure", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        columnList.add(measure);
        columnList.add(yaxis);
        return columnList;
    }
}
