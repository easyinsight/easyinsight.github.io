<%@ page import="com.easyinsight.connections.database.data.ConnectionInfo" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="org.hibernate.Transaction" %>
<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: May 10, 2010
  Time: 2:47:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    if(session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
    }
    Session dataSession = DataConnection.getSession();
    Transaction trans = dataSession.getTransaction();
    try {
        trans.begin();
        ConnectionInfo connection = (ConnectionInfo) dataSession.get(ConnectionInfo.class, Long.parseLong(request.getParameter("id")));
        connection.update(request.getParameterMap());
        dataSession.save(connection);
        trans.commit();
        %>
        <span class="success">Success!</span><script type="text/javascript">refreshDataSources();resetConnectionTab();</script>
    <%} catch(Exception e) {
        trans.rollback(); %>
        <span class="failure">An error occured in updating the query: <pre>
            <%= e.getMessage() %>
        </pre></span>
    <%
    } finally {
        dataSession.close();
    }
%>