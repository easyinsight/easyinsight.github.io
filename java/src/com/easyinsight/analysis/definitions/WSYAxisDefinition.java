package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:43:40 PM
 */
public abstract class WSYAxisDefinition extends WSChartDefinition {
    private List<AnalysisItem> measures;
    private AnalysisItem yaxis;

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
                if (subset.size() > 0) {
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
                        otherValues.add(row.getValue(getYaxis().createAggregateKey()));
                    }
                    Value otherValue = new StringValue("Other");
                    otherValue.setOtherValues(otherValues);
                    other.addValue(getYaxis().createAggregateKey(), otherValue);
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

    public AnalysisItem getYaxis() {
        return yaxis;
    }

    public void setYaxis(AnalysisItem yaxis) {
        this.yaxis = yaxis;
    }

    public List<AnalysisItem> getMeasures() {
        return measures;
    }

    public void setMeasures(List<AnalysisItem> measures) {
        this.measures = measures;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("yAxis", Arrays.asList(yaxis), structure);
        addItems("measure", measures, structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        yaxis = firstItem("yAxis", structure);
        measures = items("measure", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        if (measures != null) {
            for (AnalysisItem measure : measures) {
                columnList.add(measure);
            }
        }
        columnList.add(yaxis);
        return columnList;
    }

}
