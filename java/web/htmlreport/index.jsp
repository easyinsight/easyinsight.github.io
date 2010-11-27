<%@ page import="com.easyinsight.analysis.AnalysisService" %>
<%@ page import="com.easyinsight.analysis.WSAnalysisDefinition" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.users.User" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.export.ExportService" %>
<%@ page import="com.easyinsight.analysis.InsightResponse" %>
<%@ page import="java.util.List" %>
<%@ page import="com.easyinsight.security.PasswordService" %>
<%@ page import="com.easyinsight.users.Account" %>
<html>
<head>

</head>
<body>
    <%
        Long userID = (Long) request.getSession().getAttribute("userID");
        String reportURLKey = request.getParameter("reportID");
        if(request.getParameter("username") != null && request.getParameter("password") != null) {
            Session hibernateSession = Database.instance().createSession();
            try {
                String encryptedPass = com.easyinsight.security.PasswordService.getInstance().encrypt(request.getParameter("password"));

                java.util.List results = hibernateSession.createQuery("from User where userName = ? and password = ?").setString(0, request.getParameter("username")).setString(1, encryptedPass).list();
                if(results.size() != 1) {
                    response.sendRedirect("login.jsp?error=true&reportID=" + reportURLKey);
                    return;
                }
                User user =(User) results.get(0);
                com.easyinsight.users.Account account = user.getAccount();
                long accountID = account.getAccountID();
                request.getSession().setAttribute("accountID", accountID);
                request.getSession().setAttribute("userID", user.getUserID());
                SecurityUtil.populateThreadLocal(user.getUserName(), user.getUserID(), user.getAccount().getAccountID(), user.getAccount().getAccountType(), false, false);
            } finally {
                hibernateSession.close();
            }
        } else if (userID == null) {
            response.sendRedirect("login.jsp?reportID=" + reportURLKey);
            return;
        } else {
            Session hibernateSession = Database.instance().createSession();
            try {
                User u = (com.easyinsight.users.User)hibernateSession.get(com.easyinsight.users.User.class, userID);
                SecurityUtil.populateThreadLocal(u.getUserName(), userID, u.getAccount().getAccountID(), u.getAccount().getAccountType(), false, false);
            } finally {
                hibernateSession.close();
            }
        }

        EIConnection conn = Database.instance().getConnection();
        int dateFormat = 0;
        try {
            PreparedStatement dateFormatStmt = conn.prepareStatement("SELECT DATE_FORMAT FROM ACCOUNT WHERE ACCOUNT_ID = ?");
            dateFormatStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet rs = dateFormatStmt.executeQuery();
            rs.next();
            dateFormat = rs.getInt(1);
        } finally {
            Database.closeConnection(conn);
        }


        com.easyinsight.analysis.InsightResponse insightResponse = new AnalysisService().openAnalysisIfPossible(reportURLKey);
        if (insightResponse.getStatus() == com.easyinsight.analysis.InsightResponse.SUCCESS) {
            WSAnalysisDefinition report = new AnalysisService().openAnalysisDefinition(insightResponse.getInsightDescriptor().getId());
            if (report.getReportType() == WSAnalysisDefinition.LIST) {
                out.println(ExportService.toTable(report, dateFormat));
            } else {
                // inline image
            }
        }
    %>
</body>
</html>
