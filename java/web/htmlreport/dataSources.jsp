<%@ page import="com.easyinsight.core.DataSourceDescriptor" %>
<%@ page import="java.util.Collections" %>
<%@ page import="com.easyinsight.core.EIDescriptor" %>
<%@ page import="java.util.Comparator" %>
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
        <ul data-role="listview">
            <%
                if (session == null) {
                    response.sendRedirect("index.jsp");
                } else if (session.getAttribute("accountID") == null) {
                    response.sendRedirect("index.jsp");
                } else {
                    com.easyinsight.security.SecurityUtil.populateThreadLocal((String) session.getAttribute("userName"), (Long) session.getAttribute("userID"),
                            (Long) session.getAttribute("accountID"), (Integer) session.getAttribute("accountType"), false, (Integer) session.getAttribute("dayOfWeek"), null);
                    try {
                        java.util.List<DataSourceDescriptor> dataSources = new com.easyinsight.datafeeds.FeedService().searchForSubscribedFeeds();
                        Collections.sort(dataSources, new Comparator<EIDescriptor>() {

                            public int compare(EIDescriptor eiDescriptor, EIDescriptor eiDescriptor1) {
                                String name1 = eiDescriptor.getName() != null ? eiDescriptor.getName() : "";
                                String name2 = eiDescriptor1.getName() != null ? eiDescriptor1.getName() : "";
                                return name1.compareTo(name2);
                            }
                        });
                        for (DataSourceDescriptor dataSource : dataSources) {
                            out.println("<li><a href=\"reports.jsp?dataSourceID=" + dataSource.getId() + "\">" + dataSource.getName() + "</a></li>");
                        }
                        if (dataSources.size() == 0) {
                            out.println("<li>You haven't defined any data sources yet.</li>");
                        }
                    } finally {
                        com.easyinsight.security.SecurityUtil.clearThreadLocal();
                    }
                }
            %>    
        </ul>

	</div><!-- /content -->

	<div data-role="footer">
		<h4>Easy Insight Mobile</h4>
	</div><!-- /footer -->
</div><!-- /page -->

</body>
</html>