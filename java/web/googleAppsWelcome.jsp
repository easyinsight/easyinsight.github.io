<!DOCTYPE html>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Sign In</title>
    <jsp:include page="html/bootstrapHeader.jsp" />
    <style type="text/css">

        .center_stuff {
            text-align:center;
        }
        #googleApps {
            color: #4a4b4c;
        }

        body {
            padding-top: 45px;
            background-image: none;
        }

    </style>
</head>
<%

%>
<body>
<div class="container">
    <div class="row">

        <div class="col-md-8 col-md-offset-2">

            <div class="well">

                <input type="hidden" name='OWASP_CSRFTOKEN' value="<%= session.getAttribute("OWASP_CSRFTOKEN")%>" />
                <div style="width:100%;text-align: center">
                    <img src="/images/logo2.png" alt="Easy Insight Logo"/>
                </div>
                <div style="width:100%;text-align: center">
                    <h1 style="padding-top:40px">Welcome to Easy Insight!</h1>
                </div>
                <p style="font-size: 20px; line-height:24px;padding-top: 30px">
                    Let's go through the steps to create your Easy Insight account. You can import users from your Google Apps domain to accelerate setup.
                </p>
                <p style="font-size: 20px;padding-top: 30px">During setup you will:</p>
                <ul style="font-size: 20px;line-height: 24px">
                    <li style="line-height:24px">Choose a name for your account</li>
                    <li style="line-height:24px">Create designers and report viewers from your Google Apps users</li>
                </ul>
                <div class="row" style="padding-top:40px">
                    <div class="col-md-4" style="padding-top: 4px">
                        <a style="font-size:20px" href="<%= RedirectUtil.getURL(request, "/app/linkToExistingAction.jsp")%>">Already have an Easy Insight account?</a>
                    </div>
                    <div class="col-md-3">
                        <div style="float:right">
                            <a class="btn btn-primary" style="font-size:20px" href="<%= RedirectUtil.getURL(request, "/app/googleAppsSettings.jsp")%>">Get Started</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>