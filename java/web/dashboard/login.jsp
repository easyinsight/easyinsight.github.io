<%@ page import="java.util.List" %>
<%@ page import="com.easyinsight.security.PasswordService" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.users.User" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%
    boolean success = false;
    Session s = Database.instance().createSession();
    try {
        if(request.getParameter("login") != null && request.getParameter("password") != null) {
            String encryptedPass = PasswordService.getInstance().encrypt(request.getParameter("password"));
            List results = s.createQuery("from User where userName = ? and password = ?").setString(0, request.getParameter("login")).setString(1, encryptedPass).list();
            if(results.size() != 1) {
                success = false;
            }
            else {
                User user =(User) results.get(0);
                Account account = user.getAccount();
                long accountID = account.getAccountID();
                request.getSession().setAttribute("accountID", accountID);
                request.getSession().setAttribute("userID", user.getUserID());
                success = true;
            }
        }
    } finally {
        s.close();
    }
%>
<%  if(success) {
        String scorecardIDString = (String) request.getParameter("scorecardID");
        if (scorecardIDString == null || scorecardIDString.isEmpty() || "null".equals(scorecardIDString)) {
        %>
    <jsp:include page="scorecardList.jsp" />
    <script type="text/javascript">$(document).ready(function(){$("#username").val("");$("#password").val("");$("#loginDialog").dialog('close');})</script>
    <%
        } else {
    %>

        <jsp:include page="table.jsp" />
        <script type="text/javascript">$(document).ready(function(){$("#username").val("");$("#password").val("");$("#loginDialog").dialog('close');})</script>

    <%  }

    } else { %>
        <script type="text/javascript">$("#notice").html("Invalid username or password.");</script>
    <% } %>