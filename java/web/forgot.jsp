<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Forgot Your Password?</title>
    <jsp:include page="html/bootstrapHeader.jsp" />
    <style type="text/css">
        body {
            padding-top: 45px;
            background-image: none;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="row">

        <div class="col-md-6 col-md-offset-3">

            <form class="well" method="post" action="forgotPasswordAction.jsp" style="width:100%" id="loginForm">
                <input type="hidden" name='OWASP_CSRFTOKEN' value="<%= session.getAttribute("OWASP_CSRFTOKEN")%>" />
                <div style="width:100%;text-align: center">
                    <img src="/images/logo2.png" alt="Easy Insight Logo"/>
                </div>

                <input type="hidden" id="urlhash" name="urlhash"/>

                <label for="email" class="promptLabel">
                    What email address do you use to sign in to Easy Insight?
                </label>
                <input type="text" name="email" id="email" style="width:100%;font-size:14px;height:28px;margin-bottom: 10px" autocapitalize="off" autocorrect="off" autoFocus/>
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