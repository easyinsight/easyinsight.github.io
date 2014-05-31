package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.ReportKey;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 11/21/13
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReportMarkdownServlet extends HttpServlet {

    private String trace(AnalysisItem analysisItem, Key key, EIConnection conn, Feed baseFeed) throws SQLException {
        StringBuilder sb = new StringBuilder();
        if (key instanceof DerivedKey) {
            DerivedKey derivedKey = (DerivedKey) key;
            Feed feed = FeedRegistry.instance().getFeed(derivedKey.getFeedID(), conn);
            String dataSourceName = feed.getName();
            sb.append("From data source '''").append(dataSourceName).append("'''\r\n\r\n");
            sb.append(trace(analysisItem, derivedKey.getParentKey(), conn, feed));
            // find the data source name
        } else if (key instanceof ReportKey) {
            ReportKey reportKey = (ReportKey) key;
            WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportKey.getReportID(), conn);
            Feed feed = FeedRegistry.instance().getFeed(report.getDataFeedID(), conn);
            String reportName = report.getName();
            sb.append("From report ").append(reportName).append("\r\n\r\n");
            sb.append(trace(analysisItem, reportKey.getParentKey(), conn, feed));
            // find the report
        } else if (key instanceof NamedKey) {
            NamedKey namedKey = (NamedKey) key;

            AnalysisItem baseField = null;
            if (baseFeed instanceof AnalysisBasedFeed) {
                WSAnalysisDefinition r = ((AnalysisBasedFeed) baseFeed).getAnalysisDefinition();
                List<AnalysisItem> fields = new ArrayList<>(r.createStructure().values());
                for (AnalysisItem item : fields) {
                    if (item.isConcrete() && item.getKey().toKeyString().equals(namedKey.toKeyString())) {
                        baseField = item;
                        break;
                    }
                }
            } else {
                for (AnalysisItem item : baseFeed.getFields()) {
                    if (item.isConcrete() && item.getKey().toKeyString().equals(namedKey.toKeyString())) {
                        baseField = item;
                        break;
                    }
                }
            }
            if (baseField == null) {
                sb.append("From key ").append(namedKey.getName()).append("\r\n\r\n");
            } else {
                if (baseFeed.getDataSource() instanceof DistinctCachedSource) {
                    sb.append("From data source field '''").append(baseField.toDisplay()).append("''' on report source '''").append(baseFeed.getName()).append("''' with key '''").append(namedKey.getName()).append("'''\r\n\r\n");
                } else {
                    sb.append("From data source field '''").append(baseField.toDisplay()).append("''' on '''").append(baseFeed.getName()).append("''' with key '''").append(namedKey.getName()).append("'''\r\n\r\n");
                }
            }
            if (baseFeed.getDataSource() instanceof DistinctCachedSource) {
                DistinctCachedSource distinctCachedSource = (DistinctCachedSource) baseFeed.getDataSource();
                Long basedOn = analysisItem.getBasedOnReportField();
                if (basedOn != null) {
                    AnalysisBasedFeed cachedFeed = new AnalysisBasedFeed();
                    WSAnalysisDefinition r = new AnalysisStorage().getAnalysisDefinition(distinctCachedSource.getReportID(), conn);
                    cachedFeed.setAnalysisDefinition(r);
                    List<AnalysisItem> fields = new ArrayList<>(r.createStructure().values());
                    for (AnalysisItem field : fields) {
                        if (basedOn == field.getAnalysisItemID()) {
                            sb.append("From report field '''" + field.toDisplay() + "''' on report '''" + r.getName() + "'''\r\n\r\n");
                            sb.append(trace(field, field.getKey(), conn, cachedFeed));
                            break;
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SecurityUtil.populateThreadLocalFromSession(req);
        try {
            String reportIDString = req.getParameter("urlKey");
            JSONObject jo = new JSONObject();
            InsightResponse insightResponse = new AnalysisService().openAnalysisIfPossible(reportIDString);
            Long reportID = insightResponse.getInsightDescriptor().getId();

            EIConnection conn = Database.instance().getConnection();
            List<JSONObject> fields = new ArrayList<>();
            try {
                /*markdownBuilder.append("=='''Report Fields'''==\r\n\r\n");*/
                WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
                jo.put("reportName", report.getName());
                jo.put("reportID", report.getUrlKey());
                Feed feed = FeedRegistry.instance().getFeed(report.getDataFeedID(), conn);
                InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
                DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);
                List<AnalysisItem> addedItems = report.allAddedItems(insightRequestMetadata);
                for (AnalysisItem item : report.getAllAnalysisItems()) {

                    JSONObject fieldObject = new JSONObject();
                    fieldObject.put("id", reportIDString + "-" + item.toDisplay());
                    fieldObject.put("name", item.toDisplay());
                    String whereFrom = trace(item, item.getKey(), conn, feed);
                    fieldObject.put("whereFrom", HTMLPolicy.getPolicyFactory().sanitize(DocReader.parseMediaWiki(whereFrom)));
                    fieldObject.put("whatIsField", HTMLPolicy.getPolicyFactory().sanitize(DocReader.parseMediaWiki(item.toMarkdown(report.getDataFeedID(), addedItems, report, conn, feed))));
                    fields.add(fieldObject);
                    // where does the item come from?

                    // item name
                    // where does the data for this item come from?
                    // what does this item consist of?
                    // what uses this item?

                    //markdownBuilder.append(trace(item.getKey(), conn, feed));

                    /*markdownBuilder.append(item.toMarkdown(report.getDataFeedID(), addedItems, report, conn, feed));
                    List<String> audits = insightRequestMetadata.getFieldAudits().get(item.toDisplay());
                    if (audits != null) {
                        markdownBuilder.append("Events related to field on running report:\r\n\r\n");
                        for (String audit : audits) {
                            markdownBuilder.append("#").append(audit).append("\r\n\r\n");
                        }
                    }*/
                    /*markdownBuilder.append("----\r\n\r\n");*/
                }
                /*markdownBuilder.append("=='''Additional Fields'''==\r\n\r\n");
                for (AnalysisItem item : report.getAddedItems()) {
                    markdownBuilder.append(item.toMarkdown(report.getDataFeedID(), addedItems, report, conn, feed));
                    List<String> audits = insightRequestMetadata.getFieldAudits().get(item.toDisplay());
                    if (audits != null) {
                        markdownBuilder.append("Events related to field on running report:\r\n\r\n");
                        for (String audit : audits) {
                            markdownBuilder.append("#").append(audit).append("\r\n\r\n");
                        }
                    }
                    markdownBuilder.append("----\r\n\r\n");
                }*/
            } finally {
                Database.closeConnection(conn);
            }

            try {

                jo.put("fields", fields);
                resp.setContentType("application/json");
                resp.getOutputStream().write(jo.toString().getBytes());
                resp.getOutputStream().flush();
            } catch (JSONException e) {
                LogClass.error(e);
            }
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            SecurityUtil.clearThreadLocal();
        }

    }
}
