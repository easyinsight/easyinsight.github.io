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
</head>t/css">
        .center_stuff {
<%


%>
<body>
<div class="container">
    <div class="row">

        <div class="col-md-8 col-md-offset-2">

            <form class="well" method="post" action="/app/googleAppsCreateAction.jsp" id="loginForm">

                <input type="hidden" name='OWASP_CSRFTOKEN' value="<%= session.getAttribute("OWASP_CSRFTOKEN")%>" />
                <div style="width:100%;text-align: center">
                    <img src="/images/logo2.png" alt="Easy Insight Logo"/>
                </div>

                <label for="companyName" class="promptLabel" style="font-size: 14px">
                    Company Name
                </label>
                <input type="text" name="companyName" id="companyName" style="width:100%;font-size:14px;height:28px" autocapitalize="off" autocorrect="off" autoFocus/>
                <%
                    if (session.getAttribute("appsErrorString") != null) {
                %>
                <fieldset class="control-group error">
                    <label class="formAreaP control-label" style="font-size: 12px;padding: 0;margin-bottom: 5px"><%= session.getAttribute("appsErrorString")%></label>
                </fieldset>
                <%
                    }
                %>
                <div style="padding-top:15px">
                    By
                    clicking Create Account you agree to the <a href="/terms.html"
                                                    style="text-decoration:underline;color:#CC0033">Terms
                    of Service</a> and <a href="/privacy.html" style="text-decoration:underline;color:#CC0033">Privacy</a>
                    policies.
                </div>
                <div class="row" style="padding-top: 15px">
                    <div class="col-md-3 col-md-offset-2">
                        <input style="font-size:20px" type="submit" class="btn btn-primary" value="Create Account"/>
                    </div>
                    <div class="col-md-2">
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