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
<% if(success) {%>
<div>
    <div id="added" style="display:none">
        <table>
            <thead>
            <tr>
                <th></th>
                <th>KPI Name</th>
                <th>Latest Value</th>
                <th>Time</th>
                <th>% Change</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
                <jsp:include page="table.jsp" />
            </tbody>
        </table>
    </div>
</div>
<script type="text/javascript">$(document).ready(function() {$("#loginDialog").dialog('close');$("#scorecard").html($("#added").html())})</script>

<% } else { %>
<script type="text/javascript">$("#notice").html("Invalid username or password.");</script>
<% } %>