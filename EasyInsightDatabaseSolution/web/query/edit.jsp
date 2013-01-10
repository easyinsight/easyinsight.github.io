<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="com.easyinsight.connections.database.data.Query" %>
<%@ page import="sun.misc.Regexp" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="net.minidev.json.JSONObject" %>
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
        %>{ "error": "Error" }<%
    } else {
    Session dataSession = DataConnection.getSession();
    try {
        Query q = (Query) dataSession.get(Query.class, Long.parseLong(request.getParameter("id")));
        JSONObject j = new JSONObject();
        j.put("id", q.getId());
        j.put("name", q.getName());
        j.put("connectionId", q.getConnectionInfo().getId());
        j.put("query", q.getQuery());
        j.put("dataSource", q.getDataSource());
        j.put("schedule", q.isSchedule());
        j.put("append", q.isAppend());
        %>

        <%= j %>
        <%
    } finally {
        dataSession.close();
    }
}
%>