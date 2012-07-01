<!DOCTYPE html>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.users.User" %>
<%@ page import="com.easyinsight.users.InternalUserService" %>
<%@ page import="com.easyinsight.users.UserServiceResponse" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
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
    </style>
    <link href="/css/bootstrap-responsive.min.css" rel="stylesheet">
    <script type="text/javascript" src="/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        function preserveHash() {
            $('input[name=urlhash]').val(window.location.hash);
        }
    </script>
</head>
<%
    if(request.getSession().getAttribute("accountID") != null) {
        response.sendRedirect(RedirectUtil.getURL(request, "/app/index.jsp"));
        return;
    }
    Cookie[] cookies = request.getCookies();
    String cookieValue = null;
    String userName = null;
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if ("eiRememberMe".equals(cookie.getName())) {
                cookieValue = cookie.getValue();
            } else if ("eiUserName".equals(cookie.getName())) {
                userName = cookie.getValue();
            }
        }
    }
    if (cookieValue != null && userName != null) {
        EIConnection conn = Database.instance().getConnection();
        Session hibernateSession = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            UserServiceResponse userServiceResponse = new InternalUserService().validateCookie(cookieValue, userName, conn, hibernateSession);
            if (userServiceResponse != null) {
                SecurityUtil.populateSession(session, userServiceResponse);
                response.addCookie(new Cookie("eiUserName", userName));
                response.addCookie(new Cookie("eiRememberMe", new InternalUserService().createCookie(userServiceResponse.getUserID(), conn)));
                String redirectUrl = RedirectUtil.getURL(request, "/app/");
                if(session.getAttribute("loginRedirect") != null) {
                    redirectUrl = ((String) session.getAttribute("loginRedirect"));
                    session.removeAttribute("loginRedirect");
                }
                String urlHash = request.getParameter("urlhash");
                if(urlHash != null)
                    redirectUrl = redirectUrl + urlHash;
                response.sendRedirect(redirectUrl);
            }
            conn.commit();
            if (userServiceResponse != null) {
                return;
            }
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            hibernateSession.close();
            conn.close();
        }
    }

%>
<body>
<div class="container">
    <div class="row">

        <div class="span6 offset3">

            <form class="well" method="post" action="/app/loginAction.jsp" style="width:100%" id="loginForm" onsubmit="preserveHash()">
                <div style="width:100%;text-align: center">
                    <%
                        if (request.getParameter("subdomain") != null) {
                            %>
                    <img src="<%= RedirectUtil.getURL(request, "/app/whiteLabelImage") %>" alt="Logo Image"/>
                    <%
                        } else {
                    %>
                    <img src="/images/logo2.PNG" alt="Easy Insight Logo"/>
                    <%
                        }
                    %>
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

                <%
                    if (request.getParameter("error") != null) {
                %>
        <fieldset class="control-group error">
            <label class="formAreaP control-label" style="font-size: 12px;padding: 0;margin-bottom: 5px">We didn't recognize the username or password you entered.</label>
        </fieldset>
                <%
                    } else if (request.getParameter("passwordReset") != null) {
                        %>
        <fieldset class="control-group">
            <label class="formAreaP control-label" style="font-size: 12px;padding: 0;margin-bottom: 5px;color: #009900">Your password has been reset.</label>
        </fieldset>
                        <%
                    }
                %>
                <label class="checkbox">
                    <input type="checkbox" id="rememberMeCheckbox" name="rememberMeCheckbox">Remember me on this computer
                </label>
                <button class="btn btn-inverse" type="submit" value="Sign In">Sign In</button>
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