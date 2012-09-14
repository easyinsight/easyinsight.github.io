<!DOCTYPE html>
<%@ page import="com.easyinsight.connections.database.data.SecurityUser" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: May 11, 2010
  Time: 2:00:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<% if (SecurityUser.count() == 0) {
    response.sendRedirect("user/new.jsp");
}
    if (request.getParameter("user") != null && request.getParameter("password") != null) {
        boolean match = false;
        Session s = DataConnection.getSession();
        try {
            List<SecurityUser> users = s.createQuery("from SecurityUser where username = ?").setString(0, request.getParameter("user")).list();
            if (users.size() == 1 && users.get(0).matchPassword(request.getParameter("password"))) {
                session.setAttribute("user", users.get(0));
                match = true;
            }
        } finally {
            s.close();
        }
        if (match) {
            response.sendRedirect("index.jsp");
        }

    }
%>

<html>
<head>
    <title>Log In</title>
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <link rel="stylesheet" href="css/bootstrap.min.css"/>
    <script src="js/bootstrap.min.js" language="javascript" type="text/javascript"></script>

    <style type="text/css">
        body {
            margin: 10px;
        }

        a {
            color: #CC0033;
        }

        form label {
            width: 100px;
            display: inline-block;
        }

        form .textBox {
            width: 250px;
        }

        label {
            margin-bottom: 0px;
            margin-top: 5px;
        }

        #mainwell {
            margin-top: 45px;
        }

    </style>
</head>
<body>
<div class="container">
    <div class="well" id="mainWell">
        <div class="row"><div class="offset4"><img style="padding:10px;" src="../images/logo2.PNG" /></div></div>
            <p class="row"><span style="text-align:left" class="offset1 span6">Please log in.</span></p>

        <form action="login.jsp" method="post">
            <div class="row"><label class="offset1 span2" for="user">Username:</label> <input id="user" class="textBox span3" type="text" name="user"/></div>
            <div class="row"><label class="offset1 span2" for="password">Password:</label> <input id="password" class="textBox span3" type="password"
                                                           name="password"/></div>
            <div class="row"><div class="offset1 span2"><input type="submit" class="btn btn-inverse" value="Log In"/></div></div>
        </form>
        <div class="row"><h4 class="offset1 span6">What is the Easy Insight Database Connection?</h4></div>

        <div class="row"><p class="offset1 span10">The Easy Insight Database Connection is a Java web application that facilitates importing data from your
            database into <a href="https://www.easy-insight.com/app">Easy Insight</a>.</p></div>

        <div class="row"><p class="offset1 span10">Start by logging in above to create connections and schedule uploads from your database in order to get
            immediate visibility and reporting!</p></div>
    </div>
</div>
</body>
</html>