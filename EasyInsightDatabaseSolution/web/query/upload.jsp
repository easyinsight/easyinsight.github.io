<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="com.easyinsight.connections.database.data.Query" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: May 4, 2010
  Time: 10:08:36 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    if(session.getAttribute("user") == null) {
        %><jsp:include page="../error.jsp" /><%
    } else {
    Session dataSession = null;
    try {
        dataSession = DataConnection.getSession();
        ((Query) dataSession.get(Query.class, Long.parseLong(request.getParameter("id")))).doUpload();
    %>
        <span class="success">Success!</span>
    <%} catch(Exception e) {
        %><span class="failure">An error occured: <pre><%= e.getMessage() %></pre></span><%
    } finally {
        if(dataSession != null)
            dataSession.close();
    }
  }
%>