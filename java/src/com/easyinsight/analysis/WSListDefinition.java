package com.easyinsight.analysis;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;
import com.easyinsight.intention.Intention;
import com.easyinsight.intention.IntentionSuggestion;
import com.easyinsight.intention.ReportPropertiesIntention;
import com.easyinsight.pipeline.IComponent;
import com.easyinsight.pipeline.ListSummaryComponent;
import com.easyinsight.preferences.ApplicationSkin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.*;

/**
 * User: James Boe
 * Date: Jan 11, 2008
 * Time: 10:19:11 AM
 */
public class WSListDefinition extends WSAnalysisDefinition {
    private List<AnalysisItem> columns;
    private boolean showLineNumbers = false;
    private ListLimitsMetadata listLimitsMetadata;
    private boolean summaryTotal;
    private long listDefinitionID;
    private int rowColor1 = 0xF7F7F7;
    private int rowColor2 = 0xFFFFFF;
    private int headerColor1 = 0xFFFFFF;
    private int headerColor2 = 0xEFEFEF;
    private int textColor = 0x000000;
    private int headerTextColor = 0x000000;
    private int summaryRowTextColor = 0x000000;
    private int summaryRowBackgroundColor = 0x6699ff;
    private String defaultColumnAlignment;
    private boolean rolloverIcon;
    private boolean multiLineHeaders;

    public String getDefaultColumnAlignment() {
        return defaultColumnAlignment;
    }

    public void setDefaultColumnAlignment(String defaultColumnAlignment) {
        this.defaultColumnAlignment = defaultColumnAlignment;
    }

    public boolean isMultiLineHeaders() {
        return multiLineHeaders;
    }

    public void setMultiLineHeaders(boolean multiLineHeaders) {
        this.multiLineHeaders = multiLineHeaders;
    }

    public boolean isRolloverIcon() {
        return rolloverIcon;
    }

    public void setRolloverIcon(boolean rolloverIcon) {
        this.rolloverIcon = rolloverIcon;
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

    public boolean isSummaryTotal() {
        return summaryTotal;
    }

    public void setSummaryTotal(boolean summaryTotal) {
        this.summaryTotal = summaryTotal;
    }

    public long getListDefinitionID() {
        return listDefinitionID;
    }

    public void setListDefinitionID(long listDefinitionID) {
        this.listDefinitionID = listDefinitionID;
    }

    public ListLimitsMetadata getListLimitsMetadata() {
        return listLimitsMetadata;
    }

    public void setListLimitsMetadata(ListLimitsMetadata listLimitsMetadata) {
        this.listLimitsMetadata = listLimitsMetadata;
    }

    public List<AnalysisItem> getColumns() {
        return columns;
    }

    public void setColumns(List<AnalysisItem> columns) {
        this.columns = columns;
    }

    public boolean isShowLineNumbers() {
        return showLineNumbers;
    }

    public void setShowLineNumbers(boolean showLineNumbers) {
        this.showLineNumbers = showLineNumbers;
    }

    public String getDataFeedType() {
        return "List";
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

    public LimitsResults applyLimits(DataSet dataSet) {
        LimitsResults limitsResults;
        if (listLimitsMetadata != null && listLimitsMetadata.isLimitEnabled()) {
            int count = dataSet.getRows().size();
            limitsResults = new LimitsResults(count >= listLimitsMetadata.getNumber(), count, listLimitsMetadata.getNumber());
            if (listLimitsMetadata.getAnalysisItem() != null) {
                dataSet.sort(listLimitsMetadata.getAnalysisItem(), listLimitsMetadata.isTop());
            }
            dataSet.subset(listLimitsMetadata.getNumber());
        } else {
            limitsResults = super.applyLimits(dataSet);
        }
        return limitsResults;
    }

    public List<AnalysisItem> getLimitFields() {
        if (listLimitsMetadata != null && listLimitsMetadata.getAnalysisItem() != null && listLimitsMetadata.isLimitEnabled()) {
            return Arrays.asList(listLimitsMetadata.getAnalysisItem());
        } else {
            return super.getLimitFields();
        }
    }

    @Override
    public List<IComponent> createComponents() {
        List<IComponent> components = super.createComponents();
        if (summaryTotal) {
            components.add(new ListSummaryComponent());
        }
        return components;
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

    @Override
    public String toHTML(String targetDiv, HTMLReportMetadata htmlReportMetadata) {
        try {
            JSONObject analysisItemMap = getAnalysisItemMap();

            String timezoneOffset = "timezoneOffset='+new Date().getTimezoneOffset()+'";

            return "$.get('/app/htmlExport?reportID=" + getUrlKey() +"&embedded="+htmlReportMetadata.isEmbedded()+ "&" + timezoneOffset + "&'+ strParams, List.getCallback('" + targetDiv + "', " + jsonProperties().toString() + ", " + analysisItemMap.toString() +", " + columns.size() + "));";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject getAnalysisItemMap() throws JSONException {
        JSONObject analysisItemMap = new JSONObject();
        for (AnalysisItem i : columns) {
            if (i.getSortSequence() > 0) {
                JSONArray array = new JSONArray();
                array.put(String.valueOf(i.getItemPosition()));
                array.put(i.getSort() == 2 ? "desc" : "asc");
                analysisItemMap.put(String.valueOf(i.getSortSequence()), array);
            }
        }
        return analysisItemMap;
    }

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentDefinitions) throws JSONException {
        JSONObject list = super.toJSON(htmlReportMetadata, parentDefinitions);
        list.put("type", "list");
        list.put("key", getUrlKey());
        list.put("url", "/app/htmlExport");
        list.put("properties", jsonProperties());
        list.put("sorting", getAnalysisItemMap());
        list.put("columns", columns.size());
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
        rolloverIcon = findBooleanProperty(properties, "rolloverIcon", false);
        multiLineHeaders = findBooleanProperty(properties, "multiLineHeaders", false);
        defaultColumnAlignment = findStringProperty(properties, "defaultColumnAlignment", "left");
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
        properties.add(new ReportBooleanProperty("rolloverIcon", rolloverIcon));
        properties.add(new ReportBooleanProperty("multiLineHeaders", multiLineHeaders));
        properties.add(new ReportStringProperty("defaultColumnAlignment", defaultColumnAlignment));
        return properties;
    }


    public List<IntentionSuggestion> suggestIntentions(WSAnalysisDefinition report) {
        List<IntentionSuggestion> suggestions = new ArrayList<IntentionSuggestion>();
        WSListDefinition wsListDefinition = (WSListDefinition) report;
        if (!wsListDefinition.isSummaryTotal()) {
            suggestions.add(new IntentionSuggestion("Add a Summary Row",
                    "Add a summary row to the bottom of your report.",
                    IntentionSuggestion.SCOPE_REPORT, IntentionSuggestion.ADD_SUMMARY_ROW, IntentionSuggestion.OTHER));
        }
        return suggestions;
    }

    public List<Intention> createIntentions(List<AnalysisItem> fields, int type) throws SQLException {
        if (type == IntentionSuggestion.ADD_SUMMARY_ROW) {
            ReportPropertiesIntention reportPropertiesIntention = new ReportPropertiesIntention();
            reportPropertiesIntention.setSummaryRow(true);
            return Arrays.asList((Intention) reportPropertiesIntention);
        } else {
            return super.createIntentions(fields, type);
        }
    }

    protected boolean supportsMultiField() {
        return true;
    }

    protected List<AnalysisItem> reportFieldsForMultiField() {
        return columns;
    }

    protected void assignResults(List<AnalysisItem> fields) {
        setColumns(fields);
    }

    public int extensionType() {
        return ReportFieldExtension.TEXT;
    }

    protected boolean accepts(AnalysisItem analysisItem) {
        return true;
    }

    public void renderConfig(ApplicationSkin applicationSkin) {
        if ("Primary".equals(getColorScheme())) {
            if (applicationSkin.isSummaryTextColorEnabled()) {
                setSummaryRowTextColor(applicationSkin.getSummaryTextColor());
            }
            if (applicationSkin.isSummaryBackgroundColorEnabled()) {
                setSummaryRowBackgroundColor(applicationSkin.getSummaryBackgroundColor());
            }
            if (applicationSkin.isHeaderStartEnabled()) {
                setHeaderColor1(applicationSkin.getHeaderStart());
            }
            if (applicationSkin.isHeaderEndEnabled()) {
                setHeaderColor2(applicationSkin.getHeaderEnd());
            }
            if (applicationSkin.isTableColorStartEnabled()) {
                setRowColor1(applicationSkin.getTableColorStart());
            }
            if (applicationSkin.isTableColorEndEnabled()) {
                setRowColor2(applicationSkin.getTableColorEnd());
            }
            if (applicationSkin.isReportHeaderTextColorEnabled()) {
                setHeaderTextColor(applicationSkin.getReportHeaderTextColor());
            }
            if (applicationSkin.isTextColorEnabled()) {
                setTextColor(applicationSkin.getTextColor());
            }
        }
    }
}
