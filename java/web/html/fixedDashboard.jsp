<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.datafeeds.FeedStorage" %>
<%@ page import="com.easyinsight.core.DataSourceDescriptor" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page import="com.easyinsight.analysis.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.easyinsight.html.*" %>
<%@ page import="com.easyinsight.dashboard.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<%

    String userName = null;
    if(session.getAttribute("userName") != null) {
        userName = (String) session.getAttribute("userName");
        com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    }
    try {

        String dashboardIDString = request.getParameter("dashboardID");
        long dashboardID = new DashboardService().canAccessDashboard(dashboardIDString);

        Dashboard dashboard = new DashboardService().getDashboardView(dashboardID);

        FilterHTMLMetadata filterHTMLMetadata = new FilterHTMLMetadata(dashboard, request, null, false);
        DataSourceDescriptor dataSourceDescriptor = new FeedStorage().dataSourceURLKeyForDataSource(dashboard.getDataSourceID());
        UIData uiData = Utils.createUIData();

%>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight</title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <jsp:include page="reportDashboardHeader.jsp"/>
    <script type="text/javascript">
        var dashboardJSON = <%= dashboard.toJSON(filterHTMLMetadata) %>;
        function afterRefresh() {

        }
    </script>
    <script type="text/javascript" src="/js/dashboard.js"></script>
</head>
<body>
<nav class="navbar_first navbar navbar-static-top navbar-inverse fixed_dashboard_navbar" role="navigation">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
    </div>
    <div class="collapse navbar-collapse navbar-ex1-collapse">
        <ul class="nav navbar-nav navbar-right fixed_dashboard_navbar_toggle">
            <li>
                <a href="#" class="dropdown-toggle" data-toggle="dropdown"><!--<button class="btn btn-sm btn-default">-->
                    <i class="icon-user"></i> <%= StringEscapeUtils.escapeHtml(userName) %>
                    <span class="caret"></span>
                    <!--</button>-->
                </a>
                <ul class="dropdown dropdown-menu">
                    <li><a href="/app/logoutAction.jsp">Sign Out</a></li>
                </ul>
            </li>
        </ul>
    </div>
</nav>

<div class="container">
    <%= uiData.createHeader(dashboard.getName(), dashboard.findHeaderImage()) %>
    <jsp:include page="refreshingDataSource.jsp"/>
    <div id="base"/>
</div>


</body>
<%
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>
</html>