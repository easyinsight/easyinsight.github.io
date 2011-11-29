package com.easyinsight.api.v3;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;
import nu.xom.Document;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        InsightResponse insightResponse = new AnalysisService().openAnalysisIfPossible(reportIDString);
        SecurityUtil.authorizeInsight(insightResponse.getInsightDescriptor().getId());
        WSAnalysisDefinition report = new AnalysisService().openAnalysisDefinition(insightResponse.getInsightDescriptor().getId());
        InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
        ListDataResults results = (ListDataResults) new DataService().list(report, insightRequestMetadata);
        StringBuilder result = new StringBuilder();
        result.append("<response>\r\n");
        java.util.List<AnalysisItem> items = new java.util.ArrayList<AnalysisItem>(report.getAllAnalysisItems());
        items.remove(null);
        java.util.Collections.sort(items, new java.util.Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                return new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition());
            }
        });
        result.append("\t<rows>\r\n");
        Map<AnalysisItem, String> map = new HashMap<AnalysisItem, String>();
        for (AnalysisItem item : items) {
            String displayName = item.toDisplay();
            String replaceName = displayName.replace(' ', '_').replace('<', '_').replace('>', '_');
            if (!Character.isLetter(replaceName.charAt(0))) {
                replaceName = "f" + replaceName;
            }
            map.put(item, replaceName);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        for (com.easyinsight.analysis.ListRow listRow : results.getRows()) {
            result.append("\t\t<row>\r\n");
            for (AnalysisItem analysisItem : items) {
                for (int i = 0; i < results.getHeaders().length; i++) {
                    AnalysisItem headerItem = results.getHeaders()[i];
                    if (headerItem == analysisItem) {
                        com.easyinsight.core.Value value = listRow.getValues()[i];
                        String name = map.get(headerItem);
                        result.append("\t\t\t<").append(name).append(">");
                        if (value.type() == Value.DATE) {
                            DateValue dateValue = (DateValue) value;
                            result.append(dateFormat.format(dateValue.getDate()));
                        } else {
                            result.append(value.toString());
                        }
                        result.append("</").append(name).append(">");
                    }
                }
            }
            result.append("\t\t</row>\r\n");
        }
        result.append("\t</rows>\r\n");
        result.append("</response>");
        System.out.println(result.toString());
        return new ResponseInfo(ResponseInfo.ALL_GOOD, result.toString());
    }
}
