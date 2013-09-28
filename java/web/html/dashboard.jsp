<!DOCTYPE html>
<%@ page import="com.easyinsight.dashboard.DashboardService" %>
<%@ page import="com.easyinsight.dashboard.Dashboard" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.datafeeds.FeedStorage" %>
<%@ page import="com.easyinsight.dashboard.DashboardUIProperties" %>
<%@ page import="com.easyinsight.core.DataSourceDescriptor" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page import="com.easyinsight.analysis.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.easyinsight.html.*" %>
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
        Dashboard dashboard = new DashboardService().getDashboard(dashboardID);
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
<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.DATA_SOURCES_AND_REPORTS %>"/>
</jsp:include>
<div class="navbar">
    <div class="navbar-inner reportNavBarInner">
        <div class="container">
            <div class="span6">
                <ul class="breadcrumb reportBreadcrumb">

                    <li><a href="/app/html/">Data Sources</a> <span class="divider">/</span></li>
                    <li>
                        <a href="/app/html/reports/<%= dataSourceDescriptor.getUrlKey() %>"><%= StringEscapeUtils.escapeHtml(dataSourceDescriptor.getName())%>
                        </a><span class="divider">/</span></li>
                    <li class="active"><%= StringEscapeUtils.escapeHtml(dashboard.getName()) %>
                    </li>

                </ul>
            </div>
            <div class="span6">
                <div class="btn-toolbar pull-right" style="padding-top: 0;margin-top: 0">
                    <div class="btn-group">

                        <a class="btn btn-inverse dropdown-toggle" data-toggle="dropdown" href="#">
                            Refresh Data
                            <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <button class="btn btn-inverse" type="button" onclick="refreshReport()"
                                        style="padding:5px;margin:5px;width:150px">Refresh the Report
                                </button>
                            </li>
                            <li>
                                <button class="btn btn-inverse" type="button" id="refreshDataSourceButton"
                                        onclick="refreshDataSource()" style="padding:5px;margin:5px;width:150px">Refresh
                                    Data Source
                                </button>
                            </li>
                        </ul>
                    </div>
                    <div class="btn-group">
                        <button class="btn btn-inverse toggle-filters">Toggle Filters</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="container-fluid">
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