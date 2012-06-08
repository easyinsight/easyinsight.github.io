<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.users.User" %>
<%@ page import="com.easyinsight.users.InternalUserService" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Sign In</title>
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
    <script type="text/javascript">
        function preserveHash() {
            $('input[name=urlhash]').val(window.location.hash);
        }
    </script>
</head>
<%
    if(request.getSession().getAttribute("accountID") != null) {
        response.sendRedirect("index.jsp");
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
            User user = new InternalUserService().validateCookie(cookieValue, userName, conn, hibernateSession);
            if (user != null) {
                long accountID = user.getAccount().getAccountID();
                session.setAttribute("accountID", accountID);
                session.setAttribute("userID", user.getUserID());
                session.setAttribute("accountType", user.getAccount().getAccountType());
                session.setAttribute("userName", userName);
                session.setAttribute("dayOfWeek", user.getAccount().getFirstDayOfWeek());
                response.addCookie(new Cookie("eiUserName", userName));
                response.addCookie(new Cookie("eiRememberMe", new InternalUserService().createCookie(user.getUserID(), conn)));
                response.sendRedirect("/app/");
            }
            conn.commit();
            if (user != null) {
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

            <form class="well" method="post" action="loginAction.jsp" style="width:100%" id="loginForm" onsubmit="preserveHash()">
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

                <%
                    if (request.getParameter("error") != null) {
                %>
                <p class="formAreaP" style="font-size: 12px;padding: 0;margin-bottom: 5px">We didn't recognize the username or password you entered.</p>
                <%
                    }
                %>
                <label class="checkbox">
                    <input type="checkbox" id="rememberMeCheckbox" name="rememberMeCheckbox">Remember me on this computer
                </label>
                <button class="btn btn-inverse" type="submit" value="Sign In">Sign In</button>
                <div class="signInBar" style="padding-top: 10px">
                    <a href="newaccount" style="font-size: 12px">No account yet?</a>
                    <a href="forgot.jsp" style="font-size: 12px;float:right">Forgot your password?</a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>