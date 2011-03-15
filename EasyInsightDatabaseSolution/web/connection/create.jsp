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
            <script type="text/javascript">jSuccess("Success!", {HorizontalPosition : 'center', VerticalPosition : 'center'}); </script>
            <script type="text/javascript">refreshDataSources();resetConnectionTab();</script>
        <%} catch(Exception e) {
            trans.rollback(); %>
            <script type="text/javascript">jError("An error occured: <pre><%= e.getMessage() %></pre>", {HorizontalPosition : 'center', VerticalPosition : 'center'});</script>
        <%
        } finally {
            dataSession.close();
        }
    }
}
%>