<%@ page import="com.easyinsight.datafeeds.HTMLConnectionFactory" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.goals.InstallationSystem" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.datafeeds.FeedDefinition" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    SecurityUtil.populateThreadLocalFromSession(request);
    try {
        int connectionType = Integer.parseInt(request.getParameter("connectionType"));

        FeedDefinition dataSource;
        EIConnection conn = Database.instance().getConnection();
        try {
            dataSource = new InstallationSystem(conn).installConnection(connectionType);
        } finally {
            Database.closeConnection(conn);
        }
        request.getSession().removeAttribute("connectionError");
        HTMLConnectionFactory factory = new HTMLConnectionFactory(connectionType);
        factory.actionProcess(request, response, dataSource);
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>