<%@ page import="com.easyinsight.connections.database.data.Query" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="com.easyinsight.connections.database.data.ConnectionInfo" %>
<%@ page import="com.easyinsight.connections.database.data.FieldInfo" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: Apr 29, 2010
  Time: 10:52:15 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    if (session.getAttribute("user") == null) {
%>
<jsp:include page="../error.jsp"/>
<%
} else {
    try {
        Query q = null;
        Session dataSession = DataConnection.getSession();
        try {
            if (request.getParameter("id") != null && !request.getParameter("id").isEmpty()) {
                q = (Query) dataSession.get(Query.class, Long.parseLong(request.getParameter("id")));
                q.discoverFields(dataSession);
            }
        } finally {
            dataSession.close();
        }
%>
<h2>Test Results</h2>

<div>
    <form action="update_fields.jsp" method="post">
        <%
            int i = 0;
            for(FieldInfo ff : q.getFieldInfos()) { %>
            <input type="hidden" name="field_id_<%= i %>" value="<%= ff.getId() %>" />
            <p><%= ff.getColumnName() %></p>
            <p>Field Name: <input type="text" name="field_name_<%= i %>" value="<%= ff.getFieldName() %>" /></p>
            <p>Type: <select name="field_type_<%= i %>">
                <option value="<%= FieldInfo.DEFAULT %>" <% if(ff.getType() == FieldInfo.DEFAULT) { %>selected="selected"<% } %>>Default</option>
                <option value="<%= FieldInfo.MEASURE %>" <% if(ff.getType() == FieldInfo.MEASURE) { %>selected="selected"<% } %>>Measure</option>
                <option value="<%= FieldInfo.GROUPING %>" <% if(ff.getType() == FieldInfo.GROUPING) { %>selected="selected"<% } %>>Grouping</option>
                <option value="<%= FieldInfo.DATE %>" <% if(ff.getType() == FieldInfo.DATE) { %>selected="selected"<% } %>>Date</option>
            </select></p>
        <%
                i = i + 1;
            } %>

        <input type="submit" />
    </form>
</div>
<%
} catch (Exception e) { %>
<div id="errorDiv">
    There was an error: <%= e.getMessage() %>
</div>
<% }
}%>