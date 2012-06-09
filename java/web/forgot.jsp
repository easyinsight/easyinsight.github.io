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
<body>
<div class="container">
    <div class="row">

        <div class="span6 offset3">

            <form class="well" method="post" action="forgotPasswordAction.jsp" style="width:100%" id="loginForm">
                <div style="width:100%;text-align: center">
                    <img src="/images/logo2.PNG" alt="Easy Insight Logo"/>
                </div>

                <input type="hidden" id="urlhash" name="urlhash"/>

                <label for="email" class="promptLabel">
                    What email address do you use to sign in to Easy Insight?
                </label>
                <input type="text" name="email" id="email" style="width:100%;font-size:14px;height:28px" autocapitalize="off" autocorrect="off" autoFocus/>
                <%
                    String result = request.getParameter("result");
                    if ("noEmail".equals(result)) {
                %>
                <fieldset class="control-group error">
                    <label class="formAreaP control-label" style="font-size: 12px;padding: 0;margin-bottom: 5px">The email you specified was not found.</label>
                </fieldset>
                <%
                    } else if ("emailed".equals(result)) {
                %>
                <fieldset class="control-group">
                    <label class="formAreaP control-label" style="font-size: 12px;padding: 0;margin-bottom: 5px">An email has been sent to reset your password.</label>
                </fieldset>
                <%
                    }
                %>
                <button class="btn btn-inverse" type="submit" value="Submit">Submit</button>
                <div class="signInBar" style="padding-top: 10px">
                    <a href="login.jsp" style="font-size: 12px">Back to Sign In</a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>