<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="com.easyinsight.connections.database.data.Query" %>
<%@ page import="org.hibernate.Transaction" %>
<%@ page import="com.easyinsight.connections.database.data.UploadResult" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.io.StringWriter" %>
<%@ page import="java.io.PrintWriter" %>
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

        Transaction t = dataSession.beginTransaction();
        try {
            Query q = ((Query) dataSession.get(Query.class, Long.parseLong(request.getParameter("id"))));
            q.doUpload(dataSession);
        } finally {
            t.commit();
        }

    %>
        <script type="text/javascript">refreshQueries();jSuccess("Success!", {HorizontalPosition : 'center', VerticalPosition : 'center'}); </script>
    <%} catch(Exception e) {

        %><script type="text/javascript">refreshQueries();jError("An error occured: <pre><%= e.getMessage() %></pre>", {HorizontalPosition : 'center', VerticalPosition : 'center'});</script><%
    } finally {
        if(dataSession != null)
            dataSession.close();
    }
  }
%>