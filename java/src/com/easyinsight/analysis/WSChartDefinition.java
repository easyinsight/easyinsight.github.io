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

    private double rotationAngle;
    private double elevationAngle;

    private boolean showLegend;

    private String xAxisLabel;
    private String yAxisLabel;

    public abstract int getChartType();

    public abstract int getChartFamily();

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
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportBooleanProperty("showLegend", showLegend));
        properties.add(new ReportStringProperty("xAxisLabel", xAxisLabel));
        properties.add(new ReportStringProperty("yAxisLabel", yAxisLabel));
        return properties;
    }

    @Override
    public List<String> javaScriptIncludes() {
        List<String> includes = super.javaScriptIncludes();
        //includes.add("/js/jquery.jqplot.js");
        includes.add("/js/plugins/jqplot.pointLabels.min.js");
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
        return legendObj;
    }

    protected JSONObject getGrid() throws JSONException {
        JSONObject grid = new JSONObject();
        grid.put("background", "'#FFFFFF'");
        return grid;
    }

    protected JSONArray getSeriesColors() {
        return new JSONArray(Arrays.asList("'#a6bc59'", "'#597197'", "'#d6ab2a'", "'#d86068'", "'#5d9942'",
                "'#7a4c6c'", "'#F0B400'", "'#1E6C0B'", "'#00488C'", "'#332600'", "'#D84000'"));
    }

    protected JSONObject getMeasureAxis(AnalysisItem analysisItem) throws JSONException {
        JSONObject yAxis = new JSONObject();
        yAxis.put("pad", 1.05);
        yAxis.put("label", "'"+analysisItem.toDisplay()+"'");
        yAxis.put("labelRenderer", "$.jqplot.CanvasAxisLabelRenderer");
        yAxis.put("min", 0);
        JSONObject tickOptions = new JSONObject();
        if (analysisItem.getFormattingConfiguration().getFormattingType() == FormattingConfiguration.CURRENCY) {
            tickOptions.put("formatter", "$.jqplot.currencyTickNumberFormatter");
        } else {
            tickOptions.put("formatter", "$.jqplot.tickNumberFormatter");
        }
        yAxis.put("tickOptions", tickOptions);
        return yAxis;
    }

    protected JSONObject getGroupingAxis(AnalysisItem analysisItem) throws JSONException {
        JSONObject xAxis = new JSONObject();
        xAxis.put("renderer", "$.jqplot.CategoryAxisRenderer");

        xAxis.put("tickRenderer", "$.jqplot.CanvasAxisTickRenderer");
        xAxis.put("labelRenderer", "$.jqplot.CanvasAxisLabelRenderer");
        xAxis.put("label", "'"+analysisItem.toDisplay()+"'");
        JSONObject xAxisTicketOptions = new JSONObject();
        xAxisTicketOptions.put("angle", -15);
        xAxis.put("tickOptions", xAxisTicketOptions);
        return xAxis;
    }
}
