package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: jamesboe
 * Date: 9/26/11
 * Time: 9:44 AM
 */
public class WSTrendGridDefinition extends WSKPIDefinition {

    private long trendReportID;
    private int sortIndex;
    private boolean sortAscending;
    private int rowColor1 = 0xF7F7F7;
    private int rowColor2 = 0xFFFFFF;
    private int headerColor1 = 0xFFFFFF;
    private int headerColor2 = 0xEFEFEF;
    private int textColor = 0x000000;
    private int headerTextColor = 0x000000;
    private int summaryRowTextColor = 0x000000;
    private int summaryRowBackgroundColor = 0x6699ff;

    public int getRowColor1() {
        return rowColor1;
    }

    public void setRowColor1(int rowColor1) {
        this.rowColor1 = rowColor1;
    }

    public int getRowColor2() {
        return rowColor2;
    }

    public void setRowColor2(int rowColor2) {
        this.rowColor2 = rowColor2;
    }

    public int getHeaderColor1() {
        return headerColor1;
    }

    public void setHeaderColor1(int headerColor1) {
        this.headerColor1 = headerColor1;
    }

    public int getHeaderColor2() {
        return headerColor2;
    }

    public void setHeaderColor2(int headerColor2) {
        this.headerColor2 = headerColor2;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getHeaderTextColor() {
        return headerTextColor;
    }

    public void setHeaderTextColor(int headerTextColor) {
        this.headerTextColor = headerTextColor;
    }

    public int getSummaryRowTextColor() {
        return summaryRowTextColor;
    }

    public void setSummaryRowTextColor(int summaryRowTextColor) {
        this.summaryRowTextColor = summaryRowTextColor;
    }

    public int getSummaryRowBackgroundColor() {
        return summaryRowBackgroundColor;
    }

    public void setSummaryRowBackgroundColor(int summaryRowBackgroundColor) {
        this.summaryRowBackgroundColor = summaryRowBackgroundColor;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public boolean isSortAscending() {
        return sortAscending;
    }

    public void setSortAscending(boolean sortAscending) {
        this.sortAscending = sortAscending;
    }

    public long getTrendReportID() {
        return trendReportID;
    }

    public void setTrendReportID(long trendReportID) {
        this.trendReportID = trendReportID;
    }

    @Override
    public String getDataFeedType() {
        return AnalysisTypes.TREND_GRID;
    }

    @Override
    public List<String> javaScriptIncludes() {
        List<String> list = new ArrayList<String>();
        list.add("/js/visualizations/util.js");
        list.add("/js/visualizations/list.js");
        list.add("/js/jquery.dataTables.js");
        list.add("/js/color.js");
        return list;
    }

    @Override
    public List<String> cssIncludes() {
        return Arrays.asList("/css/jquery.dataTables.css");
    }

    public JSONObject jsonProperties() {

        JSONObject p = new JSONObject();
        try {
            List<ReportProperty> properties = createProperties();
            populateProperties(properties);
            for (ReportProperty property : properties) {
                if (property instanceof ReportNumericProperty)
                    p.put(property.getPropertyName(), ((ReportNumericProperty) property).getValue());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return p;
    }

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentDefinitions) throws JSONException {
        JSONObject list = super.toJSON(htmlReportMetadata, parentDefinitions);
        list.put("type", "trend_grid");
        list.put("key", getUrlKey());
        list.put("url", "/app/htmlExport");
        list.put("properties", jsonProperties());
        list.put("sorting", getAnalysisItemMap());
        int count = 4;
        if (getGroupings() != null) {
            count += getGroupings().size();
        }
        list.put("columns", count);
        return list;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        rowColor1 = (int) findNumberProperty(properties, "rowColor1", 0xF7F7F7);
        rowColor2 = (int) findNumberProperty(properties, "rowColor2", 0xFFFFFF);
        headerColor1 = (int) findNumberProperty(properties, "headerColor1", 0xffffff);
        headerColor2 = (int) findNumberProperty(properties, "headerColor2", 0xEFEFEF);
        textColor = (int) findNumberProperty(properties, "textColor", 0x000000);
        headerTextColor = (int) findNumberProperty(properties, "headerTextColor", 0x000000);
        summaryRowTextColor = (int) findNumberProperty(properties, "summaryRowTextColor", 0x000000);
        summaryRowBackgroundColor = (int) findNumberProperty(properties, "summaryRowBackgroundColor", 0x6699ff);
    }

    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("rowColor1", rowColor1));
        properties.add(new ReportNumericProperty("rowColor2", rowColor2));
        properties.add(new ReportNumericProperty("headerColor1", headerColor1));
        properties.add(new ReportNumericProperty("headerColor2", headerColor2));
        properties.add(new ReportNumericProperty("textColor", textColor));
        properties.add(new ReportNumericProperty("headerTextColor", headerTextColor));
        properties.add(new ReportNumericProperty("summaryRowTextColor", summaryRowTextColor));
        properties.add(new ReportNumericProperty("summaryRowBackgroundColor", summaryRowBackgroundColor));
        return properties;
    }

    private JSONObject getAnalysisItemMap() throws JSONException {
        JSONObject analysisItemMap = new JSONObject();
        /*for (AnalysisItem i : columns) {
            if (i.getSortSequence() > 0) {
                JSONArray array = new JSONArray();
                array.put(String.valueOf(i.getItemPosition()));
                array.put(i.getSort() == 2 ? "desc" : "asc");
                analysisItemMap.put(String.valueOf(i.getSortSequence()), array);
            }
        }*/
        return analysisItemMap;
    }
}
