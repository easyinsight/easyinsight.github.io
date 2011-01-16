<%@ page import="java.util.List" %>
<%@ page import="com.easyinsight.security.PasswordService" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.users.User" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="org.apache.jcs.JCS" %>
<%@ page import="org.apache.jcs.access.exception.CacheException" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%
    boolean success = false;
    long userID = 0;
    Session s = Database.instance().createSession();
    try {
        if(request.getParameter("login") != null && request.getParameter("password") != null) {
            List results = s.createQuery("from User where userName = ?").setString(0, request.getParameter("login")).list();
            if(results.size() != 1) {
                success = false;
            }
            else {
                User user =(User) results.get(0);
                String encryptedPass = PasswordService.getInstance().encrypt(request.getParameter("password"), user.getHashSalt(), user.getHashType());
                if(!encryptedPass.equals(user.getPassword())) {
                    success = false;
                } else {
                    Account account = user.getAccount();
                    long accountID = account.getAccountID();
                    request.getSession().setAttribute("accountID", accountID);
                    request.getSession().setAttribute("userID", user.getUserID());
                    userID = user.getUserID();
                    success = true;
                }
            }
        }
    } finally {
        s.close();
    }
%>
<%  if(success) {
        String scorecardIDString = (String) request.getParameter("scorecardID");
        if(scorecardIDString == null || scorecardIDString.isEmpty() || "null".equals(scorecardIDString)) {
            try {
                scorecardIDString = (String) JCS.getInstance("scorecardQueue").get(userID);
            } catch (CacheException e) {
                LogClass.error(e);
            }
        }
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