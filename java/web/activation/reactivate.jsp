<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Your Easy Insight account is not yet active</title>
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
    <link href="/css/bootstrap-responsive.css" rel="stylesheet">
    <script type="text/javascript" src="/js/bootstrap.js"></script>
</head>
<body>
<div class="container">
    <div class="row">

        <div class="span6 offset3">

            <form class="well" method="post" action="resendActivationEmail.jsp" style="width:100%" id="loginForm">
                <div style="width:100%;text-align: center">
                    <img src="/images/logo2.PNG" alt="Easy Insight Logo"/>
                </div>

                <input type="hidden" id="urlhash" name="urlhash"/>

                <p>
                    You haven't activated your Easy Insight account yet. Need a new activation email?
                </p>
                <%
                    String result = request.getParameter("result");
                    if ("emailed".equals(result)) {
                %>
                <fieldset class="control-group">
                    <label class="formAreaP control-label" style="font-size: 12px;padding: 0;margin-bottom: 5px">An activation email has been resent to your account.</label>
                </fieldset>
                <%
                    }
                %>
                <button class="btn btn-inverse" type="submit" value="Submit">Resend Email</button>
                <div class="signInBar" style="padding-top: 10px">
                    <a href="../logoutAction.jsp" style="font-size: 12px">Sign Out</a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>