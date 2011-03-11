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
    UploadResult result = new UploadResult();
    try {
        dataSession = DataConnection.getSession();
        Query q = ((Query) dataSession.get(Query.class, Long.parseLong(request.getParameter("id"))));
        result.setQuery(q);
        Transaction t = dataSession.beginTransaction();
        try {
            result.setStartTime(new Date());
            q.doUpload();
            result.setEndTime(new Date());
            result.setSuccess(true);
            dataSession.save(result);
            t.commit();
        } catch(Exception e) {
            e.printStackTrace();
            result.setEndTime(new Date());
            result.setSuccess(false);
            result.setMessage(e.getMessage().substring(0, Math.min(4096, e.getMessage().length())));
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            result.setStackTrace(sw.toString().substring(0, Math.min(4096, sw.toString().length())));
            dataSession.save(result);
            t.commit();
            throw e;
        }

    %>
        <script type="text/javascript">jSuccess("Success!", {HorizontalPosition : 'center', VerticalPosition : 'center'}); </script>
    <%} catch(Exception e) {

        %><script type="text/javascript">jError("An error occured: <pre><%= e.getMessage() %></pre>", {HorizontalPosition : 'center', VerticalPosition : 'center'});</script><%
    } finally {
        if(dataSession != null)
            dataSession.close();
    }
  }
%>