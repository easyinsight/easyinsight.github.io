<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.analysis.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.easyinsight.html.*" %>
<%@ page import="com.easyinsight.datafeeds.FeedStorage" %>
<%@ page import="com.easyinsight.core.DataSourceDescriptor" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.cache.MemCachedManager" %>
<%@ page import="java.io.ObjectInputStream" %>
<%@ page import="java.io.ByteArrayInputStream" %>
<%@ page import="com.easyinsight.core.InsightDescriptor" %>
<%@ page import="com.easyinsight.users.UserService" %>
<%@ page import="com.easyinsight.users.UserServiceResponse" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.easyinsight.users.User" %>
<%@ page import="org.hibernate.Session" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<%

    String seleniumUserName = request.getParameter("seleniumUserName");
    String seleniumPassword = request.getParameter("seleniumPassword");

    long reportID;
    int width;
    int height;
    long triggerID = Long.parseLong(request.getParameter("seleniumID"));
    EIConnection conn = Database.instance().getConnection();
    try {
        PreparedStatement stmt = conn.prepareStatement("SELECT report_id, image_preferred_width, image_preferred_height, user_id FROM " +
                "image_selenium_trigger WHERE image_selenium_trigger_id = ? AND image_state = ? AND " +
                "selenium_username = ? AND selenium_password = ?");
        stmt.setLong(1, triggerID);
        stmt.setInt(2, 0);
        stmt.setString(3, seleniumUserName);
        stmt.setString(4, seleniumPassword);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            reportID = rs.getLong(1);
            width = rs.getInt(2);
            height = rs.getInt(3);
            Long userID = rs.getLong(4);
            Session hibernateSession = Database.instance().createSession(conn);
            try {
                List results = hibernateSession.createQuery("from User where userID = ?").setLong(0, userID).list();
                if (results.size() > 0) {
                    User user = (User) results.get(0);
                    UserServiceResponse userServiceResponse = UserServiceResponse.createResponse(user, hibernateSession, conn);
                    SecurityUtil.populateSession(session, userServiceResponse);

                    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);


                } else {
                    throw new RuntimeException();
                }
            } finally {
                hibernateSession.close();
            }
        } else {
            throw new RuntimeException();
        }
    } finally {
        Database.closeConnection(conn);
    }


    try {



        WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportID);
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();

        conn = Database.instance().getConnection();
        org.hibernate.Session hibernateSession = Database.instance().createSession(conn);
        try {
            PreparedStatement query = conn.prepareStatement("SELECT filter_id FROM image_selenium_trigger_to_filter WHERE image_selenium_trigger_id = ?");
            query.setLong(1, triggerID);
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                long filterID = rs.getLong(1);
                FilterDefinition filterDefinition = (FilterDefinition) hibernateSession.createQuery("from FilterDefinition where filterID = ?").setLong(0, filterID).list().get(0);
                filterDefinition.afterLoad();
                //filterDefinition.setShowOnReportView(false);
                filters.add(filterDefinition);
            }
        } finally {
            hibernateSession.close();
            Database.closeConnection(conn);
        }
        report.setFilterDefinitions(filters);

        JSONObject reportJSON = new JSONObject();
        reportJSON.put("name", report.getName());
        reportJSON.put("id", -1);
        reportJSON.put("filters", new JSONArray());

        JSONObject styleJSON = new JSONObject();
        styleJSON.put("main_stack_start", "#FFFFFF");
        styleJSON.put("alternative_stack_start", "#FFFFFF");
        reportJSON.put("styles", styleJSON);
        JSONObject intermediate = new JSONObject();
        reportJSON.put("base", intermediate);
        intermediate.put("show_label", false);
        intermediate.put("id", (report.getUrlKey() + "_container"));
        intermediate.put("overrides", new JSONArray());
        intermediate.put("filters", new JSONArray());
        intermediate.put("type", "report");
        JSONObject jj = new JSONObject();
        jj.put("name", report.getName());
        jj.put("id", report.getUrlKey());
        HTMLReportMetadata md = new HTMLReportMetadata();
        md.setEmbedded(true);
        JSONObject reportJO = report.toJSON(md, new ArrayList<FilterDefinition>());
        jj.put("metadata", reportJO);
        JSONObject style = new JSONObject();
        style.put("preferredWidth", width);
        style.put("preferredHeight", height);
        reportJO.put("styles", style);
        intermediate.put("report", jj);

        JSONObject seleniumObject = new JSONObject();
        seleniumObject.put("seleniumID", triggerID);


        EIConnection c = Database.instance().getConnection();
        JSONObject userObject = new JSONObject();
        try {
            userObject = SecurityUtil.getUserJSON(c, request);
        } finally {
            c.close();
        }

        userObject.put("embedded", true);
        if (request.getParameter("iframeKey") != null) {
            userObject.put("iframeKey", request.getParameter("iframeKey"));
        }

%>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta charset="utf-8">
    <meta name="author" content="">
    <title>Easy Insight &mdash; <%= StringEscapeUtils.escapeHtml(report.getName()) %>
    </title>
    <jsp:include page="../html/bootstrapHeader.jsp"/>
    <jsp:include page="../html/reportDashboardHeader.jsp"/>
    <%
        List<String> jsIncludes = report.javaScriptIncludes();
        for (String jsInclude : jsIncludes) {
    %><%= "<script type=\"text/javascript\" src=\"" + jsInclude + "\"></script>"%><%
    }
    List<String> cssIncludes = report.cssIncludes();
    for (String cssInclude : cssIncludes) {
%><%= "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + cssInclude + "\" />"%><%
    }
%>
    <script type="text/javascript" src="/js/dashboard.js"></script>

    <style type="text/css">
        body {
            background-image:none;
        }
    </style>
    <script type="text/javascript" language="JavaScript">
        var dashboardJSON = <%= reportJSON %>;
        var userJSON = <%= userObject %>;
        var seleniumJSON = <%= seleniumObject %>;
    </script>
    <script type="text/javascript" src="/js/visualizations/selenium.js"></script>
</head>
<body>
<div class="container">
    <div id="base"></div>
</div>
</body>
<%
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>
</html>