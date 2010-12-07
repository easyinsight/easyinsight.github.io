<%@ page import="com.easyinsight.datafeeds.FeedService" %>
<%@ page import="com.easyinsight.datafeeds.FeedDescriptor" %>
<%@ page import="java.util.List" %>
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
                if (session == null) {
                    out.println("<li>No session found</li>");
                } else if (session.getAttribute("userName") == null) {
                    out.println("<li>Apparently none of our data in the session</li>");
                } else {
                    com.easyinsight.security.SecurityUtil.populateThreadLocal((String) session.getAttribute("userName"), (Long) session.getAttribute("userID"),
                             (Long) session.getAttribute("accountID"), (Integer) session.getAttribute("accountType"), false, false);
                    try {
                        java.util.List<com.easyinsight.datafeeds.FeedDescriptor> dataSources = new com.easyinsight.datafeeds.FeedService().searchForSubscribedFeeds();
                        for (com.easyinsight.datafeeds.FeedDescriptor dataSource : dataSources) {
                            out.println("<li><a href=\"reports.jsp?dataSourceID=" + dataSource.getId() + "\">" + dataSource.getName() + "</a></li>");
                        }
                        if (dataSources.size() == 0) {
                            out.println("<li>No Data sources!</li>");
                        }
                    } finally {
                        com.easyinsight.security.SecurityUtil.clearThreadLocal();
                    }
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