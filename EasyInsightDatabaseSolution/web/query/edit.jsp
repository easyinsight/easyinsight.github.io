<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="com.easyinsight.connections.database.data.Query" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: May 5, 2010
  Time: 11:50:52 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="application/json;charset=UTF-8" language="java" session="true" %>
<%
    if(session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
    }
    Session dataSession = DataConnection.getSession();
    try {
        Query q = (Query) dataSession.get(Query.class, Long.parseLong(request.getParameter("id")));
        %>
            {
                "id": <%= q.getId() %>,
                "name": "<%= q.getName() %>",
                "connectionId": <%= q.getConnectionInfo().getId() %>,
                "query": "<%= q.getQuery() %>",
                "dataSource": "<%= q.getDataSource() %>",
                "schedule": <%= q.isSchedule() %>,
                "append": <%= q.isAppend() %>
            }
        <%
    } finally {
        dataSession.close();
    }
%>