<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.analysis.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page import="com.easyinsight.html.UIData" %>
<%@ page import="com.easyinsight.html.Utils" %>
<%@ page import="com.easyinsight.html.DrillThroughData" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<%
    if (session.getAttribute("userID") != null) {
        com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    }
    try {
        long reportID;
        List<FilterDefinition> drillthroughFilters = new ArrayList<FilterDefinition>();
        String drillthroughArgh = request.getParameter("drillthroughKey");
        if (drillthroughArgh != null) {
            DrillThroughData drillThroughData = Utils.drillThroughFiltersForReport(drillthroughArgh);
            drillthroughFilters = drillThroughData.getFilters();
            reportID = drillThroughData.getReportID();
            SecurityUtil.authorizeInsight(reportID);
        } else {
            String reportIDString = request.getParameter("reportID");
            InsightResponse insightResponse = new AnalysisService().openAnalysisIfPossible(reportIDString);
            if (insightResponse.getStatus() == InsightResponse.SUCCESS) {
                reportID = insightResponse.getInsightDescriptor().getId();
            } else if (insightResponse.getStatus() == InsightResponse.NEED_LOGIN) {
                session.setAttribute("loginRedirect", RedirectUtil.getURL(request, request.getRequestURI() + "?" + request.getQueryString()));
                response.sendRedirect(RedirectUtil.getURL(request, "/app/login.jsp"));
                return;
            } else {
                throw new com.easyinsight.analysis.ReportNotFoundException("The report does not exist.");
            }
        }


        boolean includeHeader = request.getParameter("includeHeader") != null;
        boolean includeFilters = request.getParameter("includeFilters") != null;
        boolean includeToolbar = request.getParameter("includeToolbar") != null;
        WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportID);
        if (report == null) {
            throw new RuntimeException("Attempt made to load report " + reportID + " which doesn't exist.");
        }
        if (drillthroughFilters != null) {
            report.getFilterDefinitions().addAll(drillthroughFilters);
        }

        UIData uiData = Utils.createUIData();

%>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight &mdash; <%= StringEscapeUtils.escapeHtml(report.getName()) %></title>
    <jsp:include page="../html/bootstrapHeader.jsp"/>
    <jsp:include page="../html/reportDashboardHeader.jsp"/>

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
    <jsp:include page="../html/methods.jsp">
        <jsp:param name="reportID" value="<%= report.getUrlKey() %>"/>
        <jsp:param name="dataSourceID" value="<%= report.getDataFeedID()%>"/>
    </jsp:include>
    <jsp:include page="../html/reportLogic.jsp">
        <jsp:param name="reportID" value="<%= report.getAnalysisID()%>"/>
        <jsp:param name="drillthroughKey" value="<%= drillthroughArgh%>"/>
        <jsp:param name="embedded" value="true"/>
    </jsp:include>
</head>
<body style="padding: 0;margin:0">
<jsp:include page="../html/exportModalWindow.jsp">
    <jsp:param name="reportID" value="<%= report.getUrlKey()%>"/>
</jsp:include>
<jsp:include page="../html/emailReportWindow.jsp"/>
<jsp:include page="../html/refreshingDataSource.jsp"/>
<%
    if (includeHeader) {
        %>
<%= uiData.createHeader(report.getName()) %>
        <%
    }
%>
    <div class="container">
        <div class="row-fluid">
            <div class="span12" style="text-align:center" id="refreshDiv">
                Refreshing the data source...
                <div class="progress progress-striped active">
                    <div class="bar"
                         style="width: 100%;"></div>
                </div>
            </div>
            <div class="span12" style="text-align:center" id="problemHTML">
            </div>
        </div>
        <% if (includeFilters) { %>
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
        <% } %>
        <div class="row">
            <div class="span12">
                <div style="background-color: #ffffff;margin:5px">
                    <div id="reportTarget">
                        <div id="reportTargetReportArea" class="reportArea">
                            <%= report.rootHTML() %>
                        </div>
                        <div id="chartpseudotooltip"></div>
                        <div class="noData"> There is no data for this report. </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
<%  } catch (ReportNotFoundException rnfe) {
        LogClass.error(rnfe);
        response.sendError(404);
    } finally {
        if (session.getAttribute("userID") != null) {
            SecurityUtil.clearThreadLocal();
        }
    }
%>
</html>