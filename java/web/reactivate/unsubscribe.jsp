<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.users.UserService" %>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Initial Setup</title>
    <jsp:include page="/html/bootstrapHeader.jsp"></jsp:include>

</head>
<%
    String userName = (String) session.getAttribute("userName");
%>
<body>
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <%--<a class="brand" href="#"><img src="/images/logo3.jpg"/></a>--%>
            <div class="btn-group pull-right">
                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="icon-user"></i> <%= StringEscapeUtils.escapeHtml(userName) %>
                    <span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="/app/logoutAction.jsp">Sign Out</a></li>
                </ul>
            </div>
        </div>
    </div>
</div>
<div class="container">
    <div class="row">

        <div class="col-md-10 col-md-offset-1">

            <form class="well" method="post" action="reactivateAction.jsp" style="width:100%" id="loginForm">
                <div style="width:100%;text-align: center">
                    <img src="/images/logo2.PNG" alt="Easy Insight Logo"/>
                </div>

                <%  try {
                    new UserService().unsubscribe(request.getParameter("key"));
                } catch(Exception e) { }
                %>

                <div style="text-align: center">
                    <div>You have been unsubscribed.</div>
                </div>


            </form>
        </div>
    </div>
</div>
</body>
</html>