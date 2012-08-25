<%@ page import="com.easyinsight.connections.database.data.EIUser" %>
<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="org.hibernate.Transaction" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: Apr 27, 2010
  Time: 4:52:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    if(session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
    }
    
    if(request.getParameter("publicKey") != null && !request.getParameter("publicKey").isEmpty() &&
            request.getParameter("secretKey") != null && !request.getParameter("secretKey").isEmpty()) {
        EIUser user = EIUser.instance();
        if(user == null)
            user = new EIUser();
        user.setPublicKey(request.getParameter("publicKey"));
        user.setSecretKey(request.getParameter("secretKey"));
        if(user.validateCredentials()) {
            Session dataSession = DataConnection.getSession();
            Transaction trans = dataSession.getTransaction();
            try {
                trans.begin();
                dataSession.saveOrUpdate(user);
                trans.commit();
            } catch(Exception e) {
                trans.rollback();
                throw new RuntimeException(e);
            }
            finally {
                dataSession.close();
            }
%>
            <script type="text/javascript">jSuccess("Success!", {HorizontalPosition : 'center', VerticalPosition : 'center'}); </script>
<%      } else { %>
            <script type="text/javascript">jSuccess("Failure!", {HorizontalPosition : 'center', VerticalPosition : 'center'}); </script>
<%      }

    }
%>