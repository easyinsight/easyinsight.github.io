package com.easyinsight.analysis;

import com.easyinsight.preferences.ApplicationSkin;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * User: James Boe
 * Date: Jan 10, 2008
 * Time: 7:33:07 PM
 */
public class WSCrosstabDefinition extends WSAnalysisDefinition {

    private List<AnalysisItem> columns;
    private List<AnalysisItem> rows;
    private List<AnalysisItem> measures;
    private long crosstabDefinitionID;
    private int headerBackgroundColor = 0x333333;
    private int headerTextColor = 0xFFFFFF;
    private int summaryBackgroundColor = 0x555555;
    private int summaryTextColor = 0xFFFFFF;
    private String align;
    private boolean excludeZero;

    public boolean isExcludeZero() {
        return excludeZero;
    }

    public void setExcludeZero(boolean excludeZero) {
        this.excludeZero = excludeZero;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public int getHeaderBackgroundColor() {
        return headerBackgroundColor;
    }

    public void setHeaderBackgroundColor(int headerBackgroundColor) {
        this.headerBackgroundColor = headerBackgroundColor;
    }

    public int getHeaderTextColor() {
        return headerTextColor;
    }

    public void setHeaderTextColor(int headerTextColor) {
        this.headerTextColor = headerTextColor;
    }

    public int getSummaryBackgroundColor() {
        return summaryBackgroundColor;
    }

    public void setSummaryBackgroundColor(int summaryBackgroundColor) {
        this.summaryBackgroundColor = summaryBackgroundColor;
    }

    public int getSummaryTextColor() {
        return summaryTextColor;
    }

    public void setSummaryTextColor(int summaryTextColor) {
        this.summaryTextColor = summaryTextColor;
    }

    public long getCrosstabDefinitionID() {
        return crosstabDefinitionID;
    }

    public void setCrosstabDefinitionID(long crosstabDefinitionID) {
        this.crosstabDefinitionID = crosstabDefinitionID;
    }

    public List<AnalysisItem> getColumns() {
        return columns;
    }

    public void setColumns(List<AnalysisItem> columns) {
        this.columns = columns;
    }

    public List<AnalysisItem> getRows() {
        return rows;
    }

    public void setRows(List<AnalysisItem> rows) {
        this.rows = rows;
    }

    public List<AnalysisItem> getMeasures() {
        return measures;
    }

    public void setMeasures(List<AnalysisItem> measures) {
        this.measures = measures;
    }

    public String getDataFeedType() {
        return "Crosstab";
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> AnalysisItems = new HashSet<AnalysisItem>();
        if (columns != null) {
            AnalysisItems.addAll(columns);
        }
        if (rows != null) {
            AnalysisItems.addAll(rows);
        }
        if (measures != null) {
            AnalysisItems.addAll(measures);
        }
        return AnalysisItems;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("column", getColumns(), structure);
        addItems("row", getRows(), structure);
        addItems("measure", getMeasures(), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        setColumns(items("column", structure));
        setRows(items("row", structure));
        setMeasures(items("measure", structure));
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        headerTextColor = (int) findNumberProperty(properties, "headerTextColor", 0xFFFFFF);
        excludeZero = findBooleanProperty(properties, "excludeZero", false);
        summaryTextColor = (int) findNumberProperty(properties, "summaryTextColor", 0xFFFFFF);
        headerBackgroundColor = (int) findNumberProperty(properties, "headerBackgroundColor", 0x333333);
        summaryBackgroundColor = (int) findNumberProperty(properties, "summaryBackgroundColor", 0x555555);
        align = findStringProperty(properties, "align", "left");
    }

    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("headerTextColor", headerTextColor));
        properties.add(new ReportBooleanProperty("excludeZero", excludeZero));
        properties.add(new ReportNumericProperty("headerBackgroundColor", headerBackgroundColor));
        properties.add(new ReportNumericProperty("summaryTextColor", summaryTextColor));
        properties.add(new ReportNumericProperty("summaryBackgroundColor", summaryBackgroundColor));
        properties.add(new ReportStringProperty("align", align));
        return properties;
    }

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentDefinitions) throws JSONException {
        JSONObject list = super.toJSON(htmlReportMetadata, parentDefinitions);
        list.put("type", "crosstab");
        list.put("key", getUrlKey());
        list.put("url", "/app/htmlExport");
        return list;
    }

    public void renderConfig(ApplicationSkin applicationSkin) {
        if ("Primary".equals(getColorScheme())) {
            if (applicationSkin.isCrosstabHeaderBackgroundColorEnabled()) {
                setHeaderBackgroundColor(applicationSkin.getHeaderBarBackgroundColor());
            }
            if (applicationSkin.isCrosstabHeaderTextColorEnabled()) {
                setHeaderTextColor(applicationSkin.getCrosstabHeaderTextColor());
            }
            if (applicationSkin.isSummaryBackgroundColorEnabled()) {
                setSummaryBackgroundColor(applicationSkin.getSummaryBackgroundColor());
            }
            if (applicationSkin.isSummaryTextColorEnabled()) {
                setSummaryTextColor(applicationSkin.getSummaryTextColor());
            }
        }
    }
}
