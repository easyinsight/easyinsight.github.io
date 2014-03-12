package com.easyinsight.analysis;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;
import com.easyinsight.pipeline.IComponent;
import com.easyinsight.pipeline.MinMaxComponent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * User: James Boe
 * Date: Jan 11, 2008
 * Time: 9:10:35 PM
 */
public abstract class WSChartDefinition extends WSAnalysisDefinition {
    private LimitsMetadata limitsMetadata;
    private boolean limitOther;

    private double rotationAngle;
    private double elevationAngle;

    private boolean showLegend;

    private String xAxisLabel;
    private String yAxisLabel;

    private double xAxisMinimum;
    private boolean xAxisMinimumDefined;

    private double xAxisMaximum;
    private boolean xAxisMaximumDefined;

    private double yAxisMininum;
    private boolean yAxisMinimumDefined;

    private double yAxisMaximum;
    private boolean yAxisMaximumDefined;

    private boolean xAxisBaseAtZero;
    private boolean yAxisBaseAtZero;

    public abstract int getChartType();

    public abstract int getChartFamily();

    public boolean isLimitOther() {
        return limitOther;
    }

    public void setLimitOther(boolean limitOther) {
        this.limitOther = limitOther;
    }

    public boolean isxAxisBaseAtZero() {
        return xAxisBaseAtZero;
    }

    public void setxAxisBaseAtZero(boolean xAxisBaseAtZero) {
        this.xAxisBaseAtZero = xAxisBaseAtZero;
    }

    public boolean isyAxisBaseAtZero() {
        return yAxisBaseAtZero;
    }

    public void setyAxisBaseAtZero(boolean yAxisBaseAtZero) {
        this.yAxisBaseAtZero = yAxisBaseAtZero;
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    public void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public String getyAxisLabel() {
        return yAxisLabel;
    }

    public void setyAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public boolean isShowLegend() {
        return showLegend;
    }

    public void setShowLegend(boolean showLegend) {
        this.showLegend = showLegend;
    }

    private long chartDefinitionID;

    public long getChartDefinitionID() {
        return chartDefinitionID;
    }

    public void setChartDefinitionID(long chartDefinitionID) {
        this.chartDefinitionID = chartDefinitionID;
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public double getElevationAngle() {
        return elevationAngle;
    }

    public void setElevationAngle(double elevationAngle) {
        this.elevationAngle = elevationAngle;
    }

    public LimitsMetadata getLimitsMetadata() {
        return limitsMetadata;
    }

    public void setLimitsMetadata(LimitsMetadata limitsMetadata) {
        this.limitsMetadata = limitsMetadata;
    }

    public String getDataFeedType() {
        return "Chart";
    }

    public LimitsResults applyLimits(DataSet dataSet) {
        LimitsResults limitsResults;
        if (limitsMetadata != null && limitsMetadata.isLimitEnabled()) {
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
        } else {
            limitsResults = super.applyLimits(dataSet);
        }
        return limitsResults;
    }

    @Override
    public List<IComponent> createComponents() {
        List<IComponent> components = super.createComponents();
        components.add(new MinMaxComponent());
        return components;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        showLegend = findBooleanProperty(properties, "showLegend", true);
        xAxisLabel = findStringProperty(properties, "xAxisLabel", "");
        yAxisLabel = findStringProperty(properties, "yAxisLabel", "");
        xAxisBaseAtZero = findBooleanProperty(properties, "xAxisBaseAtZero", true);
        yAxisBaseAtZero = findBooleanProperty(properties, "yAxisBaseAtZero", true);
        limitOther = findBooleanProperty(properties, "limitOther", true);
        xAxisMinimum = findNumberProperty(properties, "xAxisMinimum", 0);
        xAxisMinimumDefined = findBooleanProperty(properties, "xAxisMinimumDefined", false);
        xAxisMaximum = findNumberProperty(properties, "xAxisMaximum", 0);
        xAxisMaximumDefined = findBooleanProperty(properties, "xAxisMaximumDefined", false);
        yAxisMininum = findNumberProperty(properties, "yAxisMinimum", 0);
        yAxisMinimumDefined = findBooleanProperty(properties, "yAxisMinimumDefined", false);
        yAxisMaximum = findNumberProperty(properties, "yAxisMaximum", 0);
        yAxisMaximumDefined = findBooleanProperty(properties, "yAxisMaximumDefined", false);
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportBooleanProperty("showLegend", showLegend));
        properties.add(new ReportStringProperty("xAxisLabel", xAxisLabel));
        properties.add(new ReportStringProperty("yAxisLabel", yAxisLabel));
        properties.add(new ReportBooleanProperty("xAxisBaseAtZero", xAxisBaseAtZero));
        properties.add(new ReportBooleanProperty("yAxisBaseAtZero", yAxisBaseAtZero));
        properties.add(new ReportBooleanProperty("limitOther", limitOther));
        properties.add(new ReportNumericProperty("xAxisMinimum", xAxisMinimum));
        properties.add(new ReportNumericProperty("xAxisMaximum", xAxisMaximum));
        properties.add(new ReportNumericProperty("yAxisMininum", yAxisMininum));
        properties.add(new ReportNumericProperty("yAxisMaximum", yAxisMaximum));
        properties.add(new ReportBooleanProperty("xAxisMinimumDefined", xAxisMinimumDefined));
        properties.add(new ReportBooleanProperty("xAxisMaximumDefined", xAxisMaximumDefined));
        properties.add(new ReportBooleanProperty("yAxisMinimumDefined", yAxisMinimumDefined));
        properties.add(new ReportBooleanProperty("yAxisMaximumDefined", yAxisMaximumDefined));
        return properties;
    }

    @Override
    public List<String> javaScriptIncludes() {
        List<String> includes = super.javaScriptIncludes();
        //includes.add("/js/jquery.jqplot.js");
        includes.add("/js/plugins/jqplot.pointLabels.js");
        includes.add("/js/plugins/jqplot.highlighter.min.js");
        includes.add("/js/plugins/jqplot.cursor.min.js");

        return includes;
    }

    public List<String> cssIncludes() {
        List<String> includes = new ArrayList<String>();
        includes.add("/css/jquery.jqplot.min.css");
        return includes;
    }

    protected JSONObject getLegend() throws JSONException {
        JSONObject legendObj = new JSONObject();
        legendObj.put("show", "true");
        legendObj.put("placement", "'outsideGrid'");
        legendObj.put("location", "'e'");

        legendObj.put("fontFamily", fontName());
        legendObj.put("border", "'none'");
        return legendObj;
    }

    protected String fontName() {
        if (isUseCustomFontFamily() && !"".equals(getCustomFontFamily())) {
            return "'" + getCustomFontFamily() + "'";
        }
        return "'Helvetica Neue'";
    }

    protected JSONObject getGrid() throws JSONException {
        JSONObject grid = new JSONObject();
        grid.put("background", "'#FFFFFF'");
        return grid;
    }

    protected List<MultiColor> configuredMultiColors() {
        return null;
    }

    public List<String> createMultiColors() {
        List<MultiColor> multiColors = configuredMultiColors();
        List<String> resultColors = new ArrayList<String>();
        if (multiColors != null && !multiColors.isEmpty()) {
            MultiColor testColor = multiColors.get(0);
            if (testColor.isColor1StartEnabled()) {
                for (MultiColor color : multiColors) {
                    if (color.isColor1StartEnabled()) {
                        resultColors.add(String.format("'#%06X'", (0xFFFFFF & color.getColor1Start())));
                    }
                }
                return resultColors;
            }
        }
        return Arrays.asList("'#a6bc59'", "'#597197'", "'#d6ab2a'", "'#d86068'", "'#5d9942'",
                "'#7a4c6c'", "'#F0B400'", "'#1E6C0B'", "'#00488C'", "'#332600'", "'#D84000'");
    }

    protected JSONArray getSeriesColors() {
        return new JSONArray(Arrays.asList("'#a6bc59'", "'#597197'", "'#d6ab2a'", "'#d86068'", "'#5d9942'",
                "'#7a4c6c'", "'#F0B400'", "'#1E6C0B'", "'#00488C'", "'#332600'", "'#D84000'"));
    }

    public double getxAxisMinimum() {
        return xAxisMinimum;
    }

    public void setxAxisMinimum(double xAxisMinimum) {
        this.xAxisMinimum = xAxisMinimum;
    }

    public boolean isxAxisMinimumDefined() {
        return xAxisMinimumDefined;
    }

    public void setxAxisMinimumDefined(boolean xAxisMinimumDefined) {
        this.xAxisMinimumDefined = xAxisMinimumDefined;
    }

    public double getxAxisMaximum() {
        return xAxisMaximum;
    }

    public void setxAxisMaximum(double xAxisMaximum) {
        this.xAxisMaximum = xAxisMaximum;
    }

    public boolean isxAxisMaximumDefined() {
        return xAxisMaximumDefined;
    }

    public void setxAxisMaximumDefined(boolean xAxisMaximumDefined) {
        this.xAxisMaximumDefined = xAxisMaximumDefined;
    }

    public double getyAxisMininum() {
        return yAxisMininum;
    }

    public void setyAxisMininum(double yAxisMininum) {
        this.yAxisMininum = yAxisMininum;
    }

    public boolean isyAxisMinimumDefined() {
        return yAxisMinimumDefined;
    }

    public void setyAxisMinimumDefined(boolean yAxisMinimumDefined) {
        this.yAxisMinimumDefined = yAxisMinimumDefined;
    }

    public double getyAxisMaximum() {
        return yAxisMaximum;
    }

    public void setyAxisMaximum(double yAxisMaximum) {
        this.yAxisMaximum = yAxisMaximum;
    }

    public boolean isyAxisMaximumDefined() {
        return yAxisMaximumDefined;
    }

    public void setyAxisMaximumDefined(boolean yAxisMaximumDefined) {
        this.yAxisMaximumDefined = yAxisMaximumDefined;
    }

    protected void axisConfigure(JSONObject axisObject, double min, boolean minEnabled, double max, boolean maxEnabled) throws JSONException {
        if (minEnabled) {
            axisObject.put("min", min);
        }
        if (maxEnabled) {
            axisObject.put("max", max);
        }
    }

    protected JSONObject getMeasureAxis(AnalysisItem analysisItem) throws JSONException {
        JSONObject yAxis = new JSONObject();
        yAxis.put("pad", 1.05);
        yAxis.put("label", "'"+analysisItem.toUnqualifiedDisplay()+"'");
        yAxis.put("labelRenderer", "$.jqplot.CanvasAxisLabelRenderer");
        yAxis.put("min", 0);
        yAxis.put("numberTicks", 8);
        JSONObject tickOptions = new JSONObject();
        if (analysisItem.getFormattingType() == FormattingConfiguration.CURRENCY) {
            tickOptions.put("formatter", "$.jqplot.currencyTickNumberFormatter");
        } else if (analysisItem.getFormattingType() == FormattingConfiguration.MILLISECONDS || analysisItem.getFormattingType() == FormattingConfiguration.SECONDS) {
            tickOptions.put("formatter", "millisecondFormatter");
            tickOptions.put("formatString", analysisItem.getFormattingType() == FormattingConfiguration.MILLISECONDS ? "'ms'" : "'s'");
        } else {
            tickOptions.put("formatter", "$.jqplot.tickNumberFormatter");
        }
        JSONObject labelOptions = new JSONObject();
        labelOptions.put("fontFamily", fontName());
        tickOptions.put("fontFamily", fontName());
        yAxis.put("labelOptions", labelOptions);
        yAxis.put("tickOptions", tickOptions);
        return yAxis;
    }

    protected JSONObject getGroupingAxis(AnalysisItem analysisItem) throws JSONException {
        JSONObject xAxis = new JSONObject();
        xAxis.put("renderer", "$.jqplot.CategoryAxisRenderer");

        xAxis.put("tickRenderer", "$.jqplot.CanvasAxisTickRenderer");
        xAxis.put("labelRenderer", "$.jqplot.CanvasAxisLabelRenderer");
        xAxis.put("label", "'"+analysisItem.toUnqualifiedDisplay()+"'");

        JSONObject labelOptions = new JSONObject();
        labelOptions.put("fontFamily", fontName());
        xAxis.put("labelOptions", labelOptions);
        JSONObject xAxisTicketOptions = new JSONObject();
        xAxisTicketOptions.put("angle", 0);
        xAxisTicketOptions.put("showGridline", "false");
        xAxisTicketOptions.put("fontFamily", fontName());
        xAxis.put("tickOptions", xAxisTicketOptions);
        return xAxis;
    }


    public JSONObject getAxes() throws JSONException {
        return null;
    }

    protected JSONArray transformColors(JSONArray colors) {
        JSONArray newColors = new JSONArray();
        try {

            for (int i = 0; i < colors.length(); i++) {
                JSONArray gradient = new JSONArray();
                String color2 = "'#FFFFFF'";
                Object color = colors.get(i);


                JSONObject colorStop = new JSONObject();
                colorStop.put("point", 1);
                colorStop.put("color", color);
                gradient.put(colorStop);

                colorStop = new JSONObject();
                colorStop.put("point", .9);
                colorStop.put("color", color2);
                gradient.put(colorStop);

                colorStop = new JSONObject();
                colorStop.put("point", .5);
                colorStop.put("color", color);
                gradient.put(colorStop);

                colorStop = new JSONObject();
                colorStop.put("point", .15);
                colorStop.put("color", color);
                gradient.put(colorStop);

                colorStop = new JSONObject();
                colorStop.put("point", 0);
                colorStop.put("color", color2);
                gradient.put(colorStop);


                newColors.put(gradient);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return newColors;
    }
}
