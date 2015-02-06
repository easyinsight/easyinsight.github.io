package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.api.v3.JSONServlet;
import com.easyinsight.api.v3.ResponseInfo;
import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.security.SecurityUtil;
import net.minidev.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by Alan on 1/22/15.
 */
@WebServlet(value = "/html/reportMetadata", asyncSupported = true)
public class ReportMetadataServlet extends JSONServlet {

    @Override
    protected ResponseInfo processGet(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        long reportValue;
        String reportID = request.getParameter("reportID");
        org.json.JSONObject jo = new org.json.JSONObject();

        InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
        if (request.getParameter("timezoneOffset") != null) {
            int timezoneOffset = Integer.parseInt(request.getParameter("timezoneOffset"));
            insightRequestMetadata.setUtcOffset(timezoneOffset);
        }

        InsightResponse insightResponse = new AnalysisService().openAnalysisIfPossible(reportID);
        reportValue = insightResponse.getInsightDescriptor().getId();
        WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportValue);
        jo.put("report", report.toJSON(new HTMLReportMetadata(), new ArrayList<>()));
        if("true".equals(request.getParameter("data"))) {
            ExportMetadata md = ExportService.createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);
            jo.put("data", ReportDataServlet.processData(insightRequestMetadata, conn, report, md));
        }

        return new ResponseInfo(ResponseInfo.ALL_GOOD, jo.toString());
    }

    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        throw new UnsupportedOperationException();
    }
}
