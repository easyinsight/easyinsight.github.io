<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <link type="text/css" rel="stylesheet" media="screen" href="login.css"/>
    <link href='https://fonts.googleapis.com/css?family=PT+Sans' rel='stylesheet' type='text/css'/>
    <link href='https://fonts.googleapis.com/css?family=Cabin:regular,bold' rel='stylesheet' type='text/css'>
</head>
<body style="width:100%;text-align:center;margin:0 auto;background-color: #F2EDED;font-family: 'Cabin',Arial,sans-serif">
<div style="width:410px;margin:0 auto;">
<div style="position: relative">
<div style="position:absolute;top:50%;height:300px;margin-top:150px;background-image:url('assets/login_background2.png');background-repeat: no-repeat;padding-top:10px;padding-left: 20px;padding-right: 20px">
    <img src="/images/logo2.PNG"/>
    <form method="post" action="forgotPasswordAction.jsp" style="width:100%">
        <p style="text-align: left;width: 350px">
            <label for="email" style="font-weight: bold; color: #333333">
                What email address do you use to sign in to Easy Insight?
            </label>
            <input type="text" name="email" id="email" style="width:370px"/>
        </p>
        <%
            String result = request.getParameter("result");
            if ("emailed".equals(result)) {
                %>
        <p style="text-align: left;width: 350px;color:#333333">
            An email has been sent to reset your password.
        </p>
                <%
            } else if ("noEmail".equals(result)) {
                %>
        <p style="text-align: left;width: 350px;color:#333333">
            The email you specified was not found.
        </p>
            <%
            }
        %>
        <div style="text-align: center;padding-top: 10px;width: 400px" class="signInBar">
            <input class="loginButton" type="submit" alt="Submit" value="Submit"/>
        </div>
        <div style="text-align: center;padding-top: 10px;width: 400px" class="signInBar">
            <a href="login.jsp" style="font-size: 12px">Back to Sign In</a>
        </div>
    </form>
</div>
</div>
</div>
</body>
</html>