package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.api.v3.JSONServlet;
import com.easyinsight.api.v3.ResponseInfo;
import com.easyinsight.database.EIConnection;
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
    protected ResponseInfo processGet(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {long reportValue;
        String reportID = request.getParameter("reportID");

        InsightResponse insightResponse = new AnalysisService().openAnalysisIfPossible(reportID);
        reportValue = insightResponse.getInsightDescriptor().getId();
        WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportValue);

        return new ResponseInfo(ResponseInfo.ALL_GOOD, report.toJSON(new HTMLReportMetadata(), new ArrayList<>()).toString());
    }

    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        throw new UnsupportedOperationException();
    }
}
