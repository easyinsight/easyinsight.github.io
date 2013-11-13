<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.datafeeds.FeedStorage" %>
<%@ page import="com.easyinsight.core.DataSourceDescriptor" %>
<%@ page import="com.easyinsight.analysis.*" %>
<%@ page import="com.easyinsight.html.*" %>
<%@ page import="com.easyinsight.dashboard.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<%


    if(session.getAttribute("userName") != null) {
        com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    }
    try {


        String drillthroughKey = request.getParameter("drillthroughKey");
        List<FilterDefinition> drillthroughFilters = new ArrayList<FilterDefinition>();
        long dashboardID = -1;
        String savedDashboardIDString = request.getParameter("savedDashboardID");
        Dashboard dashboard;
        if (savedDashboardIDString != null) {
            DashboardInfo dashboardInfo = new DashboardService().retrieveFromDashboardLink(savedDashboardIDString);
            DashboardStackPositions positions = dashboardInfo.getDashboardStackPositions();
            dashboardID = dashboardInfo.getDashboardID();
            dashboard = new DashboardService().getDashboardView(dashboardID, positions);
        } else if(drillthroughKey != null) {
            DrillThroughData drillThroughData = Utils.drillThroughFiltersForDashboard(drillthroughKey);
            drillthroughFilters = drillThroughData.getFilters();
            dashboardID = drillThroughData.getDashboardID();
            SecurityUtil.authorizeDashboard(dashboardID);
            dashboard = new DashboardService().getDashboard(dashboardID);
        } else {
            String dashboardIDString = request.getParameter("dashboardID");
            dashboardID = new DashboardService().canAccessDashboard(dashboardIDString);
            dashboard = new DashboardService().getDashboard(dashboardID);
        }
        dashboard.getFilters().addAll(drillthroughFilters);
        FilterHTMLMetadata filterHTMLMetadata = new FilterHTMLMetadata(dashboard, request, null, true);
        DataSourceDescriptor dataSourceDescriptor = new FeedStorage().dataSourceURLKeyForDataSource(dashboard.getDataSourceID());
        UIData uiData = Utils.createUIData();

        JSONObject jo = dashboard.toJSON(filterHTMLMetadata);
        if(drillthroughKey != null) {
            jo.put("drillthroughID", drillthroughKey);
        }
        EIConnection c = Database.instance().getConnection();
        JSONObject userObject = new JSONObject();
        try {
            userObject = SecurityUtil.getUserJSON(c, request);
        } finally {
            c.close();
        }
%>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight</title>
    <jsp:include page="../html/bootstrapHeader.jsp"/>
    <jsp:include page="../html/reportDashboardHeader.jsp"/>
    <script type="text/javascript">
        var dashboardJSON = <%= jo %>;
        var userJSON = <%= userObject %>;
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