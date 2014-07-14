package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.preferences.ApplicationSkin;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:45:30 PM
 */
public class WSAreaChartDefinition extends WSTwoAxisDefinition {

    private String stackingType = "stacked";
    private List<MultiColor> multiColors = new ArrayList<MultiColor>();
    private int legendMaxWidth;
    private boolean autoScale = false;
    private boolean autoScaled = false;

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

    public String getStackingType() {
        return stackingType;
    }

    public void setStackingType(String stackingType) {
        this.stackingType = stackingType;
    }

    public List<MultiColor> getMultiColors() {
        return multiColors;
    }

    public void setMultiColors(List<MultiColor> multiColors) {
        this.multiColors = multiColors;
    }

    public int getLegendMaxWidth() {
        return legendMaxWidth;
    }

    public void setLegendMaxWidth(int legendMaxWidth) {
        this.legendMaxWidth = legendMaxWidth;
    }

    public int getChartType() {
        return ChartDefinitionState.AREA_2D;
    }

    public int getChartFamily() {
        return ChartDefinitionState.AREA_FAMILY;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        stackingType = findStringProperty(properties, "stackingType", "stacked");
        autoScale = findBooleanProperty(properties, "autoScale", false);
        multiColors = multiColorProperty(properties, "multiColors");
        legendMaxWidth = (int) findNumberProperty(properties, "legendMaxWidth", 200);

    }

    protected List<MultiColor> configuredMultiColors() {
        return multiColors;
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportStringProperty("stackingType", stackingType));
        properties.add(ReportMultiColorProperty.fromColors(multiColors, "multiColors"));
        properties.add(new ReportNumericProperty("legendMaxWidth", legendMaxWidth));
        properties.add(new ReportBooleanProperty("autoScale", autoScale));
        return properties;
    }

    public void tweakReport(Map<AnalysisItem, AnalysisItem> aliasMap) {
        if (autoScale && getXaxis() != null && getXaxis().hasType(AnalysisItemTypes.DATE_DIMENSION)) {
            int daysDuration = 0;
            /*AnalysisDateDimension start;
            try {
                start = (AnalysisDateDimension) getXaxis().clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }*/
            AnalysisDateDimension xAxis = (AnalysisDateDimension) this.getXaxis();
            if (getFilterDefinitions() != null) {
                for (FilterDefinition filterDefinition : getFilterDefinitions()) {
                    if (filterDefinition instanceof RollingFilterDefinition) {
                        RollingFilterDefinition rollingFilterDefinition = (RollingFilterDefinition) filterDefinition;
                        long now = System.currentTimeMillis();
                        daysDuration = (int) ((now - MaterializedRollingFilterDefinition.findStartDate(rollingFilterDefinition, new Date())) / (1000 * 60 * 60 * 24));
                    } else if (filterDefinition instanceof FilterDateRangeDefinition) {
                        FilterDateRangeDefinition filterDateRangeDefinition = (FilterDateRangeDefinition) filterDefinition;
                        daysDuration = (int) ((filterDateRangeDefinition.getEndDate().getTime() - filterDateRangeDefinition.getStartDate().getTime()) / (1000 * 60 * 60 * 24));
                    } else if (filterDefinition instanceof MultiFlatDateFilter) {
                        MultiFlatDateFilter multiFlatDateFilter = (MultiFlatDateFilter) filterDefinition;
                        if (multiFlatDateFilter.getLevel() == AnalysisDateDimension.QUARTER_OF_YEAR_LEVEL) {
                            List<DateLevelWrapper> levelWrappers = multiFlatDateFilter.getLevels();
                            int minConcat = Integer.MAX_VALUE;
                            int maxConcat = Integer.MIN_VALUE;
                            for (DateLevelWrapper wrapper : levelWrappers) {
                                // find start date, find end date
                                String shortDisplay = wrapper.getShortDisplay();
                                int quarterNumber = Integer.parseInt(String.valueOf(shortDisplay.charAt(1)));
                                int year = Integer.parseInt(shortDisplay.substring(3));
                                int concatValue = year * 10 + quarterNumber;
                                minConcat = Math.min(minConcat, concatValue);
                                maxConcat = Math.max(maxConcat, concatValue);
                            }
                            int minYear = (int) Math.floor(minConcat / 10);
                            int minQuarter = minConcat - (minYear * 10);
                            int maxYear = (int) Math.floor(maxConcat / 10);
                            int maxQuarter = maxConcat - (maxYear * 10);
                            LocalDate minDate = LocalDate.of(minYear, ((minQuarter - 1) * 3) + 1, 1);
                            LocalDate maxDate = LocalDate.of(maxYear, ((maxQuarter - 1) * 3) + 1, 1);
                            daysDuration = (int) ChronoUnit.DAYS.between(minDate, maxDate);
                        }
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
            //aliasMap.put(xAxis, start);
        }
    }

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentDefinitions) throws JSONException {
        JSONObject areaChart = super.toJSON(htmlReportMetadata, parentDefinitions);
        areaChart.put("type", "area");
        areaChart.put("key", getUrlKey());
        areaChart.put("url", "/app/lineChartD3");
        areaChart.put("styles", htmlReportMetadata.createStyleProperties());
        return areaChart;
    }

    protected boolean supportsMultiField() {
        return isMultiMeasure();
    }

    protected List<AnalysisItem> reportFieldsForMultiField() {
        return getMeasures();
    }

    protected void assignResults(List<AnalysisItem> fields) {
        setMeasures(fields);
    }

    public void renderConfig(ApplicationSkin applicationSkin) {
        if ("Primary".equals(getColorScheme()) && applicationSkin.getMultiColors() != null && applicationSkin.getMultiColors().size() > 0 &&
                applicationSkin.getMultiColors().get(0).isColor1StartEnabled()) {
            setMultiColors(applicationSkin.getMultiColors());
        }
    }
}
