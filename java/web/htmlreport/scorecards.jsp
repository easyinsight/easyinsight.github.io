<%@ page import="com.easyinsight.scorecard.ScorecardService" %>
<%@ page import="com.easyinsight.scorecard.ScorecardDescriptor" %>
<%@ page import="java.util.List" %>
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
                        List<ScorecardDescriptor> scorecards = new ScorecardService().getScorecardDescriptors();
                        if (scorecards.size() == 0) {
                            out.println("You haven't defined any scorecards.");
                        } else {
                            for (ScorecardDescriptor scorecardDescriptor : scorecards) {
                                out.println("<li><a href=\"scorecard.jsp?scorecardID=" + scorecardDescriptor.getId() + "\">" + scorecardDescriptor.getName() + "</a></li>");
                            }
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