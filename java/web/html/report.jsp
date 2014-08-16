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
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.dashboard.SavedConfiguration" %>
<%@ page import="com.easyinsight.dashboard.DashboardService" %>
<%@ page import="com.easyinsight.dashboard.DashboardInfo" %>
<%@ page import="com.easyinsight.admin.AdminService" %>
<%@ page import="com.easyinsight.audit.ActionReportLog" %>
<%@ page import="com.easyinsight.analysis.DataService" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
    <%
    String userName = null;
    if(session.getAttribute("userName") != null) {
        userName = (String) session.getAttribute("userName");
        com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    }
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
            System.out.println(reportIDString);
            insightResponse = new AnalysisService().openAnalysisIfPossible(reportIDString);
        }

        String configID = request.getParameter("reportConfig");
        DashboardInfo positions = null;
        String selectedConfiguration = null;
        String configurationKey = null;
        if(configID != null) {
            DashboardInfo di = new DashboardService().getConfigurationForReport(configID);
            positions = di;
            selectedConfiguration = di.getSavedConfiguration().getName();
            configurationKey = di.getSavedConfiguration().getUrlKey();
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
        boolean designer = Utils.isDesigner();
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

        FilterUtils.adjustReport(report, positions);

        UIData uiData = Utils.createUIData();
        JSONObject reportJSON = new JSONObject();
        reportJSON.put("name", (Object) report.getName());
        reportJSON.put("id", -1);
        reportJSON.put("local_storage", true);
        reportJSON.put("filters", new JSONArray());
        reportJSON.put("key", (Object) report.getUrlKey());
        JSONObject styleJSON = new JSONObject();
        styleJSON.put("main_stack_start", (Object) "#FFFFFF");
        styleJSON.put("alternative_stack_start", (Object) "#FFFFFF");
        reportJSON.put("styles", styleJSON);
        reportJSON.put("local_storage", report.isEnableLocalStorage());
        JSONObject intermediate = new JSONObject();
        reportJSON.put("base", intermediate);
        intermediate.put("show_label", false);
        intermediate.put("id", (Object) (report.getUrlKey() + "_container"));
        intermediate.put("overrides", new JSONArray());
        intermediate.put("filters", new JSONArray());
        intermediate.put("type", (Object) "report");
        JSONObject jj = new JSONObject();
        jj.put("name", (Object) report.getName());
        jj.put("id", (Object) report.getUrlKey());
        jj.put("metadata", report.toJSON(new HTMLReportMetadata(), new ArrayList<FilterDefinition>()));
        intermediate.put("report", jj);
        if (drillthroughArgh != null) {
            reportJSON.put("drillthroughID", (Object) drillthroughArgh);
        }
        JSONArray ja = new JSONArray();
        for(SavedConfiguration sc : reportInfo.getConfigurations()) {
            ja.put(sc.toJSON());
        }

        reportJSON.put("configurations", ja);
        reportJSON.put("configuration_name", (Object) selectedConfiguration);
        reportJSON.put("configuration_key", (Object) configurationKey);

        try {
            new AdminService().logAction(new ActionReportLog(SecurityUtil.getUserID(false), ActionReportLog.VIEW, report.getAnalysisID()));
        } catch (Exception e) {
            LogClass.error(e);
        }

        EIConnection c = Database.instance().getConnection();
        JSONObject userObject = new JSONObject();
        try {
            userObject = SecurityUtil.getUserJSON(c, request);
        } finally {
            Database.closeConnection(c);
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
        var userJSON = <%= userObject %>;
    </script>
</head>
<body>
<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.NONE %>"/>
</jsp:include>
<div class="nav navbar-pills reportNav" style="margin-bottom: 0">
    <div class="container-fluid">
        <div class="row controlsRow">
            <div class="col-md-4 reportBlah">
                <a class="reportControl" href="/a/data_sources/<%= dataSourceDescriptor.getUrlKey() %>">Back to <%= StringEscapeUtils.escapeHtml(dataSourceDescriptor.getName())%></a>
            </div>
            <div class="col-md-8 reportControlToolbar">
                <div class="btn-toolbar pull-right">
                    <div id="configuration-dropdown" class="btn-group reportControlBtnGroup">

                    </div>
                    <div class="btn-group reportControlBtnGroup">
                        <a class="reportControl" data-toggle="dropdown" href="#">
                            Export the Report
                            <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <a class="export_excel">Export to Excel</a>
                            </li>
                            <li>
                                <a class="export_pdf">Export to PDF</a>
                            </li>
                            <li>
                                <a class="export_png">Export to PNG</a>
                            </li>
                            <li>
                                <a class="report-emailReportButton">Email the Report</a>
                            </li>
                            <li>
                                <a href="#" onclick="window.location.href = '/app/html/embeddedReport/<%= report.getUrlKey() %>'">Printable View</a>
                            </li>
                        </ul>
                    </div>
                    <div class="btn-group reportControlBtnGroup">
                        <a class="reportControl" data-toggle="dropdown" href="#">
                            Refresh Data
                            <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <a class="full_refresh">Refresh the Report</a>
                            </li>
                            <%
                                if(userName != null) {
                                FeedMetadata feedMetadata = new DataService().getFeedMetadata(report.getDataFeedID());
                                if (feedMetadata.getDataSourceInfo().getType() == DataSourceInfo.COMPOSITE_PULL || feedMetadata.getDataSourceInfo().getType() == DataSourceInfo.STORED_PULL) {
                            %>
                            <li>
                                <a href="#" onclick="refreshDataSource('<%= dataSourceDescriptor.getUrlKey() %>')">Refresh the Data Source</a>
                            </li>
                            <%
                                }
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
                    <div class="btn-group reportControlBtnGroup">
                        <a class="reportControl toggle-filters">Toggle Filters</a>
                    </div>
                    <%
                        }
                    %>
                    <% if (designer && !iPad && !phone) { %>
                    <div class="btn-group reportControlBtnGroup" style="margin-right:5px">
                        <a href="<%= RedirectUtil.getURL(request, "/app/embeddedReportEditor.jsp?reportID=" + report.getUrlKey())%>"
                           class="reportControl">Edit Report</a>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="container" style="margin-top:10px;padding:0;margin-bottom:10px">
    <div class="row" style="margin:0;padding:0">
        <div class="col-md-12" style="text-align:center;margin: 0;padding:0">
            <h2 style="color: #333333;margin:0;padding:0"><%= report.getName() %></h2>
        </div>
    </div>
</div>
<div class="container">
    <jsp:include page="exportModalWindow.jsp">
        <jsp:param name="reportID" value="<%= report.getUrlKey()%>"/>
    </jsp:include>
    <jsp:include page="emailReportWindow.jsp"/>
    <jsp:include page="refreshingDataSource.jsp"/>
    <jsp:include page="modalIndicator.jsp"/>
    <div id="base"/>
</div>
</body>
<%
    } catch (ReportAccessException rae) {
        response.sendRedirect(RedirectUtil.getURL(request, "/accessFault.jsp"));
    } catch (ReportNotFoundException e) {
        LogClass.error(e);
        response.sendError(404);
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>
</html>