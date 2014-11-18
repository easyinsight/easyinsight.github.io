<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="com.easyinsight.userupload.UserUploadService" %>
<%@ page import="com.easyinsight.core.EIDescriptor" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="java.sql.Timestamp" %>
<%
    boolean phone = false;
    String userName = request.getParameter("userName");
    boolean loggedIn = userName != null && !"null".equals(userName);
    int headerActive;
    try {
        headerActive = Integer.parseInt(request.getParameter("headerActive"));
    } catch (NumberFormatException e) {
        headerActive = HtmlConstants.NONE;
    }
    boolean designer = false;
    if (loggedIn) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement qStmt = conn.prepareStatement("SELECT ANALYST FROM USER WHERE USER_ID = ?");
            qStmt.setLong(1, SecurityUtil.getUserID());
            ResultSet rs = qStmt.executeQuery();
            rs.next();
            designer = rs.getBoolean(1);
        } finally {
            Database.closeConnection(conn);
        }
    }
%>

<nav class="navbar_first navbar navbar-static-top navbar-inverse" role="navigation">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
    </div>
    <div class="collapse navbar-collapse navbar-ex1-collapse">
        <% if (loggedIn) { %>
        <ul class="nav navbar-nav">
            <li <%= headerActive == HtmlConstants.DATA_SOURCES_AND_REPORTS ? "class=\"active\"" : ""%>><a
                    href="/a/home">Home</a></li>
            <% if (designer) { %>
            <li <%= headerActive == HtmlConstants.CONNECTIONS ? "class=\"active\"" : ""%>><a
                    href="/app/html/connections/">Connections</a></li>
            <li <%= headerActive == HtmlConstants.SCHEDULING ? "class=\"active\"" : ""%>><a
                    href="/app/embeddedScheduleManagement.jsp">Scheduling</a></li>
            <% } %>
        </ul>
        <% } %>
        <ul class="nav navbar-nav navbar-right">
            <%
                if (!loggedIn) {
            %>
            <li><a href="http://www.easy-insight.com/">Main Website</a></li>
            <li class="active"><a href="/a/home">Sign In</a></li>

            <%
            } else { %>
            <%
                List<EIDescriptor> accountReports = new UserUploadService().getAccountReports();
                if (accountReports.size() > 0) {
            %>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    <i class="icon-user"></i>Reports
                    <b class="caret"></b>
                </a>
                <ul class="dropdown-menu">
                    <%
                        for (EIDescriptor descriptor : accountReports) {
                            if (descriptor.getType() == EIDescriptor.DASHBOARD) {
                            %>
                    <li><a href="/app/html/dashboard/<%=descriptor.getUrlKey()%>"><%=StringEscapeUtils.escapeHtml(descriptor.getName())%></a></li>
                            <%
                                    } else if (descriptor.getType() == EIDescriptor.REPORT) {
                                        %>
                    <li><a href="/app/html/report/<%=descriptor.getUrlKey()%>"><%=StringEscapeUtils.escapeHtml(descriptor.getName())%></a></li>
                    <%
                                    }
                        }
                    %>
                </ul>
            </li>
            <%
                }
            %>
            <%
                EIConnection conn = Database.instance().getConnection();
                int count;
                try {
                    PreparedStatement newsStmt = conn.prepareStatement("SELECT news_dismiss_date FROM USER WHERE user_id = ?");
                    newsStmt.setLong(1, SecurityUtil.getUserID());
                    ResultSet newsRS = newsStmt.executeQuery();
                    newsRS.next();
                    Timestamp dismissDate = newsRS.getTimestamp(1);
                    count = 0;
                    if (dismissDate != null) {
                        PreparedStatement dateStmt = conn.prepareStatement("SELECT COUNT(NEWS_ENTRY_ID) FROM NEWS_ENTRY WHERE NEWS_ENTRY.entry_time > ?");
                        dateStmt.setTimestamp(1, dismissDate);
                        ResultSet rs = dateStmt.executeQuery();
                        rs.next();
                        count = rs.getInt(1);
                        dateStmt.close();
                    } else {

                    }
                    newsStmt.close();
                } finally {
                    Database.closeConnection(conn);
                }

            %>
            <li <%= headerActive == HtmlConstants.WHATS_NEW ? "class=\"active\"" : ""%>><a
                    href="/app/whatsnew.jsp">What's New<% if (count > 0) { %><span style="margin-left:5px;margin-top: -3px;vertical-align:middle" class="badge"><%= count %></span><% } %></a></li>
            <li <%= headerActive == HtmlConstants.HELP ? "class=\"active\"" : ""%>><a
                    href="/app/docs">Help</a></li>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    <i class="icon-user"></i> <%= StringEscapeUtils.escapeHtml(userName) %>
                    <b class="caret"></b>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="/a/account/">Account Settings</a></li>
                    <li class="divider"></li>
                    <% if(!phone) { %>
                    <li><a href="/app/html/flashAppAction.jsp">Switch to Full Interface</a></li>
                    <li class="divider"></li>
                    <% } %>
                    <li><a href="/app/logoutAction.jsp">Sign Out</a></li>
                </ul>
            </li>
            <% } %>
        </ul>
    </div>
</nav>