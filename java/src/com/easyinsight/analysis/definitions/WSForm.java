package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;

import java.util.*;

/**
 * User: jamesboe
 * Date: Oct 28, 2010
 * Time: 10:38:11 PM
 */
public class WSForm extends WSAnalysisDefinition {

    private List<AnalysisItem> columns;
    private long formID;
    private String direction;
    private String labelFont;
    private int labelFontSize;
    private int columnCount;

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getLabelFont() {
        return labelFont;
    }

    public void setLabelFont(String labelFont) {
        this.labelFont = labelFont;
    }

    public int getLabelFontSize() {
        return labelFontSize;
    }

    public void setLabelFontSize(int labelFontSize) {
        this.labelFontSize = labelFontSize;
    }

    public List<AnalysisItem> getColumns() {
        return columns;
    }

    public void setColumns(List<AnalysisItem> columns) {
        this.columns = columns;
    }

    public long getFormID() {
        return formID;
    }

    public void setFormID(long formID) {
        this.formID = formID;
    }

    @Override
    public String getDataFeedType() {
        return "Form";
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        for (AnalysisItem item : columns) {
            columnList.add(item);
        }
        columnList.addAll(getLimitFields());
        return columnList;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        Collections.sort(getColumns(), new Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                return new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition());
            }
        });
        addItems("", getColumns(), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        setColumns(items("", structure));
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        direction = findStringProperty(properties, "direction", "Left");
        labelFont = findStringProperty(properties, "labelFont", "Tahoma");
        labelFontSize = (int) findNumberProperty(properties, "labelFontSize", 12);
        columnCount = (int) findNumberProperty(properties, "columnCount", 1);
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportStringProperty("direction", direction));
        properties.add(new ReportStringProperty("labelFont", labelFont));
        properties.add(new ReportNumericProperty("labelFontSize", labelFontSize));
        properties.add(new ReportNumericProperty("columnCount", columnCount));
        return properties;
    }
}
