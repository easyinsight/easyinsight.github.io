<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Server Error</title>
    <jsp:include page="/html/bootstrapHeader.jsp"></jsp:include>

</head>
<body>
<%

    String userName = (String) session.getAttribute("userName");
    if (userName != null) {
        com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    }


    try {

%>
<jsp:include page="header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.NONE %>"/>
</jsp:include>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div style="width:100%;text-align: center">
                <img src="/images/logo2.png" alt="Easy Insight Logo"/>
            </div>
        </div>
        <div class="col-md-6 col-md-offset-3">
            <div class="well" style="text-align:center">
                <h3>Changing Billing Interval</h3>
                <p>We don't yet support automatically changing from Monthly to Yearly or Yearly to Monthly billing. Please contact support at support@easy-insight.com and we'll assist you with the transition.</p>
            </div>
        </div>
    </div>
</div>

<%
    } finally {
        if (userName != null) {
            SecurityUtil.clearThreadLocal();
        }
    }
%>
</body>
</html>