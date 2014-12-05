<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="com.easyinsight.solutions.SolutionService" %>
<%@ page import="com.easyinsight.solutions.Solution" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
    <%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        List<Solution> solutions = new SolutionService().getSolutions();
        List<Solution> validSolutions = new ArrayList<Solution>();
        for (Solution solution : solutions) {
            if (solution.getCategory() == 1) {
                validSolutions.add(solution);
            }
        }
%>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Connections</title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <jsp:include page="reportDashboardHeader.jsp"/>
</head>
<body>

<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.CONNECTIONS %>"/>
</jsp:include>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-12" style="background-color:#0084b4">
            <div class="row">
                <div class="col-md-8 col-md-offset-2" style="text-align:center">
                    <h2 style="color:#FFFFFF;margin-top: 20px;margin-bottom: 20px">
                        Click on a connection to get started!
                    </h2>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="container corePageWell" style="margin-top: 20px;">

    <div class="row">
        <% for (Solution solution : validSolutions) { %>
        <div class="col-md-2 col-sm-3 col-xs-6">
            <a href="/app/html/connections/<%= solution.getDataSourceType() %>">
                <div class="well data_source_grid" style="position:relative;">
                    <div></div>
                    <div class="title"><%=solution.getName()%></div>
                    <img src="/app/connectionImage/<%= solution.getSolutionID()%>" />
                </div>
            </a>
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