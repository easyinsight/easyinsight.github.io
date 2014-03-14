package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 4:49:42 PM
 */
public abstract class WSXAxisDefinition extends WSChartDefinition {

    private List<AnalysisItem> measures;
    private AnalysisItem xaxis;

    private double yAxisMin;
    private double yAxisMax;

    public LimitsResults applyLimits(DataSet dataSet) {
        LimitsResults limitsResults;
        if (getLimitsMetadata() != null && getLimitsMetadata().isLimitEnabled()) {
            int count = dataSet.getRows().size();
            limitsResults = new LimitsResults(count >= getLimitsMetadata().getNumber(), count, getLimitsMetadata().getNumber());
            Map<String, AnalysisItem> structure = new HashMap<String, AnalysisItem>();
            createReportStructure(structure);
            AnalysisMeasure analysisMeasure = null;
            for (AnalysisItem analysisItem : structure.values()) {
                if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                    analysisMeasure = (AnalysisMeasure) analysisItem;
                    break;
                }
            }
            if (analysisMeasure != null) {
                dataSet.sort(analysisMeasure, getLimitsMetadata().isTop());
                List<IRow> subset = dataSet.subset(getLimitsMetadata().getNumber());
                if (subset.size() > 0 && isLimitOther()) {
                    IRow other = dataSet.createRow();
                    Map<AnalysisItem, Aggregation> map = new HashMap<AnalysisItem, Aggregation>();
                    List<Value> otherValues = new ArrayList<Value>();
                    for (AnalysisItem measure1 : getMeasures()) {
                        AggregationFactory aggFactory = new AggregationFactory((AnalysisMeasure) measure1, false);
                        map.put(measure1, aggFactory.getAggregation());
                    }

                    for (IRow row : subset) {
                        for (AnalysisItem measure : getMeasures()) {
                            map.get(measure).addValue(row.getValue(measure));
                        }
                        otherValues.add(row.getValue(getXaxis().createAggregateKey()));
                    }
                    Value otherValue = new StringValue("Other");
                    otherValue.setOtherValues(otherValues);
                    other.addValue(getXaxis().createAggregateKey(), otherValue);
                    for (AnalysisItem measure : getMeasures()) {
                        other.addValue(measure.createAggregateKey(), map.get(measure).getValue());
                    }
                }
            }
        } else {
            limitsResults = super.applyLimits(dataSet);
        }
        return limitsResults;
    }

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

    public List<AnalysisItem> getMeasures() {
        return measures;
    }

    public void setMeasures(List<AnalysisItem> measures) {
        this.measures = measures;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("xAxis", Arrays.asList(xaxis), structure);
        addItems("measure", measures, structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        xaxis = firstItem("xAxis", structure);
        measures = items("measure", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        if (measures != null) {
            for (AnalysisItem analysisItem : measures) {
                columnList.add(analysisItem);
            }
        }
        columnList.add(xaxis);
        return columnList;
    }
}
