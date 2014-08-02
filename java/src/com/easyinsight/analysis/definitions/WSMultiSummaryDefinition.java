package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.database.EIConnection;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * User: jamesboe
 * Date: 7/30/14
 * Time: 11:38 AM
 */
public class WSMultiSummaryDefinition extends WSAnalysisDefinition {
    private long multiSummaryID;

    private List<AnalysisItem> coreItems;
    private AnalysisItem key;
    private List<InsightDescriptor> reports;

    private int summaryBackgroundColor;
    private int summaryTextColor;
    private int headerColor1;
    private int headerColor2;
    private int headerTextColor;
    private String defaultMeasureAlignment;
    private String defaultGroupingAlignnment;
    private String defaultDateAlignment;
    private boolean includeSubHeaders;

    public boolean isIncludeSubHeaders() {
        return includeSubHeaders;
    }

    public void setIncludeSubHeaders(boolean includeSubHeaders) {
        this.includeSubHeaders = includeSubHeaders;
    }

    public String getDefaultMeasureAlignment() {
        return defaultMeasureAlignment;
    }

    public void setDefaultMeasureAlignment(String defaultMeasureAlignment) {
        this.defaultMeasureAlignment = defaultMeasureAlignment;
    }

    public String getDefaultGroupingAlignnment() {
        return defaultGroupingAlignnment;
    }

    public void setDefaultGroupingAlignnment(String defaultGroupingAlignnment) {
        this.defaultGroupingAlignnment = defaultGroupingAlignnment;
    }

    public String getDefaultDateAlignment() {
        return defaultDateAlignment;
    }

    public void setDefaultDateAlignment(String defaultDateAlignment) {
        this.defaultDateAlignment = defaultDateAlignment;
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

    public long getMultiSummaryID() {
        return multiSummaryID;
    }

    public void setMultiSummaryID(long multiSummaryID) {
        this.multiSummaryID = multiSummaryID;
    }

    public List<AnalysisItem> getCoreItems() {
        return coreItems;
    }

    public void setCoreItems(List<AnalysisItem> coreItems) {
        this.coreItems = coreItems;
    }

    public AnalysisItem getKey() {
        return key;
    }

    public void setKey(AnalysisItem key) {
        this.key = key;
    }

    public List<InsightDescriptor> getReports() {
        return reports;
    }

    public void setReports(List<InsightDescriptor> reports) {
        this.reports = reports;
    }

    @Override
    public String getDataFeedType() {
        return "MultiSummary";
    }

    @Override
    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = coreItems.stream().collect(Collectors.toSet());
        columnList.add(key);
        return columnList;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        Collections.sort(getCoreItems(), (analysisItem, analysisItem1) -> new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition()));
        addItems("coreItems", getCoreItems(), structure);
        addItems("key", Arrays.asList(key), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        setCoreItems(items("coreItems", structure));
        key = firstItem("key", structure);
    }

    @Override
    public String toExportHTML(EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean email) {
        try {
            if (email) {
                return DataService.getMultiSummaryDataResults(this, insightRequestMetadata, conn).toHTMLForEmail(insightRequestMetadata, conn);
            } else {
                return DataService.getMultiSummaryDataResults(this, insightRequestMetadata, conn).toHTML(insightRequestMetadata, conn);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentDefinitions) throws JSONException {
        JSONObject list = super.toJSON(htmlReportMetadata, parentDefinitions);
        list.put("type", "multi_summary");
        list.put("key", getUrlKey());
        list.put("url", "/app/htmlExport");
        return list;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        headerColor1 = (int) findNumberProperty(properties, "headerColor1", 0xffffff);
        headerColor2 = (int) findNumberProperty(properties, "headerColor2", 0xEFEFEF);
        headerTextColor = (int) findNumberProperty(properties, "headerTextColor", 0x000000);
        summaryTextColor = (int) findNumberProperty(properties, "summaryTextColor", 0);
        summaryBackgroundColor = (int) findNumberProperty(properties, "summaryBackgroundColor", 0xaaaaaa);
        defaultMeasureAlignment = findStringProperty(properties, "defaultMeasureAlignment", "none");
        defaultDateAlignment = findStringProperty(properties, "defaultDateAlignment", "none");
        defaultGroupingAlignnment = findStringProperty(properties, "defaultGroupingAlignment", "none");
    }

    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("headerColor1", headerColor1));
        properties.add(new ReportNumericProperty("headerColor2", headerColor2));
        properties.add(new ReportNumericProperty("headerTextColor", headerTextColor));
        properties.add(new ReportNumericProperty("summaryTextColor", summaryTextColor));
        properties.add(new ReportNumericProperty("summaryBackgroundColor", summaryBackgroundColor));
        properties.add(new ReportStringProperty("defaultMeasureAlignment", defaultMeasureAlignment));
        properties.add(new ReportStringProperty("defaultDateAlignment", defaultDateAlignment));
        properties.add(new ReportStringProperty("defaultGroupingAlignment", defaultGroupingAlignnment));
        return properties;
    }
}
