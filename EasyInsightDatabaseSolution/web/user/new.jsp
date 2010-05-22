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
    if(SecurityUser.count() > 0) {
        response.sendRedirect("../login.jsp");
    }
%>
<html>
  <head>
      <title>Create User</title>
  </head>
  <body>
    <p>It appears you haven't created a user to secure your system yet.
       Enter a username and password below to ensure only authorized users can access your data.</p>
    <form action="create.jsp">
        Username: <input type="text" name="username" /><br />
        Password: <input type="password" name="password" /><br />
        <input type="submit" />
    </form>
  </body>
</html>