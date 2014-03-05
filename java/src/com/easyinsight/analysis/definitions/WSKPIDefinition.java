package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;

import java.util.*;

/**
 * User: jamesboe
 * Date: 10/5/11
 * Time: 1:39 PM
 */
public abstract class WSKPIDefinition extends WSAnalysisDefinition {
    private List<AnalysisItem> measures;
    private String filterName;
    private int dayWindow;
    private List<AnalysisItem> groupings;
    private String nowDate;
    private String previousDate;

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public String getPreviousDate() {
        return previousDate;
    }

    public void setPreviousDate(String previousDate) {
        this.previousDate = previousDate;
    }

    public List<AnalysisItem> getGroupings() {
        return groupings;
    }

    public void setGroupings(List<AnalysisItem> groupings) {
        this.groupings = groupings;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public int getDayWindow() {
        return dayWindow;
    }

    public void setDayWindow(int dayWindow) {
        this.dayWindow = dayWindow;
    }

    public List<AnalysisItem> getMeasures() {
        return measures;
    }

    public void setMeasures(List<AnalysisItem> measures) {
        this.measures = measures;
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> items = new HashSet<AnalysisItem>();
        if (measures != null) {
            items.addAll(measures);
        }
        if (groupings != null) {
            items.addAll(groupings);
        }
        return items;
    }

    @Override
    public List<FilterDefinition> retrieveFilterDefinitions() {
        List<FilterDefinition> validFilters = new ArrayList<FilterDefinition>();
        for (FilterDefinition filter : getFilterDefinitions()) {
            if (filter.isTrendFilter()) {
                continue;
            }
            validFilters.add(filter);
        }
        return validFilters;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        nowDate = findStringProperty(properties, "nowDate", "");
        previousDate = findStringProperty(properties, "previousDate", "");
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportStringProperty("nowDate", nowDate));
        properties.add(new ReportStringProperty("previousDate", previousDate));
        return properties;
    }

    @Override
    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("measures", measures, structure);
        addItems("groupings", groupings, structure);
    }

    @Override
    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        measures = items("measures", structure);
        groupings = items("groupings", structure);
    }
}
