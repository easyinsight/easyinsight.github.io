package com.easyinsight.api.v3;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ExportService;
import com.easyinsight.security.SecurityUtil;
import nu.xom.Document;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 11/28/11
 * Time: 3:27 PM
 */
public class RunReportServlet extends APIServlet {
    @Override
    protected ResponseInfo processXML(Document document, EIConnection conn, HttpServletRequest request) throws Exception {
        String reportIDString = request.getParameter("reportID");
        InsightResponse insightResponse;
        try {
            long reportID = Long.parseLong(reportIDString);
            insightResponse = new AnalysisService().openAnalysisIfPossibleByID(reportID);
        } catch (NumberFormatException e) {
            insightResponse = new AnalysisService().openAnalysisIfPossible(reportIDString);
        }
        SecurityUtil.authorizeInsight(insightResponse.getInsightDescriptor().getId());
        WSAnalysisDefinition report = new AnalysisService().openAnalysisDefinition(insightResponse.getInsightDescriptor().getId());
        for (FilterDefinition filter : report.getFilterDefinitions()) {
            if (filter.getFilterName() != null && !"".equals(filter.getFilterName())) {
                if (filter instanceof FilterValueDefinition) {
                    FilterValueDefinition filterValueDefinition = (FilterValueDefinition) filter;
                    String param = request.getParameter(filter.getFilterName());
                    if (param != null) {
                        /*try {
                            Integer paramInt = Integer.parseInt(param);
                            filterValueDefinition.setFilteredValues(Arrays.asList((Object) paramInt));
                        } catch (NumberFormatException nfe) {*/
                            filterValueDefinition.setFilteredValues(Arrays.asList((Object) param));
                        //}
                    }
                } else if (filter instanceof FilterDateRangeDefinition) {
                    FilterDateRangeDefinition filterDateRangeDefinition = (FilterDateRangeDefinition) filter;
                    String startParam = request.getParameter(filter.getFilterName() + "_start");
                    String endParam = request.getParameter(filter.getFilterName() + "_end");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    if (startParam != null) {
                        filterDateRangeDefinition.setStartDate(dateFormat.parse(startParam));
                    }
                    if (endParam != null) {
                        filterDateRangeDefinition.setEndDate(dateFormat.parse(endParam));
                    }
                }
            }
        }
        InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
        ListDataResults results = (ListDataResults) new DataService().list(report, insightRequestMetadata);
        StringBuilder result = new StringBuilder();

        java.util.List<AnalysisItem> items = new java.util.ArrayList<AnalysisItem>(report.getAllAnalysisItems());
        items.remove(null);
        java.util.Collections.sort(items, new java.util.Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                return new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition());
            }
        });
        result.append("\t<reportinfo>\r\n");
        result.append("\t\t<name>").append(report.getName()).append("</name>\r\n");
        result.append("\t\t<reporttype>").append(report.getReportType()).append("</reporttype>\r\n");
        result.append("\t</reportinfo>\r\n");
        result.append("\t<rows>\r\n");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        for (com.easyinsight.analysis.ListRow listRow : results.getRows()) {
            result.append("\t\t<row>\r\n");
            for (AnalysisItem analysisItem : items) {
                for (int i = 0; i < results.getHeaders().length; i++) {
                    AnalysisItem headerItem = results.getHeaders()[i];
                    if (headerItem == analysisItem) {
                        com.easyinsight.core.Value value = listRow.getValues()[i];
                        result.append("\t\t\t<value field=\"").append(headerItem.toDisplay()).append("\">");
                        if (value.type() == Value.DATE) {
                            DateValue dateValue = (DateValue) value;
                            result.append(dateFormat.format(dateValue.getDate()));
                        } else {
                            if (analysisItem.hasType(AnalysisItemTypes.DIMENSION) && value.type() == Value.NUMBER) {
                                result.append(String.valueOf(value.toDouble().intValue()));
                            } else {
                                result.append(value.toString());
                            }
                        }
                        result.append("</value>");
                    }
                }
            }
            result.append("\t\t</row>\r\n");
        }
        result.append("\t</rows>\r\n");
        return new ResponseInfo(ResponseInfo.ALL_GOOD, result.toString());
    }
}
