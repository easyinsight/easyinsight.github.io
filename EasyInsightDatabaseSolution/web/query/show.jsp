<%@ page import="com.easyinsight.connections.database.data.Query" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: May 3, 2010
  Time: 1:00:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    if(session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
    }
    List<Query> queries = Query.all();
if(queries.size() == 0) { %>
    There are no queries yet. Make your first query below!
<% } else { %>
    <table>
        <thead>
            <tr><th>Connection Name</th><th>Query Name</th><th>Data Source Info</th><th class="query">Query</th><th class="scheduled">Scheduled?</th><th class="append">Append Mode</th><th class="controls"></th></tr>
        </thead>
        <tbody>
    <% for(Query query: queries) { %>
        <tr>
            <td><%= query.getConnectionInfo().getName()%></td><td><%= query.getName() %></td><td><%= query.getDataSource() %></td><td class="query"><%= query.getQuery() %></td><td class="scheduled"><%= query.isSchedule() ? "yes" : "no" %></td><td class="append"><%= query.isAppend() ? "append" : "replace" %></td><td class="controls"><a href="query/upload.jsp" onclick="uploadQuery('<%= query.getId() %>');return false">Upload</a> <a href="query/edit.jsp" onclick="editQuery('<%= query.getId() %>');return false;">Edit</a><br /><a href="query/test.jsp" onclick="testIdQuery('<%= query.getId() %>');return false;">Test</a> <a href="query/delete.jsp" onclick="deleteQueryWithConfirm('<%= query.getId() %>');return false;">Delete</a></td>
        </tr>
    <% } %>
        </tbody>
    </table>
<% } %>