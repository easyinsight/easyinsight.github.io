<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Sign In</title>
    <jsp:include page="../html/bootstrapHeader.jsp" />
</head>
<body>
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {

%>
<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.ACCOUNT %>"/>
</jsp:include>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div style="width:100%;text-align: center">
                <img src="/images/logo2.PNG" alt="Easy Insight Logo"/>
            </div>
        </div>
        <div class="col-md-12">
            <div class="well" style="text-align:center;background-color: #FFFFFF">
                <h3>Enterprise Account</h3>
                <p>You have an enterprise licensed account. For making account or billing changes, please contact your account rep or <b>sales@easy-insight.com</b>.</p>
            </div>

        </div>
    </div>
</div>

<%
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>
</body>
</html>