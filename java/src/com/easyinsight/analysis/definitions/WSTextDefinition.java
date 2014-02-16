package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;
import com.easyinsight.intention.Intention;
import com.easyinsight.intention.IntentionSuggestion;
import com.easyinsight.intention.ReportPropertiesIntention;
import com.easyinsight.pipeline.IComponent;
import com.easyinsight.pipeline.ListSummaryComponent;
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
public class WSTextDefinition extends WSAnalysisDefinition {
    private List<AnalysisItem> columns;
    private long textReportID;
    private int fontColor;

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    public long getTextReportID() {
        return textReportID;
    }

    public void setTextReportID(long textReportID) {
        this.textReportID = textReportID;
    }

    public List<AnalysisItem> getColumns() {
        return columns;
    }

    public void setColumns(List<AnalysisItem> columns) {
        this.columns = columns;
    }

    public String getDataFeedType() {
        return "Text";
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

    /*@Override
    public List<String> javaScriptIncludes() {
        List<String> list = new ArrayList<String>();
        return list;
    }*/

    /*@Override
    public List<String> cssIncludes() {
        return Arrays.asList("/css/jquery.dataTables.css");
    }*/

    /*@Override
    public String toHTML(String targetDiv, HTMLReportMetadata htmlReportMetadata) {
        try {
            JSONObject analysisItemMap = new JSONObject();
            for (AnalysisItem i : columns) {
                if (i.getSortSequence() > 0) {
                    JSONArray array = new JSONArray();
                    array.put(String.valueOf(i.getItemPosition()));
                    array.put(i.getSort() == 2 ? "desc" : "asc");
                    analysisItemMap.put(String.valueOf(i.getSortSequence()), array);
                }
            }

            String timezoneOffset = "timezoneOffset='+new Date().getTimezoneOffset()+'";

            return "$.get('/app/htmlExport?reportID=" + getUrlKey() +"&embedded="+htmlReportMetadata.isEmbedded()+ "&" + timezoneOffset + "&'+ strParams, List.getCallback('" + targetDiv + "', " + jsonProperties() + ", " + analysisItemMap.toString() +", " + columns.size() + "));";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
*/
    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        fontColor = (int) findNumberProperty(properties, "fontColor", 0);
    }

    public String jsonProperties() {

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
        return p.toString();
    }

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentFilters) throws JSONException {
        JSONObject jo = super.toJSON(htmlReportMetadata, parentFilters);
        jo.put("key", getUrlKey());
        jo.put("url", "/app/htmlExport");
        jo.put("type", "text_report");
        return jo;
    }

    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("fontColor", fontColor));
        return properties;
    }
}
