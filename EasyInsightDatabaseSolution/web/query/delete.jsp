<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: May 3, 2010
  Time: 12:41:53 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="com.easyinsight.connections.database.data.Query" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="org.hibernate.Transaction" %>
<%
    if(session.getAttribute("user") == null) {
        %><jsp:include page="../error.jsp" /><%
    } else {
    Session dataSession = DataConnection.getSession();
    Transaction trans = dataSession.getTransaction();
    try {
        trans.begin();
        Query query = (Query) dataSession.get(Query.class, Long.parseLong(request.getParameter("id")));
        query.getConnectionInfo().getQueries().remove(query);
        dataSession.delete(query);
        trans.commit();
        %>
        <span class="success">Success!</span><script type="text/javascript">refreshQueries();</script>
    <%} catch(Exception e) {
        trans.rollback(); %>
        <span class="failure">An error occured in deleting the query: <pre>
            <%= e.getMessage() %>
        </pre></span>
    <%
    } finally {
        dataSession.close();
    }
}
%>