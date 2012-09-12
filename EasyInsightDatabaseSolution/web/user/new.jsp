<%@ page import="com.easyinsight.connections.database.data.SecurityUser" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: May 11, 2010
  Time: 7:32:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    if (SecurityUser.count() > 0) {
        response.sendRedirect("../login.jsp");
    }
%>
<html>
<head>
    <title>Create User</title>
    <link rel="stylesheet" href="../css/bootstrap.min.css"/>
    <script src="../js/jquery.min.js" language="javascript" type="text/javascript"></script>
    <script src="../js/bootstrap.min.js" language="javascript" type="text/javascript"></script>
    <style>
        #mainwell {
            margin-top: 45px;
        }
    </style>
</head>
<body>
<div class="container">
<div class="well" id="mainwell">
    <div class="row"><div class="offset4"><img style="padding:10px;" src="../images/logo2.PNG" /></div></div>
    <p class="row"><span style="text-align:left" class="offset3 span6">It appears you haven't created a user to secure your system yet.
        Enter a username and password below to ensure only authorized users can access your data.</span></p>

    <form action="create.jsp" method="post">
        <div class="row"><span class="span2 offset3">Username:</span><span class="span4"><input type="text" class="span4" name="username"/></span></div>
        <div class="row"><span class="span2 offset3">Password:</span><span class="span4"><input type="password" class="span4" name="password"/></span></div>
        <div class="row"><div class="span4 offset4"><input class="btn btn-inverse" type="submit"/></div></div>
    </form>
</div>
</div>
</body>
</html>