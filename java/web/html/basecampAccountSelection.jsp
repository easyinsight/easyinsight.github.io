<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.core.DataSourceDescriptor" %>
<%@ page import="com.easyinsight.core.EIDescriptor" %>
<%@ page import="com.easyinsight.core.InsightDescriptor" %>
<%@ page import="com.easyinsight.userupload.UserUploadService" %>
<%@ page import="com.easyinsight.dashboard.DashboardDescriptor" %>
<%@ page import="com.easyinsight.audit.ActionLog" %>
<%@ page import="com.easyinsight.admin.AdminService" %>
<%@ page import="com.easyinsight.audit.ActionReportLog" %>
<%@ page import="com.easyinsight.audit.ActionDashboardLog" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.easyinsight.userupload.CustomFolder" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="java.util.*" %>
<%@ page import="com.easyinsight.core.DataFolder" %>
<%@ page import="com.easyinsight.html.Utils" %>
<%@ page import="com.easyinsight.datafeeds.*" %>
<%@ page import="com.easyinsight.datafeeds.basecampnext.BasecampNextCompositeSource" %>
<%@ page import="com.easyinsight.datafeeds.basecampnext.BasecampNextAccount" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <style type="text/css">
        #refreshDiv {
            display: none;
        }
    </style>
    <title>Easy Insight Reports and Dashboards</title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <jsp:include page="reportDashboardHeader.jsp"/>
</head>
<body>
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        // find the data source
        // if it requires additional setup (i.e. Basecamp or Smartsheet, redirect appropriately)

        Collection<BasecampNextAccount> accounts;
        FeedResponse feedResponse = new FeedService().openFeedIfPossible(request.getParameter("dataSourceID"));
        if (feedResponse.getStatus() == FeedResponse.SUCCESS) {
            BasecampNextCompositeSource dataSource = (BasecampNextCompositeSource) new FeedStorage().getFeedDefinitionData(feedResponse.getFeedDescriptor().getId());

            accounts = new UserUploadService().getBasecampAccounts(dataSource);
        } else {
            throw new RuntimeException();
        }
        if (accounts.size() == 1) {
            response.sendRedirect("/app/html/dataSources/" + request.getParameter("dataSourceID") + "/basecampAccountChoice/" + accounts.iterator().next().getId());
            return;
        }
%>
<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.DATA_SOURCES_AND_REPORTS %>"/>
</jsp:include>
<div class="container corePageWell">
    <div class="row">
        <div class="col-md-6 col-md-offset-3">
            <div>We found the following options as Basecamp accounts:</div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6 col-md-offset-3">
            <ul>
            <%
                for (BasecampNextAccount account : accounts) {
                    %>
                <li>><a href="/app/html/dataSources/<%=request.getParameter("dataSourceID")%>/basecampAccountChoice/<%=account.getId()%>"><%= account.getName()%></a></li>
                    <%
                }
            %>
            </ul>
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