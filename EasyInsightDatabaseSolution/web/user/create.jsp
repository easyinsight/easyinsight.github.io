<%@ page import="com.easyinsight.connections.database.data.SecurityUser" %>
<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="org.hibernate.Transaction" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: May 11, 2010
  Time: 7:36:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<html>
  <head>
      <title>Create User</title>
  </head>
  <body>
<%
    if(SecurityUser.count() > 0) {
        response.sendRedirect("../login.jsp");
    }

    SecurityUser user = new SecurityUser();
    user.setUsername(request.getParameter("username"));
    user.createPassword(request.getParameter("password"));
    Session s = DataConnection.getSession();
    Transaction trans = s.getTransaction();
    try {
        trans.begin();
        s.persist(user);
        trans.commit();
        session.setAttribute("user", user);
        %>Success! Proceed to the <a href="../index.jsp">main page</a> now.<%
    } catch(Exception e) {
        trans.rollback();
        %>An error occurred while creating the user...<a href="new.jsp">Try again.</a> Details:<pre><%= e.getMessage() %></pre> <%
    }
    finally {
        s.close();
    }
%>
    </body>
</html>