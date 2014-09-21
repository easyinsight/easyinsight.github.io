<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="com.easyinsight.datafeeds.HTMLConnectionFactory" %>
<%@ page import="com.easyinsight.datafeeds.HTMLConnectionProperty" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page import="com.easyinsight.datafeeds.FeedType" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        int connectionID = Integer.parseInt(request.getParameter("connectionID"));
        if (connectionID == FeedType.SERVER_MYSQL.getType() || connectionID == FeedType.SERVER_SQL_SERVER.getType() || connectionID == FeedType.SERVER_POSTGRES.getType()) {
            if (request.getParameter("error") == null) {
                response.sendRedirect(RedirectUtil.getURL(request, "/app/html/databaseConnection.jsp?connectionID=" + connectionID));
            } else {
                response.sendRedirect(RedirectUtil.getURL(request, "/app/html/databaseConnection.jsp?connectionID=" + connectionID + "&error=" + request.getParameter("error")));
            }
            return;
        } /*else if (connectionID == FeedType.STATIC.getType()) {
            response.sendRedirect(RedirectUtil.getURL(request, "/a/connection"));
            return;
        }*/
        HTMLConnectionFactory factory;
        try {
            factory = new HTMLConnectionFactory(connectionID);
        } catch (UnsupportedOperationException e) {
            response.sendRedirect(RedirectUtil.getURL(request, "/app/html/fullAppRequired.jsp"));
            return;
        }
        EIConnection conn = Database.instance().getConnection();
        String existingURLKey = null;
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT DATA_FEED.API_KEY FROM DATA_FEED, UPLOAD_POLICY_USERS, USER WHERE USER.ACCOUNT_ID = ? AND USER.USER_ID = UPLOAD_POLICY_USERS.USER_ID AND " +
                    "UPLOAD_POLICY_USERS.FEED_ID = DATA_FEED.DATA_FEED_ID AND DATA_FEED.FEED_TYPE = ? AND DATA_FEED.VISIBLE = ?");
            ps.setLong(1, SecurityUtil.getAccountID());
            ps.setInt(2, connectionID);
            ps.setBoolean(3, true);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                existingURLKey = RedirectUtil.getURL(request, "/a/data_sources/" + rs.getString(1));
            }
        } finally {
            Database.closeConnection(conn);
        }
%>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title><%= "Easy Insight Connection to " + factory.getName()%></title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <jsp:include page="reportDashboardHeader.jsp"/>
</head>
<body>

<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.NONE %>"/>
</jsp:include>
<div class="container corePageWell" style="margin-top: 20px">

    <%
        if (request.getParameter("error") != null) {
            if (request.getSession().getAttribute("connectionError") == null) {
    %>
        <div class="row">
            <div class="col-md-8 col-md-offset-2">
                <div class="alert alert-danger">Something went wrong in trying to create the connection. Check the configuration information you used.</div>
            </div>
        </div><%
} else {
%>
        <div class="row">
            <div class="col-md-8 col-md-offset-2">
                <div class="alert alert-danger"><%= request.getSession().getAttribute("connectionError")%></div>
            </div>
        </div>
    <%
            request.getSession().removeAttribute(factory.getActionSummary());
            }
        } else if (existingURLKey != null) { %>
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
        <div class="alert alert-warning">You already have a data source of this connection type at <a href="<%=existingURLKey%>"><%=existingURLKey%></a>. You should probably be using that data source instead of creating a new one.</div>
        </div>
    </div>
    <% } %>


    <div class="row">
        <div class="col-md-6 col-md-offset-3">
            <form class="well" method="post" action="/app/html/connectionCreationAction.jsp">
                <div style="margin-bottom: 10px"><strong><%= factory.getTitle() %></strong></div>
                <input type="hidden" id="connectionType" name="connectionType" value="<%= connectionID%>"/>
                <% if (factory.getActionSummary() != null) { %>
                <p style="margin-top:15px;margin-bottom:15px"><%= factory.getActionSummary() %></p>
                <% } %>
                <%
                    for (HTMLConnectionProperty property : factory.getProperties()) {

                        %>
                <label for="<%= property.getSafeProperty()%>" class="promptLabel" style="font-weight:bold"><%=property.getField()%></label>
                 <%
                if (property.isPassword()) {
                %>
                <input type="password" class="form-control" name="<%= property.getSafeProperty()%>" id="<%= property.getSafeProperty()%>"/>
                <%
                    } else {
                %>
                <input type="text" class="form-control" name="<%= property.getSafeProperty()%>" id="<%= property.getSafeProperty()%>"/>
                <%
                    }
                %>
                <%
                    if (property.getExplanation() != null) { %>
                <p class="helpBlock" style="font-size:12px"><%= property.getExplanation()%></p>

                <%    } %>
                <%
                    }
                %>
                <button class="btn btn-inverse" style="margin-top: 20px" type="submit" value="Create"><%= factory.getType() == HTMLConnectionFactory.TYPE_OAUTH ? "Authorize Access" : "Create the Connection" %></button>
            </form>
        </div>

</div>
</div>
</body>
<%
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>
</html>