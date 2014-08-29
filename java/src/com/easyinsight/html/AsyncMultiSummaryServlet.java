package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSMultiSummaryDefinition;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.export.MultiSummaryData;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 5/29/14
 * Time: 10:29 AM
 */
public class AsyncMultiSummaryServlet extends HtmlServlet {

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

        try {
            int displaystart = Integer.parseInt(request.getParameter("iDisplayStart"));
            int displaylength = Integer.parseInt(request.getParameter("iDisplayLength"));

            WSMultiSummaryDefinition listDefinition = (WSMultiSummaryDefinition) report;


            System.out.println(request.getParameter("iSortCol_1"));

            String uid = request.getParameter("uid");

            MultiSummaryData multiSummaryData = DataService.multiSummaryDataViaCache(listDefinition, insightRequestMetadata, conn, uid);

            HigherLevelComparator rowComparator = new HigherLevelComparator();
            boolean sort = false;
            for (int i = 0; i < listDefinition.getCoreItems().size(); i++) {
                // what's the sort column
                String sortColumn = request.getParameter("iSortCol_" + (i));
                // which direction is it sorting
                String sortDirection = request.getParameter("sSortDir_" + (i));

                if (sortColumn != null) {
                    sort = true;
                    AnalysisItem item = listDefinition.getCoreItems().get(Integer.parseInt(sortColumn) - 1);
                    boolean descending = sortDirection.equals("desc");
                    rowComparator.addSortKey(item, !descending);
                } /*else {
                    break;
                }*/

            }

            if (sort) {
                multiSummaryData.sort(rowComparator);
            }

            java.util.List<AnalysisItem> items = new java.util.ArrayList<>(listDefinition.getCoreItems());

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
            JSONObject results = new JSONObject();
            JSONArray jsonRows = new JSONArray();
            results.put("rows", jsonRows);
            List<MultiSummaryData.HigherLevel> higherLevels = multiSummaryData.getHigherLevels();
            int totalRecords = higherLevels.size();
            //Map<String, Object> props = dataSet.getAdditionalProperties();
            int end = Math.min(higherLevels.size(), displaystart + displaylength);
            List<MultiSummaryData.HigherLevel> page = higherLevels.subList(displaystart, end);
            //dataSet = dataSet.subset(displaystart, displaystart + displaylength);
            //dataSet.setAdditionalProperties(props);
            for (MultiSummaryData.HigherLevel row : page) {
                JSONObject jsonRow = new JSONObject();
                jsonRow.put("hasNested", row.getMap().size() > 0);
                jsonRow.put("nestedData", row.createChildDiv(insightRequestMetadata, conn));
                jsonRow.put("rowID", String.valueOf(row.getRowID()));
                jsonRow.put("0", "");
                int i = 1;
                for (AnalysisItem analysisItem : items) {
                    Value value = row.getRow().getValue(analysisItem);
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

                                        String encodedValue = toDrillthroughValue(row.getRow().getValue(dataItem));
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
            results.put("columnLength", listDefinition.getCoreItems().size());
            returnObject.put("iTotalRecords", totalRecords);
            returnObject.put("iTotalDisplayRecords", totalRecords);
            returnObject.put("rowData", results);
            System.out.println(returnObject);
            response.setContentType("application/json");
            response.getOutputStream().write(returnObject.toString().getBytes());
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
