<!DOCTYPE html>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Sign In</title>
    <style type="text/css">

        .center_stuff {
            text-align:center;
        }
    </style>
    <jsp:include page="/html/bootstrapHeader.jsp"></jsp:include>
</head>
<body>
<div class="container">
    <div class="row">

        <div class="col-md-6 col-md-offset-3">

            <form class="well" method="post" action="/app/linkToExistingAction.jsp" id="loginForm" onsubmit="preserveHash()">

                <input type="hidden" name='OWASP_CSRFTOKEN' value="<%= session.getAttribute("OWASP_CSRFTOKEN")%>" />
                <div style="width:100%;text-align: center">
                    <img src="/images/logo2.PNG" alt="Easy Insight Logo"/>
                </div>
                <span>Welcome</span>

                <p>Let's go through the steps to create your Easy Insight account:</p>

                <div class="row">
                    <div class="col-md-2">
                        Already have an Easy Insight account?
                    </div>
                    <div class="col-md-2">
                        <button class="btn">Get Started</button>
                    </div>
                </div>
                <div class="signInBar" style="padding-top: 10px">
                    <a href="<%= RedirectUtil.getURL(request, "/app/newaccount")%>" style="font-size: 12px">No account yet?</a>
                    <a href="<%= RedirectUtil.getURL(request, "/app/forgot.jsp")%>" style="font-size: 12px;float:right">Forgot your password?</a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>