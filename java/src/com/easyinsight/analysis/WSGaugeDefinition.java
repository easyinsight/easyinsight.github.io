package com.easyinsight.analysis;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 31, 2009
 * Time: 10:06:29 AM
 */
public class WSGaugeDefinition extends WSAnalysisDefinition {

    private AnalysisItem measure;
    private AnalysisItem benchmarkMeasure;
    private int gaugeType;
    private long gaugeDefinitionID;
    private double maxValue;
    private double alertPoint1;
    private double alertPoint2;
    private int color1 = 16711680;
    private int color2 = 16776960;
    private int color3 = 47889;

    public int getColor1() {
        return color1;
    }

    public void setColor1(int color1) {
        this.color1 = color1;
    }

    public int getColor2() {
        return color2;
    }

    public void setColor2(int color2) {
        this.color2 = color2;
    }

    public int getColor3() {
        return color3;
    }

    public void setColor3(int color3) {
        this.color3 = color3;
    }

    public AnalysisItem getBenchmarkMeasure() {
        return benchmarkMeasure;
    }

    public void setBenchmarkMeasure(AnalysisItem benchmarkMeasure) {
        this.benchmarkMeasure = benchmarkMeasure;
    }

    public double getAlertPoint1() {
        return alertPoint1;
    }

    public void setAlertPoint1(double alertPoint1) {
        this.alertPoint1 = alertPoint1;
    }

    public double getAlertPoint2() {
        return alertPoint2;
    }

    public void setAlertPoint2(double alertPoint2) {
        this.alertPoint2 = alertPoint2;
    }

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
        if (benchmarkMeasure != null) {
            columnList.add(benchmarkMeasure);
        }
        return columnList;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("measure", Arrays.asList(measure), structure);
        if (benchmarkMeasure != null) {
            addItems("benchmark", Arrays.asList(benchmarkMeasure), structure);
        }
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        measure = firstItem("measure", structure);
        benchmarkMeasure = firstItem("benchmark", structure);
    }

    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        alertPoint1 = findNumberProperty(properties, "alertPoint1", 0);
        alertPoint2 = findNumberProperty(properties, "alertPoint2", 0);
        color1 = (int) findNumberProperty(properties, "color1", 16711680);
        color2 = (int) findNumberProperty(properties, "color2", 16776960);
        color3 = (int) findNumberProperty(properties, "color3", 47889);
    }

    public static void main(String[] args) {
        /*
        public var color1:uint = 0xFF0000;
    public var color2:uint = 0xFFFF00;
    public var color3:uint = 0x00BB11;
         */
        System.out.println(Integer.parseInt("FF0000", 16));
        System.out.println(Integer.parseInt("FFFF00", 16));
        System.out.println(Integer.parseInt("00BB11", 16));
    }

    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("alertPoint1", alertPoint1));
        properties.add(new ReportNumericProperty("alertPoint2", alertPoint2));
        properties.add(new ReportNumericProperty("color1", color1));
        properties.add(new ReportNumericProperty("color2", color2));
        properties.add(new ReportNumericProperty("color3", color3));
        return properties;
    }

    /*public List<IntentionSuggestion> suggestIntentions(WSAnalysisDefinition report) {
        List<IntentionSuggestion> suggestions = new ArrayList<IntentionSuggestion>();
        WSGaugeDefinition gaugeReport = (WSGaugeDefinition) report;
        if (gaugeReport.alertPoint1 == 0) {
            suggestions.add(new IntentionSuggestion("Set Up Gauge Coloring",
                    "This action will set up colors for the different areas within your gauge.",
                    IntentionSuggestion.SCOPE_REPORT, IntentionSuggestion.CONFIGURE_GAUGE, IntentionSuggestion.OTHER));
        }
        return suggestions;
    }

    public List<Intention> createIntentions(List<AnalysisItem> fields, int type) throws SQLException {
        if (type == IntentionSuggestion.CONFIGURE_GAUGE) {
            ReportPropertiesIntention reportPropertiesIntention = new ReportPropertiesIntention();
            reportPropertiesIntention.setSummaryRow(true);
            return Arrays.asList((Intention) reportPropertiesIntention);
        } else {
            throw new RuntimeException("Unrecognized intention type");
        }
    }*/

    @Override
    public String rootHTML() {
        return "<canvas class=\"gauge\" width=\"200\" height=\"200\" id=\"gauge"+getUrlKey()+"\"></canvas>";
    }

    @Override
    public String toHTML(String targetDiv) {
        StringBuilder sb = new StringBuilder();
        String gaugePropertiesString;
        try {
            JSONArray bands = new JSONArray();
            JSONObject band1 = new JSONObject();
            band1.put("color", "'"+String.format("#%06X", (0xFFFFFF & color1))+"'");
            band1.put("start", 0);
            band1.put("end", alertPoint1);
            bands.put(band1);
            JSONObject band2 = new JSONObject();
            band2.put("color", "'" + String.format("#%06X", (0xFFFFFF & color2)) + "'");
            band2.put("start", alertPoint1);
            band2.put("end", alertPoint2);
            bands.put(band2);
            JSONObject band3 = new JSONObject();
            band3.put("color", "'" + String.format("#%06X", (0xFFFFFF & color3)) + "'");
            band3.put("start", alertPoint2);
            band3.put("end", getMaxValue());
            bands.put(band3);
            gaugePropertiesString = bands.toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        gaugePropertiesString = gaugePropertiesString.replaceAll("\"", "");
        sb.append("var aGauge = new AquaGauge('gauge"+getUrlKey()+"');\n");
        sb.append("aGauge.props.minValue = 0;\n");
        sb.append("aGauge.props.dialSubTitle = '';\n");
        sb.append("aGauge.props.dialTitle = '';\n");
        sb.append("aGauge.props.maxValue = 1000;\n");
        sb.append("aGauge.props.rangeSegments = ").append(gaugePropertiesString).append(";\n");
        String timezoneOffset = "&timezoneOffset='+new Date().getTimezoneOffset()+'";
        String xyz = "$.getJSON('/app/gauge?reportID="+getAnalysisID()+timezoneOffset+"&'+ strParams, function(data) {\n" +
                "aGauge.refresh(data['value']);\n"+
                "afterRefresh();\n"+
                "});\n";
        sb.append(xyz);
        System.out.println(sb.toString());
        return sb.toString();
    }

    @Override
    public List<String> cssIncludes() {
        return new ArrayList<String>();
    }

    @Override
    public List<String> javaScriptIncludes() {
        return Arrays.asList("/js/AquaGauge.js", "/js/helper.js");
    }
}
