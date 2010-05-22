<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: May 3, 2010
  Time: 12:41:53 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="com.easyinsight.connections.database.data.ConnectionInfo" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="org.hibernate.Transaction" %>
<%
    if(session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
    }
    
    Session dataSession = DataConnection.getSession();
    Transaction trans = dataSession.getTransaction();
    try {
        trans.begin();
        ConnectionInfo conn = (ConnectionInfo) dataSession.get(ConnectionInfo.class, Long.parseLong(request.getParameter("id")));
        dataSession.delete(conn);
        trans.commit();
        %>
        <span class="success">Success!</span><script type="text/javascript">refreshDataSources();refreshQueries();</script>
    <%} catch(Exception e) {
        trans.rollback(); %>
        <span class="failure">An error occured in deleting the data connection: <pre>
            <%= e.getMessage() %>
        </pre></span>
    <%
    } finally {
        dataSession.close();
    }
%>