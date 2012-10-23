<!DOCTYPE html>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Sign In</title>
    <script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
    <link href="/css/bootstrap.min.css" rel="stylesheet">

    <style type="text/css">
        body {
            padding-top: 45px;
            padding-bottom: 40px;
        }

        .center_stuff {
            text-align:center;
        }
    </style>
    <link href="/css/bootstrap-responsive.min.css" rel="stylesheet">
    <script type="text/javascript" src="/js/bootstrap.min.js"></script>
</head>
<%
    request.getSession().setAttribute("googleDomain", request.getParameter("domain"));
    request.getSession().setAttribute("googleCallbackURL", request.getParameter("callback"));
%>
<body>
<div class="container">
    <div class="row">

        <div class="span8 offset2">

            <div class="well" style="text-align:center">

                <input type="hidden" name='OWASP_CSRFTOKEN' value="<%= session.getAttribute("OWASP_CSRFTOKEN")%>" />
                <div style="width:100%;text-align: center">
                    <img src="/images/logo2.PNG" alt="Easy Insight Logo"/>
                </div>
                <h1 style="padding-top:40px">Welcome to Easy Insight!</h1>
                <div class="row" style="padding-top:40px">
                    <div class="span4">
                        <a style="font-size:20px" href="<%= RedirectUtil.getURL(request, "/app/linkToExistingAction.jsp")%>">Already have an Easy Insight account?</a>
                    </div>
                    <div class="span3">
                        <a class="btn btn-primary" style="font-size:20px" href="<%= RedirectUtil.getURL(request, "/app/googleAppsSettings.jsp")%>">Get Started</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>