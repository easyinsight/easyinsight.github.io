<%@ page import="com.easyinsight.connections.database.data.SecurityUser" %>
<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="org.hibernate.Transaction" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: May 12, 2010
  Time: 12:58:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%
    if(session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
    }
    SecurityUser user = (SecurityUser) session.getAttribute("user");
    if(!request.getParameter("newPassword").equals(request.getParameter("confirmPassword"))) {
      %>
        <span class="failure">New passwords did not match.</span>
      <%
    } else if(!user.matchPassword(request.getParameter("currentPassword"))) {
        %>
        <span class="failure">Wrong password.</span>
        <%
    } else {
        Session s = DataConnection.getSession();
        Transaction trans = s.getTransaction();
        try {
            trans.begin();
            SecurityUser newUser = (SecurityUser) s.get(SecurityUser.class, user.getId());
            newUser.createPassword(request.getParameter("newPassword"));
            s.persist(newUser);
            trans.commit(); %>
            <span class="success">Success! You will need to use the new password next time you log in.</span>
        <%
        }
        catch(Exception e) {
            trans.rollback();
            %><span class="failure">An error occured: <pre><%= e.getMessage() %></pre></span><%
        }
        finally {
            s.close();
        }
    }

%>