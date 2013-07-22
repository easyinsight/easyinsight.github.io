package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.pipeline.GoalComponent;
import com.easyinsight.pipeline.IComponent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 20, 2009
 * Time: 7:23:14 PM
 */
public class WSStackedColumnChartDefinition extends WSXAxisDefinition {

    private int chartColor;
    private boolean useChartColor;
    private String columnSort;
    private AnalysisItem stackItem;
    private String labelPosition = "none";
    private int labelFontSize;
    private String labelFontWeight;
    private int labelInsideFontColor;
    private boolean useInsideLabelFontColor;
    private List<MultiColor> multiColors = new ArrayList<MultiColor>();
    private String stackSort;

    @Override
    public List<IComponent> createComponents() {
        List<IComponent> components = super.createComponents();
        if (getXaxis() != null && getXaxis().getReportFieldExtension() != null && getXaxis().getReportFieldExtension() instanceof ChartReportFieldExtension) {
            ChartReportFieldExtension cfre = (ChartReportFieldExtension) getXaxis().getReportFieldExtension();
            if (cfre.getGoal() != null) {
                components.add(new GoalComponent(cfre.getGoal()));
            }
        }
        return components;
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
        return ChartDefinitionState.COLUMN_2D_STACKED;
    }

    public int getChartFamily() {
        return ChartDefinitionState.COLUMN_FAMILY;
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

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        chartColor = (int) findNumberProperty(properties, "chartColor", 0);
        useChartColor = findBooleanProperty(properties, "useChartColor", false);
        columnSort = findStringProperty(properties, "columnSort", "Unsorted");
        labelFontWeight = findStringProperty(properties, "labelFontWeight", "none");
        labelFontSize = (int) findNumberProperty(properties, "labelFontSize", 12);
        labelPosition = findStringProperty(properties, "labelPosition", "none");
        labelInsideFontColor = (int) findNumberProperty(properties, "labelInsideFontColor", 0);
        useInsideLabelFontColor = findBooleanProperty(properties, "useInsideLabelFontColor", false);
        multiColors = multiColorProperty(properties, "multiColors");
        stackSort = findStringProperty(properties, "stackSort", "Unsorted");
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("chartColor", chartColor));
        properties.add(new ReportBooleanProperty("useChartColor", useChartColor));
        properties.add(new ReportStringProperty("columnSort", columnSort));
        properties.add(new ReportStringProperty("stackSort", stackSort));
        properties.add(new ReportStringProperty("labelPosition", labelPosition));
        properties.add(new ReportNumericProperty("labelFontSize", labelFontSize));
        properties.add(new ReportStringProperty("labelFontWeight", labelFontWeight));
        properties.add(new ReportBooleanProperty("useInsideLabelFontColor", useInsideLabelFontColor));
        properties.add(new ReportNumericProperty("labelInsideFontColor", labelInsideFontColor));
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
    public List<String> javaScriptIncludes() {
        List<String> includes = super.javaScriptIncludes();

        includes.add("/js/plugins/jqplot.categoryAxisRenderer.js");
        includes.add("/js/plugins/jqplot.canvasAxisTickRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasAxisLabelRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasTextRenderer.min.js");
        includes.add("/js/visualizations/chart.js");
        includes.add("/js/visualizations/util.js");
//        includes.add("/js/plugins/jqplot.gradientBarRenderer.js");
        return includes;
    }

    @Override
    protected JSONObject getLegend() throws JSONException {
        JSONObject o = super.getLegend();
        o.put("renderer", "$.jqplot.GradientTableLegendRenderer");
        return o;
    }

    @Override
    public String toHTML(String targetDiv, HTMLReportMetadata htmlReportMetadata) {

        JSONObject fullObject = getJsonObject(htmlReportMetadata);
        String argh = fullObject.toString();
        argh = argh.replaceAll("\"", "");
        String timezoneOffset = "&timezoneOffset='+new Date().getTimezoneOffset()+'";
        String customHeight = htmlReportMetadata.createStyleProperties().toString();
        String xyz = "$.getJSON('/app/stackedChart?reportID=" + getUrlKey() + timezoneOffset + "&'+ strParams, Chart.getStackedColumnChart('" + targetDiv + "', " + argh + "," + customHeight + "))";
        /*return "$.getJSON('/app/stackedChart?reportID="+getUrlKey()+timezoneOffset+"&'+ strParams, function(data) {\n" +
                "                var s1 = data[\"values\"];\n" +
                "                var plot1 = $.jqplot('"+targetDiv+"', s1, " + argh + ");afterRefresh();\n})";*/
        return xyz;
    }

    private JSONObject getJsonObject(HTMLReportMetadata htmlReportMetadata) {
        JSONObject params;
        JSONObject fullObject = new JSONObject();
        try {
            Map<String, Object> jsonParams = new LinkedHashMap<String, Object>();
            jsonParams.put("legend", getLegend());
            jsonParams.put("stackSeries", "true");
            JSONObject seriesDefaults = new JSONObject();
            seriesDefaults.put("renderer", "$.jqplot.GradientBarRenderer");
            JSONObject rendererOptions = new JSONObject();
            rendererOptions.put("barDirection", "'vertical'");
            rendererOptions.put("varyBarColor", "true");
            rendererOptions.put("barMargin", 45);
            rendererOptions.put("highlightMouseOver", "true");
            seriesDefaults.put("rendererOptions", rendererOptions);
            jsonParams.put("seriesDefaults", seriesDefaults);
            jsonParams.put("grid", getGrid());
            JSONObject axes = new JSONObject();
            axes.put("xaxis", getGroupingAxis(getXaxis()));
            axes.put("yaxis", getMeasureAxis(getMeasures().get(0)));
            jsonParams.put("axes", axes);
            JSONArray seriesColors = getSeriesColors();
            jsonParams.put("seriesColors", seriesColors);

            params = new JSONObject(jsonParams);
            fullObject.put("jqplotOptions", params);
            JSONObject drillthroughOptions = new JSONObject();
            drillthroughOptions.put("embedded", htmlReportMetadata.isEmbedded());
            fullObject.put("drillthrough", drillthroughOptions);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return fullObject;
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

    protected JSONArray transformColors(JSONArray colors) {
        JSONArray newColors = new JSONArray();
        try {

            for (int i = 0; i < colors.length(); i++) {
                JSONArray gradient = new JSONArray();
                String color2 = "'#FFFFFF'";
                Object color = colors.get(i);


                JSONObject colorStop = new JSONObject();
                colorStop.put("point", 0);
                colorStop.put("color", color);
                gradient.put(colorStop);

                colorStop = new JSONObject();
                colorStop.put("point", .15);
                colorStop.put("color", color2);
                gradient.put(colorStop);

                colorStop = new JSONObject();
                colorStop.put("point", .5);
                colorStop.put("color", color);
                gradient.put(colorStop);

                colorStop = new JSONObject();
                colorStop.put("point", .9);
                colorStop.put("color", color);
                gradient.put(colorStop);

                colorStop = new JSONObject();
                colorStop.put("point", 1);
                colorStop.put("color", color2);
                gradient.put(colorStop);


                newColors.put(gradient);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return newColors;
    }

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentDefinitions) throws JSONException {
        JSONObject pie = super.toJSON(htmlReportMetadata, parentDefinitions);
        pie.put("parameters", getJsonObject(htmlReportMetadata));
        pie.put("key", getUrlKey());
        pie.put("type", "stacked_column");
        pie.put("styles", htmlReportMetadata.createStyleProperties());
        pie.put("url", "/app/stackedChart");
        return pie;
    }
}
