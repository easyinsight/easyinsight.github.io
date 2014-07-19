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
            c.close();
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
        <div class="container">
            <div class="col-md-4 reportBlah">
                <a class="reportControl" href="/a/data_sources/<%= dataSourceDescriptor.getUrlKey() %>"><%= StringEscapeUtils.escapeHtml(dataSourceDescriptor.getName())%></a>
            </div>
            <div class="col-md-8 reportControlToolbar">
                <div class="btn-toolbar pull-right">
                    <div id="configuration-dropdown" class="btn-group reportControlBtnGroup">


                    </div>
                    <div class="btn-group reportControlBtnGroup">
                        <a class="reportControl" data-toggle="dropdown" href="#">
                            Export the Dashboard
                            <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <button class="btn btn-inverse export_dashboard_pdf" type="button"
                                        style="padding:5px;margin:5px;width:150px">Export to PDF
                                </button>
                            </li>
                        </ul>
                    </div>
                    <div class="btn-group reportControlBtnGroup">
                        <a class="reportControl dropdown-toggle" data-toggle="dropdown" href="#">
                            Refresh Data
                            <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <button class="btn btn-inverse full_refresh" type="button"
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
                    <div class="btn-group reportControlBtnGroup">
                        <a class="reportControl toggle-filters">Toggle Filters</a>
                    </div>
                    <% if (designer && !iPad && !phone) { %>
                    <div class="btn-group">
                        <a href="<%= RedirectUtil.getURL(request, "/app/embeddedDashboardEditor.jsp?dashboardID=" + dashboard.getUrlKey())%>"
                           class="reportControl">Edit Dashboard</a>
                    </div>
                    <% } %>
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