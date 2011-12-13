<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.easyinsight.export.ExportService" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page import="com.easyinsight.analysis.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
	<title>Easy Insight Mobile</title>
	<link rel="stylesheet" href="/css/jquery.mobile-1.0a1.min.css" />
	<script src="/js/jquery-1.4.3.min.js"></script>
	<script src="/js/jquery.mobile-1.0a1.min.js"></script>
</head>
<body>

<div data-role="page">

	<div data-role="header">
		<h1>Easy Insight Mobile</h1>
	</div><!-- /header -->

	<div data-role="content">
        <%
            if (session == null) {
                response.sendRedirect("index.jsp");
            } else if (session.getAttribute("accountID") == null) {
                response.sendRedirect("index.jsp");
            } else {
                try {
                    com.easyinsight.security.SecurityUtil.populateThreadLocal((String) session.getAttribute("userName"), (Long) session.getAttribute("userID"),
                            (Long) session.getAttribute("accountID"), (Integer) session.getAttribute("accountType"), false, (Integer) session.getAttribute("dayOfWeek"), null);
                    try {
                        EIConnection conn = Database.instance().getConnection();
                        try {
                            out.println(ExportService.exportScorecard(Long.parseLong(request.getParameter("scorecardID")), new InsightRequestMetadata(), conn));
                        } finally {
                            Database.closeConnection(conn);
                        }
                    } finally {
                        SecurityUtil.clearThreadLocal();
                    }
                } catch (Exception e) {
                    com.easyinsight.logging.LogClass.error(e);
                }
            }
        %>

	</div><!-- /content -->

	<div data-role="footer">
		<h4>Easy Insight Mobile</h4>
	</div><!-- /footer -->
</div><!-- /page -->

</body>
</html>