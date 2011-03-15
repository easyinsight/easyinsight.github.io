<%@ page import="com.easyinsight.connections.database.data.Query" %>
<%@ page import="java.util.List" %>
<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.connections.database.data.UploadResult" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: May 3, 2010
  Time: 1:00:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    if(session.getAttribute("user") == null) {
        %><jsp:include page="../error.jsp" /><%
    } else {
        Session dataSession = DataConnection.getSession();
        try {
    List<Query> queries = Query.all(dataSession);
if(queries.size() == 0) { %>
    There are no queries yet. Make your first query by clicking on the button above.
<% } else { %>
    <table>
        <thead>
            <tr><th></th><th>Connection Name</th><th>Query Name</th><th>Data Source Info</th><th class="query">Query</th><th class="scheduled">Scheduled?</th><th class="append">Append Mode</th><th>Last Upload</th><th class="controls"></th></tr>
        </thead>
        <tbody>
    <% for(Query query: queries) { %>
    <% UploadResult result = null;
            if(query.getUploadResults().size() > 0) {
                result = query.getUploadResults().get(0);
            }
    %>
        <tr>
            <td><img class="statusImage" src="images/<%= result == null ? "bullet_square_grey.png" : (result.isSuccess() ? "bullet_ball_green.png" : "bullet_square_glass_red.png") %>" /></td>
            <td><%= query.getConnectionInfo().getName()%></td><td><%= query.getName() %></td><td><%= query.getDataSource() %></td><td class="query"><%= query.getQuery() %></td><td class="scheduled"><%= query.isSchedule() ? "yes" : "no" %></td><td class="append"><%= query.isAppend() ? "append" : "replace" %></td>
            <td><%= result != null ? format.format(result.getStartTime()) : "Never" %></td>
            <td class="controls"><a href="query/upload.jsp" onclick="uploadQuery('<%= query.getId() %>');return false">Upload</a> <a href="query/edit.jsp" onclick="editQuery('<%= query.getId() %>');return false;">Edit</a><br /><a href="query/test.jsp" onclick="testIdQuery('<%= query.getId() %>');return false;">Test</a> <a href="query/delete.jsp" onclick="deleteQueryWithConfirm('<%= query.getId() %>');return false;">Delete</a><% if(result != null && !result.isSuccess()) { %><div class="errorDialog"><%= result.getMessage() %><br /><pre><%= result.getStackTrace() %></pre></div><% } %></td>
        </tr>
    <% } %>
        </tbody>
    </table>
    <script type="text/javascript">prepDialogs();</script>
<% }
}
finally {
    dataSession.close();
}
} 
%>