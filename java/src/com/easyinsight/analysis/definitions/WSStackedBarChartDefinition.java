package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;
import com.easyinsight.preferences.ApplicationSkin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 20, 2009
 * Time: 7:23:14 PM
 */
public class WSStackedBarChartDefinition extends WSYAxisDefinition {

    private int chartColor;
    private boolean useChartColor;
    private String columnSort;
    private AnalysisItem stackItem;
    private String labelPosition = "none";
    private int labelInsideFontColor;
    private boolean useInsideLabelFontColor;
    private int labelFontSize;
    private String labelFontWeight;
    private List<MultiColor> multiColors = new ArrayList<MultiColor>();
    private String stackSort;
    private int stackLimit = 15;

    public LimitsResults applyLimits(DataSet dataSet) {
        LimitsResults limitsResults;
        LimitsMetadata limitsMetadata = getLimitsMetadata();
        if (limitsMetadata != null && limitsMetadata.isLimitEnabled()) {

            AnalysisItem xAxisItem = getYaxis();
            AnalysisItem measureItem = getMeasures().get(0);
            AnalysisItem stackAxisItem = getStackItem();
            final Map<Value, Aggregation> aggregationMap = new HashMap<Value, Aggregation>();
            AggregationFactory aggregationFactory = new AggregationFactory((AnalysisMeasure) measureItem, false);
            for (IRow row : dataSet.getRows()) {
                Value yAxisValue = row.getValue(xAxisItem);
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
                    Value value = row.getValue(xAxisItem);
                    /*if (aggregationValues.contains(value)) {

                    } else {
                        row.addValue(xAxisItem.createAggregateKey(), new StringValue("Other"));
                    }*/
                    //}
                    if (!aggregationValues.contains(value)) {
                        //row.addValue(yAxisItem.createAggregateKey(), new StringValue("Other"));
                        Value stackValue = row.getValue(stackItem.createAggregateKey());
                        Aggregation aggregation = aggregateMap.get(stackValue);
                        if (aggregation == null) {
                            aggregation = aggregationFactory.getAggregation();
                            aggregateMap.put(stackValue, aggregation);
                        }
                        aggregation.addValue(row.getValue(measureItem));
                        //others.add(row);
                        iter.remove();
                    }
                }

                if (isLimitOther()) {

                    for (Map.Entry<Value, Aggregation> entry : aggregateMap.entrySet()) {
                        IRow otherRow = dataSet.createRow();
                        otherRow.addValue(xAxisItem.createAggregateKey(), new StringValue("Other"));
                        otherRow.addValue(stackItem.createAggregateKey(), entry.getKey());
                        otherRow.addValue(measureItem.createAggregateKey(), entry.getValue().getValue());
                    }

                }

                limitsResults = new LimitsResults(true, limitsMetadata.getNumber(), aggregationValues.size());
            } else {
                limitsResults = super.applyLimits(dataSet);
            }
        } else {
            limitsResults = super.applyLimits(dataSet);
        }
        if (stackLimit > 0) {
            AnalysisItem xAxisItem = getYaxis();
            AnalysisItem measureItem = getMeasures().get(0);
            AnalysisItem stackAxisItem = getStackItem();
            final Map<Value, Aggregation> aggregationMap = new HashMap<Value, Aggregation>();
            AggregationFactory aggregationFactory = new AggregationFactory((AnalysisMeasure) measureItem, false);
            for (IRow row : dataSet.getRows()) {
                Value yAxisValue = row.getValue(stackAxisItem);
                Aggregation aggregation = aggregationMap.get(yAxisValue);
                if (aggregation == null) {
                    aggregation = aggregationFactory.getAggregation();
                    aggregationMap.put(yAxisValue, aggregation);
                }
                aggregation.addValue(row.getValue(measureItem));
            }
            List<Value> aggregationValues = new ArrayList<Value>(aggregationMap.keySet());
            if (aggregationValues.size() > stackLimit) {
                Collections.sort(aggregationValues, new Comparator<Value>() {

                    public int compare(Value value, Value value1) {
                        return aggregationMap.get(value1).getValue().toDouble().compareTo(aggregationMap.get(value).toDouble());
                    }
                });
                aggregationValues = aggregationValues.subList(0, stackLimit);
                //IRow otherRow = dataSet.createRow();
                Iterator<IRow> iter = dataSet.getRows().iterator();


                Map<Value, Aggregation> aggregateMap = new HashMap<Value, Aggregation>();
                while (iter.hasNext()) {
                    IRow row = iter.next();
                    //if (row != otherRow) {
                    Value value = row.getValue(stackAxisItem);
                    /*if (aggregationValues.contains(value)) {

                    } else {
                        row.addValue(xAxisItem.createAggregateKey(), new StringValue("Other"));
                    }*/
                    //}
                    if (!aggregationValues.contains(value)) {
                        //row.addValue(yAxisItem.createAggregateKey(), new StringValue("Other"));
                        Value stackValue = row.getValue(xAxisItem.createAggregateKey());
                        Aggregation aggregation = aggregateMap.get(stackValue);
                        if (aggregation == null) {
                            aggregation = aggregationFactory.getAggregation();
                            aggregateMap.put(stackValue, aggregation);
                        }
                        aggregation.addValue(row.getValue(measureItem));
                        //others.add(row);
                        iter.remove();
                    }
                }

                if (isLimitOther()) {

                    for (Map.Entry<Value, Aggregation> entry : aggregateMap.entrySet()) {
                        IRow otherRow = dataSet.createRow();
                        otherRow.addValue(stackItem.createAggregateKey(), new StringValue("Other"));
                        otherRow.addValue(xAxisItem.createAggregateKey(), entry.getKey());
                        otherRow.addValue(measureItem.createAggregateKey(), entry.getValue().getValue());
                    }

                }
            }
        }
        return limitsResults;
    }

    public int getStackLimit() {
        return stackLimit;
    }

    public void setStackLimit(int stackLimit) {
        this.stackLimit = stackLimit;
    }

    public String getStackSort() {
        return stackSort;
    }

    public void setStackSort(String stackSort) {
        this.stackSort = stackSort;
    }

    public List<MultiColor> getMultiColors() {
        return multiColors;
    }

    public void setMultiColors(List<MultiColor> multiColors) {
        this.multiColors = multiColors;
    }

    public int getLabelFontSize() {
        return labelFontSize;
    }

    public void setLabelFontSize(int labelFontSize) {
        this.labelFontSize = labelFontSize;
    }

    public String getLabelFontWeight() {
        return labelFontWeight;
    }

    public void setLabelFontWeight(String labelFontWeight) {
        this.labelFontWeight = labelFontWeight;
    }

    public String getLabelPosition() {
        return labelPosition;
    }

    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }

    public AnalysisItem getStackItem() {
        return stackItem;
    }

    public void setStackItem(AnalysisItem stackItem) {
        this.stackItem = stackItem;
    }

    public int getChartType() {
        return ChartDefinitionState.BAR_2D_STACKED;
    }

    public int getChartFamily() {
        return ChartDefinitionState.BAR_FAMILY;
    }

    public String getColumnSort() {
        return columnSort;
    }

    public void setColumnSort(String columnSort) {
        this.columnSort = columnSort;
    }

    public int getChartColor() {
        return chartColor;
    }

    public void setChartColor(int chartColor) {
        this.chartColor = chartColor;
    }

    public boolean isUseChartColor() {
        return useChartColor;
    }

    public void setUseChartColor(boolean useChartColor) {
        this.useChartColor = useChartColor;
    }

    public int getLabelInsideFontColor() {
        return labelInsideFontColor;
    }

    public void setLabelInsideFontColor(int labelInsideFontColor) {
        this.labelInsideFontColor = labelInsideFontColor;
    }

    public boolean isUseInsideLabelFontColor() {
        return useInsideLabelFontColor;
    }

    public void setUseInsideLabelFontColor(boolean useInsideLabelFontColor) {
        this.useInsideLabelFontColor = useInsideLabelFontColor;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        chartColor = (int) findNumberProperty(properties, "chartColor", 0);
        useChartColor = findBooleanProperty(properties, "useChartColor", false);
        columnSort = findStringProperty(properties, "columnSort", "Unsorted");
        stackSort = findStringProperty(properties, "stackSort", "Unsorted");
        labelFontWeight = findStringProperty(properties, "labelFontWeight", "none");
        labelFontSize = (int) findNumberProperty(properties, "labelFontSize", 12);
        labelPosition = findStringProperty(properties, "labelPosition", "none");
        labelInsideFontColor = (int) findNumberProperty(properties, "labelInsideFontColor", 0);
        useInsideLabelFontColor = findBooleanProperty(properties, "useInsideLabelFontColor", false);
        stackLimit = (int) findNumberProperty(properties, "stackLimit", 15);
        multiColors = multiColorProperty(properties, "multiColors");
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("chartColor", chartColor));
        properties.add(new ReportBooleanProperty("useChartColor", useChartColor));
        properties.add(new ReportStringProperty("columnSort", columnSort));
        properties.add(new ReportStringProperty("stackSort", stackSort));
        properties.add(new ReportNumericProperty("labelFontSize", labelFontSize));
        properties.add(new ReportStringProperty("labelFontWeight", labelFontWeight));
        properties.add(new ReportStringProperty("labelPosition", labelPosition));
        properties.add(new ReportBooleanProperty("useInsideLabelFontColor", useInsideLabelFontColor));
        properties.add(new ReportNumericProperty("labelInsideFontColor", labelInsideFontColor));
        properties.add(new ReportNumericProperty("stackLimit", stackLimit));
        properties.add(ReportMultiColorProperty.fromColors(multiColors, "multiColors"));
        return properties;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        super.createReportStructure(structure);
        addItems("stackItem", Arrays.asList(stackItem), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        super.populateFromReportStructure(structure);
        stackItem = firstItem("stackItem", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = super.getAllAnalysisItems();
        columnList.add(stackItem);
        return columnList;
    }

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentDefinitions) throws JSONException {
        JSONObject pie = super.toJSON(htmlReportMetadata, parentDefinitions);
        pie.put("key", getUrlKey());
        pie.put("type", "stacked_bar");
        pie.put("styles", htmlReportMetadata.createStyleProperties());
        pie.put("url", "/app/stackedChart");
        return pie;
    }

    protected JSONArray getSeriesColors() {
        List<MultiColor> multiColors1 = new ArrayList<MultiColor>();
        for(MultiColor mc : multiColors) {
            if(mc.isColor1StartEnabled())
                multiColors1.add(mc);
        }
        if (multiColors1.size() > 0) {
            JSONArray colors = new JSONArray();
            try {

                for (MultiColor mc : multiColors1) {
                    if(mc.isColor1StartEnabled()) {
                        JSONArray gradient = new JSONArray();
                        JSONObject color1 = new JSONObject();
                        color1.put("color", String.format("'#%06X'", (0xFFFFFF & mc.getColor1Start())));
                        color1.put("point", 0);
                        JSONObject color2 = new JSONObject();
                        color2.put("color", String.format("'#%06X'", (0xFFFFFF & mc.getColor1Start())));
                        color2.put("point", 1);
                        gradient.put(color1);
                        gradient.put(color2);
                        colors.put(gradient);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return colors;

        } else {
            return transformColors(super.getSeriesColors());
        }
    }

    public void renderConfig(ApplicationSkin applicationSkin) {
        if ("Primary".equals(getColorScheme()) && applicationSkin.getMultiColors() != null && applicationSkin.getMultiColors().size() > 0 &&
                applicationSkin.getMultiColors().get(0).isColor1StartEnabled()) {
            setMultiColors(applicationSkin.getMultiColors());
        }
    }
}
