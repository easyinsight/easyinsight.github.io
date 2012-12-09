package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 20, 2009
 * Time: 7:23:14 PM
 */
public class WSColumnChartDefinition extends WSXAxisDefinition {

    private int chartColor;
    private int gradientColor;
    private boolean useChartColor;
    private String columnSort;
    private String axisType = "Linear";
    private String labelPosition = "none";
    private int labelFontSize;
    private String labelFontWeight;

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

    public int getChartType() {
        return ChartDefinitionState.COLUMN_2D;
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
        gradientColor = (int) findNumberProperty(properties, "gradientColor", 0);
        useChartColor = findBooleanProperty(properties, "useChartColor", false);
        columnSort = findStringProperty(properties, "columnSort", "Unsorted");
        axisType = findStringProperty(properties, "axisType", "Linear");
        labelPosition = findStringProperty(properties, "labelPosition", "none");
        labelFontWeight = findStringProperty(properties, "labelFontWeight", "none");
        labelFontSize = (int) findNumberProperty(properties, "labelFontSize", 12);
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("chartColor", chartColor));
        properties.add(new ReportNumericProperty("gradientColor", gradientColor));
        properties.add(new ReportBooleanProperty("useChartColor", useChartColor));
        properties.add(new ReportStringProperty("columnSort", columnSort));
        properties.add(new ReportStringProperty("labelPosition", labelPosition));
        properties.add(new ReportNumericProperty("labelFontSize", labelFontSize));
        properties.add(new ReportStringProperty("labelFontWeight", labelFontWeight));
        return properties;
    }

    @Override
    public List<String> javaScriptIncludes() {
        List<String> includes = super.javaScriptIncludes();
        includes.add("/js/plugins/jqplot.gradientBarRenderer.js");
        includes.add("/js/plugins/jqplot.categoryAxisRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasTextRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasAxisLabelRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasAxisTickRenderer.min.js");
        includes.add("/js/visualizations/chart.js");
        includes.add("/js/visualizations/util.js");
        return includes;
    }

    @Override
    public String rootHTML() {
        return "<div id=\"chartpseudotooltip\"></div>";
    }

    @Override
    public String toHTML(String targetDiv, HTMLReportMetadata htmlReportMetadata) {
        String color;
        String color2;
        if (useChartColor) {
            color = String.format("'#%06X'", (0xFFFFFF & chartColor));
            color2 = String.format("'#%06X'", (0xFFFFFF & gradientColor));
        } else {
            color = "'#FF0000'";
            color2 = "'#990000'";
        }

        JSONObject params;
        JSONObject fullObject = new JSONObject();
        try {
            Map<String, Object> jsonParams = new LinkedHashMap<String, Object>();
            if (getMeasures().size() == 1) {
                JSONArray colorObj = new JSONArray();

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

//                colorObj.put("first", "'" + color + "'");
//                colorObj.put("second", "'" + color2 + "'");
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
            rendererOptions.put("varyBarColor", "true");
            rendererOptions.put("barMargin", (45 / 2));
            seriesDefaults.put("rendererOptions", rendererOptions);
            jsonParams.put("seriesDefaults", seriesDefaults);
            JSONObject grid = getGrid();
            jsonParams.put("grid", grid);
            JSONObject axes = new JSONObject();
            JSONObject xAxis = getGroupingAxis(getXaxis());
            axes.put("xaxis", xAxis);
            axes.put("yaxis", getMeasureAxis(getMeasures().get(0)));
            jsonParams.put("axes", axes);
            params = new JSONObject(jsonParams);
            fullObject.put("jqplotOptions", params);
            JSONObject drillthroughOptions = new JSONObject();
            drillthroughOptions.put("embedded", htmlReportMetadata.isEmbedded());
            fullObject.put("drillthrough", drillthroughOptions);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        String argh = fullObject.toString();
        argh = argh.replaceAll("\"", "");
        String timezoneOffset = "&timezoneOffset='+new Date().getTimezoneOffset()+'";
        /*AnalysisItem xAxis = getXaxis();
        DrillThrough drillThrough = null;
        if (xAxis.getLinks() != null) {
            for (Link link : xAxis.getLinks()) {
                if (link instanceof DrillThrough && link.isDefaultLink()) {
                    drillThrough = (DrillThrough) link;
                }
            }
        }

        String drillString = "";
        if (drillThrough != null) {
            StringBuilder paramBuilder = new StringBuilder();
            paramBuilder.append("reportID=").append(getUrlKey()).append("&drillthroughID=").append(drillThrough.getLinkID()).append("&").append("sourceField=").append(xAxis.getAnalysisItemID());
            drillString = paramBuilder.toString();
        }*/
        String styleProps = htmlReportMetadata.createStyleProperties();

        String xyz = "$.getJSON('/app/columnChart?reportID="+getUrlKey()+timezoneOffset+"&'+ strParams, Chart.getColumnChartCallback('"+ targetDiv + "', " + argh + ","+styleProps+"))";
        return xyz;
    }
}
