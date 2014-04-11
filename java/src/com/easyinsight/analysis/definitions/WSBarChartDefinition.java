package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
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
public class WSBarChartDefinition extends WSYAxisDefinition {

    private int chartColor;
    private int gradientColor;
    private boolean useChartColor;
    private String columnSort;
    private String axisType = "Linear";
    private String labelPosition = "none";
    private int labelFontSize;
    private int labelInsideFontColor;
    private int labelOutsideFontColor;
    private boolean useInsideLabelFontColor;
    private boolean useOutsideLabelFontColor;
    private String labelFontWeight;
    private List<MultiColor> multiColors = new ArrayList<MultiColor>();

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

    public int getLabelInsideFontColor() {
        return labelInsideFontColor;
    }

    public void setLabelInsideFontColor(int labelInsideFontColor) {
        this.labelInsideFontColor = labelInsideFontColor;
    }

    public int getLabelOutsideFontColor() {
        return labelOutsideFontColor;
    }

    public void setLabelOutsideFontColor(int labelOutsideFontColor) {
        this.labelOutsideFontColor = labelOutsideFontColor;
    }

    public boolean isUseInsideLabelFontColor() {
        return useInsideLabelFontColor;
    }

    public void setUseInsideLabelFontColor(boolean useInsideLabelFontColor) {
        this.useInsideLabelFontColor = useInsideLabelFontColor;
    }

    public boolean isUseOutsideLabelFontColor() {
        return useOutsideLabelFontColor;
    }

    public void setUseOutsideLabelFontColor(boolean useOutsideLabelFontColor) {
        this.useOutsideLabelFontColor = useOutsideLabelFontColor;
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

    public String getAxisType() {
        return axisType;
    }

    public void setAxisType(String axisType) {
        this.axisType = axisType;
    }

    public int getGradientColor() {
        return gradientColor;
    }

    public void setGradientColor(int gradientColor) {
        this.gradientColor = gradientColor;
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

    public int getChartType() {
        return ChartDefinitionState.BAR_2D;
    }

    public int getChartFamily() {
        return ChartDefinitionState.BAR_FAMILY;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        chartColor = (int) findNumberProperty(properties, "chartColor", 0);
        gradientColor = (int) findNumberProperty(properties, "gradientColor", 0);
        useChartColor = findBooleanProperty(properties, "useChartColor", false);
        columnSort = findStringProperty(properties, "columnSort", "Unsorted");
        axisType = findStringProperty(properties, "axisType", "Linear");
        labelPosition = findStringProperty(properties, "labelPosition", "none");
        labelFontWeight = findStringProperty(properties, "labelFontWeight", "none");
        labelFontSize = (int) findNumberProperty(properties, "labelFontSize", 12);
        labelInsideFontColor = (int) findNumberProperty(properties, "labelInsideFontColor", 0);
        labelOutsideFontColor = (int) findNumberProperty(properties, "labelOutsideFontColor", 0);
        useInsideLabelFontColor = findBooleanProperty(properties, "useInsideLabelFontColor", false);
        useOutsideLabelFontColor = findBooleanProperty(properties, "useOutsideLabelFontColor", false);
        multiColors = multiColorProperty(properties, "multiColors");
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("chartColor", chartColor));
        properties.add(new ReportNumericProperty("gradientColor", gradientColor));
        properties.add(new ReportNumericProperty("labelFontSize", labelFontSize));
        properties.add(new ReportStringProperty("labelFontWeight", labelFontWeight));
        properties.add(new ReportBooleanProperty("useChartColor", useChartColor));
        properties.add(new ReportStringProperty("columnSort", columnSort));
        properties.add(new ReportStringProperty("axisType", axisType));
        properties.add(new ReportStringProperty("labelPosition", labelPosition));
        properties.add(new ReportBooleanProperty("useInsideLabelFontColor", useInsideLabelFontColor));
        properties.add(new ReportBooleanProperty("useOutsideLabelFontColor", useOutsideLabelFontColor));
        properties.add(new ReportNumericProperty("labelInsideFontColor", labelInsideFontColor));
        properties.add(new ReportNumericProperty("labelOutsideFontColor", labelOutsideFontColor));
        properties.add(ReportMultiColorProperty.fromColors(multiColors, "multiColors"));
        return properties;
    }

    @Override
    public List<String> javaScriptIncludes() {
        List<String> includes = super.javaScriptIncludes();
//        includes.add("/js/plugins/jqplot.gradientBarRenderer.js");
//        includes.add("/js/plugins/jqplot.categoryAxisRenderer.js");
//        includes.add("/js/plugins/jqplot.canvasTextRenderer.min.js");
//        includes.add("/js/plugins/jqplot.canvasAxisLabelRenderer.min.js");
//        includes.add("/js/plugins/jqplot.canvasAxisTickRenderer.min.js");
//        includes.add("/js/plugins/jqplot.pointLabels.js");
//        includes.add("/js/visualizations/chart.js");
//        includes.add("/js/visualizations/util.js");
        return includes;
    }

    @Override
    public String toHTML(String targetDiv, HTMLReportMetadata htmlReportMetadata) {
        JSONObject fullObject = getJsonObject(htmlReportMetadata);


        String argh = fullObject.toString();
        argh = argh.replaceAll("\"", "");

        String timezoneOffset = "&timezoneOffset='+new Date().getTimezoneOffset()+'";
        String customHeight = htmlReportMetadata.createStyleProperties().toString();
        return "$.getJSON('/app/columnChart?reportID=" + getUrlKey() + timezoneOffset + "&'+ strParams, Chart.getBarChartCallback('" + targetDiv + "', " + argh + ",false," + customHeight + "))";
    }

    private JSONObject getJsonObject(HTMLReportMetadata htmlReportMetadata) {
        String color;
        String color2;
        JSONArray colorObj = new JSONArray();
        try {
            if (useChartColor) {
                color = String.format("'#%06X'", (0xFFFFFF & chartColor));
                color2 = String.format("'#%06X'", (0xFFFFFF & gradientColor));
                JSONObject colorStop = new JSONObject();
                colorStop.put("point", 0);
                colorStop.put("color", color);
                colorObj.put(colorStop);

                colorStop = new JSONObject();
                colorStop.put("point", 1);
                colorStop.put("color", color2);
                colorObj.put(colorStop);
            } else {

                color = "'#FF0000'";
                color2 = "'#990000'";


                JSONObject colorStop = new JSONObject();
                colorStop.put("point", 0);
                colorStop.put("color", color);
                colorObj.put(colorStop);

                colorStop = new JSONObject();
                colorStop.put("point", .15);
                colorStop.put("color", color2);
                colorObj.put(colorStop);

                colorStop = new JSONObject();
                colorStop.put("point", .5);
                colorStop.put("color", color);
                colorObj.put(colorStop);

                colorStop = new JSONObject();
                colorStop.put("point", .9);
                colorStop.put("color", color);
                colorObj.put(colorStop);

                colorStop = new JSONObject();
                colorStop.put("point", 1);
                colorStop.put("color", color2);
                colorObj.put(colorStop);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JSONObject params;
        JSONObject fullObject = new JSONObject();
        try {
            Map<String, Object> jsonParams = new LinkedHashMap<String, Object>();
            if (getMeasures().size() == 1) {


                jsonParams.put("seriesColors", new JSONArray(Arrays.asList(colorObj)));

            } else {
                JSONArray seriesColors = getSeriesColors();
                jsonParams.put("seriesColors", seriesColors);
                jsonParams.put("legend", getLegend());
            }
            JSONObject seriesDefaults = new JSONObject();
            seriesDefaults.put("renderer", "$.jqplot.GradientBarRenderer");
            JSONObject rendererOptions = new JSONObject();
            rendererOptions.put("fillToZero", "true");
            rendererOptions.put("barDirection", "'horizontal'");
            rendererOptions.put("varyBarColor", "true");
            rendererOptions.put("shadowDepth", 1);
            rendererOptions.put("barMargin", 3);
            rendererOptions.put("barPadding", 0);
            seriesDefaults.put("rendererOptions", rendererOptions);
            jsonParams.put("seriesDefaults", seriesDefaults);
            JSONObject grid = getGrid();
            jsonParams.put("grid", grid);
            jsonParams.put("axes", getAxes());
            params = new JSONObject(jsonParams);
            fullObject.put("jqplotOptions", params);
            JSONObject drillthroughOptions = new JSONObject();
            drillthroughOptions.put("embedded", htmlReportMetadata.isEmbedded());
            fullObject.put("drillthrough", drillthroughOptions);

            if ("auto".equals(getLabelPosition())) {
                JSONObject labels = new JSONObject();
                labels.put("location", "'e'");
                labels.put("show", "true");
                labels.put("edgetolerance", -15);
                labels.put("escapeHTML", false);
                labels.put("formatString", "'%g'");
                seriesDefaults.put("pointLabels", labels);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return fullObject;
    }

    @Override
    public JSONObject getAxes() throws JSONException {
        JSONObject axes = new JSONObject();
        JSONObject xAxis = getMeasureAxis(getMeasures().get(0));
        axisConfigure(xAxis, getxAxisMinimum(), isxAxisMinimumDefined(), getxAxisMaximum(), isxAxisMaximumDefined());
        axes.put("xaxis", xAxis);
        xAxis.put("renderer", "$.jqplot.MeasureAxisRenderer");
        JSONObject yAxis = getGroupingAxis(getYaxis());
        axes.put("yaxis", yAxis);
        return axes;
    }

    public void renderConfig(ApplicationSkin applicationSkin) {
        if (getMeasures().size() == 1 && "Primary".equals(getColorScheme()) && applicationSkin.isCustomChartColorEnabled()) {
            setChartColor(applicationSkin.getCustomChartColor());
            setUseChartColor(true);
            setGradientColor(applicationSkin.getCustomChartColor());
        } else if (getMeasures().size() == 1 && "Secondary".equals(getColorScheme()) && applicationSkin.isSecondaryColorEnabled()) {
            setChartColor(applicationSkin.getSecondaryColor());
            setUseChartColor(true);
            setGradientColor(applicationSkin.getSecondaryColor());
        } else if (getMeasures().size() > 1 && "Primary".equals(getColorScheme()) && applicationSkin.getMultiColors() != null && applicationSkin.getMultiColors().size() > 0 &&
                applicationSkin.getMultiColors().get(0).isColor1StartEnabled()) {
            setMultiColors(applicationSkin.getMultiColors());
        } else if (getMeasures().size() > 1 && "Secondary".equals(getColorScheme()) && applicationSkin.getSecondaryMultiColors() != null && applicationSkin.getSecondaryMultiColors().size() > 0 &&
                applicationSkin.getSecondaryMultiColors().get(0).isColor1StartEnabled()) {
            setMultiColors(applicationSkin.getSecondaryMultiColors());
        }
    }

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentDefinitions) throws JSONException {
        JSONObject areaChart = super.toJSON(htmlReportMetadata, parentDefinitions);
        areaChart.put("type", "bar");
        areaChart.put("key", getUrlKey());
        areaChart.put("url", "/app/columnChart");
        areaChart.put("parameters", getJsonObject(htmlReportMetadata));
        areaChart.put("styles", htmlReportMetadata.createStyleProperties());
        return areaChart;
    }

    @Override
    protected List<MultiColor> configuredMultiColors() {
        return multiColors;
    }
}