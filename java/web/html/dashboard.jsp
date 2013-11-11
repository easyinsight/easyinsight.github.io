<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.datafeeds.FeedStorage" %>
<%@ page import="com.easyinsight.core.DataSourceDescriptor" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page import="com.easyinsight.analysis.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.easyinsight.html.*" %>
<%@ page import="org.json.JSONObject" %>
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

        String drillthroughKey = request.getParameter("drillthroughKey");
        List<FilterDefinition> drillthroughFilters = new ArrayList<FilterDefinition>();
        long dashboardID = -1;
        String savedDashboardIDString = request.getParameter("savedDashboardID");
        String configurationIDString = request.getParameter("dashboardConfig");
        String selectedConfiguration = null;
        String configurationKey = null;
        DashboardStackPositions positions = null;
        Dashboard dashboard;
        if (savedDashboardIDString != null) {
            DashboardInfo dashboardInfo = new DashboardService().retrieveFromDashboardLink(savedDashboardIDString);
            positions = dashboardInfo.getDashboardStackPositions();
            dashboardID = dashboardInfo.getDashboardID();
        } else if(configurationIDString != null) {
            DashboardInfo dashboardInfo = new DashboardService().getConfigurationForDashboard(configurationIDString);
            positions = dashboardInfo.getSavedConfiguration().getDashboardStackPositions();
            configurationKey = dashboardInfo.getSavedConfiguration().getUrlKey();
            selectedConfiguration = dashboardInfo.getSavedConfiguration().getName();
            dashboardID = dashboardInfo.getDashboardID();
        }

        if(drillthroughKey != null) {
            DrillThroughData drillThroughData = Utils.drillThroughFiltersForDashboard(drillthroughKey);
            drillthroughFilters = drillThroughData.getFilters();
            dashboardID = drillThroughData.getDashboardID();
            SecurityUtil.authorizeDashboard(dashboardID);

        } else if(request.getParameter("dashboardID") != null) {
            String dashboardIDString = request.getParameter("dashboardID");
            dashboardID = new DashboardService().canAccessDashboard(dashboardIDString);
        }

        dashboard = new DashboardService().getDashboardView(dashboardID, positions);



        FilterHTMLMetadata filterHTMLMetadata = new FilterHTMLMetadata(dashboard, request, drillthroughKey, false);
        DataSourceDescriptor dataSourceDescriptor = new FeedStorage().dataSourceURLKeyForDataSource(dashboard.getDataSourceID());
        UIData uiData = Utils.createUIData();
        dashboard.getFilters().addAll(drillthroughFilters);
        JSONObject jo = dashboard.toJSON(filterHTMLMetadata);
        if(drillthroughKey != null) {
            jo.put("drillthroughID", drillthroughKey);
        }
        jo.put("configuration_name", selectedConfiguration);
        jo.put("configuration_key", configurationKey);
%>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight</title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <jsp:include page="reportDashboardHeader.jsp"/>
    <script type="text/javascript">
        var dashboardJSON = <%= jo %>;
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
<div class="nav nav-pills reportNav">
        <div class="container">
            <div class="col-md-6 reportBlah">
                <a class="reportControl" href="/app/html/reports/<%= dataSourceDescriptor.getUrlKey() %>"><%= StringEscapeUtils.escapeHtml(dataSourceDescriptor.getName())%></a>
            </div>
            <div class="col-md-6 reportControlToolbar">
                <div class="btn-toolbar pull-right">
                    <div id="configuration-dropdown" class="btn-group reportControlBtnGroup">


                    </div>
                    <div class="btn-group reportControlBtnGroup">
                        <a class="reportControl dropdown-toggle" data-toggle="dropdown" href="#">
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
                                        onclick="refreshDataSource('<%= dataSourceDescriptor.getUrlKey() %>')" style="padding:5px;margin:5px;width:150px">Refresh
                                    Data Source
                                </button>
                            </li>
                        </ul>
                    </div>
                    <div class="btn-group reportControlBtnGroup">
                        <a class="reportControl toggle-filters">Toggle Filters</a>
                    </div>
                </div>
            </div>
        </div>
</div>

<div class="container">
<%--    <%= uiData.createHeader(dashboard.getName(), dashboard.findHeaderImage()) %>--%>
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