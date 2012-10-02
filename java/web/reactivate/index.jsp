<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.users.Account" %>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Initial Setup</title>
    <script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="/js/jquery-ui-1.8.20.custom.min.js"></script>
    <link href="/css/bootstrap.css" rel="stylesheet">
    <link href="/css/smoothness/jquery-ui-1.8.20.custom.css" rel="stylesheet">

    <style type="text/css">
        body {
            padding-top: 75px;
            padding-bottom: 40px;
        }
    </style>
    <link href="/css/bootstrap-responsive.css" rel="stylesheet">
    <script type="text/javascript" src="/js/bootstrap.js"></script>
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

        <div class="span10 offset1">

            <form class="well" method="post" action="reactivateAction.jsp" style="width:100%" id="loginForm">
                <div style="width:100%;text-align: center">
                    <img src="/images/logo2.PNG" alt="Easy Insight Logo"/>
                </div>

                <input type="hidden" id="urlhash" name="urlhash"/>

                <div style="height:250px">
                    <div style="float:left;padding-left: 60px">
                        <img src="/images/ZendeskDashboardA.png" width="300" height="225"/>
                    </div>
                    <div style="float:right;padding-right: 60px">
                        <img src="/images/ZendeskChartA.png" width="300" height="225"/>
                    </div>
                </div>

                <div style="text-align: center">
                    <p style="font-size: 18px"><strong>Ready to start a fresh 30 day trial of Easy Insight?</strong></p>

                    <button class="btn btn-large btn-inverse" type="submit" value="Reset the Password">Start a New Trial!</button>
                </div>

            </form>
        </div>
    </div>
</div>
</body>
</html>