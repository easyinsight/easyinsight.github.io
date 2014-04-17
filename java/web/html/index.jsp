<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.core.DataSourceDescriptor" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="com.easyinsight.tag.Tag" %>
<%@ page import="com.easyinsight.userupload.UserUploadService" %>
<%@ page import="com.easyinsight.export.ExportService" %>
<%@ page import="com.easyinsight.analysis.AnalysisDateDimension" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="com.easyinsight.jsphelpers.EIHelper" %>
<%@ page import="com.easyinsight.export.ExportMetadata" %>
<%@ page import="com.easyinsight.analysis.InsightRequestMetadata" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Data Sources</title>
    <jsp:include page="bootstrapHeader.jsp"/>
    <%
        String userName = (String) session.getAttribute("userName");
        com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
        EIConnection conn = Database.instance().getConnection();
        try {

            java.util.List<DataSourceDescriptor> dataSources = new com.easyinsight.datafeeds.FeedService().searchForSubscribedFeeds();
            EIHelper.sortStuff(dataSources);
            JSONArray ja = new JSONArray();
            ExportMetadata md = ExportService.createExportMetadata(SecurityUtil.getAccountID(), conn, new InsightRequestMetadata());
            for (DataSourceDescriptor d : dataSources) {
                ja.put(d.toJSON(md));
            }
    %>
    <script type="text/javascript" src="/js/underscore-min.js"></script>
    <script type="text/javascript" src="/js/datasources.js"></script>
    <script type="text/javascript" language="JavaScript">
        var dataSources = <%= ja.toString() %>;
    </script>
</head>
<body>
<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.DATA_SOURCES_AND_REPORTS %>"/>
</jsp:include>
<div class="container corePageWell">
    <div class="row">
        <jsp:include page="../recent_actions.jsp"/>
        <div class="col-md-9">
            <div class="tag-list">
                <% for(Tag t : new UserUploadService().getDataSourceTags()) { %>
                <a class="btn btn-default tag-select" data-toggle="button" data-tag-id="<%= t.getId() %>" href="#"><%= StringEscapeUtils.escapeHtml(t.getName()) %></a>
                <% } %>
            </div>

            <div id="data_sources">

            </div>

        </div>
    </div>
</div>

<script id="data_source_template" type="text/template">
    <table class="table table-striped table-bordered table-condensed">
        <thead>
        <tr>
            <th>Data Source Name</th>
            <th style="width:150px">Last Refresh Time</th>
        </tr>
        </thead>
        <tbody>
        <@ _.each(data_sources, function(e,i,l) { @>
        <tr>
            <td style="font-weight:500">
                <a href="reports/<@- e.url_key @>"><@- e.name @>
                </a>
            </td>
            <td>
                <@- e.last_refresh_time @>
            </td>
        </tr>
        <@ }) @>
        </tbody>
    </table>
</script>
</body>
<%
    } finally {
        Database.closeConnection(conn);
        SecurityUtil.clearThreadLocal();
    }
%>
</html>