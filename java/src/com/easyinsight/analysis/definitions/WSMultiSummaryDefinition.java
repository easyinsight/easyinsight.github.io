package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ExportService;
import com.easyinsight.preferences.ApplicationSkin;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
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
    private boolean nestedReportTitles;
    private boolean nestedReportHeaders;
    private int nestedFontSize;
    private boolean defaultToExpanded;

    public boolean isNestedReportHeaders() {
        return nestedReportHeaders;
    }

    public void setNestedReportHeaders(boolean nestedReportHeaders) {
        this.nestedReportHeaders = nestedReportHeaders;
    }

    public int getNestedFontSize() {
        return nestedFontSize;
    }

    public void setNestedFontSize(int nestedFontSize) {
        this.nestedFontSize = nestedFontSize;
    }

    public boolean isDefaultToExpanded() {
        return defaultToExpanded;
    }

    public void setDefaultToExpanded(boolean defaultToExpanded) {
        this.defaultToExpanded = defaultToExpanded;
    }

    public boolean isNestedReportTitles() {
        return nestedReportTitles;
    }

    public void setNestedReportTitles(boolean nestedReportTitles) {
        this.nestedReportTitles = nestedReportTitles;
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

    public Workbook toExcel(InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws SQLException {
        try {
            return DataService.getMultiSummaryDataResults(this, insightRequestMetadata, conn).toExcel(insightRequestMetadata, conn, true);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private String toHTMLTableSkeletonForAsync() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table class=\"table table-condensed table-bordered\">");
        sb.append("<thead>");
        sb.append("<tr style=\"background-color:").append(ExportService.createHexString(getHeaderColor1())).append("\">");
        sb.append("<th style=\"text-align:left\"></th>");
        for (AnalysisItem item : getCoreItems()) {
            sb.append("<th style=\"text-align:center;color:").append(ExportService.createHexString(getHeaderTextColor())).append("\">").append(item.toUnqualifiedDisplay()).append("</th>");
        }
        sb.append("</tr>");
        sb.append("</thead>");
        sb.append("<tbody>");
        sb.append("</tbody>");
        /*for (HigherLevel higherLevel : higherLevels) {
            sb.append(higherLevel.toHTML(insightRequestMetadata, conn));
        }*/
        sb.append("</table>");
        return sb.toString();
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
            /*p.put("lockHeaders", lockHeaders);
            p.put("showLineNumbers", showLineNumbers);*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return p;
    }


    private JSONObject getAnalysisItemMap() throws JSONException {
        JSONObject analysisItemMap = new JSONObject();
        JSONArray columnJSON = new JSONArray();
        JSONObject columnClassJSON = new JSONObject();

        JSONObject sorting = new JSONObject();

        JSONObject controlColumnObject = new JSONObject();
        String controlClassName = String.valueOf(0);
        controlColumnObject.put("sClass", controlClassName);
        columnJSON.put(controlColumnObject);
        JSONObject controlStyleData = new JSONObject();
        columnClassJSON.put(controlClassName, controlStyleData);
        controlStyleData.put("align", "left");

        for (int i = 0; i < coreItems.size(); i++) {
            AnalysisItem analysisItem = coreItems.get(i);
            JSONObject columnObject = new JSONObject();
            String className = String.valueOf(i + 1);
            columnObject.put("sClass", className);
            columnJSON.put(columnObject);

            JSONObject styleData = new JSONObject();
            columnClassJSON.put(className, styleData);
            if (analysisItem.getReportFieldExtension() != null && analysisItem.getReportFieldExtension() instanceof TextReportFieldExtension) {
                TextReportFieldExtension tfe = (TextReportFieldExtension) analysisItem.getReportFieldExtension();
                if ("Center".equals(tfe.getAlign()) || "center".equals(tfe.getAlign())) {
                    styleData.put("align", "center");
                } else if ("Right".equals(tfe.getAlign()) || "right".equals(tfe.getAlign())) {
                    styleData.put("align", "right");
                } else {
                    styleData.put("align", "left");
                }
                if (tfe.getFixedWidth() > 0) {
                    // TODO: impl
                }
            } else {
                styleData.put("align", "left");
            }


            if (analysisItem.getSortSequence() > 0) {
                JSONArray array = new JSONArray();
                array.put(String.valueOf(analysisItem.getItemPosition()));
                array.put(analysisItem.getSort() == 2 ? "desc" : "asc");
                sorting.put(String.valueOf(analysisItem.getSortSequence()), array);
            }
        }
        analysisItemMap.put("columns", columnJSON);
        analysisItemMap.put("classes", columnClassJSON);
        analysisItemMap.put("sorting", sorting);
        return analysisItemMap;
    }

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentDefinitions) throws JSONException {
        JSONObject list = super.toJSON(htmlReportMetadata, parentDefinitions);
        list.put("type", "multi_summary");
        list.put("tableHTML", toHTMLTableSkeletonForAsync());
        list.put("key", getUrlKey());
        list.put("url", "/app/htmlExport");

        list.put("properties", jsonProperties());
        list.put("columnData", getAnalysisItemMap());
        list.put("columns", coreItems.size());
        list.put("uid", getUrlKey() + System.currentTimeMillis());

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

        nestedReportTitles = findBooleanProperty(properties, "nestedReportTitles", false);
        nestedReportHeaders = findBooleanProperty(properties, "nestedReportHeaders", true);
        defaultToExpanded = findBooleanProperty(properties, "defaultToExpanded", false);
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
        properties.add(new ReportBooleanProperty("nestedReportTitles", nestedReportTitles));
        properties.add(new ReportBooleanProperty("nestedReportHeaders", nestedReportHeaders));
        properties.add(new ReportBooleanProperty("defaultToExpanded", defaultToExpanded));
        return properties;
    }

    public void renderConfig(ApplicationSkin applicationSkin) {
        if ("Primary".equals(getColorScheme())) {
            if (applicationSkin.isHeaderStartEnabled()) {
                setHeaderColor1(applicationSkin.getHeaderStart());
            }
            if (applicationSkin.isHeaderEndEnabled()) {
                setHeaderColor2(applicationSkin.getHeaderEnd());
            }
            if (applicationSkin.isReportHeaderTextColorEnabled()) {
                setHeaderTextColor(applicationSkin.getReportHeaderTextColor());
            }
        }
    }
}
