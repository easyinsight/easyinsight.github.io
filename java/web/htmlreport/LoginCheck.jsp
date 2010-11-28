<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.users.User" %>
<%
    String userName = request.getParameter("userName");
    String password = request.getParameter("password");
    org.hibernate.Session hibernateSession = com.easyinsight.database.Database.instance().createSession();
    try {
        String encryptedPass = com.easyinsight.security.PasswordService.getInstance().encrypt(password);

        java.util.List results = hibernateSession.createQuery("from User where userName = ? and password = ?").setString(0, userName).setString(1, encryptedPass).list();
        if(results.size() != 1) {
            response.sendRedirect("jquerytest.jsp?error=true");
            return;
        } else {
            com.easyinsight.users.User user =(com.easyinsight.users.User) results.get(0);
            com.easyinsight.users.Account account = user.getAccount();
            long accountID = account.getAccountID();
            session.setAttribute("accountID", accountID);
            session.setAttribute("userID", user.getUserID());
            session.setAttribute("accountType", account.getAccountType());
            session.setAttribute("userName", userName);
            response.sendRedirect("dataSources.jsp");
            return;
        }
    } finally {
        hibernateSession.close();
    }
%>