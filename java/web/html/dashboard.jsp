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
        boolean phone = request.getHeader("User-Agent").toLowerCase().matches(".*(android|avantgo|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*")||request.getHeader("User-Agent").toLowerCase().substring(0,4).matches("1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|e\\-|e\\/|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|xda(\\-|2|g)|yas\\-|your|zeto|zte\\-");

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
                        <a href="<%= RedirectUtil.getURL(request, "/app/#dashboardID=" + dashboard.getUrlKey())%>"
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
    <%= uiData.createHeader(dashboard.getName()) %>
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