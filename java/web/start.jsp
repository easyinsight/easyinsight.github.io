<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %><%
    long userID = (Long) session.getAttribute("userID");

    EIConnection conn = Database.instance().getConnection();
    try {
        PreparedStatement ps = conn.prepareStatement("SELECT use_html_version FROM ACCOUNT, USER WHERE user_id = ? and user.account_id = account.account_id");
        ps.setLong(1, userID);
        ResultSet rs = ps.executeQuery();
        rs.next();
        boolean useHtmlVersion = rs.getBoolean(1);
        if (useHtmlVersion) {
            response.sendRedirect(RedirectUtil.getURL(request, "/app/html"));
        } else {
            response.sendRedirect(RedirectUtil.getURL(request, "/app"));
        }
    } finally {
        Database.closeConnection(conn);
    }
%>