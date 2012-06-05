<%@ page import="java.util.Enumeration" %>
<%@ page import="com.easyinsight.users.InternalUserService" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.users.User" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <link type="text/css" rel="stylesheet" media="screen" href="/app/login.css"/>
    <script type="text/javascript" src="/js/jquery.min.js"></script>
    <link href='https://fonts.googleapis.com/css?family=PT+Sans' rel='stylesheet' type='text/css'/>
    <link href='https://fonts.googleapis.com/css?family=Cabin:regular,bold' rel='stylesheet' type='text/css'>
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
<div style="width:410px;margin:0 auto;">
<div style="position: relative">
<div class="formArea">
    <img src="/images/logo2.PNG"/>
    <form method="post" action="loginAction.jsp" style="width:100%" id="loginForm" onsubmit="preserveHash()">
        <input type="hidden" id="urlhash" name="urlhash"/>
        <p class="formAreaP">
            <label for="userName" class="promptLabel">
                User Name or Email
            </label>
            <input type="text" name="userName" id="userName" style="width:370px" autocapitalize="off" autocorrect="off" autoFocus/>
        </p>
        <p class="formAreaP" style="margin-bottom: 10px">
            <label for="password" class="promptLabel">
                Password
            </label>
            <input type="password" name="password" id="password" style="width:370px"/>
        </p>
        <%
            if (request.getParameter("error") != null) {
                %>
        <p class="formAreaP" style="font-size: 12px;padding: 0;margin-bottom: 5px">We didn't recognize the username or password you entered.</p>
                <%
            }
        %>
        <div id="rememberMe">
            <label>
                <input type="checkbox" id="rememberMeCheckbox" name="rememberMeCheckbox"/>
                Remember me on this computer
            </label>
        </div>
        <div class="signInBar">
            <input class="loginButton" type="submit" alt="Sign In" value="Sign In" style="margin-right: 195px"/>
            <a href="forgot.jsp" style="font-size: 12px">Forgot your password?</a>
        </div>
    </form>
</div>
</div>
</div>
</body>
</html>