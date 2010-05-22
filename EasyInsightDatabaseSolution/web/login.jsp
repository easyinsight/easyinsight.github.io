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
<% if(SecurityUser.count() == 0) {
    response.sendRedirect("user/new.jsp");
}
    if(request.getParameter("user") != null && request.getParameter("password") != null) {
        boolean match = false;
        Session s = DataConnection.getSession();
        try {
            List<SecurityUser> users = s.createQuery("from SecurityUser where username = ?").setString(0, request.getParameter("user")).list();
            if(users.size() == 1 && users.get(0).matchPassword(request.getParameter("password"))) {
                session.setAttribute("user", users.get(0));
                match = true;
            }
        } finally {
            s.close();
        }
        if(match) {
            response.sendRedirect("index.jsp");
        }

    }
%>

<html>
  <head>
      <title>Log In</title>
      <style type="text/css">
          body {
              margin: 10px;
          }
          a {
              color:#CC0033;
          }
      </style>
  </head>
  <body>
    <img src="images/logo.jpg" alt="Easy Insight Logo" /><h1>Database Solution</h1>
    <h3>Please log in.</h3>
    <form action="login.jsp" method="post">
      Username: <input type="text" name="user" /><br />
      Password: <input type="password" name="password" /><br />
      <input type="submit" value="Log In"/>
    </form>
    <h2>What is the Easy Insight Database Solution?</h2>
    <p>The Easy Insight Database Solution is a Java web application that facilitates importing data from your database into <a href="https://www.easy-insight.com/app">Easy Insight</a>.</p>
    <p>Start by logging in above to create connections and schedule uploads from your database in order to get better insight!</p>
  </body>
</html>