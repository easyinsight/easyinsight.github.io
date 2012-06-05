<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <link type="text/css" rel="stylesheet" media="screen" href="/css/login.css"/>
    <link href='https://fonts.googleapis.com/css?family=PT+Sans' rel='stylesheet' type='text/css'/>
    <link href='https://fonts.googleapis.com/css?family=Cabin:regular,bold' rel='stylesheet' type='text/css'>
</head>
<%
    String resetPassword = request.getParameter("resetPassword");
    request.getSession().setAttribute("resetPassword", resetPassword);
%>
<body>
<div style="width:410px;margin:0 auto;">
<div style="position: relative">
<div class="formArea">
    <img src="/images/logo2.PNG"/>
    <form method="post" action="resetPasswordAction.jsp" style="width:100%" id="loginForm">
        <p class="formAreaP" style="padding-top:3px;padding-bottom:3px;margin-top:3px;margin-bottom:3px">
            <label for="userName" class="promptLabel">
                User Name or Email
            </label>
            <input type="text" name="userName" id="userName" style="width:370px" autocapitalize="off" autocorrect="off" autoFocus/>
        </p>
        <p class="formAreaP" style="padding-top:3px;padding-bottom:3px;margin-top:3px;margin-bottom:3px">
            <label for="password" class="promptLabel">
                Password
            </label>
            <input type="password" name="password" id="password" style="width:370px"/>
        </p>
        <p class="formAreaP" style="padding-top:3px;padding-bottom:3px;margin-top:3px;margin-bottom:3px">
            <label for="confirmPassword" class="promptLabel">
                Confirm Password
            </label>
            <input type="password" name="confirmPassword" id="confirmPassword" style="width:370px"/>
        </p>
        <%
            String errorString = (String) request.getSession().getAttribute("errorString");
            if (errorString != null) {
                request.getSession().removeAttribute("errorString");
        %>
        <p class="formAreaP" style="font-size: 12px;padding: 0;margin-bottom: 5px"><%= errorString%> </p>
        <%
            }
        %>
        <div class="signInBar">
            <input class="loginButton" type="submit" alt="Reset my Password" value="Reset the Password" style="margin-right: 195px"/>
        </div>
    </form>
</div>
</div>
</div>
</body>
</html>