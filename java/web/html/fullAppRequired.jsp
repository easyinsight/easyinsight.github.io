<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {


%>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Connection</title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <jsp:include page="reportDashboardHeader.jsp"/>
</head>
<body>

<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.NONE %>"/>
</jsp:include>
<div class="container corePageWell" style="margin-top: 20px">
    <div class="row">
        <div class="col-md-6 col-md-offset-3">
            <div class="well" style="text-align:center;background-color: #FFFFFF">
                <h3>Not Yet Supported in HTML5</h3>
                <p>We're working on gradually supporting all of the connection types in HTML5, but we don't quite have this one implemented yet. You'll need to switch over to the <a href="https://www.easy-insight.com/app?full">full interface</a> to install this connection.</p>
            </div>
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