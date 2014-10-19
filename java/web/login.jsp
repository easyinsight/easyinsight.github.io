<!DOCTYPE html>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.users.User" %>
<%@ page import="com.easyinsight.users.InternalUserService" %>
<%@ page import="com.easyinsight.users.UserServiceResponse" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page import="com.easyinsight.users.UserService" %>
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
    <script type="text/javascript">
        function preserveHash() {
            $('input[name=urlhash]').val(window.location.hash);
        }

        $(function() {
            preserveHash()
            $("#googleApps").click(function(e) {
                e.preventDefault();
                var v = $("input[name=urlhash]").val();
                if(!v || v == "")
                    v = "";
                window.location = "/app/ga_login.jsp" + v;
            })
        })
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
                String ipAddress  = request.getHeader("X-FORWARDED-FOR");
                if(ipAddress == null) {
                    ipAddress = request.getRemoteAddr();
                }
                new UserService().logAuthentication(userName, userServiceResponse.getUserID(), userServiceResponse.isSuccessful(), ipAddress, request.getHeader("User-Agent"));
                SecurityUtil.populateSession(session, userServiceResponse);
                Cookie usernameCookie = new Cookie("eiUserName", userName);
                usernameCookie.setSecure(true);
                usernameCookie.setMaxAge(60 * 60 * 24 * 30);
                response.addCookie(usernameCookie);
                Cookie rememberMeCookie = new Cookie("eiRememberMe", new InternalUserService().createCookie(userServiceResponse.getUserID(), conn));
                rememberMeCookie.setSecure(true);
                rememberMeCookie.setMaxAge(60 * 60 * 24 * 30);
                response.addCookie(rememberMeCookie);
                String redirectUrl = RedirectUtil.getURL(request, "/app/");
                boolean loginRedirectSet = false;
                if(session.getAttribute("loginRedirect") != null) {
                    loginRedirectSet = true;
                    redirectUrl = ((String) session.getAttribute("loginRedirect"));
                    session.removeAttribute("loginRedirect");
                }
                String urlHash = request.getParameter("urlhash");
                if(urlHash != null) {
                    redirectUrl = redirectUrl + urlHash;
                } else if (!loginRedirectSet) {
                    if (userServiceResponse.isDefaultHTML()) {
                        response.sendRedirect(RedirectUtil.getURL(request, "/a/home"));
                        return;
                    }
                }
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
            Database.closeConnection(conn);
        }
    }

%>
<body>
<div class="container">
    <div class="row">

        <div class="col-md-6 col-md-offset-3">

            <form class="well" method="post" action="/app/loginAction.jsp" id="loginForm" onsubmit="preserveHash()">
                <input type="hidden" name='OWASP_CSRFTOKEN' value="<%= session.getAttribute("OWASP_CSRFTOKEN")%>" />
                <div style="width:100%;text-align: center">
                    <%
                        if (request.getParameter("subdomain") != null) {
                            %>
                    <img src="<%= RedirectUtil.getURL(request, "/app/whiteLabelImage") %>" alt="Logo Image"/>
                    <%
                        } else {
                    %>
                    <img src="/images/logo2.png" alt="Easy Insight Logo"/>
                    <%
                        }
                    %>
                </div>

                <input type="hidden" id="urlhash" name="urlhash"/>

                <label for="userName" class="promptLabel">
                    User Name or Email
                </label>
                <input type="text" class="form-control" name="userName" id="userName" style="width:100%;font-size:14px;height:32px" autocapitalize="off" autocorrect="off" autoFocus/>

                <label for="password" class="promptLabel">
                    Password
                </label>
                <input type="password" class="form-control" name="password" id="password" style="width:100%;font-size:14px;height:32px"/>
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
                <%--<button class="btn" id="googleApps">Sign In With <img src="/images/apps_logo_3D_online_medium.png" alt="Google Apps Login" height="16" width="72" /></button>--%>
                <div class="signInBar" style="padding-top: 10px">
                    <%
                        if (request.getParameter("subdomain") == null) {
                    %>
                    <a href="<%= RedirectUtil.getURL(request, "/app/newaccountb")%>" style="font-size: 12px">No account yet?</a>
                    <% } %>
                    <a href="<%= RedirectUtil.getURL(request, "/app/forgot.jsp")%>" style="font-size: 12px;float:right">Forgot your password?</a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>