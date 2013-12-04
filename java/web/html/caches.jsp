<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.core.DataSourceDescriptor" %>
<%@ page import="com.easyinsight.core.EIDescriptor" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.Collections" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="com.easyinsight.export.ExportService" %>
<%@ page import="com.easyinsight.analysis.AnalysisDateDimension" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Data Sources</title>
    <jsp:include page="bootstrapHeader.jsp"/>
</head>
<body>
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {


%>
<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.DATA_SOURCES_AND_REPORTS %>"/>
</jsp:include>
<div class="container corePageWell">
    <div class="row">
        <div class="col-md-12">
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th>Report Name</th>
                        <th>Last Run</th>
                        <th>Scheduled to Run</th>
                        <th>Used In</th>
                    </tr>
                </thead>

            <%
                int accountTier = SecurityUtil.getAccountTier();

                DateFormat dateFormat = ExportService.getDateFormat(AnalysisDateDimension.MINUTE_LEVEL, null);
                if (dateFormat == null) {
                    throw new RuntimeException();
                }
                EIConnection conn = Database.instance().getConnection();
                try {

                    PreparedStatement ps;
                    if (accountTier == Account.ADMINISTRATOR) {
                        ps = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID, FEED_NAME, LAST_REFRESH_START, ANALYSIS_ID, ANALYSIS.TITLE FROM DATA_FEED, cached_addon_report_source, analysis WHERE " +
                                "cached_addon_report_source.data_source_id = data_feed.data_feed_id AND cached_addon_report_source.report_id = analysis.analysis_id");
                    } else {
                        ps = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID, FEED_NAME, LAST_REFRESH_START, ANALYSIS_ID, ANALYSIS.TITLE FROM DATA_FEED, cached_addon_report_source, analysis, upload_policy_users, user WHERE " +
                                "cached_addon_report_source.data_source_id = data_feed.data_feed_id AND data_feed.data_feed_id = upload_policy_users.feed_id and " +
                                "upload_policy_users.user_id = user.user_id and user.account_id = ? AND cached_addon_report_source.report_id = analysis.analysis_id");
                        ps.setLong(1, SecurityUtil.getAccountID());
                    }

                    PreparedStatement findStmt = conn.prepareStatement("SELECT min(cache_time) FROM cache_to_rebuild WHERE data_source_id = ?");
                    PreparedStatement findUsesStmt = conn.prepareStatement("SELECT report_to_report_stub.report_id, analysis.title, analysis.url_key FROM " +
                            "report_to_report_stub, report_stub, analysis WHERE " +
                            "report_stub.report_id = ? AND " +
                            "report_stub.report_stub_id = report_to_report_stub.report_stub_id AND report_to_report_stub.report_id = analysis.analysis_id");

                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        long id = rs.getLong(1);
                        Date lastRefreshStart = rs.getTimestamp(3);
                        long baseReportID = rs.getLong(4);
                        String baseReportName = rs.getString(5);
                        findStmt.setLong(1, id);
                        ResultSet cacheRS = findStmt.executeQuery();
                        Date cacheTime = null;
                        if (cacheRS.next()) {
                            cacheTime = cacheRS.getTimestamp(1);
                        }
                        findUsesStmt.setLong(1, baseReportID);
                        ResultSet usedRS = findUsesStmt.executeQuery();
                        StringBuilder sb = new StringBuilder();
                        while (usedRS.next()) {
                            long reportID = usedRS.getLong(1);
                            String reportName = usedRS.getString(2);
                            String urlKey = usedRS.getString(3);
                            sb.append("<a href=\"").append(RedirectUtil.getURL(request, "/app/html/report/" + urlKey)).append("\">").append(reportName).append("</a>");
                            sb.append(",");
                        }
                        if (sb.length() > 0) {
                            sb.deleteCharAt(sb.length() - 1);
                        }
            %>
                <tr>
                    <td>
                        <%= baseReportName %>
                    </td>
                    <td>
                        <%= lastRefreshStart == null ? "" : dateFormat.format(lastRefreshStart) %>
                    </td>
                    <td>
                        <%= cacheTime == null ? "" : dateFormat.format(cacheTime) %>
                    </td>
                    <td>
                        <%= sb.toString() %>
                    </td>
                </tr>
                <%
                    }
                } catch (Exception e) {
                    LogClass.error(e);
                } finally {
                    Database.closeConnection(conn);
                }
            %>
            </table>
        </div>
    </div>
</div>
</body>
<%
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>
</html>