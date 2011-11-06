package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;

import java.util.*;

/**
 * User: jamesboe
 * Date: Sep 27, 2010
 * Time: 4:51:51 PM
 */
public class WSVerticalListDefinition extends WSAnalysisDefinition {

    private List<AnalysisItem> measures;
    private AnalysisItem column;

    private long verticalListID;

    private int headerWidth;
    private int columnWidth;

    private String patternName;

    public String getPatternName() {
        return patternName;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    public int getHeaderWidth() {
        return headerWidth;
    }

    public void setHeaderWidth(int headerWidth) {
        this.headerWidth = headerWidth;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public long getVerticalListID() {
        return verticalListID;
    }

    public void setVerticalListID(long verticalListID) {
        this.verticalListID = verticalListID;
    }

    @Override
    public String getDataFeedType() {
        return AnalysisTypes.VERTICAL_LIST;
    }

    @Override
    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> items = new HashSet<AnalysisItem>();
        if (measures != null) {
            items.addAll(measures);
        }
        if (column != null) {
            items.add(column);
        }
        return items;
    }

    @Override
    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("measures", measures, structure);
        if (column != null) {
            addItems("grouping", Arrays.asList(column), structure);
        }
    }

    @Override
    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        measures = items("measures", structure);
        List<AnalysisItem> columns = items("grouping", structure);
        if (columns.size() > 0) {
            column = columns.get(0);
        }
    }

    public List<AnalysisItem> getMeasures() {
        return measures;
    }

    public void setMeasures(List<AnalysisItem> measures) {
        this.measures = measures;
    }

    public AnalysisItem getColumn() {
        return column;
    }

    public void setColumn(AnalysisItem column) {
        this.column = column;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        headerWidth = (int) findNumberProperty(properties, "headerWidth", 140);
        columnWidth = (int) findNumberProperty(properties, "columnWidth", 73);
        patternName = findStringProperty(properties, "patternName", "");
    }

    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("headerWidth", headerWidth));
        properties.add(new ReportNumericProperty("columnWidth", columnWidth));
        properties.add(new ReportStringProperty("patternName", patternName));
        return properties;
    }
}
