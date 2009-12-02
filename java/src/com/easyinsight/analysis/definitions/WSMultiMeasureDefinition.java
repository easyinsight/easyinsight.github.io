package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.WSChartDefinition;
import com.easyinsight.analysis.AnalysisItem;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:45:09 PM
 */
public abstract class WSMultiMeasureDefinition extends WSChartDefinition {
    private List<AnalysisItem> measures;
    private AnalysisItem xaxis;

    public AnalysisItem getXaxis() {
        return xaxis;
    }

    public void setXaxis(AnalysisItem xaxis) {
        this.xaxis = xaxis;
    }

    public List<AnalysisItem> getMeasures() {
        return measures;
    }

    public void setMeasures(List<AnalysisItem> measures) {
        this.measures = measures;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("xAxis", Arrays.asList(xaxis), structure);
        addItems("measures", measures, structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        xaxis = firstItem("xAxis", structure);
        measures = items("measures", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        columnList.addAll(measures);
        columnList.add(xaxis);
        return columnList;
    }
}