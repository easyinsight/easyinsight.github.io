<%@ page import="com.easyinsight.datafeeds.FeedService" %>
<%@ page import="com.easyinsight.datafeeds.FeedDescriptor" %>
<%@ page import="java.util.List" %>
<%@ page import="com.easyinsight.analysis.AnalysisService" %>
<%@ page import="com.easyinsight.core.InsightDescriptor" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
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
        <ul data-role="listview">
            <%
                com.easyinsight.security.SecurityUtil.populateThreadLocal((String) session.getAttribute("userName"), (Long) session.getAttribute("userID"),
                     (Long) session.getAttribute("accountID"), (Integer) session.getAttribute("accountType"), false, false, 1);
                try {
                    long dataSourceID = Long.parseLong(request.getParameter("dataSourceID"));
                    Collection<InsightDescriptor> reports = new AnalysisService().getInsightDescriptorsForDataSource(dataSourceID);
                    for (InsightDescriptor report : reports) {
                        out.println("<li><a href=\"reportDisplay.jsp?reportID=" + report.getId() + "\">" + report.getName() + "</a></li>");
                    }
                    if (reports.size() == 0) {
                        out.println("<li>Nothing!</li>");
                    }
                } finally {
                    com.easyinsight.security.SecurityUtil.clearThreadLocal();
                }
            %>
        </ul>

	</div><!-- /content -->

	<div data-role="footer">
		<h4>Page Footer</h4>
	</div><!-- /footer -->
</div><!-- /page -->

</body>
</html>