package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:45:09 PM
 */
public abstract class WSTwoAxisDefinition extends WSChartDefinition {
    private AnalysisItem measure;
    private AnalysisItem xaxis;
    private AnalysisItem yaxis;
    private List<AnalysisItem> measures;
    private boolean multiMeasure;

    public LimitsResults applyLimits(DataSet dataSet) {
        LimitsResults limitsResults;
        LimitsMetadata limitsMetadata = getLimitsMetadata();
        if (limitsMetadata != null && limitsMetadata.isLimitEnabled()) {
            if (!isMultiMeasure()) {
                AnalysisItem yAxisItem = getYaxis();
                AnalysisItem measureItem = getMeasure();
                final Map<Value, Aggregation> aggregationMap = new HashMap<Value, Aggregation>();
                AggregationFactory aggregationFactory = new AggregationFactory((AnalysisMeasure) measureItem, false);
                for (IRow row : dataSet.getRows()) {
                    Value yAxisValue = row.getValue(yAxisItem);
                    Aggregation aggregation = aggregationMap.get(yAxisValue);
                    if (aggregation == null) {
                        aggregation = aggregationFactory.getAggregation();
                        aggregationMap.put(yAxisValue, aggregation);
                    }
                    aggregation.addValue(row.getValue(measureItem));
                }
                List<Value> aggregationValues = new ArrayList<Value>(aggregationMap.keySet());
                if (aggregationValues.size() > limitsMetadata.getNumber()) {
                    Collections.sort(aggregationValues, new Comparator<Value>() {

                        public int compare(Value value, Value value1) {
                            return aggregationMap.get(value1).getValue().toDouble().compareTo(aggregationMap.get(value).toDouble());
                        }
                    });
                    aggregationValues = aggregationValues.subList(0, limitsMetadata.getNumber());
                    //IRow otherRow = dataSet.createRow();
                    Iterator<IRow> iter = dataSet.getRows().iterator();


                    Map<Value, Aggregation> aggregateMap = new HashMap<Value, Aggregation>();
                    while (iter.hasNext()) {
                        IRow row = iter.next();
                        //if (row != otherRow) {
                        Value value = row.getValue(yAxisItem);
                        /*if (aggregationValues.contains(value)) {

                        } else {
                            row.addValue(xAxisItem.createAggregateKey(), new StringValue("Other"));
                        }*/
                        //}
                        if (!aggregationValues.contains(value)) {
                            //row.addValue(yAxisItem.createAggregateKey(), new StringValue("Other"));
                            Value xAxisValue = row.getValue(getXaxis());
                            Aggregation aggregation = aggregateMap.get(xAxisValue);
                            if (aggregation == null) {
                                aggregation = aggregationFactory.getAggregation();
                                aggregateMap.put(xAxisValue, aggregation);
                            }
                            aggregation.addValue(row.getValue(measureItem));
                            //others.add(row);
                            iter.remove();
                        }
                    }

                    if (isLimitOther()) {

                        for (Map.Entry<Value, Aggregation> entry : aggregateMap.entrySet()) {
                            IRow otherRow = dataSet.createRow();
                            otherRow.addValue(yAxisItem.createAggregateKey(), new StringValue("Other"));
                            otherRow.addValue(getXaxis().createAggregateKey(), entry.getKey());
                            otherRow.addValue(getMeasure().createAggregateKey(), entry.getValue().getValue());
                        }

                    }

                    limitsResults = new LimitsResults(true, limitsMetadata.getNumber(), aggregationValues.size());
                } else {
                    limitsResults = super.applyLimits(dataSet);
                }
            } else {
                limitsResults = super.applyLimits(dataSet);
            }
            return limitsResults;
        } else {
            return super.applyLimits(dataSet);
        }
    }

    public List<AnalysisItem> getMeasures() {
        return measures;
    }

    public void setMeasures(List<AnalysisItem> measures) {
        this.measures = measures;
    }

    public boolean isMultiMeasure() {
        return multiMeasure;
    }

    public void setMultiMeasure(boolean multiMeasure) {
        this.multiMeasure = multiMeasure;
    }

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
        addItems("measures", measures, structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        xaxis = firstItem("xAxis", structure);
        yaxis = firstItem("yAxis", structure);
        measure = firstItem("measure", structure);
        measures = items("measures", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        columnList.add(xaxis);
        if (multiMeasure) {
            columnList.addAll(measures);
        } else {
            columnList.add(measure);   
            columnList.add(yaxis);
        }

        return columnList;
    }

    private String form;
    private String baseAtZero;
    private String interpolateValues;

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getBaseAtZero() {
        return baseAtZero;
    }

    public void setBaseAtZero(String baseAtZero) {
        this.baseAtZero = baseAtZero;
    }

    public String getInterpolateValues() {
        return interpolateValues;
    }

    public void setInterpolateValues(String interpolateValues) {
        this.interpolateValues = interpolateValues;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        form = findStringProperty(properties, "form", "segment");
        baseAtZero = findStringProperty(properties, "baseAtZero", "true");
        interpolateValues = findStringProperty(properties, "interpolateValues", "false");
        multiMeasure = findBooleanProperty(properties, "multiMeasure", false);
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportStringProperty("form", form));
        properties.add(new ReportStringProperty("baseAtZero", baseAtZero));
        properties.add(new ReportStringProperty("interpolateValues", interpolateValues));
        properties.add(new ReportBooleanProperty("multiMeasure", multiMeasure));
        return properties;
    }
}
