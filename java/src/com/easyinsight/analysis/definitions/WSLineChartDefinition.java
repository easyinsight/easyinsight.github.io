package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:45:18 PM
 */
public class WSLineChartDefinition extends WSTwoAxisDefinition {

    private boolean autoScale = true;
    private boolean autoScaled = false;
    private Date xAxisMaximum = null;

    private int strokeWeight = 2;
    private boolean alignLabelsToUnits = true;

    private boolean showPoints = true;

    private List<MultiColor> multiColors = new ArrayList<MultiColor>();
    private int legendMaxWidth;

    /*public LimitsResults applyLimits(DataSet dataSet) {
        LimitsResults limitsResults;
        LimitsMetadata limitsMetadata = getLimitsMetadata();
        if (limitsMetadata != null && limitsMetadata.isLimitEnabled()) {
            if (!isMultiMeasure()) {
                AnalysisItem xAxisItem = getXaxis();
                AnalysisItem measureItem = getMeasure();
                final Map<Value, Aggregation> aggregationMap = new HashMap<Value, Aggregation>();
                AggregationFactory aggregationFactory = new AggregationFactory((AnalysisMeasure) measureItem, false);
                for (IRow row : dataSet.getRows()) {
                    Value xAxisValue = row.getValue(xAxisItem);
                    Aggregation aggregation = aggregationMap.get(xAxisValue);
                    if (aggregation == null) {
                        aggregation = aggregationFactory.getAggregation();
                        aggregationMap.put(xAxisValue, aggregation);
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
                    aggregationValues.subList(0, limitsMetadata.getNumber());
                    IRow otherRow = dataSet.createRow();
                    Iterator<IRow> iter = dataSet.getRows().iterator();
                    while (iter.hasNext()) {
                        IRow row = iter.next();
                        if (row != otherRow) {
                            Value value = row.getValue(xAxisItem);
                            if (aggregationValues.contains(value)) {

                            } else {
                                row.addValue(xAxisItem.createAggregateKey(), new StringValue("Other"));
                            }
                        }
                    }
                } else {
                    limitsResults = super.applyLimits(dataSet);
                }
            } else {
                int count = dataSet.getRows().size();
                limitsResults = new LimitsResults(count >= limitsMetadata.getNumber(), count, limitsMetadata.getNumber());
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
                    dataSet.sort(analysisMeasure, limitsMetadata.isTop());
                    dataSet.subset(limitsMetadata.getNumber());
                }
            //}
        } else {
            limitsResults = super.applyLimits(dataSet);
        }
        return limitsResults;
    }*/

    public int getStrokeWeight() {
        return strokeWeight;
    }

    public void setStrokeWeight(int strokeWeight) {
        this.strokeWeight = strokeWeight;
    }

    public Date getXAxisMaximum() {
        return xAxisMaximum;
    }

    public void setXAxisMaximum(Date xAxisMaximum) {
        this.xAxisMaximum = xAxisMaximum;
    }

    public boolean isShowPoints() {
        return showPoints;
    }

    public void setShowPoints(boolean showPoints) {
        this.showPoints = showPoints;
    }

    public int getLegendMaxWidth() {
        return legendMaxWidth;
    }

    public void setLegendMaxWidth(int legendMaxWidth) {
        this.legendMaxWidth = legendMaxWidth;
    }

    public boolean isAutoScaled() {
        return autoScaled;
    }

    public void setAutoScaled(boolean autoScaled) {
        this.autoScaled = autoScaled;
    }

    public boolean isAlignLabelsToUnits() {
        return alignLabelsToUnits;
    }

    public void setAlignLabelsToUnits(boolean alignLabelsToUnits) {
        this.alignLabelsToUnits = alignLabelsToUnits;
    }

    public boolean isAutoScale() {
        return autoScale;
    }

    public void setAutoScale(boolean autoScale) {
        this.autoScale = autoScale;
    }

    public int getChartType() {
        return ChartDefinitionState.LINE_2D;
    }

    public int getChartFamily() {
        return ChartDefinitionState.LINE_FAMILY;
    }

    public void untweakReport(Map<AnalysisItem, AnalysisItem> aliasMap) {
        if (getXaxis() != null && getXaxis().hasType(AnalysisItemTypes.DATE_DIMENSION)) {
            AnalysisDateDimension date = (AnalysisDateDimension) getXaxis();
            if (date.getRevertDateLevel() != 0) {
                date.setDateLevel(date.getRevertDateLevel());
            }
        }
    }

    public void tweakReport(Map<AnalysisItem, AnalysisItem> aliasMap) {
        if (autoScale && getXaxis() != null && getXaxis().hasType(AnalysisItemTypes.DATE_DIMENSION)) {
            int daysDuration = 0;
        AnalysisDateDimension start;
        try {
            start = (AnalysisDateDimension) getXaxis().clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        AnalysisDateDimension xAxis = (AnalysisDateDimension) this.getXaxis();
            if (getFilterDefinitions() != null) {
                for (FilterDefinition filterDefinition : getFilterDefinitions()) {
                    if (filterDefinition instanceof RollingFilterDefinition) {
                        RollingFilterDefinition rollingFilterDefinition = (RollingFilterDefinition) filterDefinition;
                        long now = System.currentTimeMillis();
                        daysDuration = (int)((now - MaterializedRollingFilterDefinition.findStartDate(rollingFilterDefinition, new Date())) / (1000 * 60 * 60 * 24));
                    } else if (filterDefinition instanceof FilterDateRangeDefinition) {
                        FilterDateRangeDefinition filterDateRangeDefinition = (FilterDateRangeDefinition) filterDefinition;
                        daysDuration = (int)((filterDateRangeDefinition.getEndDate().getTime() - filterDateRangeDefinition.getStartDate().getTime()) / (1000 * 60 * 60 * 24));
                    }
                }
            }
            if (daysDuration > (365 * 6)) {
                if (xAxis.getDateLevel() != AnalysisDateDimension.YEAR_LEVEL) {
                    autoScaled = true;
                    xAxis.setRevertDateLevel(xAxis.getDateLevel());
                    xAxis.setDateLevel(AnalysisDateDimension.YEAR_LEVEL);
                }
            } else if (daysDuration > (365 * 2)) {
                if (xAxis.getDateLevel() != AnalysisDateDimension.MONTH_LEVEL) {
                    autoScaled = true;
                    xAxis.setRevertDateLevel(xAxis.getDateLevel());
                    xAxis.setDateLevel(AnalysisDateDimension.MONTH_LEVEL);
                }
            } else if (daysDuration >= (90)) {
                if (xAxis.getDateLevel() != AnalysisDateDimension.WEEK_LEVEL) {
                    autoScaled = true;
                    xAxis.setRevertDateLevel(xAxis.getDateLevel());
                    xAxis.setDateLevel(AnalysisDateDimension.WEEK_LEVEL);
                }
            } else if (daysDuration > 6) {
                if (xAxis.getDateLevel() != AnalysisDateDimension.DAY_LEVEL) {
                    autoScaled = true;
                    xAxis.setRevertDateLevel(xAxis.getDateLevel());
                    xAxis.setDateLevel(AnalysisDateDimension.DAY_LEVEL);
                }
            }
            if (xAxis.hasType(AnalysisItemTypes.STEP)) {
                AnalysisStep analysisStep = (AnalysisStep) xAxis;
                analysisStep.getStartDate().setDateLevel(analysisStep.getDateLevel());
                analysisStep.getEndDate().setDateLevel(analysisStep.getDateLevel());
            }
            aliasMap.put(xAxis, start);
        }
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        strokeWeight = (int) findNumberProperty(properties, "strokeWeight", 2);
        legendMaxWidth = (int) findNumberProperty(properties, "legendMaxWidth", 200);
        autoScale = findBooleanProperty(properties, "autoScale", false);
        alignLabelsToUnits = findBooleanProperty(properties, "alignLabelsToUnits", true);
        showPoints = findBooleanProperty(properties, "showPoints", true);
        multiColors = multiColorProperty(properties, "multiColors");
    }

    public List<MultiColor> getMultiColors() {
        return multiColors;
    }

    public void setMultiColors(List<MultiColor> multiColors) {
        this.multiColors = multiColors;
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("strokeWeight", strokeWeight));
        properties.add(new ReportNumericProperty("legendMaxWidth", legendMaxWidth));
        properties.add(new ReportBooleanProperty("autoScale", autoScale));
        properties.add(new ReportBooleanProperty("alignLabelsToUnits", alignLabelsToUnits));
        properties.add(new ReportBooleanProperty("showPoints", showPoints));
        properties.add(ReportMultiColorProperty.fromColors(multiColors, "multiColors"));
        return properties;
    }

    @Override
    public List<String> javaScriptIncludes() {
        List<String> includes = super.javaScriptIncludes();
        includes.add("/js/plugins/jqplot.dateAxisRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasTextRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasAxisTickRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasAxisLabelRenderer.min.js");
        includes.add("/js/plugins/jqplot.enhancedLegendRenderer.min.js");
        includes.add("/js/visualizations/chart.js");
        includes.add("/js/visualizations/util.js");
        return includes;
    }

    @Override
    public String toHTML(String targetDiv, HTMLReportMetadata htmlReportMetadata) {

        JSONObject params;
        JSONObject object = new JSONObject();
        try {
            Map<String, Object> jsonParams = new LinkedHashMap<String, Object>();

            JSONObject grid = getGrid();
            jsonParams.put("grid", grid);
            JSONObject axes = new JSONObject();
            JSONObject xAxis = getGroupingAxis(getXaxis());
            xAxis.put("renderer", "$.jqplot.DateAxisRenderer");

            JSONObject xAxisTickOptions = xAxis.getJSONObject("tickOptions");
            AnalysisDateDimension date = (AnalysisDateDimension) this.getXaxis();
            if (date.getDateLevel() == AnalysisDateDimension.DAY_LEVEL) {
                xAxisTickOptions.put("formatString", "'%b %#d'");
            } else if (date.getDateLevel() == AnalysisDateDimension.MONTH_LEVEL) {
                xAxisTickOptions.put("formatString", "'%b'");
            } else if (date.getDateLevel() == AnalysisDateDimension.YEAR_LEVEL) {
                xAxisTickOptions.put("formatString", "'%b'");
            } else {
                xAxisTickOptions.put("formatString", "'%b %#d'");
            }

            xAxis.put("tickOptions", xAxisTickOptions);
            axes.put("xaxis", xAxis);
            if (getMeasure() != null) {
                axes.put("yaxis", getMeasureAxis(getMeasure()));
            }

            jsonParams.put("axes", axes);
            JSONObject legend = getLegend();
            jsonParams.put("legend", legend);
            JSONObject highlighter = new JSONObject();
            highlighter.put("show", true);
            highlighter.put("sizeAdjust", 7.5);
            highlighter.put("useAxesFormatters", "true");
            jsonParams.put("highlighter", highlighter);
            JSONObject cursor = new JSONObject();
            cursor.put("show", false);
            jsonParams.put("cursor", cursor);
            JSONArray seriesColors = getSeriesColors();
            jsonParams.put("seriesColors", seriesColors);
            params = new JSONObject(jsonParams);
            object.put("jqplotOptions", params);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String argh = object.toString();
        argh = argh.replaceAll("\"", "");
        String timezoneOffset = "&timezoneOffset='+new Date().getTimezoneOffset()+'";
        String customHeight = htmlReportMetadata.createStyleProperties();
        argh = "$.getJSON('/app/twoAxisChart?reportID="+getUrlKey()+timezoneOffset+"&'+ strParams, Chart.getCallback('" + targetDiv + "', " + argh + ", true,"+customHeight+"))";
        return argh;
    }
}
