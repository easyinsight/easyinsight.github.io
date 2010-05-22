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
        response.sendRedirect("login.jsp");
      }
      Query q = new Query(request.getParameterMap());
      Session dataSession = DataConnection.getSession();
      Transaction trans = dataSession.getTransaction();
      try {
          trans.begin();
          q.setConnectionInfo((ConnectionInfo) dataSession.get(ConnectionInfo.class, Long.parseLong(request.getParameter("queryConnection"))));
          dataSession.save(q);
          trans.commit(); %>
    <span class="success">Success!</span><script type="text/javascript">refreshQueries();$('#editQuery')[0].reset();$('#editQuery').hide();$('#newQueryButton').show();</script>
    <%
      } catch(Exception e) {
          trans.rollback();
          %>
        <span class="failure">An error occured: <pre><%= e.getMessage() %></pre></span>
    <%
      } finally {
          dataSession.close();
      }
  %>