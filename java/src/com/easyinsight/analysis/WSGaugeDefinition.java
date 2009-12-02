package com.easyinsight.analysis;

import java.util.Set;
import java.util.Map;
import java.util.Arrays;
import java.util.HashSet;

/**
 * User: James Boe
 * Date: Mar 31, 2009
 * Time: 10:06:29 AM
 */
public class WSGaugeDefinition extends WSAnalysisDefinition {

    private AnalysisItem measure;
    private int gaugeType;
    private long gaugeDefinitionID;
    private double maxValue;

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public long getGaugeDefinitionID() {
        return gaugeDefinitionID;
    }

    public void setGaugeDefinitionID(long gaugeDefinitionID) {
        this.gaugeDefinitionID = gaugeDefinitionID;
    }

    public int getGaugeType() {
        return gaugeType;
    }

    public void setGaugeType(int gaugeType) {
        this.gaugeType = gaugeType;
    }

    public AnalysisItem getMeasure() {
        return measure;
    }

    public void setMeasure(AnalysisItem measure) {
        this.measure = measure;
    }

    public String getDataFeedType() {
        return AnalysisTypes.GAUGE;
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        columnList.add(measure);
        return columnList;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("measure", Arrays.asList(measure), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        measure = firstItem("measure", structure);
    }
}
