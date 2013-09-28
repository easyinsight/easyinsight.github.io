<!DOCTYPE html>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Account Management</title>
    <script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="/js/jquery-ui-1.8.20.custom.min.js"></script>
    <link href="/css/bootstrap.css" rel="stylesheet">
    <link href="/css/smoothness/jquery-ui-1.8.20.custom.css" rel="stylesheet">

    <style type="text/css">
        body {
            padding-top: 45px;
            padding-bottom: 40px;
        }
    </style>
    <link href='https://fonts.googleapis.com/css?family=PT+Sans' rel='stylesheet' type='text/css'/>
    <link href="/css/bootstrap-responsive.css" rel="stylesheet">
    <script type="text/javascript" src="/js/bootstrap.js"></script>
</head>
<%


%>
<body>
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        if (!SecurityUtil.isAccountAdmin()) {
            response.sendRedirect("access.jsp");
            return;
        }
        if ((SecurityUtil.getAccountTier() == Account.PREMIUM || SecurityUtil.getAccountTier() == Account.ENTERPRISE)) {
            response.sendRedirect("access.jsp");
            return;
        }

        Session hibernateSession = Database.instance().createSession();
        Account account = null;
        try {
            account = (Account) hibernateSession.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);
        } finally {
            hibernateSession.close();
        }

        session.removeAttribute("accountTypeChange");

%>
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
                    <%
                        if (account.getAccountState() == Account.TRIAL || account.getAccountState() == Account.ACTIVE) {
                    %>
                    <li><a href="../html/flashAppAction.jsp">Back to Full Interface</a></li>
                    <%
                        }
                    %>
                    <li><a href="/app/logoutAction.jsp">Sign Out</a></li>
                </ul>
            </div>
            <div class="nav-collapse">
                <ul class="nav">
                    <li class="active"><a href="/app/billing/accountType.jsp">Account Configuration</a></li>
                    <li><a href="/app/billing">Billing Setup</a></li>
                </ul>
            </div>
        </div>
    </div>
</div>
<div class="container">
    <div class="row">
        <div class="span12">
            <div style="width:100%;text-align: center">
                <img src="/images/logo2.PNG" alt="Easy Insight Logo"/>
            </div>
        </div>
        <div class="span12 well">
            <div class="row">
                <div class="span8 offset2 well" style="background-color: #FFFFFF">
                    <div style="float:left;height:90px;padding-top:30px;padding-right:60px"><h4>Your free trial account
                        has been changed to:</h4></div>
                    <div style="height:90px">
                        <p><%= account.planName() %>
                        </p>

                        <p><%= account.designers() %>
                        </p>

                        <p><%= account.billingInterval() %>
                        </p>

                        <p><%= account.storageString() %>
                        </p>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="span6 offset3 well" style="background-color: #FFFFFF">
                    <div>
                        <span>Want to go ahead and enter your billing information now to ensure uninterrupted service?</span>
                    </div>
                    <div>
                        <a href="<%= RedirectUtil.getURL(request, "/app/billing") %>" class="btn btn-success">Enter Billing</a>
                        <a href="<%= RedirectUtil.getURL(request, "/app") %>" class="btn btn-inverse">Not Yet, Take Me Back to Application</a>
                    </div>
                </div>
            </div>
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
