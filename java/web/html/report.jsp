<%--suppress ALL --%>
<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.analysis.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.datafeeds.FeedStorage" %>
<%@ page import="java.util.List" %>
<%@ page import="com.easyinsight.core.DataSourceDescriptor" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.easyinsight.html.DrillThroughData" %>
<%@ page import="com.easyinsight.html.Utils" %>
<%@ page import="com.easyinsight.html.UIData" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        long reportID;
        List<FilterDefinition> drillthroughFilters = new ArrayList<FilterDefinition>();
        String drillthroughArgh = request.getParameter("drillthroughKey");
        if (drillthroughArgh != null) {
            DrillThroughData drillThroughData = Utils.drillThroughFiltersForReport(drillthroughArgh);
            drillthroughFilters = drillThroughData.getFilters();
            reportID = drillThroughData.getReportID();
        } else {
            String reportIDString = request.getParameter("reportID");
            InsightResponse insightResponse = new AnalysisService().openAnalysisIfPossible(reportIDString);
            if (insightResponse.getStatus() == InsightResponse.SUCCESS) {
                reportID = insightResponse.getInsightDescriptor().getId();
            } else if (insightResponse.getStatus() == InsightResponse.PRIVATE_ACCESS) {
                throw new ReportAccessException();
            } else {
                throw new com.easyinsight.analysis.ReportNotFoundException("The report does not exist.");
            }
        }
        boolean phone = Utils.isPhone(request);
        boolean iPad = Utils.isTablet(request);
        ReportInfo reportInfo = new AnalysisService().getReportInfo(reportID);
        boolean editable = reportInfo.isAdmin();
        WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportID);
        if (report == null) {
            throw new com.easyinsight.analysis.ReportNotFoundException("Attempt made to load report " + reportID + " which doesn't exist.");
        }
        if (drillthroughFilters != null) {
            report.getFilterDefinitions().addAll(drillthroughFilters);
        }
        DataSourceDescriptor dataSourceDescriptor = new FeedStorage().dataSourceURLKeyForDataSource(report.getDataFeedID());

        UIData uiData = Utils.createUIData();

%>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta charset="utf-8">
    <meta name="author" content="">
    <title>Easy Insight &mdash; <%= StringEscapeUtils.escapeHtml(report.getName()) %></title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <jsp:include page="reportDashboardHeader.jsp"/>

    <%
        List<String> jsIncludes = report.javaScriptIncludes();
        for (String jsInclude : jsIncludes) {
            %><%= "<script type=\"text/javascript\" src=\"" + jsInclude + "\"></script>"%><%
        }
        List<String> cssIncludes = report.cssIncludes();
        for (String cssInclude : cssIncludes) {
            %><%= "<link rel=\"stylesheet\" type=\"text/css\" href=\""+cssInclude+"\" />"%><%
        }
    %>
    <jsp:include page="methods.jsp">
        <jsp:param name="reportID" value="<%= report.getUrlKey() %>"/>
        <jsp:param name="dataSourceID" value="<%= report.getDataFeedID()%>"/>
    </jsp:include>
    <jsp:include page="reportLogic.jsp">
        <jsp:param name="reportID" value="<%= report.getAnalysisID()%>"/>
        <jsp:param name="drillthroughKey" value="<%= drillthroughArgh%>"/>
        <jsp:param name="embedded" value="false"/>
    </jsp:include>

</head>
<body>

<div class="navbar navbar-fixed-top">
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
                            <li><a href="#" onclick="toggleFilters()">Toggle Filters</a></li>
                            <li><a href="#" onclick="refreshReport()">Refresh Report</a></li>
                        <%
                            FeedMetadata feedMetadata = new DataService().getFeedMetadata(report.getDataFeedID());
                            if (feedMetadata.getDataSourceInfo().getType() == DataSourceInfo.COMPOSITE_PULL || feedMetadata.getDataSourceInfo().getType() == DataSourceInfo.STORED_PULL) {
                        %>
                            <li><a href="#" onclick="refreshDataSource()">Refresh the Data Source</a></li>
                        <%
                            }
                        %>
                        <% } else { %>
                            <li><a href="/app/html/flashAppAction.jsp">Switch to Full Interface</a></li>
                        <% } %>
                        <%--<li><a href="#">Profile</a></li>--%>
                        <li class="divider"></li>
                        <li><a href="/app/logoutAction.jsp">Sign Out</a></li>
                    </ul>
                </div>
            </div>

            <% if (!phone) { %>
            <div class="nav-collapse">
                <ul class="nav">
                    <li><a href="/app/html">Data Sources</a></li>
                    <li><a href="/app/html/reports/<%= dataSourceDescriptor.getUrlKey() %>"><%=StringEscapeUtils.escapeHtml(dataSourceDescriptor.getName())%></a></li>
                    <li class="active"><a href="#"><%= StringEscapeUtils.escapeHtml(report.getName()) %></a></li>
                </ul>
            </div>
            <div class="nav-collapse btn-toolbar" style="margin-top:0px;margin-bottom: 0px">
                <div class="btn-group">
                    <a class="btn btn-inverse dropdown-toggle" data-toggle="dropdown" href="#">
                        Export the Report
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><button class="btn btn-inverse" type="button" onclick="window.location.href='/app/exportExcel?reportID=<%= report.getUrlKey() %>'" style="padding:5px;margin:5px;width:150px">Export to Excel</button></li>
                        <li><button class="btn btn-inverse" type="button" onclick="$('#emailReportWindow').modal(true, true, true)" style="padding:5px;margin:5px;width:150px">Email the Report</button></li>
                    </ul>
                </div>
                <div class="btn-group">
                    <a class="btn btn-inverse dropdown-toggle" data-toggle="dropdown" href="#">
                        Refresh Data
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><button class="btn btn-inverse" type="button" onclick="refreshReport()" style="padding:5px;margin:5px;width:150px">Refresh the Report</button></li>
                        <%
                            FeedMetadata feedMetadata = new DataService().getFeedMetadata(report.getDataFeedID());
                            if (feedMetadata.getDataSourceInfo().getType() == DataSourceInfo.COMPOSITE_PULL || feedMetadata.getDataSourceInfo().getType() == DataSourceInfo.STORED_PULL) {
                        %>
                        <li><button class="btn btn-inverse" type="button" id="refreshDataSourceButton" onclick="refreshDataSource()" style="padding:5px;margin:5px;width:150px">Refresh Data Source</button></li>
                        <%
                            }
                        %>
                    </ul>
                </div>
                <%
                    boolean visibleFilter = false;
                    for (FilterDefinition filter : report.getFilterDefinitions()) {
                        if (filter.isShowOnReportView()) {
                            visibleFilter = true;
                            break;
                        }
                    }
                    if (visibleFilter) {
                %>
                <div class="btn-group">
                    <button class="btn btn-inverse" onclick="toggleFilters()">Toggle Filters</button>
                </div>
                <%
                    }
                    if (!iPad && editable) {
                %>
                <div class="btn-group">
                    <a href="<%= RedirectUtil.getURL(request, "/app/#analysisID=" + report.getUrlKey())%>" class="btn btn-inverse">Edit Report</a>
                </div>
                <%
                    }
                %>
            </div>
            <% } %>
        </div>
    </div>
</div>
<div class="container-fluid" id="reportHeader">
    <%= uiData.createHeader(report.getName()) %>
    <div class="row-fluid" id="filterRow">
        <div class="span12">
            <%
                for (FilterDefinition filterDefinition : report.getFilterDefinitions()) {
                    if (filterDefinition.isShowOnReportView()) {
                        %>
            <div class="filterDiv"><%=filterDefinition.toHTML(new FilterHTMLMetadata(report))%></div>
                        <%
                    }
                }
            %>
        </div>
    </div>
</div>
<div class="container">
    <jsp:include page="exportModalWindow.jsp">
        <jsp:param name="reportID" value="<%= report.getUrlKey()%>"/>
    </jsp:include>
    <jsp:include page="emailReportWindow.jsp"/>
    <jsp:include page="refreshingDataSource.jsp"/>

    <div class="row">
        <div class="span12">
            <div class="well" style="background-color: #ffffff">
                <div id="chartpseudotooltip" style="z-index:100;"></div>
                <div id="reportTarget">
                    <div id="reportTargetReportArea" class="reportArea">
                        <%= report.rootHTML() %>
                    </div>
                    <div class="noData">We didn't find any data for the fields and filters that you specified in the report.</div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<%
    } catch (ReportAccessException rae) {
        response.sendRedirect(RedirectUtil.getURL(request, "accessFault.jsp"));
    } catch(ReportNotFoundException e) {
        LogClass.error(e);
        response.sendError(404);
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>
</html>