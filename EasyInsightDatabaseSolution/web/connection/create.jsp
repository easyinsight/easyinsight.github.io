<%@ page import="com.easyinsight.connections.database.data.ConnectionInfo" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="org.hibernate.Transaction" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: Apr 30, 2010
  Time: 2:03:06 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    if(session.getAttribute("user") == null) {
        %><jsp:include page="../error.jsp" /><%
    } else {
    ConnectionInfo conn = ConnectionInfo.createConnectionInfo(request.getParameterMap());
    if(conn != null) {
        Session dataSession = DataConnection.getSession();
        Transaction trans = dataSession.getTransaction();
        try {
            trans.begin();
            dataSession.save(conn);
            trans.commit();
            %>
            <span class="success">Success!</span><script type="text/javascript">refreshDataSources();</script>
        <%} catch(Exception e) {
            trans.rollback(); %>
            <span class="failure">An error occured in saving the data connection: <pre>
                <%= e.getMessage() %>
            </pre></span>
        <%
        } finally {
            dataSession.close();
        }
    }
}
%>