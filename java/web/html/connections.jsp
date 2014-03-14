<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="com.easyinsight.datafeeds.HTMLConnectionFactory" %>
<%@ page import="com.easyinsight.datafeeds.HTMLConnectionProperty" %>
<%@ page import="com.easyinsight.solutions.SolutionService" %>
<%@ page import="com.easyinsight.solutions.Solution" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
    <%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        List<Solution> solutions = new SolutionService().getSolutions();
%>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title><%= "Easy Insight Connections"%></title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <jsp:include page="reportDashboardHeader.jsp"/>
</head>
<body>

<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.CONNECTIONS %>"/>
</jsp:include>
<div class="container corePageWell" style="margin-top: 20px">
    <div class="row">
        <% for (Solution solution : solutions) { %>
        <div class="col-md-2" style="height:150px;text-align:center;border-style: solid; border-radius: 3px;margin: 5px">
            <div style="">
                <div style="margin-bottom: 10px">
            <a href="/app/html/connections/<%= solution.getDataSourceType() %>"><%=solution.getName()%></a>
                </div>
                <div>
                    <a href="/app/html/connections/<%= solution.getDataSourceType() %>"><img src="/app/connectionImage/<%= solution.getSolutionID()%>" style="max-width: 240px;max-height: 150px"/></a>
                </div>
            </div>
        </div>
        <% } %>
    </div>
</div>
</body>
<%
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>
</html>