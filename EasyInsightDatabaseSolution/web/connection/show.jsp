<%@ page import="com.easyinsight.connections.database.data.ConnectionInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="com.easyinsight.connections.database.data.Query" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: Apr 30, 2010
  Time: 2:17:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    if(session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
    }
    List<ConnectionInfo> connections = ConnectionInfo.all();
if(connections.size() == 0) { %>
    There are no connections yet. Make your first connection below!
<% } else { %>
    <table>
        <thead>
            <tr><th>Name</th><th>Data Source Type</th><th>Username</th><th>Data Source Info</th><th></th></tr>
        </thead>
        <tbody>
    <% for(ConnectionInfo info : connections) { %>
        <tr>
            <td><%= info.getName() %></td><td><%= info.typeName() %></td><td><%= info.getUsername() %></td><td><%= info.sourceInfo() %></td><td><a href="connection/edit.jsp" onclick="editConnection('<%= info.getId() %>');return false;">Edit</a> <a href="connection/delete.jsp" onclick="deleteConnection('<%= info.getId() %>');return false;">Delete</a></td>
        </tr>
    <% } %>
        </tbody>
    </table>
<% } %>