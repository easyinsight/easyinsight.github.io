package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 5/29/14
 * Time: 10:29 AM
 */
public class AsyncListServlet extends HtmlServlet {

    private static String toDrillthroughValue(Value value) throws UnsupportedEncodingException {
        String encodedValue;
        String drillthroughValueString;
        if (value.type() == Value.NUMBER) {
            drillthroughValueString = String.valueOf(value.toDouble().intValue());
        } else {
            drillthroughValueString = value.toString();
        }
        encodedValue = StringEscapeUtils.escapeHtml(URLEncoder.encode(drillthroughValueString, "UTF-8"));
        return encodedValue;
    }

    @Override
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSAnalysisDefinition report, ExportMetadata md) throws Exception {

        int displaystart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int displaylength = Integer.parseInt(request.getParameter("iDisplayLength"));

        // what's the sort column
        String sortColumn = request.getParameter("iSortCol_0");
        // which direction is it sorting
        String sortDirection = request.getParameter("sSortDir_0");

        String uid = request.getParameter("uid");
        DataSet dataSet = DataService.listDataSetViaCache(report, insightRequestMetadata, conn, uid);
        WSListDefinition listDefinition = (WSListDefinition) report;
        if (sortColumn != null) {
            AnalysisItem item = listDefinition.getColumns().get(Integer.parseInt(sortColumn));
            dataSet.sort(item, sortDirection.equals("desc"));
        }

        java.util.List<AnalysisItem> items = new java.util.ArrayList<>(listDefinition.getColumns());

        Map<AnalysisItem, Link> linkMap = new HashMap<>();

        for (AnalysisItem headerItem : items) {
            if (headerItem.getLinks() != null) {
                Link defaultLink = null;

                for (Link link : headerItem.getLinks()) {
                    if (link.isDefaultLink()) {
                        defaultLink = link;
                    }
                }
                if (defaultLink == null && headerItem.getLinks().size() == 1) {
                    defaultLink = headerItem.getLinks().get(0);
                }


                linkMap.put(headerItem, defaultLink);
            }
        }

        JSONObject returnObject = new JSONObject();
        JSONArray jsonRows = new JSONArray();
        int totalRecords = dataSet.getRows().size();
        dataSet = dataSet.subset(displaystart, displaystart + displaylength);
        for (IRow row : dataSet.getRows()) {
            JSONObject jsonRow = new JSONObject();
            int i = 0;
            for (AnalysisItem analysisItem : items) {
                Value value = row.getValue(analysisItem);
                String string = ExportService.createValue(md, analysisItem, value, false);
                jsonRow.put(String.valueOf(i), string);

                if (value.getValueExtension() != null && value.getValueExtension() instanceof TextValueExtension) {
                    TextValueExtension textValueExtension = (TextValueExtension) value.getValueExtension();
                    if (textValueExtension.getColor() != 0) {
                        String hexString = ExportService.createHexString(textValueExtension.getColor());
                        jsonRow.put(i + "fg", hexString);
                        // set color
                        //styleString.append(";color:").append(hexString);
                    }
                    if (textValueExtension.getBackgroundColor() != TextValueExtension.WHITE) {
                        String hexString = ExportService.createHexString(textValueExtension.getBackgroundColor());
                        jsonRow.put(i + "bg", hexString);
                        // set background color
                        //styleString.append(";background-color:").append(hexString);
                    }
                    if (textValueExtension.isBold()) {
                        jsonRow.put(i + "bold", true);
                        // set bold
                        // styleString.append(";font-weight:bold");
                    }
                }

                Link defaultLink = linkMap.get(analysisItem);
                boolean showLink = false;
                if (defaultLink != null) {
                    if (defaultLink instanceof URLLink) {
                        showLink = value.getLinks() != null && value.getLinks().get(analysisItem.toDisplay()) != null;

                        if (showLink) {
                            String url = value.getLinks().get(analysisItem.toDisplay());
                            jsonRow.put(i + "url", url);
                            // add link to data
                        }
                    } else if (defaultLink instanceof DrillThrough) {
                        StringBuilder sb = new StringBuilder();
                        DrillThrough drillThrough = (DrillThrough) defaultLink;
                        sb.append("<a class=\"list_drillthrough\" href=\"#\" data-reportid=\"");
                        sb.append(report.getUrlKey());
                        sb.append("\" data-drillthroughid=\"");
                        sb.append(drillThrough.createID());
                        sb.append("\" data-embedded=\"");
                        // todo: fix
                        sb.append(false);
                        sb.append("\" data-source=\"");
                        sb.append(analysisItem.getAnalysisItemID());
                        sb.append("\"");
                        if (drillThrough.isFilterRowGroupings()) {


                            for (AnalysisItem dataItem : items) {
                                if (dataItem.hasType(AnalysisItemTypes.DIMENSION)) {

                                    String encodedValue = toDrillthroughValue(row.getValue(dataItem));
                                    sb.append(" data-drillthrough");
                                    sb.append(dataItem.getAnalysisItemID());
                                    sb.append("=\"");
                                    sb.append(encodedValue);
                                    sb.append("\"");
                                }

                            }
                        } else {
                            sb.append(" data-drillthrough");
                            sb.append(analysisItem.getAnalysisItemID());
                            sb.append("=\"");

                            try {
                                sb.append(toDrillthroughValue(value));
                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }
                            sb.append("\"");
                        }
                        sb.append(">");
                        sb.append(string);
                        sb.append("</a>");
                        jsonRow.put(i + "dt", sb.toString());
                    }
                }
                i++;
            }
            jsonRows.put(jsonRow);
        }
        returnObject.put("iTotalRecords", totalRecords);
        returnObject.put("iTotalDisplayRecords", totalRecords);
        returnObject.put("aaData", jsonRows);
        response.setContentType("application/json");
        response.getOutputStream().write(returnObject.toString().getBytes());
        response.getOutputStream().flush();
    }
}
