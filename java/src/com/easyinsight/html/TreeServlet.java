package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSBarChartDefinition;
import com.easyinsight.analysis.definitions.WSColumnChartDefinition;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.export.TreeData;
import com.easyinsight.pipeline.PipelineData;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * User: jamesboe
 * Date: 5/23/12
 * Time: 4:56 PM
 */
public class TreeServlet extends HtmlServlet {
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata,
                           EIConnection conn, WSAnalysisDefinition report, ExportMetadata md) throws Exception {
        WSTreeDefinition tree = (WSTreeDefinition) report;
        tree.setSummaryTotal(true);
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);
        PipelineData pipelineData = dataSet.getPipelineData();

        AnalysisHierarchyItem hierarchy = (AnalysisHierarchyItem) tree.getHierarchy();
        TreeData treeData = new TreeData(tree, hierarchy, md, dataSet);
        for (IRow row : dataSet.getRows()) {
            treeData.addRow(row);
        }
        JSONArray values = new JSONArray();
        List<TreeRow> rows = treeData.toTreeRows(pipelineData);
        for (TreeRow row : rows) {
            values.put(row.toJSON(tree, md));
        }

        JSONObject ret = new JSONObject();



        JSONArray columns = new JSONArray();
        JSONObject hierarchyColumn = new JSONObject();
        hierarchyColumn.put("key", "key");
        hierarchyColumn.put("label", hierarchy.toUnqualifiedDisplay());
        hierarchyColumn.put("type", "text");

        String hierAlign = "left";
        if (hierarchy.getReportFieldExtension() != null && hierarchy.getReportFieldExtension() instanceof TextReportFieldExtension) {
            TextReportFieldExtension textReportFieldExtension = (TextReportFieldExtension) hierarchy.getReportFieldExtension();
            if (textReportFieldExtension.getAlign() != null) {
                if ("left".equals(textReportFieldExtension.getAlign()) || "Left".equals(textReportFieldExtension.getAlign())) {
                    hierAlign = "left";
                } else if ("center".equals(textReportFieldExtension.getAlign()) || "Center".equals(textReportFieldExtension.getAlign())) {
                    hierAlign = "center";
                } else if ("right".equals(textReportFieldExtension.getAlign()) || "Right".equals(textReportFieldExtension.getAlign())) {
                    hierAlign = "right";
                }
            }
        }
        hierarchyColumn.put("align", hierAlign);

        //hierarchyColumn.put("width", "50%");
        columns.put(hierarchyColumn);
        for (AnalysisItem item : tree.getItems()) {
            JSONObject itemColumn = new JSONObject();
            itemColumn.put("key", item.qualifiedName());
            //itemColumn.put("width", "50%");
            itemColumn.put("label", item.toUnqualifiedDisplay());
            itemColumn.put("type", "text");

            String align = "left";
            if (item.getReportFieldExtension() != null && item.getReportFieldExtension() instanceof TextReportFieldExtension) {
                TextReportFieldExtension textReportFieldExtension = (TextReportFieldExtension) item.getReportFieldExtension();
                if (textReportFieldExtension.getAlign() != null) {
                    if ("left".equals(textReportFieldExtension.getAlign()) || "Left".equals(textReportFieldExtension.getAlign())) {
                        align = "left";
                    } else if ("center".equals(textReportFieldExtension.getAlign()) || "Center".equals(textReportFieldExtension.getAlign())) {
                        align = "center";
                    } else if ("right".equals(textReportFieldExtension.getAlign()) || "Right".equals(textReportFieldExtension.getAlign())) {
                        align = "right";
                    }
                }
            }
            itemColumn.put("align", align);
            /*
            DrillThrough drillThrough = (DrillThrough) defaultLink;
                        sb.append("<a class=\"list_drillthrough\" href=\"#\" data-reportid=\"");
                        sb.append(report.getUrlKey());
                        sb.append("\" data-drillthroughid=\"");
                        sb.append(drillThrough.createID());
                        sb.append("\" data-embedded=\"");
                        sb.append(exportProperties.isEmbedded());
                        sb.append("\" data-source=\"");
                        sb.append(analysisItem.getAnalysisItemID());
                        sb.append("\"");
                        if (drillThrough.isFilterRowGroupings()) {


                            for (AnalysisItem dataItem : headers) {
                                if (dataItem.hasType(AnalysisItemTypes.DIMENSION)) {
                                    int k = unsortedHeaders.indexOf(dataItem);
                                    String encodedValue;

                                    try {
                                        encodedValue = toDrillthroughValue(listRow.getValues()[k], dataItem, exportMetadata);
                                        sb.append(" data-drillthrough");
                                        sb.append(dataItem.getAnalysisItemID());
                                        sb.append("=\"");
                                        sb.append(encodedValue);
                                        sb.append("\"");
                                    } catch (UnsupportedEncodingException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                            }
                        } else {
                            sb.append(" data-drillthrough");
                            sb.append(analysisItem.getAnalysisItemID());
                            sb.append("=\"");

                            try {
                                sb.append(toDrillthroughValue(value, analysisItem, exportMetadata));
                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }
                            sb.append("\"");
                        }
                        if (colorString != null) {
                            sb.append(" style=\"color:").append(colorString).append("\"");
                        }
                        sb.append(">");
                        showLink = true;
                    }
             */

            columns.put(itemColumn);
        }

        ret.put("columns", columns);

        JSONObject all = new JSONObject();
        all.put("key", "All");
        Map<AnalysisItem, Aggregation> grandTotals = treeData.getGrandTotalTotals();
        for (AnalysisItem reportItem : tree.getItems()) {
            String string = "";
            if (reportItem.hasType(AnalysisItemTypes.MEASURE)) {
                Aggregation aggregation = grandTotals.get(reportItem);
                if (aggregation != null) {
                    string = ExportService.createValue(md, reportItem, aggregation.getValue(), false);
                }
            }
            all.put(reportItem.qualifiedName(), string);

        }
        /*
        if (tree.isSummaryTotal()) {
            String tdStyle = "border-color:#000000;padding:6px;border-style:solid;border-width:1px";
            sb.append("<tr>");
            sb.append("<td style=\"" + tdStyle + "\"></td>");
            Map<AnalysisItem, Aggregation> grandTotals = treeData.getGrandTotalTotals();
            for (AnalysisItem reportItem : tree.getItems()) {
                sb.append("<td style=\"" + tdStyle + "\">");
                if (reportItem.hasType(AnalysisItemTypes.MEASURE)) {
                    Aggregation aggregation = grandTotals.get(reportItem);
                    Value value = aggregation.getValue();

                    sb.append(ExportService.createValue(exportMetadata, reportItem, value, false));
                }
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
         */

        all.put("_values", values);
        JSONArray retValues = new JSONArray();
        retValues.put(all);
        ret.put("values", retValues);

        response.setContentType("application/json");
        response.getOutputStream().write(ret.toString().getBytes());
        response.getOutputStream().flush();
    }
}
