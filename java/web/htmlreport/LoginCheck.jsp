<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.users.User" %>
<%
    String userName = request.getParameter("userName");
    String password = request.getParameter("password");
    org.hibernate.Session hibernateSession = com.easyinsight.database.Database.instance().createSession();
    try {
        java.util.List results = hibernateSession.createQuery("from User where userName = ?").setString(0, userName).list();
        if(results.size() != 1) {
            response.sendRedirect("jquerytest.jsp?error=true");
            return;
        } else {
            com.easyinsight.users.User user =(com.easyinsight.users.User) results.get(0);
            String encryptedPass = com.easyinsight.security.PasswordService.getInstance().encrypt(password, user.getHashSalt(), user.getHashType());
            if(!encryptedPass.equals(user.getPassword())) {
                response.sendRedirect("jquerytest.jsp?error=true");
                return;
            }
            com.easyinsight.users.Account account = user.getAccount();
            long accountID = account.getAccountID();
            session.setAttribute("accountID", accountID);
            session.setAttribute("userID", user.getUserID());
            session.setAttribute("accountType", account.getAccountType());
            session.setAttribute("userName", userName);
            response.sendRedirect("menu.jsp");
            return;
        }
    } finally {
        hibernateSession.close();
    }
%>