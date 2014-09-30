<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.analysis.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.users.UserServiceResponse" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.easyinsight.users.User" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.analysis.definitions.WSDiagramDefinition" %>
<%@ page import="com.easyinsight.core.InsightDescriptor" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="com.easyinsight.util.RandomTextGenerator" %>
<%@ page import="java.sql.Timestamp" %>
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

        String dtUrlKey;
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

            PreparedStatement saveDrillStmt = conn.prepareStatement("INSERT INTO DRILLTHROUGH_SAVE (REPORT_ID, URL_KEY, SAVE_TIME) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            saveDrillStmt.setLong(1, report.getAnalysisID());
            dtUrlKey = RandomTextGenerator.generateText(40);
            saveDrillStmt.setString(2, dtUrlKey);
            saveDrillStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            saveDrillStmt.execute();
            long drillID = Database.instance().getAutoGenKey(saveDrillStmt);
            saveDrillStmt.close();
            PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO DRILLTHROUGH_REPORT_SAVE_FILTER (DRILLTHROUGH_SAVE_ID, FILTER_ID) VALUES (?, ?)");
            for (FilterDefinition filter : filters) {
                filter.beforeSave(hibernateSession);
                hibernateSession.save(filter);
                hibernateSession.flush();
                saveStmt.setLong(1, drillID);
                saveStmt.setLong(2, filter.getFilterID());
                saveStmt.execute();
            }
            saveStmt.close();
        } finally {
            hibernateSession.close();
            Database.closeConnection(conn);
        }
        report.setFilterDefinitions(filters);

        JSONObject reportJSON = new JSONObject();
        reportJSON.put("name", report.getName());
        reportJSON.put("id", -1);
        reportJSON.put("filters", new JSONArray());
        reportJSON.put("drillthroughID", dtUrlKey);

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
        style.put("png", true);
        reportJO.put("styles", style);
        intermediate.put("report", jj);

        JSONObject seleniumObject = new JSONObject();
        seleniumObject.put("seleniumID", triggerID);
        if (report instanceof WSGaugeDefinition) {
            seleniumObject.put("reportType", "gauge");
        } else if (report instanceof WSDiagramDefinition) {
            seleniumObject.put("reportType", "diagram");
        } else {
            seleniumObject.put("reportType", "svg");
        }

        EIConnection c = Database.instance().getConnection();
        JSONObject userObject = new JSONObject();
        try {
            userObject = SecurityUtil.getUserJSON(c, request);
        } finally {
            Database.closeConnection(c);
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
    <title>Easy Insight</title>
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