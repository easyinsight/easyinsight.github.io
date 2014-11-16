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
<%@ page import="java.sql.Connection" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.admin.AdminService" %>
<%@ page import="com.easyinsight.audit.ActionDashboardLog" %>
<%@ page import="org.json.JSONException" %>
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

        boolean phone = Utils.isPhone(request);
        boolean iPad = Utils.isTablet(request);
        boolean designer = Utils.isDesigner();

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

        try {
            new AdminService().logAction(new ActionDashboardLog(SecurityUtil.getUserID(false), ActionDashboardLog.VIEW, dashboard.getId()));
        } catch (Exception e) {
            LogClass.error(e);
        }

        FilterHTMLMetadata filterHTMLMetadata = new FilterHTMLMetadata(dashboard, request, drillthroughKey, false);
        DataSourceDescriptor dataSourceDescriptor = new FeedStorage().dataSourceURLKeyForDataSource(dashboard.getDataSourceID());
        UIData uiData = Utils.createUIData();
        dashboard.getFilters().addAll(drillthroughFilters);
        JSONObject jo = dashboard.toJSON(filterHTMLMetadata);
        jo.put("data_source_id", (Object) dataSourceDescriptor.getUrlKey());
        if(drillthroughKey != null) {
            jo.put("drillthroughID", (Object) drillthroughKey);
        }
        jo.put("configuration_name", (Object) selectedConfiguration);
        jo.put("configuration_key", (Object) configurationKey);
        EIConnection c = Database.instance().getConnection();

        JSONObject userObject = new JSONObject();
        try {
            userObject = SecurityUtil.getUserJSON(c, request);
        } finally {
            Database.closeConnection(c);
        }

        boolean onlyTopReports = false;
        try {
            onlyTopReports = userObject.getBoolean("onlyTopReports");
        } catch (Exception e) {
        }

%>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title><%= StringEscapeUtils.escapeHtml(dashboard.getName())%></title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <jsp:include page="reportDashboardHeader.jsp"/>
    <script type="text/javascript">
        var dashboardJSON = <%= jo %>;
        var userJSON = <%= userObject %>;
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
    <div class="container-fluid">
        <div class="row controlsRow">

            <div class="col-md-4 reportBlah">
                <% if (!onlyTopReports) { %>
                <a class="reportControl visible-sm visible-md visible-lg" href="/a/data_sources/<%= dataSourceDescriptor.getUrlKey() %>">Back to <%= StringEscapeUtils.escapeHtml(dataSourceDescriptor.getName())%></a>
                <% } %>
            </div>
            <div class="col-md-8 col-xs-12 reportControlToolbar">

                <div class="reportBlah visible-xs pull-left" style="margin-top: 0">
                    <a class="reportControl" href="/a/data_sources/<%= dataSourceDescriptor.getUrlKey() %>">Reports</a>
                </div>

                <div class="btn-toolbar pull-right">
                    <div id="configuration-dropdown" class="btn-group reportControlBtnGroup visible-sm visible-md visible-lg">


                    </div>
                    <div class="btn-group reportControlBtnGroup">
                        <a class="reportControl visible-sm visible-md visible-lg" data-toggle="dropdown" href="#">
                            Export the Dashboard
                            <span class="caret"></span>
                        </a>
                        <a class="reportControl visible-xs" data-toggle="dropdown" href="#">
                            Export
                            <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <a class="export_dashboard_pdf">Export to PDF</a>
                            </li>
                            <li>
                                <a class="embed_dashboard">Embed the Dashboard</a>
                            </li>
                        </ul>
                    </div>
                    <div class="btn-group reportControlBtnGroup">
                        <a class="reportControl dropdown-toggle visible-sm visible-md visible-lg" data-toggle="dropdown" href="#">
                            Refresh Data
                            <span class="caret"></span>
                        </a>
                        <a class="reportControl dropdown-toggle visible-xs" data-toggle="dropdown" href="#">
                            Refresh
                            <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <a class="full_refresh">Refresh the Dashboard</a>
                            </li>
                            <li>
                                <a onclick="refreshDataSource('<%= dataSourceDescriptor.getUrlKey() %>')" id="refreshDataSourceButton">Refresh the Data Source</a>
                            </li>
                        </ul>
                    </div>
                    <div class="btn-group reportControlBtnGroup">
                        <a class="reportControl toggle-filters visible-sm visible-md visible-lg">Toggle Filters</a>
                        <a class="reportControl toggle-filters visible-xs">Filters</a>
                    </div>
                    <% if (designer && !iPad && !phone) { %>
                    <div class="btn-group reportControlBtnGroup visible-sm visible-md visible-lg" style="margin-right: 5px">
                        <a href="<%= RedirectUtil.getURL(request, "/app/html/dashboard/" + dashboard.getUrlKey()) + "/edit" %>"
                           class="reportControl">Edit Dashboard</a>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</div>

<%--<% if (!tipsEnabled) { %>
<div class="container">
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="alert alert-tutorial alert-dismissible" style="font-size:16px" role="alert">
                <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <p>Congratulations, you've installed your connection to <%=StringEscapeUtils.escapeHtml(dataSourceDescriptor.getName())%>! What you see below is the default dashboard. You can click on the different tabs to navigate through the prebuilt reports. You can also click on values in the reports to drill into details. When you're done checking out the reports, click on the <%= StringEscapeUtils.escapeHtml(dataSourceDescriptor.getName())%> link in the upper left.</p><p>Need more help? Check out <a href="https://www.easy-insight.com/app/docScreencasts.jsp" class="alert-link">screencasts</a>, <a href="#" class="alert-link">help</a>, or contact us at <strong>support@easy-insight.com</strong> or <strong>1-720-316-8174</strong>.
            </div>
        </div>
    </div>
</div>
<% } %>--%>

<div class="container-fluid">
<%--    <%= uiData.createHeader(dashboard.getName(), dashboard.findHeaderImage()) %>--%>
    <jsp:include page="refreshingDataSource.jsp"/>
    <jsp:include page="embedDashboardWindow.jsp"/>
    <jsp:include page="modalIndicator.jsp"/>
    <div id="base"/>
</div>


</body>
<%
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>
</html>