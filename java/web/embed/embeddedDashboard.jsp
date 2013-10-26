<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.datafeeds.FeedStorage" %>
<%@ page import="com.easyinsight.core.DataSourceDescriptor" %>
<%@ page import="com.easyinsight.analysis.*" %>
<%@ page import="com.easyinsight.html.*" %>
<%@ page import="com.easyinsight.dashboard.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<%


    if(session.getAttribute("userName") != null) {
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
    <jsp:include page="../html/bootstrapHeader.jsp"/>
    <jsp:include page="../html/reportDashboardHeader.jsp"/>
    <script type="text/javascript">
        var dashboardJSON = <%= dashboard.toJSON(filterHTMLMetadata) %>;
        function afterRefresh() {

        }
    </script>
    <script type="text/javascript" src="/js/dashboard.js"></script>
</head>
<body>
<div class="nav nav-pills reportNav">
    <div class="container">
        <div class="col-md-6">
            <div class="btn-toolbar pull-right" style="padding-top: 0;margin-top: 0">
                <div class="btn-group">

                    <a class="btn btn-inverse dropdown-toggle" data-toggle="dropdown" href="#">
                        Refresh Data
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li>
                            <button class="btn btn-inverse" type="button" onclick="refreshReport()"
                                    style="padding:5px;margin:5px;width:150px">Refresh the Dashboard
                            </button>
                        </li>
                        <li>
                            <button class="btn btn-inverse" type="button" id="refreshDataSourceButton"
                                    onclick="refreshDataSource('<%= dataSourceDescriptor.getUrlKey() %>')" style="padding:5px;margin:5px;width:150px">Refresh
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

<div class="container">
    <%= uiData.createHeader(dashboard.getName(), dashboard.findHeaderImage()) %>
    <jsp:include page="../html/refreshingDataSource.jsp"/>
    <div id="base"/>
</div>


</body>
<%
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>
</html>