<%@ page import="org.hibernate.Session" session="true" %>
<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="com.easyinsight.connections.database.data.ConnectionInfo" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: May 10, 2010
  Time: 12:57:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="application/json;charset=UTF-8" language="java" %>
<%
    if(session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
    }
    Session dataSession = DataConnection.getSession();
    try {
        ConnectionInfo c = (ConnectionInfo) dataSession.get(ConnectionInfo.class, Long.parseLong(request.getParameter("id")));
        %>
            <%= c.toJSON() %>
        <%
    } finally {
        dataSession.close();
    }
%>