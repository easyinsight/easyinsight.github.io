<%@ page import="com.easyinsight.connections.database.data.Query" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="com.easyinsight.connections.database.data.ConnectionInfo" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: Apr 29, 2010
  Time: 10:52:15 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    if(session.getAttribute("user") == null) {
        %><jsp:include page="../error.jsp" /><%
    } else {
    try {
    Query q = null;
    Session dataSession = DataConnection.getSession();
    try {
        if(request.getParameter("id") != null && !request.getParameter("id").isEmpty() && !"edit".equals(request.getParameter("edit"))) {
            q = (Query) dataSession.get(Query.class, Long.parseLong(request.getParameter("id")));
        } else {
            q = new Query(request.getParameterMap());
            q.setConnectionInfo(ConnectionInfo.instance());
        }
    } finally {
        dataSession.close();
    }
    Connection conn = q.getConnectionInfo().createConnection();
    try {
        conn.setAutoCommit(false);
        ResultSet rs = q.executeQuery(conn, 5);
        %>
        <h2>Test Results</h2>
        <div>
            <table cellspacing="0">
                <thead>
                    <%
                        for(int column = 1;column <= rs.getMetaData().getColumnCount();column++) {
                            %>
                            <th>
                                <%= rs.getMetaData().getColumnName(column) %>
                            </th>
                            <%
                        }
                    %>
                </thead>
            <%
            while(rs.next()) {
            %>
                <tr>
                    <% for(int column = 1;column <= rs.getMetaData().getColumnCount();column++) { %>
                    <td>
                        <%= String.valueOf(rs.getObject(column)) %>
                    </td>
                    <% } %>
                </tr>
            <%
            }
            %>
            </table>
        </div>
        <button onclick="$('#queryResults').html('');">Clear</button>
        <%
    }
    finally {
        conn.close();
    }
}
    catch(Exception e) { %>
    <div id="errorDiv">
        There was an error: <%= e.getMessage() %>
    </div>
    <% }
    }%>