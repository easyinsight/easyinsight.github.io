<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="com.easyinsight.connections.database.data.ConnectionInfo" %>
<%@ page import="com.easyinsight.connections.database.data.Query" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="org.hibernate.Transaction" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: Apr 27, 2010
  Time: 11:12:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
  <%
      if(session.getAttribute("user") == null) {
        %><jsp:include page="../error.jsp" /><%
      } else {
      Query q = new Query(request.getParameterMap());
      Session dataSession = DataConnection.getSession();
      Transaction trans = dataSession.getTransaction();
      try {
          trans.begin();
          q.setConnectionInfo(ConnectionInfo.instance());
          dataSession.save(q);
          trans.commit(); %>
    <script type="text/javascript">jSuccess("Success!", {HorizontalPosition : 'center', VerticalPosition : 'center'}); </script>
    <script type="text/javascript">refreshQueries();$('#createQuery')[0].reset();$('#createQuery').hide();$('#newQueryButton').show();$("#queryList").show();</script>
    <%
      } catch(Exception e) {
          trans.rollback();
          %>
        <script type="text/javascript">jError("An error occured: <pre><%= e.getMessage() %></pre>", {HorizontalPosition : 'center', VerticalPosition : 'center'});</script>
    <%
      } finally {
          dataSession.close();
      }
    }
  %>