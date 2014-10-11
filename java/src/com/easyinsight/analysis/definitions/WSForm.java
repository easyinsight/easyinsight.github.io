package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
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

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentDefinitions) throws JSONException {
        JSONObject list = super.toJSON(htmlReportMetadata, parentDefinitions);
        list.put("type", "form");
        list.put("key", getUrlKey());
        list.put("url", "/app/htmlExport");
        return list;
    }

    public String toExportHTML(EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean email) {
        try {
            DataSet dataSet = DataService.listDataSet(this, insightRequestMetadata, conn);
            ExportMetadata md = ExportService.createExportMetadata(conn);
            if (email) {
                if (dataSet.getRows().size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (IRow row : dataSet.getRows()) {

                        sb.append("<table>\n");
                        for (AnalysisItem analysisItem : columns) {
                            String value = ExportService.createValue(md, analysisItem, row.getValue(analysisItem), false);
                            sb.append("<tr><td style=\"text-align:right;font-weight:bold;padding:5px\">").append(analysisItem.toUnqualifiedDisplay()).append(":</td>").append("<td style=\"text-align:left;padding:5px\">").append(value).append("</td>").append("</tr>");
                        }
                        sb.append("</table>");
                    }
                    return sb.toString();
                } else {
                    return "";
                }
            } else {
                if (dataSet.getRows().size() > 0) {
                    IRow row = dataSet.getRow(0);
                    StringBuilder sb = new StringBuilder();
                    sb.append("<table>\n");
                    for (AnalysisItem analysisItem : columns) {
                        String value = ExportService.createValue(md, analysisItem, row.getValue(analysisItem), false);
                        sb.append("<tr><td style=\"text-align:right;font-weight:bold;padding:5px\">").append(analysisItem.toUnqualifiedDisplay()).append(":</td>").append("<td style=\"text-align:left;padding:5px\">").append(value).append("</td>").append("</tr>");
                    }
                    sb.append("</table>");
                    return sb.toString();
                } else {
                    return "";
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
