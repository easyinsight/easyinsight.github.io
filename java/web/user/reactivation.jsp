<%@ page import="com.easyinsight.html.RedirectUtil" %>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Initial Setup</title>
    <jsp:include page="../html/bootstrapHeader.jsp" />
    <style type="text/css">

        .center_stuff {
            text-align:center;
        }
        #googleApps {
            color: #4a4b4c;
        }

        body {
            padding-top: 45px;
        }


    </style>
</head>
<%
    String resetPassword = request.getParameter("passwordReset");
    if (resetPassword != null) {
        session.setAttribute("resetPassword", resetPassword);
    }
%>
<!-- add project type as filter, add report with project metrics by project time,
                        add report for on time analysis by variable time range, lookup tables -->
<body>
<div class="container">
    <div class="row">

        <div class="col-md-6 col-md-offset-3">

            <form class="well" method="post" action="reactivatePasswordAction.jsp" style="width:100%;background-color: #FFFFFF" id="loginForm">

                <div class="row">
                    <div class="col-md-12" style="text-align: center">
                        <div style="padding: 10px;background-color: #FFFFFF">
                            <img src="/images/logo2.png" alt="Easy Insight Logo"/>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12" style="text-align:center">
                        <h2 class="productHeader" >Welcome back to Easy Insight!</h2>
                    </div>
                </div>


                <div class="row" style="margin-top: 10px">
                    <div class="col-md-12">
                        <p>Create a new password for your account and you'll be off and running with a new 30 day free trial.</p>
                    </div>
                </div>

                <label for="password" class="promptLabel">
                    Password
                </label>
                <input type="password" name="password" id="password" style="width:100%;font-size:14px;height:32px;margin-bottom:5px"/>

                <label for="confirmPassword" class="promptLabel">
                    Confirm Password
                </label>
                <input type="password" name="confirmPassword" id="confirmPassword" style="width:100%;font-size:14px;height:32px;margin-bottom:5px"/>
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
                <div class="row">
                    <div class="col-md-12">
                        <button class="btn btn-primary" type="submit" value="Reset the Password">Update my Password</button>
                    </div>
                </div>

            </form>
        </div>
    </div>
</div>
</body>
</html>