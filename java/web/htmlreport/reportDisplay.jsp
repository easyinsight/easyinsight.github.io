<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.export.ExportService" %>
<%@ page import="com.easyinsight.analysis.*" %>
<%@ page import="com.easyinsight.export.DeliveryScheduledTask" %>
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
                String userName = (String) session.getAttribute("userName");
                Long userID = (Long) session.getAttribute("userID");
                Long accountID = (Long) session.getAttribute("accountID");
                Integer accountType = (Integer) session.getAttribute("accountType");
                Integer dayOfWeek = (Integer) session.getAttribute("dayOfWeek");
                if (dayOfWeek == null) {
                    dayOfWeek = 1;
                }
                com.easyinsight.security.SecurityUtil.populateThreadLocal(userName, userID,
                        accountID, accountType, false, dayOfWeek, null);

                EIConnection conn = Database.instance().getConnection();
                try {
                    long reportID = Long.parseLong(request.getParameter("reportID"));
                    com.easyinsight.analysis.InsightResponse insightResponse = new AnalysisService().openAnalysisIfPossibleByID(reportID);
                    if (insightResponse.getStatus() == com.easyinsight.analysis.InsightResponse.SUCCESS) {
                        WSAnalysisDefinition report = new AnalysisService().openAnalysisDefinition(insightResponse.getInsightDescriptor().getId());
                        InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
                        String html = DeliveryScheduledTask.createHTMLTable(conn, report, insightRequestMetadata, true);
                        out.println(html);
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