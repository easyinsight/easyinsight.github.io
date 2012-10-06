<!DOCTYPE html>
<%@ page import="com.easyinsight.dashboard.DashboardService" %>
<%@ page import="com.easyinsight.dashboard.Dashboard" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.analysis.FilterHTMLMetadata" %>
<%@ page import="com.easyinsight.dashboard.DashboardUIProperties" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page import="com.easyinsight.analysis.FilterDefinition" %>
<%@ page import="com.easyinsight.html.UIData" %>
<%@ page import="com.easyinsight.html.Utils" %>
<%@ page import="com.easyinsight.html.DrillThroughData" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<%
    if (session.getAttribute("userID") != null) {
        com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    }
    try {
        long dashboardID;
        List<FilterDefinition> drillthroughFilters = new ArrayList<FilterDefinition>();
        String drillthroughArgh = request.getParameter("drillthroughKey");
        if (drillthroughArgh != null) {
            DrillThroughData drillThroughData = Utils.drillThroughFiltersForDashboard(drillthroughArgh);
            drillthroughFilters = drillThroughData.getFilters();
            dashboardID = drillThroughData.getDashboardID();
            SecurityUtil.authorizeDashboard(dashboardID);
        } else {
            String dashboardIDString = request.getParameter("dashboardID");
            dashboardID = new DashboardService().canAccessDashboard(dashboardIDString);
            if (dashboardID == 0) {
                throw new com.easyinsight.security.SecurityException();
            } else if (dashboardID == -1) {
                session.setAttribute("loginRedirect", RedirectUtil.getURL(request, request.getRequestURI() + "?" + request.getQueryString()));
                response.sendRedirect(RedirectUtil.getURL(request, "/app/login.jsp"));
                return;
            }
        }

        Dashboard dashboard = new DashboardService().getDashboardView(dashboardID);
        DashboardUIProperties dashboardUIProperties = dashboard.findHeaderImage();

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



        boolean includeHeader = request.getParameter("includeHeader") != null;
        boolean includeFilters = request.getParameter("includeFilters") != null;
        boolean includeToolbar = request.getParameter("includeToolbar") != null;

        UIData uiData = Utils.createUIData();

%>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight &mdash; <%= StringEscapeUtils.escapeHtml(dashboard.getName()) %></title>
    <jsp:include page="../html/bootstrapHeader.jsp"/>
    <jsp:include page="../html/reportDashboardHeader.jsp"/>


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
    </script>
</head>
<body>
<%
    if (includeHeader) {
%>
<%= uiData.createHeader(dashboard.getName()) %>
<%
    }
%>
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
<div class="container" style="padding-top:10px">
    <jsp:include page="../html/refreshingDataSource.jsp"/>
    <div class="row">
        <div class="span12">
            <%= dashboard.getRootElement().toHTML(new FilterHTMLMetadata(dashboard, request, drillthroughArgh, true)) %>
        </div>
    </div>
</div>
</body>
<%
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>
</html>