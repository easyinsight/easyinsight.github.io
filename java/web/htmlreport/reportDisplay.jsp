<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.export.ExportService" %>
<%@ page import="com.easyinsight.analysis.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>Easy Insight Mobile</title>
	<link rel="stylesheet" href="/css/jquery.mobile-1.0a1.min.css" />
	<script src="/js/jquery-1.4.3.min.js"></script>
	<script src="/js/jquery.mobile-1.0a1.min.js"></script>

</head>
<body>

<div data-role="page" id="page-report-display">

	<div data-role="header">
		<h1>Easy Insight Mobile</h1>
	</div><!-- /header -->

	<div data-role="content" id="blah">
        <%

            if (session == null) {
                response.sendRedirect("index.jsp");
            } else if (session.getAttribute("accountID") == null) {
                response.sendRedirect("index.jsp");
            } else {
                com.easyinsight.security.SecurityUtil.populateThreadLocal((String) session.getAttribute("userName"), (Long) session.getAttribute("userID"),
                         (Long) session.getAttribute("accountID"), (Integer) session.getAttribute("accountType"), false, false, 1);

                EIConnection conn = Database.instance().getConnection();
                try {
                    long reportID = Long.parseLong(request.getParameter("reportID"));
                    com.easyinsight.analysis.InsightResponse insightResponse = new AnalysisService().openAnalysisIfPossibleByID(reportID);
                    if (insightResponse.getStatus() == com.easyinsight.analysis.InsightResponse.SUCCESS) {
                        WSAnalysisDefinition report = new AnalysisService().openAnalysisDefinition(insightResponse.getInsightDescriptor().getId());
                        if (report.getReportType() == WSAnalysisDefinition.LIST) {
                            ListDataResults dataResults = (ListDataResults) new DataService().list(report, new InsightRequestMetadata());
                            out.println(ExportService.toTable(report, dataResults, conn));
                        } else {
                            session.setAttribute("report", report);
                            out.println("<div id=\"reportImage\"/>");
                            //out.println("<img src=\"/app/htmlimage\" alt=\"" + report.getName() + "\"/>");
                        }
                    }
                } finally {
                    Database.closeConnection(conn);
                }
            }
            SecurityUtil.clearThreadLocal();
        %>

	</div><!-- /content -->

	<div data-role="footer">
		<h4>Easy Insight Mobile</h4>
	</div><!-- /footer -->
</div><!-- /page -->

</body>
</html>