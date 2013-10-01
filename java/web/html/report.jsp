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
            </div>
        </div>
    </div>
<div class="container" id="reportHeader">
    <%= uiData.createHeader(report.getName()) %>
    <div class="row" id="filterRow">
        <div class="col-md-12 filters">
            <%
                for (FilterDefinition filterDefinition : report.getFilterDefinitions()) {
                    if (filterDefinition.isShowOnReportView()) {
            %>
            <div class="filter"><%=filterDefinition.toHTML(new FilterHTMLMetadata(report))%>
            </div>
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
        <div class="col-md-12">
            <div class="well reportWell" style="background-color: #FFFFFF">
                <div id="chartpseudotooltip" style="z-index:100;"></div>
                <div id="reportTarget">
                    <div id="reportTargetReportArea" class="reportArea">
                        <%= report.rootHTML() %>
                    </div>
                    <div class="noData">We didn't find any data for the fields and filters that you specified in the
                        report.
                    </div>
                </div>
            </div>
        </div>
    </div>
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