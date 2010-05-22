<%@ page import="com.easyinsight.connections.database.data.ConnectionInfo" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: May 3, 2010
  Time: 1:53:19 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="application/json;charset=UTF-8" language="java" session="true" %>
<%
    if(session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
    }
    boolean first = true; %>
[<% for(ConnectionInfo conn: ConnectionInfo.all()) { %><%= first ? "" : ", "%> <%= conn.toJSON() %><%
        first = false;
    } %>]