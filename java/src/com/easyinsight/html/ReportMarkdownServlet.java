package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.documentation.DocReader;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.util.HTMLPolicy;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 11/21/13
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReportMarkdownServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SecurityUtil.populateThreadLocalFromSession(req);
        try {
            String reportIDString = req.getParameter("urlKey");
            InsightResponse insightResponse = new AnalysisService().openAnalysisIfPossible(reportIDString);
            Long reportID = insightResponse.getInsightDescriptor().getId();
            StringBuilder markdownBuilder = new StringBuilder();
            EIConnection conn = Database.instance().getConnection();
            try {
                markdownBuilder.append("=='''Report Fields'''==\r\n\r\n");
                WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
                Feed feed = FeedRegistry.instance().getFeed(report.getDataFeedID(), conn);
                InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
                DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);
                for (AnalysisItem item : report.getAllAnalysisItems()) {
                    markdownBuilder.append(item.toMarkdown(report.getDataFeedID(), report.getAddedItems(), report, conn, feed));
                    List<String> audits = insightRequestMetadata.getFieldAudits().get(item.toDisplay());
                    if (audits != null) {
                        markdownBuilder.append("Events related to field on running report:\r\n\r\n");
                        for (String audit : audits) {
                            markdownBuilder.append("#").append(audit).append("\r\n\r\n");
                        }
                    }
                    markdownBuilder.append("----\r\n\r\n");
                }
                markdownBuilder.append("=='''Additional Fields'''==\r\n\r\n");
                for (AnalysisItem item : report.getAddedItems()) {
                    markdownBuilder.append(item.toMarkdown(report.getDataFeedID(), report.getAddedItems(), report, conn, feed));
                    List<String> audits = insightRequestMetadata.getFieldAudits().get(item.toDisplay());
                    if (audits != null) {
                        markdownBuilder.append("Events related to field on running report:\r\n\r\n");
                        for (String audit : audits) {
                            markdownBuilder.append("#").append(audit).append("\r\n\r\n");
                        }
                    }
                    markdownBuilder.append("----\r\n\r\n");
                }
            } finally {
                Database.closeConnection(conn);
            }
            String html = DocReader.parseMediaWiki(markdownBuilder.toString());
            html = HTMLPolicy.getPolicyFactory().sanitize(html);
            try {
                JSONObject jo = new JSONObject();
                jo.put("markdown", html);
                resp.setContentType("application/json");
                resp.getOutputStream().write(jo.toString().getBytes());
                resp.getOutputStream().flush();
            } catch (JSONException e) {
                LogClass.error(e);
            }
        } finally {
            SecurityUtil.clearThreadLocal();
        }

    }
}
