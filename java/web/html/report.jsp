<%--suppress ALL --%>
<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.analysis.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.datafeeds.FeedStorage" %>
<%@ page import="java.util.List" %>
<%@ page import="com.easyinsight.core.DataSourceDescriptor" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.easyinsight.html.*" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        long reportID;
        List<FilterDefinition> drillthroughFilters = new ArrayList<FilterDefinition>();
        String drillthroughArgh = request.getParameter("drillthroughKey");

        InsightResponse insightResponse = null;
        if (drillthroughArgh != null) {
            DrillThroughData drillThroughData = Utils.drillThroughFiltersForReport(drillthroughArgh);
            drillthroughFilters = drillThroughData.getFilters();
            reportID = drillThroughData.getReportID();
            insightResponse = new AnalysisService().openAnalysisIfPossibleByID(reportID);
        } else {
            String reportIDString = request.getParameter("reportID");
            insightResponse = new AnalysisService().openAnalysisIfPossible(reportIDString);
        }

        if (insightResponse.getStatus() == InsightResponse.SUCCESS) {
            reportID = insightResponse.getInsightDescriptor().getId();
        } else if (insightResponse.getStatus() == InsightResponse.PRIVATE_ACCESS) {
            throw new ReportAccessException();
        } else {
            throw new com.easyinsight.analysis.ReportNotFoundException("The report does not exist.");
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
        JSONObject reportJSON = new JSONObject();
        reportJSON.put("name", report.getName());
        reportJSON.put("id", -1);
        reportJSON.put("local_storage", true);
        reportJSON.put("filters", new JSONArray());
        JSONObject styleJSON = new JSONObject();
        styleJSON.put("main_stack_start", "#FFFFFF");
        styleJSON.put("alternative_stack_start", "#FFFFFF");
        reportJSON.put("styles", styleJSON);
        reportJSON.put("local_storage", report.isPersistState());
        JSONObject intermediate = new JSONObject();
        reportJSON.put("base", intermediate);
        intermediate.put("show_label", false);
        intermediate.put("id", report.getUrlKey() + "_container");
        intermediate.put("overrides", new JSONArray());
        intermediate.put("filters", new JSONArray());
        intermediate.put("type", "report");
        JSONObject jj = new JSONObject();
        jj.put("name", report.getName());
        jj.put("id", report.getUrlKey());
        jj.put("metadata", report.toJSON(new HTMLReportMetadata(), new ArrayList<FilterDefinition>()));
        intermediate.put("report", jj);
        if(drillthroughArgh != null) {
            reportJSON.put("drillthroughID", drillthroughArgh);
        }
%>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta charset="utf-8">
    <meta name="author" content="">
    <title>Easy Insight &mdash; <%= StringEscapeUtils.escapeHtml(report.getName()) %>
    </title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <jsp:include page="reportDashboardHeader.jsp"/>
    <%
        List<String> jsIncludes = report.javaScriptIncludes();
        for (String jsInclude : jsIncludes) {
    %><%= "<script type=\"text/javascript\" src=\"" + jsInclude + "\"></script>"%><%
    }
    List<String> cssIncludes = report.cssIncludes();
    for (String cssInclude : cssIncludes) {
%><%= "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + cssInclude + "\" />"%><%
    }
%>
    <script type="text/javascript" src="/js/dashboard.js"></script>
    <script type="text/javascript" language="JavaScript">
        var dashboardJSON = <%= reportJSON %>;
    </script>
</head>
<body>
<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.DATA_SOURCES_AND_REPORTS %>"/>
</jsp:include>
<div class="nav navbar-pills reportNav">
<div class="container">
    <div class="row">
        <div class="col-md-6">


    <ul class="breadcrumb reportBreadcrumb">
        <li><a href="/app/html/">Data Sources</a> <span class="divider"></span></li>
        <li>
            <a href="/app/html/reports/<%= dataSourceDescriptor.getUrlKey() %>"><%= StringEscapeUtils.escapeHtml(dataSourceDescriptor.getName())%>
            </a><span class="divider"></span></li>
        <li class="active"><%= StringEscapeUtils.escapeHtml(report.getName()) %>
        </li>
    </ul>

</div>
        <div class="col-md-6">
        <div class="btn-toolbar pull-right" style="padding-top: 0;margin-top: 0">
                           <div class="btn-group" style="padding-top: 0;margin-top: 0">
                               <a class="btn btn-inverse dropdown-toggle" data-toggle="dropdown" href="#">
                                   Export the Report
                                   <span class="caret"></span>
                               </a>
                               <ul class="dropdown-menu">
                                   <li>
                                       <button class="btn btn-inverse" type="button"
                                               onclick="window.location.href='/app/exportExcel?reportID=<%= report.getUrlKey() %>'"
                                               style="padding:5px;margin:5px;width:150px">Export to Excel
                                       </button>
                                   </li>
                                   <li>
                                       <button class="btn btn-inverse" type="button"
                                               onclick="$('#emailReportWindow').modal(true, true, true)"
                                               style="padding:5px;margin:5px;width:150px">Email the Report
                                       </button>
                                   </li>
                               </ul>
                           </div>
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
                                       FeedMetadata feedMetadata = new DataService().getFeedMetadata(report.getDataFeedID());
                                       if (feedMetadata.getDataSourceInfo().getType() == DataSourceInfo.COMPOSITE_PULL || feedMetadata.getDataSourceInfo().getType() == DataSourceInfo.STORED_PULL) {
                                   %>
                                   <li>
                                       <button class="btn btn-inverse" type="button" id="refreshDataSourceButton"
                                               onclick="refreshDataSource('<%= dataSourceDescriptor.getUrlKey() %>')" style="padding:5px;margin:5px;width:150px">Refresh
                                           Data
                                           Source
                                       </button>
                                   </li>
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
                               <button class="btn btn-inverse toggle-filters">Toggle Filters</button>
                           </div>
                           <%
                               }
                           %>
                           <div class="btn-group">
                               <a href="<%= RedirectUtil.getURL(request, "/app/#analysisID=" + report.getUrlKey())%>"
                                  class="btn btn-inverse">Edit Report</a>
                           </div>
                       </div>
        </div>
                   </div>
            </div>
        </div>
<div class="container">
    <jsp:include page="exportModalWindow.jsp">
        <jsp:param name="reportID" value="<%= report.getUrlKey()%>"/>
    </jsp:include>
    <jsp:include page="emailReportWindow.jsp"/>
    <jsp:include page="refreshingDataSource.jsp"/>
    <%= uiData.createHeader(report.getName()) %>
    <div id="base"/>
</div>
</body>
<%
    } catch (ReportAccessException rae) {
        response.sendRedirect(RedirectUtil.getURL(request, "accessFault.jsp"));
    } catch (ReportNotFoundException e) {
        LogClass.error(e);
        response.sendError(404);
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>
</html>