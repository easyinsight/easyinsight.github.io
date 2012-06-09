<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Forgot Your Password?</title>
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
<%
    String resetPassword = request.getParameter("resetPassword");
    request.getSession().setAttribute("resetPassword", resetPassword);
%>
<body>
<div class="container">
    <div class="row">

        <div class="span6 offset3">

            <form class="well" method="post" action="resetPasswordAction.jsp" style="width:100%" id="loginForm">
                <div style="width:100%;text-align: center">
                    <img src="/images/logo2.PNG" alt="Easy Insight Logo"/>
                </div>

                <input type="hidden" id="urlhash" name="urlhash"/>

                <label for="userName" class="promptLabel">
                    User Name or Email
                </label>
                <input type="text" name="userName" id="userName" style="width:100%;font-size:14px;height:28px" autocapitalize="off" autocorrect="off" autoFocus/>

                <label for="password" class="promptLabel">
                    Password
                </label>
                <input type="password" name="password" id="password" style="width:100%;font-size:14px;height:28px"/>

                <label for="confirmPassword" class="promptLabel">
                    Confirm Password
                </label>
                <input type="password" name="confirmPassword" id="confirmPassword" style="width:100%;font-size:14px;height:28px"/>
                <%
                    String errorString = (String) request.getSession().getAttribute("errorString");
                    if (errorString != null) {
                        request.getSession().removeAttribute("errorString");
                %>
                <fieldset class="control-group error">
                    <label class="formAreaP control-label" style="font-size: 12px;padding: 0;margin-bottom: 5px"><%= errorString%></label>
                </fieldset>
                <%
                    }
                %>
                <button class="btn btn-inverse" type="submit" value="Reset the Password">Reset the Password</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>