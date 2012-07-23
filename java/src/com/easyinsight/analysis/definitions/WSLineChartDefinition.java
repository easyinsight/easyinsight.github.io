package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
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

    public boolean isAutoScaled() {
        return autoScaled;
    }

    public void setAutoScaled(boolean autoScaled) {
        this.autoScaled = autoScaled;
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

    public void tweakReport(DataSet dataSet) {
        if (autoScale && getXaxis().hasType(AnalysisItemTypes.DATE_DIMENSION)) {
            int daysDuration = 0;
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
            if (daysDuration == 0) {
                long startTime = 0;
                long endTime = 0;
                for (IRow row : dataSet.getRows()) {
                    Value value = row.getValue(xAxis.createAggregateKey());
                    if (value.type() == Value.DATE) {
                        DateValue dateValue = (DateValue ) value;
                        if (dateValue.getDate() != null) {
                            long time = dateValue.getDate().getTime();
                            if (startTime == 0 || time < startTime) {
                                startTime = time;
                            }
                            if (endTime == 0 || time > endTime) {
                                endTime = time;
                            }
                        }
                    }
                }
                daysDuration = (int)((endTime - startTime) / (1000 * 60 * 60 * 24));
            }
            if (daysDuration > (365 * 6)) {
                if (xAxis.getDateLevel() != AnalysisDateDimension.YEAR_LEVEL) {
                    autoScaled = true;
                    xAxis.setDateLevel(AnalysisDateDimension.YEAR_LEVEL);
                }
            } else if (daysDuration > (365 * 2)) {
                if (xAxis.getDateLevel() != AnalysisDateDimension.MONTH_LEVEL) {
                    autoScaled = true;
                    xAxis.setDateLevel(AnalysisDateDimension.MONTH_LEVEL);
                }
            } else if (daysDuration >= (90)) {
                if (xAxis.getDateLevel() != AnalysisDateDimension.WEEK_LEVEL) {
                    autoScaled = true;
                    xAxis.setDateLevel(AnalysisDateDimension.WEEK_LEVEL);
                }
            } else if (daysDuration > 6) {
                if (xAxis.getDateLevel() != AnalysisDateDimension.DAY_LEVEL) {
                    autoScaled = true;
                    xAxis.setDateLevel(AnalysisDateDimension.DAY_LEVEL);
                }
            }
            if (xAxis.hasType(AnalysisItemTypes.STEP)) {
                AnalysisStep analysisStep = (AnalysisStep) xAxis;
                analysisStep.getStartDate().setDateLevel(analysisStep.getDateLevel());
                analysisStep.getEndDate().setDateLevel(analysisStep.getDateLevel());
            }
        }
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        strokeWeight = (int) findNumberProperty(properties, "strokeWeight", 2);
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("strokeWeight", strokeWeight));
        return properties;
    }

    @Override
    public List<String> javaScriptIncludes() {
        List<String> includes = super.javaScriptIncludes();
        includes.add("/js/plugins/jqplot.dateAxisRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasTextRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasAxisTickRenderer.min.js");
        includes.add("/js/visualizations/chart.js");
        includes.add("/js/visualizations/util.js");
        return includes;
    }

    @Override
    public String toHTML(String targetDiv) {

        JSONObject params;
        try {
            Map<String, Object> jsonParams = new LinkedHashMap<String, Object>();

            JSONObject grid = new JSONObject();
            grid.put("background", "'#FFFFFF'");
            jsonParams.put("grid", grid);
            JSONObject axes = new JSONObject();
            JSONObject xAxis = new JSONObject();
            xAxis.put("renderer", "$.jqplot.DateAxisRenderer");

            JSONObject xAxisTicketOptions = new JSONObject();
            AnalysisDateDimension date = (AnalysisDateDimension) this.getXaxis();
            if (date.getDateLevel() == AnalysisDateDimension.DAY_LEVEL) {
                xAxisTicketOptions.put("formatString", "'%b %#d'");
            } else if (date.getDateLevel() == AnalysisDateDimension.MONTH_LEVEL) {
                xAxisTicketOptions.put("formatString", "'%b'");
            } else if (date.getDateLevel() == AnalysisDateDimension.YEAR_LEVEL) {
                xAxisTicketOptions.put("formatString", "'%b'");
            } else {
                xAxisTicketOptions.put("formatString", "'%b %#d'");
            }

            xAxis.put("tickOptions", xAxisTicketOptions);
            axes.put("xaxis", xAxis);
            jsonParams.put("axes", axes);
            JSONObject highlighter = new JSONObject();
            highlighter.put("show", true);
            highlighter.put("sizeAdjust", 7.5);
            jsonParams.put("highlighter", highlighter);
            JSONObject cursor = new JSONObject();
            cursor.put("show", false);
            jsonParams.put("cursor", cursor);
            JSONArray seriesColors = new JSONArray(Arrays.asList("'#a6bc59'", "'#597197'", "'#d6ab2a'", "'#d86068'", "'#5d9942'",
                    "'#7a4c6c'", "'#F0B400'", "'#1E6C0B'", "'#00488C'", "'#332600'", "'#D84000'"));
            jsonParams.put("seriesColors", seriesColors);
            params = new JSONObject(jsonParams);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String argh = params.toString();
        argh = argh.replaceAll("\"", "");
        String timezoneOffset = "&timezoneOffset='+new Date().getTimezoneOffset()+'";

        argh = "$.getJSON('/app/twoAxisChart?reportID="+getAnalysisID()+timezoneOffset+"&'+ strParams, Chart.getCallback('" + targetDiv + "', " + argh + ", true))";
        return argh;
    }
}
