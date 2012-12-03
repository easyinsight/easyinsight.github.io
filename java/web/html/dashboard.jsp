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
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        long dashboardID;
        List<FilterDefinition> drillthroughFilters = new ArrayList<FilterDefinition>();
        String drillthroughArgh = request.getParameter("drillthroughKey");
        if (drillthroughArgh != null) {
            DrillThroughData drillThroughData = Utils.drillThroughFiltersForDashboard(drillthroughArgh);
            drillthroughFilters = drillThroughData.getFilters();
            dashboardID = drillThroughData.getDashboardID();
        } else {
            String dashboardIDString = request.getParameter("dashboardID");
            dashboardID = new DashboardService().canAccessDashboard(dashboardIDString);
        }

        if (dashboardID == 0) {
            throw new ReportNotFoundException("Can't find the dashboard.");
        }
        Dashboard dashboard = new DashboardService().getDashboardView(dashboardID);
        DashboardUIProperties dashboardUIProperties = dashboard.findHeaderImage();

        DataSourceDescriptor dataSourceDescriptor = new FeedStorage().dataSourceURLKeyForDataSource(dashboard.getDataSourceID());
        Map<String, FilterDefinition> filterMap = new HashMap<String, FilterDefinition>();
        for (FilterDefinition filterDefinition : dashboard.getFilters()) {
            filterMap.put(filterDefinition.label(false), filterDefinition);
        }
        if (drillthroughFilters != null) {
            for (FilterDefinition filterDefinition : drillthroughFilters) {
                FilterDefinition replaced = filterMap.put(filterDefinition.label(false), filterDefinition);
                if (replaced != null) {
                    filterDefinition.setFilterID(replaced.getFilterID());
                }
            }
        }
        dashboard.setFilters(new ArrayList<FilterDefinition>(filterMap.values()));
        UIData uiData = Utils.createUIData();

%>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight &mdash; <%= StringEscapeUtils.escapeHtml(dashboard.getName()) %></title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <jsp:include page="reportDashboardHeader.jsp"/>

    <%
        Set<String> jsIncludes = new HashSet<String>(dashboard.getRootElement().jsIncludes());
        for (String jsInclude : jsIncludes) {
            %><%= "<script type=\"text/javascript\" src=\"" + jsInclude + "\"></script>"%><%
        }
        Set<String> cssIncludes = new HashSet<String>(dashboard.getRootElement().cssIncludes());
        for (String cssInclude : cssIncludes) {
            %><%= "<link rel=\"stylesheet\" type=\"text/css\" href=\""+cssInclude+"\" />"%><%
        }
    %>
    <script type="text/javascript">

        var initCount = 0;
        var requiredCount = <%= dashboard.requiredInitCount() %>;

        function updateInitCount(renderFunction) {
            initCount++;
            if (initCount == requiredCount) {
                reportReady = true;
                doneBuildingDashboard();
            } else if (initCount > requiredCount) {
                renderFunction();
            }
        }

        function doneBuildingDashboard() {
            refreshDashboard();
        }

        function refreshDashboard() {
            <%= dashboard.getRootElement().refreshFunction() %>
        }

        function afterRefresh() {

        }
    </script>
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
                            <%
                                FeedMetadata feedMetadata = new DataService().getFeedMetadata(dashboard.getDataSourceID());
                                if (feedMetadata.getDataSourceInfo().getType() == DataSourceInfo.COMPOSITE_PULL || feedMetadata.getDataSourceInfo().getType() == DataSourceInfo.STORED_PULL) {
                            %>
                            <li>
                                <button class="btn btn-inverse" type="button" id="refreshDataSourceButton"
                                        onclick="refreshDataSource()" style="padding:5px;margin:5px;width:150px">Refresh
                                    Data
                                    Source
                                </button>
                            </li>
                            <%
                                }
                            %>
                        </ul>
                    </div>
                    <div class="btn-group">
                        <a href="<%= RedirectUtil.getURL(request, "/app/#dashboardAdminID=" + dashboard.getUrlKey())%>"
                           class="btn btn-inverse">Edit Dashboard</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%--<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <a data-target=".nav-collapse" data-toggle="collapse" class="btn btn-navbar">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <div class="nav-collapse">
                <div class="btn-group pull-right">
                    <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="icon-user"></i> <%= StringEscapeUtils.escapeHtml(userName) %>
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <% if (phone) { %>
                        <li><a href="/app/html">Data Sources</a></li>
                        <li><a href="/app/html/reports/<%= dataSourceDescriptor.getUrlKey() %>"><%=StringEscapeUtils.escapeHtml(dataSourceDescriptor.getName())%></a></li>
                        <li><a href="#" onclick="refreshDashboard()">Refresh Dashboard</a></li>
                        <%
                            FeedMetadata feedMetadata = new DataService().getFeedMetadata(dashboard.getDataSourceID());
                            if (feedMetadata.getDataSourceInfo().getType() == DataSourceInfo.COMPOSITE_PULL || feedMetadata.getDataSourceInfo().getType() == DataSourceInfo.STORED_PULL) {
                        %>
                        <li><a href="#" onclick="refreshDataSource()">Refresh the Data Source</a></li>
                        <%
                            }
                        %>
                        <% } else { %>
                        <div class="btn-group">
                            <a href="<%= RedirectUtil.getURL(request, "/app/#dashboardID=" + dashboard.getUrlKey())%>" class="btn btn-inverse">Edit Dashboard</a>
                        </div>
                        <% } %>
                        &lt;%&ndash;<li><a href="#">Profile</a></li>&ndash;%&gt;
                        <li class="divider"></li>
                        <li><a href="/app/logoutAction.jsp">Sign Out</a></li>
                    </ul>
                </div>
            </div>

            <div class="nav-collapse">
                <ul class="nav">
                    <li><a href="/app/html">Data Sources</a></li>
                    <li><a href="/app/html/reports/<%= dataSourceDescriptor.getUrlKey() %>"><%=StringEscapeUtils.escapeHtml(dataSourceDescriptor.getName())%></a></li>
                    <li class="active"><a href="#"><%= StringEscapeUtils.escapeHtml(dashboard.getName()) %></a></li>
                </ul>
            </div>
            <div class="nav-collapse btn-toolbar" style="margin-top:0px;margin-bottom: 0px">
                <div class="btn-group">
                    <a href="<%= RedirectUtil.getURL(request, "/app/#dashboardID=" + dashboard.getUrlKey())%>" class="btn btn-inverse">Edit Dashboard</a>
                </div>
            </div>
        </div>
    </div>
</div>--%>
<div class="container-fluid">
    <%= uiData.createHeader(dashboard.getName(), dashboardUIProperties) %>
</div>
<div class="container">
    <div class="row-fluid" id="filterRow">
        <div class="span12">

            <%
                for (FilterDefinition filterDefinition : dashboard.getFilters()) {
                    if (filterDefinition.isShowOnReportView()) {
                        FilterHTMLMetadata filterHTMLMetadata = new FilterHTMLMetadata(dashboard, request, null, false);
                        filterHTMLMetadata.setOnChange("refreshDashboard");
                        out.println("<div class=\"filterDiv\">" + filterDefinition.toHTML(filterHTMLMetadata) + "</div>");
                    }
                }
            %>

        </div>
    </div>
</div>
<div class="container" style="padding-top:10px">
    <jsp:include page="refreshingDataSource.jsp"/>
    <div class="row">
        <div class="span12">
            <div class="well reportWell" style="background-color: #FFFFFF">
                <div id="chartpseudotooltip" style="z-index:100;"></div>
                <%= dashboard.getRootElement().toHTML(new FilterHTMLMetadata(dashboard, request, drillthroughArgh, false)) %>
            </div>
        </div>
    </div>
</div>
</body>
<%
    } catch(ReportNotFoundException e) {
        LogClass.error(e);
        response.sendError(404);
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>
</html>