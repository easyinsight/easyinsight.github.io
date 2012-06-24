package com.easyinsight.api.v3;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;
import nu.xom.Document;
import org.json.JSONStringer;
import org.json.JSONWriter;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * User: jamesboe
 * Date: 11/28/11
 * Time: 3:27 PM
 */
public class RunReportJSONServlet extends JSONServlet {
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
        RunReportServlet.populateFiltersFromRequest(request, report);
        InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
        ListDataResults results = (ListDataResults) new DataService().list(report, insightRequestMetadata);
        JSONStringer jsonStringer = new JSONStringer();

        java.util.List<AnalysisItem> items = new java.util.ArrayList<AnalysisItem>(report.getAllAnalysisItems());
        items.remove(null);
        java.util.Collections.sort(items, new java.util.Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                return new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition());
            }
        });
        JSONWriter rows = jsonStringer.array();

        //JSONWriter rowsWriter = rows.key("rows").array();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        for (ListRow listRow : results.getRows()) {
            JSONWriter row = rows.object();
            for (AnalysisItem analysisItem : items) {
                for (int i = 0; i < results.getHeaders().length; i++) {
                    AnalysisItem headerItem = results.getHeaders()[i];
                    if (headerItem == analysisItem) {
                        Value value = listRow.getValues()[i];
                        String valueString;
                        if (value.type() == Value.DATE) {
                            DateValue dateValue = (DateValue) value;
                            valueString = dateFormat.format(dateValue.getDate());
                        } else if (value.type() == Value.EMPTY) {
                            valueString = "";
                        } else {
                            valueString = value.toString();
                        }
                        row.key(headerItem.toDisplay()).value(valueString);
                    }
                }
            }
            row.endObject();
        }
        rows.endArray();

        // zgcBLUnaFuJaRuwUrrDW

        System.out.println(jsonStringer.toString());
        return new ResponseInfo(ResponseInfo.ALL_GOOD, jsonStringer.toString());
    }
}
