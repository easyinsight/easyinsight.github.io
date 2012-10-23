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


%>
<body>
<div class="container">
    <div class="row">

        <div class="span8 offset2">

            <form class="well" method="post" action="/app/googleAppsCreateAction.jsp" id="loginForm">

                <input type="hidden" name='OWASP_CSRFTOKEN' value="<%= session.getAttribute("OWASP_CSRFTOKEN")%>" />
                <div style="width:100%;text-align: center">
                    <img src="/images/logo2.PNG" alt="Easy Insight Logo"/>
                </div>

                <label for="companyName" class="promptLabel" style="font-size: 14px">
                    Company Name
                </label>
                <input type="text" name="companyName" id="companyName" style="width:100%;font-size:14px;height:28px" autocapitalize="off" autocorrect="off" autoFocus/>
                <div style="padding-top:15px">
                    By
                    clicking Create Account you agree to the <a href="/terms.html"
                                                    style="text-decoration:underline;color:#CC0033">Terms
                    of Service</a> and <a href="/privacy.html" style="text-decoration:underline;color:#CC0033">Privacy</a>
                    policies.
                </div>
                <div class="row" style="padding-top: 15px">
                    <div class="span3 offset2">
                        <input style="font-size:20px" type="submit" class="btn btn-primary" value="Create Account"/>
                    </div>
                    <div class="span2">
                        <div style="padding-top: 6px">
                            <a style="font-size:20px" href="googleAppsWelcome.jsp">Cancel</a>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>