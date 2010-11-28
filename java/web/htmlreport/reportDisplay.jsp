<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.easyinsight.analysis.AnalysisService" %>
<%@ page import="com.easyinsight.analysis.WSAnalysisDefinition" %>
<%@ page import="com.easyinsight.export.ExportService" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<!DOCTYPE html>
<html>
	<head>
	<title>Easy Insight Mobile</title>
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.0a1/jquery.mobile-1.0a1.min.css" />
	<script src="http://code.jquery.com/jquery-1.4.3.min.js"></script>
	<script src="http://code.jquery.com/mobile/1.0a1/jquery.mobile-1.0a1.min.js"></script>
</head>
<body>

<div data-role="page">

	<div data-role="header">
		<h1>Easy Insight Mobile</h1>
	</div><!-- /header -->

	<div data-role="content">
        <%
            try {
                com.easyinsight.security.SecurityUtil.populateThreadLocal((String) session.getAttribute("userName"), (Long) session.getAttribute("userID"),
                         (Long) session.getAttribute("accountID"), (Integer) session.getAttribute("accountType"), false, false);
                try {
                    EIConnection conn = Database.instance().getConnection();
                    int dateFormat = 0;
                    try {
                        PreparedStatement dateFormatStmt = conn.prepareStatement("SELECT DATE_FORMAT FROM ACCOUNT WHERE ACCOUNT_ID = ?");
                        dateFormatStmt.setLong(1, SecurityUtil.getAccountID());
                        ResultSet rs = dateFormatStmt.executeQuery();
                        rs.next();
                        dateFormat = rs.getInt(1);
                    } finally {
                        Database.closeConnection(conn);
                    }


                    com.easyinsight.analysis.InsightResponse insightResponse = new AnalysisService().openAnalysisIfPossibleByID(Long.parseLong(request.getParameter("reportID")));
                    if (insightResponse.getStatus() == com.easyinsight.analysis.InsightResponse.SUCCESS) {
                        WSAnalysisDefinition report = new AnalysisService().openAnalysisDefinition(insightResponse.getInsightDescriptor().getId());
                        if (report.getReportType() == WSAnalysisDefinition.LIST) {
                            out.println(ExportService.toTable(report, dateFormat));
                        } else {
                            out.println("<p>To be implemented...</p>");
                        }
                    }
                } finally {
                    SecurityUtil.clearThreadLocal();
                }
            } catch (Exception e) {
                com.easyinsight.logging.LogClass.error(e);
            }
        %>

	</div><!-- /content -->

	<div data-role="footer">
		<h4>Page Footer</h4>
	</div><!-- /footer -->
</div><!-- /page -->

</body>
</html>